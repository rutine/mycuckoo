package com.mycuckoo.admin.service.cms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mycuckoo.admin.constant.enums.FieldType;
import com.mycuckoo.admin.entity.cms.Form;
import com.mycuckoo.admin.entity.cms.FormData;
import com.mycuckoo.admin.entity.cms.FormField;
import com.mycuckoo.admin.entity.cms.FormVersion;
import com.mycuckoo.admin.entity.dto.cms.FieldConfig;
import com.mycuckoo.admin.entity.dto.cms.FormDTOs;
import com.mycuckoo.admin.entity.vo.cms.FormBusinessVO;
import com.mycuckoo.admin.entity.vo.cms.FormDataVO;
import com.mycuckoo.admin.entity.vo.cms.FormVO;
import com.mycuckoo.admin.repository.cms.FormDataMapper;
import com.mycuckoo.admin.repository.cms.FormMapper;
import com.mycuckoo.admin.repository.cms.FormVersionMapper;
import com.mycuckoo.admin.service.cms.processor.FieldContext;
import com.mycuckoo.admin.service.cms.processor.FieldProcessor;
import com.mycuckoo.admin.service.cms.processor.FieldProcessorFactory;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.core.util.JsonUtils;
import com.mycuckoo.core.util.web.SessionContextHolder;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class FormService {

    @Autowired
    private FormMapper formMapper;
    @Autowired
    private FormVersionMapper versionMapper;
    @Autowired
    private FormDataMapper dataMapper;
    @Autowired
    private FieldProcessorFactory processorFactory;

    /**
     * 保存表单定义
     */
    @Transactional
    public void save(FormDTOs.SaveDTO dto) {
        Long userId = SessionContextHolder.getUserId();

        // 1. 校验字段名唯一
        validateFieldNames(dto.getConfigs());

        Form form = new Form();
        form.setId(dto.getId());
        form.setBizType(dto.getBizType());
        form.setCode(dto.getBizType());
        form.setName(dto.getName());
        form.setConfig(JsonUtils.toJson(dto.getConfigs()));
        form.setMemo(dto.getMemo());
        form.setUpdater(userId + "");
        form.setUpdateTime(LocalDateTime.now());

        if (dto.getId() == null) {
            Form old = formMapper.getByBizType(dto.getBizType());
            if (old == null) {
                form.setTenantId(SessionContextHolder.getTenantId());
                form.setVersion(1);
                form.setStatus(1);
                form.setCreator(userId + "");
                form.setCreateTime(LocalDateTime.now());
                formMapper.save(form);
            } else {
                form.setVersion(old.getVersion() + 1);
                formMapper.update(form);
            }
        } else {
            Form old = formMapper.get(dto.getId());
            Assert.notNull(old, "表单不存在");
            form.setVersion(old.getVersion() + 1);
            formMapper.update(form);
        }

        FormVersion version = new FormVersion();
        version.setFormId(form.getId());
        version.setVersion(form.getVersion());
        version.setConfig(form.getConfig());
        version.setCreator(userId + "");
        version.setCreateTime(LocalDateTime.now());
        versionMapper.save(version);
    }

    /**
     * 按表单编码查表单
     */
    public FormVO get(Long id) {
        Form form = formMapper.get(id);
        if (form == null) return null;

        FormVO vo = new FormVO();
        BeanUtils.copyProperties(form, vo);
        vo.setConfigs(parseConfigs(form.getConfig()));
        return vo;
    }

    /**
     * 按表单编码查表单
     */
    public FormVO get(String bizType) {
        Form form = formMapper.getByBizType(bizType);
        if (form == null) return null;

        FormVO vo = new FormVO();
        BeanUtils.copyProperties(form, vo);
        vo.setConfigs(parseConfigs(form.getConfig()));
        return vo;
    }

    /**
     * 按模块编码查表单
     */
    public Page<FormVO> findByPage(Querier querier) {
        Page<Form> page = formMapper.findByPage(querier.getQ(), querier);
        List<FormVO> result = new ArrayList<>();
        for (Form form : page) {
            FormVO vo = new FormVO();
            BeanUtils.copyProperties(form, vo);
            result.add(vo);
        }
        return new PageImpl<>(result, querier, page.getTotalElements());
    }

    /**
     * 按业务编码和业务ID查表单配置和数据
     * 有 bizId 时查数据，没有时只返回配置
     */
    public FormBusinessVO getData(String bizType, String bizId) {
        Form form = formMapper.getByBizType(bizType);
        if (form == null) return null;

        FormBusinessVO vo = new FormBusinessVO();
        vo.setFormId(form.getId());
        vo.setFormCode(form.getCode());
        vo.setFormName(form.getName());
        vo.setFormVersion(form.getVersion());

        String configJson = form.getConfig();
        if (configJson != null && !configJson.isEmpty()) {
            vo.setConfigs(JsonUtils.fromJson(configJson,
                    new TypeReference<List<FieldConfig>>() {}));
        } else {
            vo.setConfigs(new ArrayList<>());
        }

        if (bizId != null && !bizId.isEmpty()) {
            FormData entity = dataMapper.getByBizId(form.getCode(), bizId);
            if (entity != null) {
                vo.setDataId(entity.getId());
                vo.setBizId(entity.getBizId());

                configJson = getConfigByVersion(entity.getFormId(), entity.getFormVersion());
                List<FormField> fields = parseFields(entity.getFormId(), entity.getFormCode(), configJson);

                Map<String, Object> data = JsonUtils.fromJson(entity.getData(), Map.class);
                Map<String, Object> processed = processorFactory.afterLoad(fields, data);

                vo.setData(processed);
                vo.setFormVersion(entity.getFormVersion());
                vo.setCreator(entity.getCreator());
                vo.setCreateTime(entity.getCreateTime());
                vo.setUpdater(entity.getUpdater());
                vo.setUpdateTime(entity.getUpdateTime());

                if (configJson != null && !configJson.isEmpty()) {
                    vo.setConfigs(JsonUtils.fromJson(configJson,
                            new TypeReference<List<FieldConfig>>() {}));
                }
            }
        }

        return vo;
    }

    /**
     * 保存表单数据
     */
    @Transactional
    public Long saveData(FormDTOs.DataSaveDTO dto) {
        Form form = formMapper.getByBizType(dto.getBizType());
        Assert.notNull(form, "表单不存在");

        List<FormField> fields = parseFields(form.getId(), form.getCode(), form.getConfig());
        validate(fields, dto.getData());
        Map<String, Object> processed = processorFactory.beforeSave(fields, dto.getData());

        Long userId = SessionContextHolder.getUserId();

        FormData entity = new FormData();
        entity.setFormId(form.getId());
        entity.setFormCode(form.getCode());
        entity.setFormVersion(form.getVersion());
        entity.setBizId(dto.getBizId());
        entity.setData(JsonUtils.toJson(processed));
        if (dto.getId() != null) {
            dataMapper.update(entity);
        } else {
            dataMapper.save(entity);
            entity.setCreator(userId + "");
            entity.setCreateTime(LocalDateTime.now());
        }
        return entity.getId();
    }

    /**
     * 分页查表单数据
     */
    public List<FormDataVO> findDataByPage(String formCode, String bizId) {
        List<FormData> list = dataMapper.findByPage(formCode, bizId);
        Map<Integer, List<FormField>> fieldCache = new HashMap<>();

        List<FormDataVO> result = new ArrayList<>();
        for (FormData entity : list) {
            List<FormField> fields = fieldCache.computeIfAbsent(
                    entity.getFormVersion(),
                    v -> parseFields(entity.getFormId(), entity.getFormCode(),
                            getConfigByVersion(entity.getFormId(), v))
            );

            Map<String, Object> data = JsonUtils.fromJson(entity.getData(), Map.class);
            Map<String, Object> processed = processorFactory.afterLoad(fields, data);

            FormDataVO vo = new FormDataVO();
            vo.setId(entity.getId());
            vo.setFormId(entity.getFormId());
            vo.setFormCode(entity.getFormCode());
            vo.setFormVersion(entity.getFormVersion());
            vo.setBizId(entity.getBizId());
            vo.setData(processed);
            vo.setCreator(entity.getCreator());
            vo.setCreateTime(entity.getCreateTime());
            vo.setUpdater(entity.getUpdater());
            vo.setUpdateTime(entity.getUpdateTime());
            result.add(vo);
        }
        return result;
    }

    /**
     * 修改单条数据的字段值
     */
    @Transactional
    public void updateFieldValue(Long dataId, String field, Object value, String operator) {
        FormData entity = dataMapper.get(dataId);
        Assert.notNull(entity, "数据不存在");

        String configJson = getConfigByVersion(entity.getFormId(), entity.getFormVersion());
        List<FormField> fields = parseFields(entity.getFormId(), entity.getFormCode(), configJson);

        FormField fieldMeta = fields.stream()
                .filter(f -> f.getField().equals(field))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("字段不存在"));

        Map<String, Object> data = JsonUtils.fromJson(entity.getData(), Map.class);
        data.put(field, value);

        validate(fields, data);

        FieldProcessor processor = processorFactory.get(fieldMeta.getProcessor());
        if (processor != null) {
            value = processor.beforeSave(new FieldContext(fieldMeta, null, null), value);
            data.put(field, value);
        }

        entity.setData(JsonUtils.toJson(data));
        entity.setUpdater(operator);
        entity.setUpdateTime(LocalDateTime.now());
        dataMapper.update(entity);
    }

    /**
     * 批量重跑某字段的处理器
     */
    @Transactional
    public int reprocessField(String bizType, String field, String operator) {
        Form form = formMapper.getByBizType(bizType);
        Assert.notNull(form, "表单不存在");

        List<FormField> fields = parseFields(form.getId(), form.getCode(), form.getConfig());
        FormField target = fields.stream()
                .filter(f -> f.getField().equals(field))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("字段不存在"));

        FieldProcessor processor = processorFactory.get(target.getProcessor());
        Assert.notNull(processor, "字段没有处理器");

        int pageSize = 500;
        long lastId = 0;
        int total = 0;

        while (true) {
            List<FormData> list = dataMapper.listByCursor(form.getCode(), lastId, pageSize);
            if (list.isEmpty()) break;

            for (FormData item : list) {
                Map<String, Object> data = JsonUtils.fromJson(item.getData(), Map.class);
                Object oldValue = data.get(field);
                Object newValue = processor.beforeSave(new FieldContext(target, form, null), oldValue);

                if (Objects.equals(oldValue, newValue)) continue;

                data.put(field, newValue);

                FormData update = new FormData();
                update.setId(item.getId());
                update.setData(JsonUtils.toJson(data));
                update.setUpdater(operator);
                update.setUpdateTime(LocalDateTime.now());
                dataMapper.update(update);
                total++;
            }

            lastId = list.get(list.size() - 1).getId();
        }

        return total;
    }

    // ==================== 私有方法 ====================

    /**
     * 解析 config JSON 为字段配置列表，兼容双重序列化
     */
    private List<FieldConfig> parseConfigs(String configJson) {
        if (configJson == null || configJson.isEmpty()) return new ArrayList<>();
        if (configJson.startsWith("\"")) {
            configJson = JsonUtils.fromJson(configJson, String.class);
        }
        return JsonUtils.fromJson(configJson, new TypeReference<List<FieldConfig>>() {});
    }

    /**
     * 从 config 解析字段列表
     */
    private List<FormField> parseFields(Long formId, String formCode, String configJson) {
        List<FieldConfig> configs = parseConfigs(configJson);
        List<FormField> fields = new ArrayList<>();
        flatten(configs, fields, formId, formCode, null, null);
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setSort(i);
        }
        return fields;
    }

    /**
     * 按版本获取配置
     */
    private String getConfigByVersion(Long formId, Integer version) {
        Form form = formMapper.get(formId);
        if (form == null) return null;
        if (version == null || version.equals(form.getVersion())) {
            return form.getConfig();
        }
        FormVersion v = versionMapper.getByVersion(formId, version);
        return v != null ? v.getConfig() : form.getConfig();
    }

    /**
     * 递归拍平字段
     */
    private void flatten(List<FieldConfig> configs, List<FormField> result,
                         Long formId, String formCode, String groupId, String groupTitle) {
        if (configs == null) return;
        for (FieldConfig c : configs) {
            if (FieldType.isGroup(c.getType())) {
                flatten(c.getChildren(), result, formId, formCode, c.getId(), c.getTitle());
            } else {
                FormField f = new FormField();
                f.setFormId(formId);
                f.setFormCode(formCode);
                f.setField(c.getField());
                f.setTitle(c.getTitle());
                f.setType(c.getType());
                f.setProcessor(c.getProcessor());
                f.setRequired(Boolean.TRUE.equals(c.getRequired()) ? 1 : 0);
                f.setCol(c.getCol() == null ? 12 : c.getCol());
                f.setGroupId(groupId);
                f.setGroupTitle(groupTitle);
                f.setOptions(c.getOptions() == null ? null : JsonUtils.toJson(c.getOptions()));
                f.setPlaceholder(c.getPlaceholder());
                f.setDefaultValue(c.getDefaultValue());
                f.setMinValue(c.getMinValue());
                f.setMaxValue(c.getMaxValue());
                f.setRegex(c.getRegex());
                f.setMax(c.getMax());
                f.setDefaultBool(c.getDefaultBool());
                f.setAccept(c.getAccept());
                f.setExts(c.getExts());
                f.setExtra(c.getExtra());
                result.add(f);
            }
        }
    }
    /**
     * 校验字段名唯一（含分组内）
     */
    private void validateFieldNames(List<FieldConfig> configs) {
        if (configs == null || configs.isEmpty()) return;

        Set<String> fieldNames = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        collectFieldNames(configs, fieldNames, duplicates);

        if (!duplicates.isEmpty()) {
            throw new RuntimeException("字段名重复：" + String.join("、", duplicates));
        }
    }
    private void collectFieldNames(List<FieldConfig> configs, Set<String> fieldNames, List<String> duplicates) {
        if (configs == null) return;
        for (FieldConfig c : configs) {
            if (FieldType.isGroup(c.getType())) {
                collectFieldNames(c.getChildren(), fieldNames, duplicates);
            } else if (c.getField() != null && !c.getField().isEmpty()) {
                if (!fieldNames.add(c.getField())) {
                    if (!duplicates.contains(c.getField())) {
                        duplicates.add(c.getField());
                    }
                }
            }
        }
    }
    /**
     * 校验
     */
    private void validate(List<FormField> fields, Map<String, Object> data) {
        for (FormField f : fields) {
            Object val = data.get(f.getField());
            boolean empty = val == null || "".equals(val.toString());

            if (f.getRequired() == 1 && empty) {
                throw new RuntimeException(f.getTitle() + "不能为空");
            }
            if (empty) continue;

            // 最大长度：文本类
            if (f.getMax() != null && Arrays.asList(1, 2, 10, 11).contains(f.getType())
                    && val.toString().length() > f.getMax()) {
                throw new RuntimeException(f.getTitle() + "长度不能超过" + f.getMax());
            }

            // 数值范围：数字/小数
            if (f.getMinValue() != null) {
                BigDecimal v = new BigDecimal(val.toString());
                if (v.compareTo(f.getMinValue()) < 0) {
                    throw new RuntimeException(f.getTitle() + "不能小于" + f.getMinValue());
                }
            }
            if (f.getMaxValue() != null) {
                BigDecimal v = new BigDecimal(val.toString());
                if (v.compareTo(f.getMaxValue()) > 0) {
                    throw new RuntimeException(f.getTitle() + "不能大于" + f.getMaxValue());
                }
            }

            // 评分上限
            if (f.getMax() != null && f.getType() == 15) {
                int v = Integer.parseInt(val.toString());
                if (v > f.getMax()) {
                    throw new RuntimeException(f.getTitle() + "不能超过" + f.getMax() + "分");
                }
            }

            // 正则
            if (f.getRegex() != null && !f.getRegex().isEmpty()) {
                if (!Pattern.matches(f.getRegex(), val.toString())) {
                    throw new RuntimeException(f.getTitle() + "格式不正确");
                }
            }
        }
    }
}
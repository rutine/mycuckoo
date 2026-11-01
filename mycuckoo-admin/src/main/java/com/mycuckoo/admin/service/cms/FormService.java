package com.mycuckoo.admin.service.cms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mycuckoo.admin.constant.enums.FieldType;
import com.mycuckoo.admin.entity.cms.CmsForm;
import com.mycuckoo.admin.entity.cms.CmsFormData;
import com.mycuckoo.admin.entity.cms.CmsFormField;
import com.mycuckoo.admin.entity.cms.CmsFormVersion;
import com.mycuckoo.admin.entity.dto.cms.FieldConfig;
import com.mycuckoo.admin.entity.dto.cms.FormDTOs;
import com.mycuckoo.admin.entity.vo.cms.FormBusinessVO;
import com.mycuckoo.admin.entity.vo.cms.FormDataVO;
import com.mycuckoo.admin.entity.vo.cms.FormVO;
import com.mycuckoo.admin.repository.cms.CmsFormDataMapper;
import com.mycuckoo.admin.repository.cms.CmsFormMapper;
import com.mycuckoo.admin.repository.cms.CmsFormVersionMapper;
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
    private CmsFormMapper formMapper;
    @Autowired
    private CmsFormVersionMapper versionMapper;
    @Autowired
    private CmsFormDataMapper dataMapper;
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

        CmsForm form = new CmsForm();
        form.setId(dto.getId());
        form.setCode(dto.getCode());
        form.setName(dto.getName());
        form.setBizType(dto.getBizType());
        form.setConfig(JsonUtils.toJson(dto.getConfigs()));
        form.setMemo(dto.getMemo());
        form.setUpdater(userId + "");
        form.setUpdateTime(LocalDateTime.now());

        if (dto.getId() == null) {
            CmsForm old = formMapper.getByCode(dto.getCode());
            if (old != null && !old.getBizType().equals(dto.getBizType())) throw new RuntimeException("表单编码已存在");
            if (old == null) {
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
            CmsForm old = formMapper.get(dto.getId());
            Assert.notNull(old, "表单不存在");
            form.setVersion(old.getVersion() + 1);
            formMapper.update(form);
        }

        CmsFormVersion version = new CmsFormVersion();
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
        CmsForm form = formMapper.get(id);
        if (form == null) return null;

        FormVO vo = new FormVO();
        BeanUtils.copyProperties(form, vo);
        vo.setConfigs(parseConfigs(form.getConfig()));
        return vo;
    }

    /**
     * 按表单编码查表单
     */
    public FormVO get(String code) {
        CmsForm form = formMapper.getByCode(code);
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
        Page<CmsForm> page = formMapper.findByPage(querier.getQ(), querier);
        List<FormVO> result = new ArrayList<>();
        for (CmsForm form : page) {
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
    public FormBusinessVO getData(String formCode, String bizId) {
        CmsForm form = formMapper.getByCode(formCode);
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
            CmsFormData entity = dataMapper.getByBizId(form.getCode(), bizId);
            if (entity != null) {
                vo.setDataId(entity.getId());
                vo.setBizId(entity.getBizId());

                configJson = getConfigByVersion(entity.getFormId(), entity.getFormVersion());
                List<CmsFormField> fields = parseFields(entity.getFormId(), entity.getFormCode(), configJson);

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
        CmsForm form = formMapper.getByCode(dto.getFormCode());
        if (form == null) throw new RuntimeException("表单不存在");

        List<CmsFormField> fields = parseFields(form.getId(), form.getCode(), form.getConfig());
        validate(fields, dto.getData());
        Map<String, Object> processed = processorFactory.beforeSave(fields, dto.getData());

        Long userId = SessionContextHolder.getUserId();

        CmsFormData entity = new CmsFormData();
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
        List<CmsFormData> list = dataMapper.findByPage(formCode, bizId);
        Map<Integer, List<CmsFormField>> fieldCache = new HashMap<>();

        List<FormDataVO> result = new ArrayList<>();
        for (CmsFormData entity : list) {
            List<CmsFormField> fields = fieldCache.computeIfAbsent(
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
        CmsFormData entity = dataMapper.get(dataId);
        if (entity == null) throw new RuntimeException("数据不存在");

        String configJson = getConfigByVersion(entity.getFormId(), entity.getFormVersion());
        List<CmsFormField> fields = parseFields(entity.getFormId(), entity.getFormCode(), configJson);

        CmsFormField fieldMeta = fields.stream()
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
    public int reprocessField(String formCode, String field, String operator) {
        CmsForm form = formMapper.getByCode(formCode);
        if (form == null) throw new RuntimeException("表单不存在");

        List<CmsFormField> fields = parseFields(form.getId(), form.getCode(), form.getConfig());
        CmsFormField target = fields.stream()
                .filter(f -> f.getField().equals(field))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("字段不存在"));

        FieldProcessor processor = processorFactory.get(target.getProcessor());
        if (processor == null) throw new RuntimeException("字段没有处理器");

        int pageSize = 500;
        long lastId = 0;
        int total = 0;

        while (true) {
            List<CmsFormData> list = dataMapper.listByCursor(formCode, lastId, pageSize);
            if (list.isEmpty()) break;

            for (CmsFormData item : list) {
                Map<String, Object> data = JsonUtils.fromJson(item.getData(), Map.class);
                Object oldValue = data.get(field);
                Object newValue = processor.beforeSave(new FieldContext(target, form, null), oldValue);

                if (Objects.equals(oldValue, newValue)) continue;

                data.put(field, newValue);

                CmsFormData update = new CmsFormData();
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
    private List<CmsFormField> parseFields(Long formId, String formCode, String configJson) {
        List<FieldConfig> configs = parseConfigs(configJson);
        List<CmsFormField> fields = new ArrayList<>();
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
        CmsForm form = formMapper.get(formId);
        if (form == null) return null;
        if (version == null || version.equals(form.getVersion())) {
            return form.getConfig();
        }
        CmsFormVersion v = versionMapper.getByVersion(formId, version);
        return v != null ? v.getConfig() : form.getConfig();
    }

    /**
     * 递归拍平字段
     */
    private void flatten(List<FieldConfig> configs, List<CmsFormField> result,
                         Long formId, String formCode, String groupId, String groupTitle) {
        if (configs == null) return;
        for (FieldConfig c : configs) {
            if (FieldType.isGroup(c.getType())) {
                flatten(c.getChildren(), result, formId, formCode, c.getId(), c.getTitle());
            } else {
                CmsFormField f = new CmsFormField();
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
    private void validate(List<CmsFormField> fields, Map<String, Object> data) {
        for (CmsFormField f : fields) {
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
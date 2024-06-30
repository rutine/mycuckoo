package com.mycuckoo.service.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.constant.enums.ModuleLevel;
import com.mycuckoo.core.repository.param.FilterType;
import com.mycuckoo.core.util.CommonUtils;
import com.mycuckoo.core.util.web.SessionUtil;
import com.mycuckoo.domain.platform.DictSmallType;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.domain.platform.TableConfig;
import com.mycuckoo.repository.platform.TableConfigMapper;
import com.mycuckoo.web.vo.req.TableConfigReqVos;
import com.mycuckoo.web.vo.res.TableConfigVos;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/1 10:37
 */
@Service
@Transactional(readOnly = true)
public class TableConfigService {

    @Autowired
    private TableConfigMapper tableConfigMapper;

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private DictionaryService dictionaryService;


    @Transactional
    public int save(TableConfigReqVos.Create vo) {
        ModuleMenu menu = moduleService.get(vo.getModuleId());
        Assert.notNull(menu, "所属模块不存在");
        Assert.state(menu.getLevel().equals(ModuleLevel.THREE.value()), "所属模块应该是三级模块");
        List<TableConfig> old = tableConfigMapper.findByModuleId(vo.getModuleId());
        List<Long> oldIds = old.stream().map(TableConfig::getTableId).collect(Collectors.toList());

        String user = SessionUtil.getUserId().toString();
        int i = 0;
        List<TableConfig> configs = Lists.newArrayList();
        for (TableConfigReqVos.CreateConfig config : vo.getConfigs()) {
            Assert.notNull(FilterType.of(config.getFilterType()), "过滤类型不正确");
            oldIds.remove(config.getTableId());

            TableConfig newConfig = new TableConfig();
            BeanUtils.copyProperties(config, newConfig);
            newConfig.setModuleId(vo.getModuleId());
            newConfig.setTableCode(vo.getTableCode());
            newConfig.setOrder(++i);
            newConfig.setUpdator(user);
            newConfig.setUpdateTime(LocalDateTime.now());
            if (config.getTableId() != null) {
                int row = tableConfigMapper.update(newConfig);
                if (row > 0) {
                    continue;
                }
            }
            newConfig.setCreator(user);
            newConfig.setCreateTime(LocalDateTime.now());
            configs.add(newConfig);
        }

        if (!configs.isEmpty()) {
            tableConfigMapper.batchInsert(configs);
        }
        if (!oldIds.isEmpty()) {
            tableConfigMapper.deleteByIds(oldIds);
        }

        return configs.size();
    }

    public List<TableConfigVos.Detail> findByModuleId(Long moduleId) {
        List<TableConfig> list = tableConfigMapper.findByModuleId(moduleId);

        return list.stream().sorted(Comparator.comparing(TableConfig::getOrder)).map(s -> {
            TableConfigVos.Detail t = new TableConfigVos.Detail();
            BeanUtils.copyProperties(s, t);
            return t;
        }).collect(Collectors.toList());
    }

    public List<TableConfigVos.Config> findByTableCode(String tableCode) {
        List<TableConfig> list = tableConfigMapper.findByTableCode(tableCode);

        List<String> dictCodes = list.stream()
                .filter(o -> o.getType().equals("dict") && CommonUtils.isNotBlank(o.getExtra()))
                .map(TableConfig::getExtra)
                .collect(Collectors.toList());
        Map<String, List<DictSmallType>> dictMap = dictionaryService.findSmallTypeMapByBigTypeCodes(dictCodes);

        return list.stream().sorted(Comparator.comparing(TableConfig::getOrder)).map(s -> {
            TableConfigVos.Config t = new TableConfigVos.Config();
            BeanUtils.copyProperties(s, t);
            t.setDict(dictMap.get(t.getExtra()));
            return t;
        }).collect(Collectors.toList());
    }
}

package com.mycuckoo.admin.service.platform;

import com.google.common.collect.Lists;
import com.mycuckoo.admin.constant.enums.ModuleLevel;
import com.mycuckoo.core.repository.param.FilterType;
import com.mycuckoo.core.util.StrUtils;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.admin.entity.platform.DictionaryItem;
import com.mycuckoo.admin.entity.platform.ModuleMenu;
import com.mycuckoo.admin.entity.platform.TableConfig;
import com.mycuckoo.admin.repository.platform.TableConfigMapper;
import com.mycuckoo.admin.entity.vo.TableConfigVos;
import com.mycuckoo.admin.entity.dto.TableConfigDtos;
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
    public int save(TableConfigVos.Create vo) {
        ModuleMenu menu = moduleService.get(vo.getModuleId());
        Assert.notNull(menu, "所属模块不存在");
        Assert.state(menu.getLevel().equals(ModuleLevel.THREE.code), "所属模块应该是三级模块");
        List<TableConfig> old = tableConfigMapper.findByModuleId(vo.getModuleId());
        List<Long> oldIds = old.stream().map(TableConfig::getTableId).collect(Collectors.toList());

        String user = SessionContextHolder.getUserId().toString();
        int i = 0;
        List<TableConfig> configs = Lists.newArrayList();
        for (TableConfigVos.CreateConfig config : vo.getConfigs()) {
            Assert.notNull(FilterType.of(config.getFilterType()), "过滤类型不正确");
            oldIds.remove(config.getTableId());

            TableConfig newConfig = new TableConfig();
            BeanUtils.copyProperties(config, newConfig);
            newConfig.setModuleId(vo.getModuleId());
            newConfig.setTableCode(vo.getTableCode());
            newConfig.setOrder(++i);
            newConfig.setUpdater(user);
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

    public List<TableConfigDtos.Detail> findByModuleId(Long moduleId) {
        List<TableConfig> list = tableConfigMapper.findByModuleId(moduleId);

        return list.stream().sorted(Comparator.comparing(TableConfig::getOrder)).map(s -> {
            TableConfigDtos.Detail t = new TableConfigDtos.Detail();
            BeanUtils.copyProperties(s, t);
            return t;
        }).collect(Collectors.toList());
    }

    public List<TableConfigDtos.Config> findByTableCode(String tableCode) {
        List<TableConfig> list = tableConfigMapper.findByTableCode(tableCode);

        List<String> dictCodes = list.stream()
                .filter(o -> o.getType().equals("dict") && StrUtils.isNotBlank(o.getExtra()))
                .map(TableConfig::getExtra)
                .collect(Collectors.toList());
        Map<String, List<DictionaryItem>> dictMap = dictionaryService.findItemMapByDictCodes(dictCodes);

        return list.stream().sorted(Comparator.comparing(TableConfig::getOrder)).map(s -> {
            TableConfigDtos.Config t = new TableConfigDtos.Config();
            BeanUtils.copyProperties(s, t);
            t.setDict(dictMap.get(t.getExtra()));
            return t;
        }).collect(Collectors.toList());
    }
}

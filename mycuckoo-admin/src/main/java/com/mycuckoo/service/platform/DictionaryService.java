package com.mycuckoo.service.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.core.constant.enums.ModuleName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.Dictionary;
import com.mycuckoo.domain.platform.DictionaryItem;
import com.mycuckoo.domain.platform.DictionaryItemExtend;
import com.mycuckoo.repository.platform.DictionaryMapper;
import com.mycuckoo.repository.platform.DictionaryItemMapper;
import com.mycuckoo.core.util.web.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mycuckoo.constant.AdminConst.DISABLE;
import static com.mycuckoo.constant.AdminConst.ENABLE;

/**
 * 功能说明: 字典业务类
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 10:00
 */
@Service
@Transactional(readOnly = true)
public class DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private DictionaryItemMapper dictionaryItemMapper;


    @Transactional
    public boolean disEnable(long dictId, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        dictionaryMapper.updateStatus(dictId, enable ? ENABLE : DISABLE);

        Dictionary entity = get(dictId);
        writeLog(entity, enable ? "启用" : "禁用");

        return true;
    }

    public boolean existByCode(String dictCode) {
        int count = dictionaryMapper.countByCode(dictCode);
        if (count > 0) return true;

        return false;
    }

    public List<DictionaryItem> findItemsByDictCode(String dictCode) {
        return dictionaryItemMapper.findByDictCode(dictCode);
    }

    public Map<String, List<DictionaryItem>> findItemMapByDictCodes(List<String> dictCodes) {
        if (dictCodes == null || dictCodes.isEmpty()) {
            return Maps.newHashMap();
        }

        List<DictionaryItemExtend> list = dictionaryItemMapper.findByDictCodes(dictCodes);

        return list.stream().collect(Collectors.groupingBy(DictionaryItemExtend::getDictCode, Collectors.toList()));
    }

    public Dictionary get(long dictId) {
        return dictionaryMapper.get(dictId);
    }

    public Page<Dictionary> findByPage(Querier querier) {
        return dictionaryMapper.findByPage(querier.getQ(), querier);
    }

    @Transactional
    public void update(Dictionary entity) {
        Dictionary old = get(entity.getDictId());
        Assert.notNull(old, "字典不存在!");
        Assert.state(old.getCode().equals(entity.getCode())
                || !existByCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");

        entity.setUpdator(SessionContextHolder.getUserId().toString());
        entity.setUpdateTime(LocalDateTime.now());
        dictionaryItemMapper.deleteByDictId(entity.getDictId());
        dictionaryMapper.update(entity);

        for (DictionaryItem item : entity.getItems()) {
            item.setDictId(entity.getDictId());
        }
        this.saveItems(entity.getItems());

        writeLog(entity, "修改");
    }

    @Transactional
    public void save(Dictionary entity) {
        Assert.state(!existByCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");

        entity.setUpdator(SessionContextHolder.getUserId().toString());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreator(SessionContextHolder.getUserId().toString());
        entity.setCreateTime(LocalDateTime.now());
        entity.setStatus(ENABLE);
        dictionaryMapper.save(entity);

        for (DictionaryItem item : entity.getItems()) {
            item.setDictId(entity.getDictId());
        }
        this.saveItems(entity.getItems());

        writeLog(entity, "新增");
    }

    @Transactional
    public void saveItems(List<DictionaryItem> items) {
        items.forEach(item -> {
            item.setCreator(SessionContextHolder.getUserId().toString());
            item.setCreateTime(LocalDateTime.now());
            dictionaryItemMapper.save(item);
        });
    }

    // --------------------------- 私有方法-------------------------------

    /**
     * 公用模块写日志
     *
     * @param entity   字典
     * @param logLevel 日志级别
     * @param action      操作名称
     */
    private void writeLog(Dictionary entity, String action) {

        LogOperator.begin()
                .module(ModuleName.SYS_TYPEDIC)
                .id(entity.getDictId())
                .title(SessionContextHolder.getUserName() + action + "字典")
                .content("字典名称：%s", entity.getName())
                .emit();
    }
}
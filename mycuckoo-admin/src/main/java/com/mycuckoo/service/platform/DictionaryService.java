package com.mycuckoo.service.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.DictBigType;
import com.mycuckoo.domain.platform.DictSmallType;
import com.mycuckoo.domain.platform.DictSmallTypeExtend;
import com.mycuckoo.repository.platform.DictBigTypeMapper;
import com.mycuckoo.repository.platform.DictSmallTypeMapper;
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
 * 功能说明: 字典大小类业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:29:37 AM
 */
@Service
@Transactional(readOnly = true)
public class DictionaryService {

    @Autowired
    private DictBigTypeMapper dictBigTypeMapper;
    @Autowired
    private DictSmallTypeMapper dictSmallTypeMapper;


    @Transactional
    public boolean disEnableBigType(long bigTypeId, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        if (!enable) {
            dictBigTypeMapper.updateStatus(bigTypeId, DISABLE);
        } else {
            dictBigTypeMapper.updateStatus(bigTypeId, ENABLE);
        }

        DictBigType entity = getBigTypeByBigTypeId(bigTypeId);
        writeLog(entity, LogLevel.SECOND, enable ? OptName.ENABLE : OptName.DISABLE);

        return true;
    }

    public boolean existBigTypeByBigTypeCode(String bigTypeCode) {
        int count = dictBigTypeMapper.countByCode(bigTypeCode);
        if (count > 0) return true;

        return false;
    }

    public List<DictSmallType> findSmallTypesByBigTypeCode(String bigTypeCode) {
        return dictSmallTypeMapper.findByBigTypeCode(bigTypeCode);
    }

    public Map<String, List<DictSmallType>> findSmallTypeMapByBigTypeCodes(List<String> bigTypeCodes) {
        if (bigTypeCodes == null || bigTypeCodes.isEmpty()) {
            return Maps.newHashMap();
        }

        List<DictSmallTypeExtend> list = dictSmallTypeMapper.findByBigTypeCodes(bigTypeCodes);

        return list.stream().collect(Collectors.groupingBy(DictSmallTypeExtend::getBigTypeCode, Collectors.toList()));
    }

    public DictBigType getBigTypeByBigTypeId(long bigTypeId) {
        return dictBigTypeMapper.get(bigTypeId);
    }

    public Page<DictBigType> findBigTypesByPage(Querier querier) {
        return dictBigTypeMapper.findByPage(querier.getQ(), querier);
    }

    @Transactional
    public void updateBigType(DictBigType entity) {
        DictBigType old = getBigTypeByBigTypeId(entity.getBigTypeId());
        Assert.notNull(old, "字典不存在!");
        Assert.state(old.getCode().equals(entity.getCode())
                || !existBigTypeByBigTypeCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");

        entity.setUpdator(SessionContextHolder.getUserId().toString());
        entity.setUpdateTime(LocalDateTime.now());
        dictSmallTypeMapper.deleteByBigTypeId(entity.getBigTypeId());
        dictBigTypeMapper.update(entity);

        for (DictSmallType smallType : entity.getSmallTypes()) {
            smallType.setBigTypeId(entity.getBigTypeId());
        }
        this.saveDicSmallTypes(entity.getSmallTypes());

        writeLog(entity, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void saveBigType(DictBigType entity) {
        Assert.state(!existBigTypeByBigTypeCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");

        entity.setUpdator(SessionContextHolder.getUserId().toString());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreator(SessionContextHolder.getUserId().toString());
        entity.setCreateTime(LocalDateTime.now());
        entity.setStatus(ENABLE);
        dictBigTypeMapper.save(entity);

        for (DictSmallType dictSmallType : entity.getSmallTypes()) {
            dictSmallType.setBigTypeId(entity.getBigTypeId());
        }
        this.saveDicSmallTypes(entity.getSmallTypes());

        writeLog(entity, LogLevel.SECOND, OptName.SAVE);
    }

    @Transactional
    public void saveDicSmallTypes(List<DictSmallType> smallTypes) {
        smallTypes.forEach(dicSmallType -> {
            dicSmallType.setCreator(SessionContextHolder.getUserId().toString());
            dicSmallType.setCreateTime(LocalDateTime.now());
            dictSmallTypeMapper.save(dicSmallType);
        });
    }

    // --------------------------- 私有方法-------------------------------

    /**
     * 公用模块写日志
     *
     * @param entity     字典大类
     * @param logLevel   日志级别
     * @param opt        操作名称
     * @author rutine
     * @time Oct 14, 2012 4:08:38 PM
     */
    private void writeLog(DictBigType entity, LogLevel logLevel, OptName opt) {

        LogOperator.begin()
                .module(ModuleName.SYS_TYPEDIC)
                .operate(opt)
                .id(entity.getBigTypeId())
                .title(null)
                .content("字典大类名称：%s", entity.getName())
                .level(logLevel)
                .emit();
    }
}

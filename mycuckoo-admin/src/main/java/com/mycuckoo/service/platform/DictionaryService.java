package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.domain.platform.DictBigType;
import com.mycuckoo.domain.platform.DictSmallType;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.Pageable;
import com.mycuckoo.repository.platform.DictBigTypeMapper;
import com.mycuckoo.repository.platform.DictSmallTypeMapper;
import com.mycuckoo.util.web.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mycuckoo.constant.ServiceConst.DISABLE;
import static com.mycuckoo.constant.ServiceConst.ENABLE;

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

    public DictBigType getBigTypeByBigTypeId(long bigTypeId) {
        return dictBigTypeMapper.get(bigTypeId);
    }

    public Page<DictBigType> findBigTypesByPage(Map<String, Object> params, Pageable page) {
        return dictBigTypeMapper.findByPage(params, page);
    }

    @Transactional
    public void updateBigType(DictBigType entity) {
        DictBigType old = getBigTypeByBigTypeId(entity.getBigTypeId());
        Assert.notNull(old, "字典不存在!");
        Assert.state(old.getCode().equals(entity.getCode())
                || !existBigTypeByBigTypeCode(entity.getCode()), "编码[" + entity.getCode() + "]已存在!");

        entity.setUpdater(SessionUtil.getUserCode());
        entity.setUpdateDate(new Date());
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

        entity.setUpdater(SessionUtil.getUserCode());
        entity.setUpdateDate(new Date());
        entity.setCreator(SessionUtil.getUserCode());
        entity.setCreateDate(new Date());
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
            dicSmallType.setCreator(SessionUtil.getUserCode());
            dicSmallType.setCreateDate(new Date());
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

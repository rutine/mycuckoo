package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.domain.platform.DictBigType;
import com.mycuckoo.domain.platform.DictSmallType;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.DictBigTypeMapper;
import com.mycuckoo.repository.platform.DictSmallTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.constant.BaseConst.SPLIT;
import static com.mycuckoo.constant.ServiceConst.*;

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
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public boolean disEnableDicBigType(long bigTypeId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
            DictBigType dictBigType = getDicBigTypeByBigTypeId(bigTypeId);
            dictBigTypeMapper.updateStatus(bigTypeId, DISABLE);

            writeLog(dictBigType, LogLevel.SECOND, OptName.DISABLE);
        } else {
            DictBigType dictBigType = getDicBigTypeByBigTypeId(bigTypeId);
            dictBigTypeMapper.updateStatus(bigTypeId, ENABLE);

            writeLog(dictBigType, LogLevel.SECOND, OptName.ENABLE);
        }

        return true;
    }

    public boolean existDicBigTypeByBigTypeCode(String bigTypeCode) {
        int count = dictBigTypeMapper.countByBigTypeCode(bigTypeCode);
        if (count > 0) return true;

        return false;
    }

    public List<DictSmallType> findDicSmallTypesByBigTypeCode(String bigTypeCode) {
        return dictSmallTypeMapper.findByBigTypeCode(bigTypeCode);
    }

    public DictBigType getDicBigTypeByBigTypeId(long bigTypeId) {
        return dictBigTypeMapper.get(bigTypeId);
    }

    public Page<DictBigType> findDicBigTypesByPage(Map<String, Object> params, Pageable page) {
        return dictBigTypeMapper.findByPage(params, page);
    }

    @Transactional
    public void updateDicBigType(DictBigType dictBigType) {
        DictBigType old = getDicBigTypeByBigTypeId(dictBigType.getBigTypeId());
        Assert.notNull(old, "字典不存在!");
        Assert.state(old.getBigTypeCode().equals(dictBigType.getBigTypeCode())
                || !existDicBigTypeByBigTypeCode(dictBigType.getBigTypeCode()), "编码[" + dictBigType.getBigTypeCode() + "]已存在!");

        dictSmallTypeMapper.deleteByBigTypeId(dictBigType.getBigTypeId());
        dictBigTypeMapper.update(dictBigType);

        for (DictSmallType smallType : dictBigType.getSmallTypes()) {
            smallType.setBigTypeId(dictBigType.getBigTypeId());
        }
        this.saveDicSmallTypes(dictBigType.getSmallTypes());

        writeLog(dictBigType, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void saveDicBigType(DictBigType dictBigType) {
        Assert.state(!existDicBigTypeByBigTypeCode(dictBigType.getBigTypeCode()), "编码[" + dictBigType.getBigTypeCode() + "]已存在!");

        dictBigType.setStatus(ENABLE);
        dictBigTypeMapper.save(dictBigType);

        for (DictSmallType dictSmallType : dictBigType.getSmallTypes()) {
            dictSmallType.setBigTypeId(dictBigType.getBigTypeId());
        }
        this.saveDicSmallTypes(dictBigType.getSmallTypes());

        writeLog(dictBigType, LogLevel.SECOND, OptName.SAVE);
    }

    @Transactional
    public void saveDicSmallTypes(List<DictSmallType> dictSmallTypes) {
        dictSmallTypes.forEach(dicSmallType -> {
            dictSmallTypeMapper.save(dicSmallType);
        });
    }

    // --------------------------- 私有方法-------------------------------

    /**
     * 公用模块写日志
     *
     * @param dictBigType 字典大类
     * @param logLevel   日志级别
     * @param opt        操作名称
     * @throws ApplicationException
     * @author rutine
     * @time Oct 14, 2012 4:08:38 PM
     */
    private void writeLog(DictBigType dictBigType, LogLevel logLevel, OptName opt) {
        String optContent = "字典大类名称：" + dictBigType.getBigTypeName() + SPLIT;

        sysOptLogService.saveLog(logLevel, opt, SYS_TYPEDIC, optContent, dictBigType.getBigTypeId() + "");
    }
}

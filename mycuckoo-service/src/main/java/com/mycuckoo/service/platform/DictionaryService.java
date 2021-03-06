package com.mycuckoo.service.platform;

import com.mycuckoo.common.constant.LogLevel;
import com.mycuckoo.common.constant.OptName;
import com.mycuckoo.domain.platform.DicBigType;
import com.mycuckoo.domain.platform.DicSmallType;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.DicBigTypeMapper;
import com.mycuckoo.repository.platform.DicSmallTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

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
    private DicBigTypeMapper dicBigTypeMapper;
    @Autowired
    private DicSmallTypeMapper dicSmallTypeMapper;
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public boolean disEnableDicBigType(long bigTypeId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
            DicBigType dicBigType = getDicBigTypeByBigTypeId(bigTypeId);
            dicBigTypeMapper.updateStatus(bigTypeId, DISABLE);

            writeLog(dicBigType, LogLevel.SECOND, OptName.DISABLE);
        } else {
            DicBigType dicBigType = getDicBigTypeByBigTypeId(bigTypeId);
            dicBigTypeMapper.updateStatus(bigTypeId, ENABLE);

            writeLog(dicBigType, LogLevel.SECOND, OptName.ENABLE);
        }

        return true;
    }

    public boolean existDicBigTypeByBigTypeCode(String bigTypeCode) {
        int count = dicBigTypeMapper.countByBigTypeCode(bigTypeCode);
        if (count > 0) return true;

        return false;
    }

    public List<DicSmallType> findDicSmallTypesByBigTypeCode(String bigTypeCode) {
        return dicSmallTypeMapper.findByBigTypeCode(bigTypeCode);
    }

    public DicBigType getDicBigTypeByBigTypeId(long bigTypeId) {
        return dicBigTypeMapper.get(bigTypeId);
    }

    public Page<DicBigType> findDicBigTypesByPage(Map<String, Object> params, Pageable page) {
        return dicBigTypeMapper.findByPage(params, page);
    }

    @Transactional
    public void updateDicBigType(DicBigType dicBigType) {
        DicBigType old = getDicBigTypeByBigTypeId(dicBigType.getBigTypeId());
        Assert.notNull(old, "字典不存在!");
        Assert.state(old.getBigTypeCode().equals(dicBigType.getBigTypeCode())
                || !existDicBigTypeByBigTypeCode(dicBigType.getBigTypeCode()), "编码[" + dicBigType.getBigTypeCode() + "]已存在!");

        dicSmallTypeMapper.deleteByBigTypeId(dicBigType.getBigTypeId());
        dicBigTypeMapper.update(dicBigType);

        for (DicSmallType smallType : dicBigType.getSmallTypes()) {
            smallType.setBigTypeId(dicBigType.getBigTypeId());
        }
        this.saveDicSmallTypes(dicBigType.getSmallTypes());

        writeLog(dicBigType, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void saveDicBigType(DicBigType dicBigType) {
        Assert.state(!existDicBigTypeByBigTypeCode(dicBigType.getBigTypeCode()), "编码[" + dicBigType.getBigTypeCode() + "]已存在!");

        dicBigType.setStatus(ENABLE);
        dicBigTypeMapper.save(dicBigType);

        for (DicSmallType dicSmallType : dicBigType.getSmallTypes()) {
            dicSmallType.setBigTypeId(dicBigType.getBigTypeId());
        }
        this.saveDicSmallTypes(dicBigType.getSmallTypes());

        writeLog(dicBigType, LogLevel.SECOND, OptName.SAVE);
    }

    @Transactional
    public void saveDicSmallTypes(List<DicSmallType> dicSmallTypes) {
        dicSmallTypes.forEach(dicSmallType -> {
            dicSmallTypeMapper.save(dicSmallType);
        });
    }

    // --------------------------- 私有方法-------------------------------

    /**
     * 公用模块写日志
     *
     * @param dicBigType 字典大类
     * @param logLevel   日志级别
     * @param opt        操作名称
     * @throws ApplicationException
     * @author rutine
     * @time Oct 14, 2012 4:08:38 PM
     */
    private void writeLog(DicBigType dicBigType, LogLevel logLevel, OptName opt) {
        String optContent = "字典大类名称：" + dicBigType.getBigTypeName() + SPLIT;

        sysOptLogService.saveLog(logLevel, opt, SYS_TYPEDIC, optContent, dicBigType.getBigTypeId() + "");
    }
}

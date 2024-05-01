package com.mycuckoo.service.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.utils.CommonUtils;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.OperateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.constant.BaseConst.SPLIT;
import static com.mycuckoo.constant.ServiceConst.*;

/**
 * 功能说明: 模块操作业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:35:54 AM
 */
@Service
@Transactional(readOnly = true)
public class OperateService {
    static Logger logger = LoggerFactory.getLogger(OperateService.class);

    @Autowired
    private OperateMapper operateMapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public boolean disEnable(long operateId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
            Operate operate = get(operateId);
            operate.setStatus(DISABLE);
            operateMapper.update(operate); //修改模块操作
            moduleService.deleteModOptRefByOperateId(operateId); //根据操作ID删除模块操作关系,级联删除权限

            writeLog(operate, LogLevel.SECOND, OptName.DISABLE);
        } else {
            Operate operate = get(operateId);
            operate.setStatus(ENABLE);
            operateMapper.update(operate); //修改模块操作

            writeLog(operate, LogLevel.SECOND, OptName.ENABLE);
        }

        return true;
    }

    public List<Operate> findAll() {
        return operateMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
    }

    public Page<Operate> findByPage(String optName, Pageable page) {
        logger.debug("start={} limit={} optName={}", page.getOffset(), page.getPageSize(), optName);

        Map<String, Object> params = Maps.newHashMap();
        if (!CommonUtils.isNullOrEmpty(optName)) {
            params.put("optName", "%" + optName + "%");
        }

        return operateMapper.findByPage(params, page);
    }

    public boolean existsByName(String moduleOptName) {
        int count = operateMapper.countByName(moduleOptName);

        logger.debug("find module total is {}", count);

        if (count > 0) {
            return true;
        }
        return false;
    }

    public Operate get(Long operateId) {
        logger.debug("will find moduleopt id is {}", operateId);

        return operateMapper.get(operateId);
    }

    @Transactional
    public void update(Operate operate) {
        Operate old = get(operate.getOperateId());
        Assert.notNull(old, "操作不存在!");
        Assert.state(old.getOptName().equals(operate.getOptName())
                || !existsByName(operate.getOptName()), "操作名[" + operate.getOptName() + "]已存在!");

        operateMapper.update(operate);

        writeLog(operate, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Operate operate) {
        Assert.state(!existsByName(operate.getOptName()), "操作名[" + operate.getOptName() + "]已存在!");

        operate.setStatus(ENABLE);
        operateMapper.save(operate);

        writeLog(operate, LogLevel.FIRST, OptName.SAVE);
    }


    // --------------------------- 私有方法-------------------------------

    /**
     * 公用模块写日志
     *
     * @param operate  模块对象
     * @param logLevel 日志级别
     * @param opt      操作名称
     * @throws ApplicationException
     * @author rutine
     * @time Oct 14, 2012 1:17:12 PM
     */
    private void writeLog(Operate operate, LogLevel logLevel, OptName opt) {
        StringBuilder optContent = new StringBuilder();
        optContent.append(operate.getOptName()).append(SPLIT)
                .append(operate.getOptIconCls()).append(SPLIT)
                .append(operate.getOptLink()).append(SPLIT);

        sysOptLogService.saveLog(logLevel, opt, SYS_MODOPT_MGR,
                optContent.toString(), operate.getOperateId() + "");
    }

}

package com.mycuckoo.service.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.operator.LogOperator;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.OperateMapper;
import com.mycuckoo.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

import static com.mycuckoo.constant.ServiceConst.DISABLE;
import static com.mycuckoo.constant.ServiceConst.ENABLE;

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


    @Transactional
    public boolean disEnable(long id, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        Operate operate = get(id);
        if (!enable) {
            operate.setStatus(DISABLE);
            operateMapper.update(operate); //修改模块操作
            moduleService.deleteModOptRefByOperateId(id); //根据操作ID删除模块操作关系,级联删除权限
        } else {
            operate.setStatus(ENABLE);
            operateMapper.update(operate); //修改模块操作
        }

        writeLog(operate, LogLevel.SECOND, enable ?  OptName.ENABLE : OptName.DISABLE);

        return true;
    }

    public List<Operate> findAll() {
        return operateMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE)).getContent();
    }

    public Page<Operate> findByPage(String optName, Pageable page) {
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
     * @param entity  模块对象
     * @param logLevel 日志级别
     * @param opt      操作名称
     * @throws ApplicationException
     * @author rutine
     * @time Oct 14, 2012 1:17:12 PM
     */
    private void writeLog(Operate entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.SYS_MODOPT_MGR)
                .operate(opt)
                .id(entity.getOperateId())
                .title(null)
                .content("操作名称：%s, 图标：%s, url: %s",
                        entity.getOptName(), entity.getOptIconCls(), entity.getOptLink())
                .level(logLevel)
                .emit();
    }

}

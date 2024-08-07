package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.repository.platform.OperateMapper;
import com.mycuckoo.core.util.web.SessionContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

import static com.mycuckoo.constant.AdminConst.DISABLE;
import static com.mycuckoo.constant.AdminConst.ENABLE;

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
        return operateMapper.findByPage(null, Querier.EMPTY).getContent();
    }

    public Page<Operate> findByPage(Querier querier) {
        return operateMapper.findByPage(querier.getQ(), querier);
    }

    public boolean existsByCode(String code) {
        int count = operateMapper.countByCode(code);

        logger.debug("find module total is {}", count);

        if (count > 0) {
            return true;
        }
        return false;
    }

    public boolean existsByName(String name) {
        int count = operateMapper.countByName(name);

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
        Assert.state(old.getCode().equals(operate.getCode())
                || !existsByCode(operate.getCode()), "操作编码[" + operate.getCode() + "]已存在!");
        Assert.state(old.getName().equals(operate.getName())
                || !existsByName(operate.getName()), "操作名[" + operate.getName() + "]已存在!");

        //不允许修改
        operate.setCode(null);

        operate.setUpdator(SessionContextHolder.getUserId().toString());
        operate.setUpdateTime(LocalDateTime.now());
        operateMapper.update(operate);

        writeLog(operate, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Operate operate) {
        Assert.state(!existsByCode(operate.getCode()), "操作编码[" + operate.getCode() + "]已存在!");
        Assert.state(!existsByName(operate.getName()), "操作名[" + operate.getName() + "]已存在!");

        operate.setStatus(ENABLE);
        operate.setUpdator(SessionContextHolder.getUserId().toString());
        operate.setUpdateTime(LocalDateTime.now());
        operate.setCreator(SessionContextHolder.getUserId().toString());
        operate.setCreateTime(LocalDateTime.now());
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
     * @throws MyCuckooException
     * @author rutine
     * @time Oct 14, 2012 1:17:12 PM
     */
    private void writeLog(Operate entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.SYS_OPT_MGR)
                .operate(opt)
                .id(entity.getOperateId())
                .title(null)
                .content("操作名称：%s, 图标：%s, url: %s",
                        entity.getName(), entity.getIconCls(), entity.getCode())
                .level(logLevel)
                .emit();
    }

}

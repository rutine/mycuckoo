package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.SystemException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SchedulerJob;
import com.mycuckoo.repository.platform.SchedulerJobMapper;
import com.mycuckoo.core.util.web.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mycuckoo.constant.AdminConst.DISABLE;
import static com.mycuckoo.constant.AdminConst.ENABLE;

/**
 * 功能说明: 任务业务类
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 25, 2014 10:48:05 AM
 */
@Service
@Transactional(readOnly = true)
public class SchedulerService {
    static Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private SchedulerJobMapper schedulerJobMapper;


    @Transactional
    public void delete(Long jobId) throws SystemException {
        SchedulerJob old = get(jobId);
        schedulerJobMapper.delete(jobId);
        SchedulerHandle.getInstance().stopJob(old.getJobName());

        LogOperator.begin()
                .module(ModuleName.SYS_SCHEDULER)
                .operate(OptName.DELETE)
                .id(old.getJobId())
                .title(null)
                .content("任务名称：%s, 任务类描述：%s, 触发器类型: %s, 时间表达式: %s",
                        old.getJobName(), old.getJobClass(), old.getTriggerType(), old.getCron())
                .level(LogLevel.THIRD)
                .emit();
    }

    public Page<SchedulerJob> findByPage(Querier querier) {
        return schedulerJobMapper.findByPage(querier.getQ(), querier);
    }

    public SchedulerJob get(Long jobId) {
        return schedulerJobMapper.get(jobId);
    }

    public boolean existsByJobName(String jobName) {
        int count = schedulerJobMapper.countByJobName(jobName);
        if (count > 0) return true;

        return false;
    }

    @Transactional
    public void update(SchedulerJob entity) {
        SchedulerJob old = get(entity.getJobId());
        Assert.notNull(old, "任务不存在!");
        Assert.state(old.getJobId().equals(entity.getJobId())
                || !existsByJobName(entity.getJobName()), "任务名称[" + entity.getJobName() + "]已存在!");

        entity.setUpdateTime(LocalDateTime.now());
        entity.setUpdator(SessionUtil.getUserId().toString());
        schedulerJobMapper.update(entity);

        LogOperator.begin()
                .module(ModuleName.SYS_SCHEDULER)
                .operate(OptName.MODIFY)
                .id(old.getJobId())
                .title(null)
                .content("任务名称：%s, 任务类描述：%s, 触发器类型: %s, 时间表达式: %s",
                        old.getJobName(), old.getJobClass(), old.getTriggerType(), old.getCron())
                .level(LogLevel.SECOND)
                .emit();
    }

    @Transactional
    public void save(SchedulerJob entity) throws SystemException {
        Assert.state(!existsByJobName(entity.getJobName()), "任务名称[" + entity.getJobName() + "]已存在!");

        entity.setStatus(ENABLE);
        entity.setUpdateTime(LocalDateTime.now());
        entity.setUpdator(SessionUtil.getUserId().toString());
        entity.setCreateTime(LocalDateTime.now());
        entity.setCreator(SessionUtil.getUserId().toString());
        schedulerJobMapper.save(entity);
        if (ENABLE.equals(entity.getStatus())) { //增加任务且状态为启用将任务加入调度
            SchedulerHandle.getInstance().startJob(entity);
        }

        LogOperator.begin()
                .module(ModuleName.SYS_SCHEDULER)
                .operate(OptName.SAVE)
                .id(entity.getJobId())
                .title(null)
                .content("任务名称：%s, 任务类描述：%s, 触发器类型: %s, 时间表达式: %s",
                        entity.getJobName(), entity.getJobClass(), entity.getTriggerType(), entity.getCron())
                .level(LogLevel.FIRST)
                .emit();
    }

    @Transactional
    public void startScheduler() throws SystemException {
        List<SchedulerJob> jobList = findAll();
        List<Long> jobIds = new ArrayList<>();
        for (SchedulerJob job : jobList) {
            jobIds.add(job.getJobId());
        }

        schedulerJobMapper.updateStatuses(jobIds.toArray(new Long[jobIds.size()]), ENABLE);

        SchedulerHandle scheduler = SchedulerHandle.getInstance();
        scheduler.setJobList(jobList);
        scheduler.startScheduler(true);

        LogOperator.begin()
                .module(ModuleName.SYS_SCHEDULER)
                .operate(OptName.START_SCHEDULER)
                .id("")
                .title(null)
                .content("启动调度器并初始化任务")
                .level(LogLevel.THIRD)
                .emit();
    }

    @Transactional
    public void stopScheduler() throws SystemException {
        List<SchedulerJob> jobList = findAll();
        List<Long> jobIds = new ArrayList<>();
        for (SchedulerJob job : jobList) {
            jobIds.add(job.getJobId());
        }

        SchedulerHandle.getInstance().stopScheduler();

        schedulerJobMapper.updateStatuses(jobIds.toArray(new Long[jobIds.size()]), DISABLE);

        LogOperator.begin()
                .module(ModuleName.SYS_SCHEDULER)
                .operate(OptName.STOP_SCHEDULER)
                .id("")
                .title(null)
                .content("停止调度器")
                .level(LogLevel.THIRD)
                .emit();
    }

    @Transactional
    public void startJob(long jobId) throws SystemException {
        SchedulerJob entity = get(jobId);
        SchedulerHandle.getInstance().startJob(entity);

        schedulerJobMapper.updateStatus(jobId, ENABLE); // 更改状态

        LogOperator.begin()
                .module(ModuleName.SYS_SCHEDULER)
                .operate(OptName.START_JOB)
                .id(entity.getJobId())
                .title(null)
                .content("启动job：%s, job类描述：%s, 触发器类型: %s",
                        entity.getJobName(), entity.getJobClass(), entity.getTriggerType())
                .level(LogLevel.THIRD)
                .emit();
    }

    @Transactional
    public void stopJob(long jobId) throws SystemException {
        SchedulerJob entity = get(jobId);
        SchedulerHandle.getInstance().stopJob(entity.getJobName());
        schedulerJobMapper.updateStatus(jobId, DISABLE); //  更改状态

        LogOperator.begin()
                .module(ModuleName.SYS_SCHEDULER)
                .operate(OptName.STOP_JOB)
                .id(entity.getJobId())
                .title(null)
                .content("停止job：%s", entity.getJobName())
                .level(LogLevel.THIRD)
                .emit();
    }


    // -----------------------------私有方法----------------------------

    /**
     * 查询所有任务调度
     *
     * @return
     * @author rutine
     * @time Oct 30, 2012 8:10:54 PM
     */
    private List<SchedulerJob> findAll() {
        Page<SchedulerJob> page = schedulerJobMapper.findByPage(null, Querier.EMPTY);

        return page.getContent();
    }

}

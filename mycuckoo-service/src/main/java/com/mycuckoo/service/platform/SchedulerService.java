package com.mycuckoo.service.platform;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.platform.SchedulerJob;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.SchedulerJobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

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
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public void delete(Long jobId) throws SystemException {
        SchedulerJob schedulerJob = get(jobId);
        schedulerJobMapper.delete(jobId);
        SchedulerHandle.getInstance().stopJob(schedulerJob.getJobName());

        StringBuilder optContent = new StringBuilder();
        optContent.append("任务名称：").append(schedulerJob.getJobName()).append(SPLIT);
        optContent.append("任务类描述:").append(schedulerJob.getJobClass()).append(SPLIT);
        optContent.append("触发器类型:").append(schedulerJob.getTriggerType()).append(SPLIT);
        optContent.append("时间表达式:").append(schedulerJob.getCronExpression()).append(SPLIT);
        sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.DELETE, SYS_SCHEDULER,
                optContent.toString(), schedulerJob.getJobId().toString());
    }

    public Page<SchedulerJob> findByPage(Map<String, Object> params, Pageable page) {
        return schedulerJobMapper.findByPage(params, page);
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
    public void update(SchedulerJob schedulerJob) {
        schedulerJobMapper.update(schedulerJob);

        StringBuilder optContent = new StringBuilder();
        optContent.append("任务名称：").append(schedulerJob.getJobName()).append(SPLIT);
        optContent.append("任务类描述:").append(schedulerJob.getJobClass()).append(SPLIT);
        optContent.append("触发器类型:").append(schedulerJob.getTriggerType()).append(SPLIT);
        optContent.append("时间表达式:").append(schedulerJob.getCronExpression()).append(SPLIT);
        sysOptLogService.saveLog(LogLevelEnum.SECOND, OptNameEnum.MODIFY, SYS_SCHEDULER,
                optContent.toString(), schedulerJob.getJobId().toString());
    }

    @Transactional
    public void save(SchedulerJob schedulerJob) throws SystemException {
        schedulerJobMapper.save(schedulerJob);
        if (ENABLE.equals(schedulerJob.getStatus())) { //增加任务且状态为启用将任务加入调度
            SchedulerHandle.getInstance().startJob(schedulerJob);
        }

        StringBuilder optContent = new StringBuilder();
        optContent.append("任务名称：").append(schedulerJob.getJobName()).append(SPLIT);
        optContent.append("任务类描述:").append(schedulerJob.getJobClass()).append(SPLIT);
        optContent.append("触发器类型:").append(schedulerJob.getTriggerType()).append(SPLIT);
        optContent.append("时间表达式:").append(schedulerJob.getCronExpression()).append(SPLIT);
        sysOptLogService.saveLog(LogLevelEnum.FIRST, OptNameEnum.SAVE, SYS_SCHEDULER,
                optContent.toString(), schedulerJob.getJobId().toString());
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

        sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.START_SCHEDULER, SYS_SCHEDULER, "启动调度器并初始化任务", "");
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

        sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.STOP_SCHEDULER, SYS_SCHEDULER, "停止调度器", "");
    }

    @Transactional
    public void startJob(long jobId) throws SystemException {
        SchedulerJob schedulerJob = get(jobId);
        SchedulerHandle.getInstance().startJob(schedulerJob);

        schedulerJobMapper.updateStatus(jobId, ENABLE); // 更改状态

        StringBuilder optContent = new StringBuilder();
        optContent.append("启动job::").append(schedulerJob.getJobName()).append(SPLIT);
        optContent.append("job类描述:").append(schedulerJob.getJobClass()).append(SPLIT);
        optContent.append("触发器类型:").append(schedulerJob.getTriggerType()).append(SPLIT);
        sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.START_JOB, SYS_SCHEDULER,
                optContent.toString(), "" + jobId);
    }

    @Transactional
    public void stopJob(long jobId, String jobName) throws SystemException {
        SchedulerHandle.getInstance().stopJob(jobName);
        schedulerJobMapper.updateStatus(jobId, DISABLE); //  更改状态

        sysOptLogService.saveLog(LogLevelEnum.THIRD, OptNameEnum.STOP_JOB, SYS_SCHEDULER, "停止job", jobId + "");
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
        Page<SchedulerJob> page = schedulerJobMapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE));

        return page.getContent();
    }

}

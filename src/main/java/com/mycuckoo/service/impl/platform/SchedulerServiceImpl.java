package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.DELETE_OPT;
import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.ENABLE;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_SECOND;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_THIRD;
import static com.mycuckoo.common.constant.Common.MODIFY_OPT;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_SCHEDULER;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSchedulerJob;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplSchedulerJobRepository;
import com.mycuckoo.service.iface.platform.SchedulerService;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 任务业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:48:05 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class SchedulerServiceImpl implements SchedulerService {
	
	static Logger logger = LoggerFactory.getLogger(SchedulerServiceImpl.class);
	private SysplSchedulerJobRepository schedulerJobRepository;
	private SystemOptLogService sysOptLogService;

	@Transactional(readOnly = false)
	@Override
	public void deleteSchedulerJobByJobId(Long jobId, HttpServletRequest request) 
			throws ApplicationException {
		
		SysplSchedulerJob sysplSchedulerJob = getSchedulerJobByJobId(jobId);
		schedulerJobRepository.delete(jobId);
		SchedulerHandle.getInstance().stopJob(sysplSchedulerJob.getJobName());
		String optContent = "任务名称：" + sysplSchedulerJob.getJobName() + SPLIT
			+ "任务类描述:" + sysplSchedulerJob.getJobClassDescript() + SPLIT
			+ "触发器类型:" + sysplSchedulerJob.getTriggerType() + SPLIT
			+ "时间表达式:" + sysplSchedulerJob.getTimeExpress() + SPLIT;
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, SYS_SCHEDULER, DELETE_OPT, optContent.toString(), 
				sysplSchedulerJob.getJobId().toString(), request);
	}

	@Override
	public Page<SysplSchedulerJob> findSchedulerJobsByCon(String jobName, String triggerType, Pageable page) {
		return schedulerJobRepository.findSchedulerJobsByCon(jobName, triggerType, page);
	}

	@Override
	public SysplSchedulerJob getSchedulerJobByJobId(Long jobId) {
		return schedulerJobRepository.get(jobId);
	}

	@Override
	public boolean existsSchedulerJobByJobName(String jobName) {
		int count = schedulerJobRepository.countSchedulerJobsByJobName(jobName);
		if(count > 0 ) return true;
		
		return false;
	}

	@Transactional(readOnly=false)
	@Override
	public void updateSchedulerJob(SysplSchedulerJob sysplSchedulerJob, 
			HttpServletRequest request) throws ApplicationException {
		
		schedulerJobRepository.update(sysplSchedulerJob);
		String optContent = "任务名称：" + sysplSchedulerJob.getJobName() + SPLIT
			+ "任务类描述:" + sysplSchedulerJob.getJobClassDescript() + SPLIT
			+ "触发器类型:" + sysplSchedulerJob.getTriggerType() + SPLIT
			+ "时间表达式:" + sysplSchedulerJob.getTimeExpress() + SPLIT;
		sysOptLogService.saveLog(LOG_LEVEL_SECOND, SYS_SCHEDULER, MODIFY_OPT, optContent.toString(), 
				sysplSchedulerJob.getJobId().toString(), request);
	}

	@Transactional(readOnly=false)
	@Override
	public void saveSchedulerJob(SysplSchedulerJob sysplSchedulerJob, 
			HttpServletRequest request) throws ApplicationException {
		
		schedulerJobRepository.save(sysplSchedulerJob);
		if(ENABLE.equals(sysplSchedulerJob.getStatus())){ //增加任务且状态为启用将任务加入调度
			SchedulerHandle.getInstance().startJob(sysplSchedulerJob);
		}
		String optContent = "任务名称：" + sysplSchedulerJob.getJobName() + SPLIT
			+ "任务类描述:" + sysplSchedulerJob.getJobClassDescript() + SPLIT
			+ "触发器类型:" + sysplSchedulerJob.getTriggerType() + SPLIT
			+ "时间表达式:" + sysplSchedulerJob.getTimeExpress() + SPLIT;
		sysOptLogService.saveLog(LOG_LEVEL_FIRST, SYS_SCHEDULER, SAVE_OPT, optContent.toString(), 
				sysplSchedulerJob.getJobId().toString(), request);
	}

	@Transactional(readOnly=false)
	@Override
	public void startScheduler(HttpServletRequest request) throws ApplicationException {
		List<SysplSchedulerJob> jobList = findAllSchedulerJobs();
		SchedulerHandle scheduler = SchedulerHandle.getInstance();
		scheduler.setJobList(jobList);
		List<Long> jobIds = new ArrayList<Long>();
		for(SysplSchedulerJob job : jobList) {
			jobIds.add(job.getJobId());
		}
		schedulerJobRepository.updateSchedulerJobsStatus(jobIds.toArray(new Long[0]), ENABLE);
		scheduler.startScheduler(true);
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, SYS_SCHEDULER, "启动调度器", "启动调度器并初始化任务", "", request);
	}
	
	@Transactional(readOnly=false)
	@Override
	public void stopScheduler(HttpServletRequest request) throws ApplicationException {
		SchedulerHandle.getInstance().stopScheduler();
		List<SysplSchedulerJob> jobList = findAllSchedulerJobs();
		List<Long> jobIds = new ArrayList<Long>();
		for(SysplSchedulerJob job : jobList) {
			jobIds.add(job.getJobId());
		}
		schedulerJobRepository.updateSchedulerJobsStatus(jobIds.toArray(new Long[0]), DISABLE);
		try {
			sysOptLogService.saveLog(LOG_LEVEL_THIRD, SYS_SCHEDULER, "停止调度器", "停止调度器", "", request);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	@Transactional(readOnly=false)
	@Override
	public void startJob(long jobId, HttpServletRequest request) throws ApplicationException {
		SysplSchedulerJob sysplSchedulerJob = getSchedulerJobByJobId(jobId);
		SchedulerHandle.getInstance().startJob(sysplSchedulerJob);
		String optContent = "启动job:" + SPLIT 
			+ "jobName:" + sysplSchedulerJob.getJobName() + SPLIT 
			+ "job类描述:" + sysplSchedulerJob.getJobClassDescript() + SPLIT 
			+ "触发器类型:" + sysplSchedulerJob.getTriggerType() + SPLIT;
		schedulerJobRepository.updateScheduleJobStatus(jobId, ENABLE); // 更改状态
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, SYS_SCHEDULER, "启动job", optContent, "" + jobId, request);
	}
	
	@Transactional(readOnly=false)
	@Override
	public void stopJob(long jobId, String jobName, HttpServletRequest request) 
			throws ApplicationException {
		
		SchedulerHandle.getInstance().stopJob(jobName);
		schedulerJobRepository.updateScheduleJobStatus(jobId, DISABLE); //  更改状态
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, SYS_SCHEDULER, "停止job", "停止job", "", request);
	}



	
	// -----------------------------私有方法----------------------------
	/**
	 * 查询所有任务调度
	 *
	 * @return
	 * @author rutine
	 * @time Oct 30, 2012 8:10:54 PM
	 */
	private List<SysplSchedulerJob> findAllSchedulerJobs() {
		return (List<SysplSchedulerJob>) schedulerJobRepository.findAll();
	}
	
	
	
	
	
	// -----------------------------依赖注入----------------------------
	@Autowired
	public void setSchedulerJobRepository(
		SysplSchedulerJobRepository schedulerJobRepository) {
		this.schedulerJobRepository = schedulerJobRepository;
	}
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

}

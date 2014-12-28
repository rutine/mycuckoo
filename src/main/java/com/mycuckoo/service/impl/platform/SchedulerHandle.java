package com.mycuckoo.service.impl.platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycuckoo.domain.platform.SysplSchedulerJob;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 调度器操作
 *
 * @author rutine
 * @time Sep 25, 2014 10:44:47 AM
 * @version 2.0.0
 */
public class SchedulerHandle {

	private static Logger logger = LoggerFactory.getLogger(SchedulerHandle.class);
	private static SchedulerHandle schedulerHandle = new SchedulerHandle();
	
	private static final String SIMPLE = "simple";
	private static final String CRON = "cron";
	private static final String TRIGGER_NAME = "_trigger";
	
	private SchedulerFactory sf = new StdSchedulerFactory();
	private Scheduler scheduler = null;
	private List<SysplSchedulerJob> jobList;
	
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		SchedulerHandle handler = SchedulerHandle.getInstance();
		List<SysplSchedulerJob> jobList = new ArrayList<SysplSchedulerJob>();
		SysplSchedulerJob job = new SysplSchedulerJob();
		job.setJobName("jobtest");
		job.setTriggerType(SchedulerHandle.SIMPLE);
		job.setJobClassDescript("com.mycuckoo.service.impl.platform.module.JobTest");
		job.setRepeatTime(10);
		job.setSplitTime(5000L);
		job.setStartTime(new Date());
		jobList.add(job);
		handler.setJobList(jobList);
		try {
			while (true) {
				String command = reader.readLine();
				if ("exit".equals(command)) {
					System.exit(0);
				} else if ("startScheduler".equals(command)) {
					handler.startScheduler(true);
				} else if ("stopScheduler".equals(command)) {
					handler.stopScheduler();
				} else if ("jobtest".equals(command)) {
					boolean bol = handler.existsJobTask("jobtest");
					System.out.println("jobtest wether exist " + bol);
				} else if ("startJob".equals(command)) {
					SysplSchedulerJob job2 = new SysplSchedulerJob();
					job2.setJobName("jobtest2");
					job2.setTriggerType(SchedulerHandle.CRON);
					job2.setJobClassDescript("com.mycuckoo.service.impl.platform.module.JobTest");
					job2.setRepeatTime(10);
					job2.setSplitTime(5000L);
					job2.setStartTime(new Date());
					job2.setTimeExpress("0/3 * * * * ?");
					handler.startJob(job2);
				} else if ("stopJob".equals(command)) {
					handler.stopJob("jobtest2");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private SchedulerHandle() { }
	
	public static SchedulerHandle getInstance() {
		return schedulerHandle;
	}

	/**
	 * 启动调度器
	 * 
	 * @param isInit 是否需要初始化 true 需要 false 不需要
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 10:00:39 AM
	 */
	public void startScheduler(boolean isInit) throws ApplicationException {
		try {
			if (scheduler == null) {
				scheduler = sf.getScheduler();
				scheduler.start();
				logger.info("scheduler has already started.");
				if (isInit) init(jobList); // 初始化所有任务
			}
		} catch (SchedulerException e) {
			throw new ApplicationException("start job Scheduler failure!", e);
		}
	}
	
	/**
	 * 停止调度器
	 *
	 * @author rutine
	 * @time Oct 27, 2012 10:06:58 AM
	 */
	public void stopScheduler() throws ApplicationException {
		try {
			if(scheduler != null) {
				scheduler.shutdown();
				scheduler = null;
				logger.info("scheduler has already stopped.");
			}
		} catch(SchedulerException e) {
			throw new ApplicationException("stop job Scheduler failure!", e);
		}
	}
	
	
	/**
	 * 开始job
	 *
	 * @param schedulerJob   任务对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 1:15:27 PM
	 */
	public void startJob(SysplSchedulerJob schedulerJob) throws ApplicationException {
		if(schedulerJob == null) {
			throw new ApplicationException("job task is null");
		}
		startScheduler(false);
		addJob(schedulerJob);
		
		logger.info("job task has already added.");
	}
	
	/**
	 * 停止job
	 *
	 * @param jobName   任务名称
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 1:15:36 PM
	 */
	public void stopJob(String jobName) throws ApplicationException {
		try {
			if (scheduler == null) return;
			boolean triggerBol = scheduler.unscheduleJob(jobName + TRIGGER_NAME, Scheduler.DEFAULT_GROUP);
			boolean jobBol = scheduler.deleteJob(jobName, Scheduler.DEFAULT_GROUP);
			
			logger.info(jobName + TRIGGER_NAME + " stop task result " + triggerBol);
			logger.info(jobName + " job stop task result " + jobBol);
		} catch (SchedulerException e) {
			throw new ApplicationException("stop job task failure!", e);
		}
	}
	
	/**
	 * 判断任务是否已经存在并运行中
	 *
	 * @param jobName 任务名称
	 * @return
	 * @author rutine
	 * @time Oct 27, 2012 10:25:01 AM
	 */
	public boolean existsJobTask(String jobName) throws ApplicationException {
		boolean isExists = false;
		if(scheduler == null) {
			return isExists;
		}
		
		try {
			String[] jobNames = scheduler.getJobNames(Scheduler.DEFAULT_GROUP);
			for(String jobNamee : jobNames) {
				if(jobNamee.equals(jobName))  {
					isExists = true;
					break;
				}
			}
		} catch(SchedulerException e) {
			throw new ApplicationException(e);
		}
		
		return isExists;
	}
	
	
	// ----------------------private-----------------------
	/**
	 * 初始化所有jobs
	 *
	 * @param jobList
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 10:17:05 AM
	 */
	private void init(List<SysplSchedulerJob> jobList) throws ApplicationException {
		if(jobList == null) throw new ApplicationException("jobList is null");
		for(SysplSchedulerJob sysplSchedulerJob : jobList) {
			addJob(sysplSchedulerJob);
		}
		
		logger.info("job scheduler has already inited.");
	}
	/**
	 * 调度中增加任务
	 *
	 * @param sysplSchedulerJob 任务对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 27, 2012 10:19:30 AM
	 */
	private void addJob(SysplSchedulerJob sysplSchedulerJob) throws ApplicationException {
		String jobName = sysplSchedulerJob.getJobName();
		if(existsJobTask(jobName)) {
			logger.info("{} job task already exists", jobName);
			return;
		}
		
		String jobClassName = sysplSchedulerJob.getJobClassDescript();
		String triggerType = sysplSchedulerJob.getTriggerType();
		String timeExpress = sysplSchedulerJob.getTimeExpress();
		Date startTime = sysplSchedulerJob.getStartTime() == null ? new Date() : sysplSchedulerJob.getStartTime();
		Date endTime = sysplSchedulerJob.getEndTime();
		int repeatCount = sysplSchedulerJob.getRepeatTime() == null ? 0 : sysplSchedulerJob.getRepeatTime();
		long repeatInterval = sysplSchedulerJob.getSplitTime() == null ? 0 : sysplSchedulerJob.getSplitTime();
		
		JobDetail job = null;
		Trigger trigger = null;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(jobClassName);
		} catch(ClassNotFoundException e) {
			throw new ApplicationException(e);
		}
		
		try {
			if (SIMPLE.equalsIgnoreCase(triggerType)) {
				job = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, clazz);
				trigger = new SimpleTrigger(jobName + TRIGGER_NAME, Scheduler.DEFAULT_GROUP, startTime, endTime, repeatCount, repeatInterval);
			} else if (CRON.equalsIgnoreCase(triggerType)) {
				job = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, clazz);
				trigger = new CronTrigger(jobName + TRIGGER_NAME, Scheduler.DEFAULT_GROUP, timeExpress);
			}
			scheduler.scheduleJob(job, trigger);
		} catch (ParseException e) {
			throw new ApplicationException("parsing " + jobName + " job task occurs error!", e);
		} catch (SchedulerException e) {
			throw new ApplicationException("starting " + jobName + " job task failure!", e);
		}
	}
	
	
	// ----------------------getter、setter-----------------------
	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public List<SysplSchedulerJob> getJobList() {
		return jobList;
	}

	public void setJobList(List<SysplSchedulerJob> jobList) {
		this.jobList = jobList;
	}
	
}

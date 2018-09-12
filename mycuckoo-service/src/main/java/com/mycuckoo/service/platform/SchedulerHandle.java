package com.mycuckoo.service.platform;

import com.mycuckoo.domain.platform.SchedulerJob;
import com.mycuckoo.exception.SystemException;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能说明: 调度器操作
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 25, 2014 10:44:47 AM
 */
public class SchedulerHandle {
    private static final String SIMPLE = "simple";
    private static final String CRON = "cron";
    private static final String TRIGGER_NAME = "_trigger";

    private static Logger logger = LoggerFactory.getLogger(SchedulerHandle.class);
    private static SchedulerHandle schedulerHandle = new SchedulerHandle();


    private SchedulerFactory sf = new StdSchedulerFactory();
    private Scheduler scheduler = null;
    private List<SchedulerJob> jobList;


    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SchedulerHandle handler = SchedulerHandle.getInstance();
        List<SchedulerJob> jobList = new ArrayList<SchedulerJob>();
        SchedulerJob job = new SchedulerJob();
        job.setJobName("testJob");
        job.setTriggerType(SchedulerHandle.SIMPLE);
        job.setJobClass("com.mycuckoo.service.platform.job.TestJob");
        job.setRepeatCount(10);
        job.setIntervalTime(5000L);
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
                } else if ("testJob".equals(command)) {
                    boolean bol = handler.existsJobTask("testJob");
                    System.out.println("jobtest wether exist " + bol);
                } else if ("startJob".equals(command)) {
                    SchedulerJob job2 = new SchedulerJob();
                    job2.setJobName("testJob2");
                    job2.setTriggerType(SchedulerHandle.CRON);
                    job2.setJobClass("com.mycuckoo.service.platform.job.TestJob");
                    job2.setRepeatCount(10);
                    job2.setIntervalTime(5000L);
                    job2.setStartTime(new Date());
                    job2.setCronExpression("0/3 * * * * ?");
                    handler.startJob(job2);
                } else if ("stopJob".equals(command)) {
                    handler.stopJob("testJob2");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private SchedulerHandle() {
    }

    public static SchedulerHandle getInstance() {
        return schedulerHandle;
    }

    /**
     * 启动调度器
     *
     * @param isInit 是否需要初始化 true 需要 false 不需要
     * @throws SystemException
     * @author rutine
     * @time Oct 27, 2012 10:00:39 AM
     */
    public void startScheduler(boolean isInit) throws SystemException {
        try {
            if (scheduler == null) {
                scheduler = sf.getScheduler();
                scheduler.start();
                logger.info("scheduler has already started.");
                if (isInit) init(jobList); // 初始化所有任务
            }
        } catch (SchedulerException e) {
            throw new SystemException("start job Scheduler failure!", e);
        }
    }

    /**
     * 停止调度器
     *
     * @author rutine
     * @time Oct 27, 2012 10:06:58 AM
     */
    public void stopScheduler() throws SystemException {
        try {
            if (scheduler != null) {
                scheduler.shutdown();
                scheduler = null;
                logger.info("scheduler has already stopped.");
            }
        } catch (SchedulerException e) {
            throw new SystemException("stop job Scheduler failure!", e);
        }
    }


    /**
     * 开始job
     *
     * @param schedulerJob 任务对象
     * @throws SystemException
     * @author rutine
     * @time Oct 27, 2012 1:15:27 PM
     */
    public void startJob(SchedulerJob schedulerJob) throws SystemException {
        if (schedulerJob == null) {
            throw new SystemException("job task is null");
        }
        startScheduler(false);
        addJob(schedulerJob);

        logger.info("job task has already added.");
    }

    /**
     * 停止job
     *
     * @param jobName 任务名称
     * @throws SystemException
     * @author rutine
     * @time Oct 27, 2012 1:15:36 PM
     */
    public void stopJob(String jobName) throws SystemException {
        try {
            if (scheduler == null) return;

            TriggerKey triggerKey = new TriggerKey(jobName + TRIGGER_NAME, Scheduler.DEFAULT_GROUP);
            boolean triggerBol = scheduler.unscheduleJob(triggerKey);
            boolean jobBol = scheduler.deleteJob(new JobKey(jobName, Scheduler.DEFAULT_GROUP));

            logger.info("{}{} stop task result: {}", jobName, TRIGGER_NAME, triggerBol);
            logger.info("{} job stop task result ", jobName, jobBol);
        } catch (SchedulerException e) {
            throw new SystemException("stop job task failure!", e);
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
    public boolean existsJobTask(String jobName) throws SystemException {
        boolean isExists = false;
        if (scheduler == null) {
            return isExists;
        }

        try {
            return scheduler.checkExists(new JobKey(jobName, Scheduler.DEFAULT_GROUP));
        } catch (SchedulerException e) {
            throw new SystemException("check exists job error", e);
        }
    }


    // ----------------------private-----------------------

    /**
     * 初始化所有jobs
     *
     * @param jobList
     * @throws SystemException
     * @author rutine
     * @time Oct 27, 2012 10:17:05 AM
     */
    private void init(List<SchedulerJob> jobList) throws SystemException {
        if (jobList == null) throw new SystemException("jobList is null");
        for (SchedulerJob schedulerJob : jobList) {
            addJob(schedulerJob);
        }

        logger.info("job scheduler has already inited.");
    }

    /**
     * 调度中增加任务
     *
     * @param schedulerJob 任务对象
     * @throws SystemException
     * @author rutine
     * @time Oct 27, 2012 10:19:30 AM
     */
    private void addJob(SchedulerJob schedulerJob) throws SystemException {
        String jobName = schedulerJob.getJobName();
        if (existsJobTask(jobName)) {
            logger.info("{} job task already exists", jobName);
            return;
        }

        String jobClassName = schedulerJob.getJobClass();
        String triggerType = schedulerJob.getTriggerType();
        String cronExpression = schedulerJob.getCronExpression();
        Date startTime = schedulerJob.getStartTime() == null ? new Date() : schedulerJob.getStartTime();
        Date endTime = schedulerJob.getEndTime();
        int repeatCount = schedulerJob.getRepeatCount() == null ? 0 : schedulerJob.getRepeatCount();
        long intervalTime = schedulerJob.getIntervalTime() == null ? 0 : schedulerJob.getIntervalTime();
        Class<? extends Job> clazz = null;
        try {
            clazz = (Class<? extends Job>) Class.forName(jobClassName);
        } catch (ClassNotFoundException e) {
            throw new SystemException("add job error", e);
        }

        try {
            JobDetail job = null;
            Trigger trigger = null;
            if (SIMPLE.equalsIgnoreCase(triggerType)) {
                job = JobBuilder.newJob(clazz).withIdentity(jobName, Scheduler.DEFAULT_GROUP).build();
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName + TRIGGER_NAME, Scheduler.DEFAULT_GROUP)
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                .withRepeatCount(repeatCount)
                                .withIntervalInMilliseconds(intervalTime)
                        )
                        .startAt(startTime)
                        .endAt(endTime)
                        .build();
            } else if (CRON.equalsIgnoreCase(triggerType)) {
                job = JobBuilder.newJob(clazz).withIdentity(jobName, Scheduler.DEFAULT_GROUP).build();
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(jobName + TRIGGER_NAME, Scheduler.DEFAULT_GROUP)
                        .withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression(cronExpression)))
                        .startAt(startTime)
                        .endAt(endTime)
                        .build();
            }
            scheduler.scheduleJob(job, trigger);
        } catch (ParseException e) {
            throw new SystemException("parsing " + jobName + " job task occurs error!", e);
        } catch (SchedulerException e) {
            throw new SystemException("starting " + jobName + " job task failure!", e);
        }
    }


    // ----------------------setter-----------------------
    public void setJobList(List<SchedulerJob> jobList) {
        this.jobList = jobList;
    }

}

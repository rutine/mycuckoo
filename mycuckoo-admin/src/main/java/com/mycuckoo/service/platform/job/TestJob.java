package com.mycuckoo.service.platform.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能说明: 删除日志Job
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:35:31 AM
 */
public class TestJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(TestJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String name = context.getJobDetail().getKey().getName();

        logger.info("job name is: {}", name);
    }

}

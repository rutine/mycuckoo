package com.mycuckoo.service.impl.platform.job;

import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * 功能说明: 删除日志Job
 *
 * @author rutine
 * @time Sep 25, 2014 10:35:31 AM
 * @version 2.0.0
 */
public class JobTest implements StatefulJob {
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String name = context.getJobDetail().getName();
		System.out.println("job name is: " + name + " " + (new Date()));
	}

}

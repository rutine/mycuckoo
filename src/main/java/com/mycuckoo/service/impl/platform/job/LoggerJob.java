package com.mycuckoo.service.impl.platform.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycuckoo.common.utils.SpringContextUtils;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.service.iface.platform.SystemOptLogService;

/**
 * 功能说明: 定时删除系统日志任务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:58:00 AM
 * @version 2.0.0
 */
public class LoggerJob implements Job {
	
	private static Logger logger = LoggerFactory.getLogger(LoggerJob.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String name = context.getJobDetail().getName();
		String days = SystemConfigXmlParse.getSystemConfigBean().getLogRecordKeepDays();
		String number = "-0123456789";
		for(int i = 0; i < days.length(); i++) {
			char c = days.charAt(i);
			if(number.indexOf(c) < 0) return;
		}
		
		logger.info("log keep days: " + days);
		logger.info("job name is: " + name + " " + dateFormat.format(new Date()));
		
		SystemOptLogService sysOptLogService = (SystemOptLogService) SpringContextUtils.getBean("sysOptLogServiceImpl");
		sysOptLogService.deleteLog(Integer.parseInt(days));
	}

}

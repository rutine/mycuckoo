package com.mycuckoo.service.platform.job;

import com.mycuckoo.utils.SpringContextUtils;
import com.mycuckoo.utils.SystemConfigXmlParse;
import com.mycuckoo.service.platform.SystemOptLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能说明: 定时删除系统日志任务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:58:00 AM
 */
public class LoggerJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(LoggerJob.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String name = context.getJobDetail().getKey().getName();
        String days = SystemConfigXmlParse.getInstance().getSystemConfigBean().getLogRecordKeepDays();
        String number = "-0123456789";
        for (int i = 0; i < days.length(); i++) {
            char c = days.charAt(i);
            if (number.indexOf(c) < 0) return;
        }

        logger.info("job name is: {} {}, keep days: {}",
                name, dateFormat.format(new Date()), days);

        SystemOptLogService sysOptLogService = SpringContextUtils.getBean(SystemOptLogService.class);
        sysOptLogService.deleteLog(Integer.parseInt(days));
    }

}

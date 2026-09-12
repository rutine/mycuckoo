package com.mycuckoo.listener;

import com.mycuckoo.core.operator.event.LogEvent;
import com.mycuckoo.service.platform.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * 功能说明: 日志监听器
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 11:30
 */
public class LogEventListener implements ApplicationListener<LogEvent> {

    @Autowired
    private SystemLogService logService;


    @Override
    public void onApplicationEvent(LogEvent event) {
        LogEvent.Payload payload = (LogEvent.Payload) event.getSource();

        logService.save(payload.getModule(), payload.getId(), payload.getTitle(), payload.getContent());
    }
}
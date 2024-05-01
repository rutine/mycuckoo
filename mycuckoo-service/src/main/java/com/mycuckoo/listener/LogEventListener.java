package com.mycuckoo.listener;

import com.mycuckoo.operator.event.LogEvent;
import com.mycuckoo.service.platform.SystemOptLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 功能说明: 日志监听器
 *
 * @author rutine
 * @version 4.0.0
 * @time May 1, 2024 8:55:40 AM
 */
@Component
public class LogEventListener implements ApplicationListener<LogEvent> {

    @Autowired
    private SystemOptLogService logService;


    @Override
    public void onApplicationEvent(LogEvent event) {
        LogEvent.Payload payload = (LogEvent.Payload) event.getSource();

        logService.save(payload.getModule(), payload.getOperate(), payload.getId(), payload.getTitle(), payload.getContent(), payload.getLevel());
    }
}

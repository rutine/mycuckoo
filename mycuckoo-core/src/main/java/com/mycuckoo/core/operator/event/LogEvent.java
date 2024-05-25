package com.mycuckoo.core.operator.event;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import org.springframework.context.ApplicationEvent;

/**
 * @author rutine
 * @version 4.0.0
 * @time April 30, 2024 11:02:40 AM
 */
public class LogEvent extends ApplicationEvent {
    public LogEvent(LogEvent.Payload source) {
        super(source);
    }

    public static class Payload {
        private ModuleName module;
        private OptName operate;
        private String id;
        private String title;
        private String content;
        private LogLevel level;

        public Payload(ModuleName module, OptName operate, String id, String title, String content, LogLevel level) {
            this.module = module;
            this.operate = operate;
            this.id = id;
            this.title = title;
            this.content = content;
            this.level = level;
        }

        public ModuleName getModule() {
            return module;
        }

        public OptName getOperate() {
            return operate;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public LogLevel getLevel() {
            return level;
        }
    }


}


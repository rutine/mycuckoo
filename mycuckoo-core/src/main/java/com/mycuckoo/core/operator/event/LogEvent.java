package com.mycuckoo.core.operator.event;

import com.mycuckoo.core.constant.enums.ModuleName;
import org.springframework.context.ApplicationEvent;

/**
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 11:30
 */
public class LogEvent extends ApplicationEvent {
    public LogEvent(LogEvent.Payload source) {
        super(source);
    }

    public static class Payload {
        private ModuleName module;
        private String id;
        private String title;
        private String content;

        public Payload(ModuleName module, String id, String title, String content) {
            this.module = module;
            this.id = id;
            this.title = title;
            this.content = content;
        }

        public ModuleName getModule() {
            return module;
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
    }


}
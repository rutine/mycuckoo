package com.mycuckoo.operator;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.operator.event.LogEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Component;

/**
 * 功能说明: 日志操作器
 *
 * @author rutine
 * @version 4.0.0
 * @time May 1, 2024 8:55:40 AM
 */
@Component
public final class LogOperator {
    public final static String COMMA = ",";
    public final static String DUNHAO = "、";

    private static ApplicationEventMulticaster multicaster;
    private static Object[] EMPTY = new Object[0];

    private ModuleName module;
    private OptName operate;
    private Object id;
    private String title;
    private LogOperator.ContentWrapper contentWrapper;
    private LogLevel level;

    private LogOperator() {
    }

    public static void setEventMulticaster(ApplicationEventMulticaster multicaster) {
        if (multicaster != null) {
            multicaster = multicaster;
        }

    }

    public static LogOperator begin() {
        return new LogOperator();
    }

    public static LogOperator.Argument argument(Object argument) {
        return new LogOperator.Argument(argument);
    }


    public LogOperator module(ModuleName module) {
        this.module = module;
        return this;
    }

    public LogOperator operate(OptName operate) {
        this.operate = operate;
        return this;
    }

    public LogOperator id(Object id) {
        this.id = id;
        return this;
    }

    public LogOperator title(String title) {
        this.title = title;
        return this;
    }

    public LogOperator content(String pattern, Object... arguments) {
        if (pattern != null) {
            this.contentWrapper = new LogOperator.ContentWrapper(pattern, arguments);
        }

        return this;
    }

    public LogOperator level(LogLevel level) {
        this.level = level;
        return this;
    }

    public void emit() {
        Object[] arguments = EMPTY;
        if (this.contentWrapper != null && this.contentWrapper.arguments != null && this.contentWrapper.arguments.length > 0) {
            arguments = new Object[this.contentWrapper.arguments.length];
            int i = 0;
            Object[] var3 = this.contentWrapper.arguments;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Object obj = var3[var5];
                if (obj instanceof LogOperator.Argument) {
                    obj = ((LogOperator.Argument)obj).value;
                }

                arguments[i++] = obj == null ? "" : obj;
            }
        }

        String content = null;
        if (this.contentWrapper != null) {
            content = arguments != EMPTY ? String.format(this.contentWrapper.content, arguments) : this.contentWrapper.content;
        }

        String idStr = null;
        if (this.id instanceof String) {
            idStr = (String) this.id;
        } else if (this.id != null) {
            idStr = id.toString();
        }

        LogEvent event = new LogEvent(new LogEvent.Payload(this.module, this.operate, idStr, this.title, content, this.level));
        multicaster.multicastEvent(event);
    }

    static class Argument {
        Object value;
        boolean sensitive;

        public Argument(Object value) {
            this(value, true);
        }

        public Argument(Object value, boolean sensitive) {
            this.value = value;
            this.sensitive = sensitive;
        }
    }

    static class ContentWrapper {
        String content;
        Object[] arguments;

        public ContentWrapper(String content, Object... arguments) {
            this.content = content;
            this.arguments = arguments;
        }
    }
}

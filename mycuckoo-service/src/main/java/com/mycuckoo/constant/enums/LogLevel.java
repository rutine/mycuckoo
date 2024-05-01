package com.mycuckoo.constant.enums;

/**
 * 功能说明: 日志级别(日志功能设置)
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 2, 2017 10:03:50 AM
 */
public enum LogLevel {
    WITHOD(0) {
        public Integer value() {
            return 0;
        }
    },
    FIRST(1) {
        public Integer value() {
            return 1;
        }
    },
    SECOND(2) {
        public Integer value() {
            return 2;
        }
    },
    THIRD(3) {
        public Integer value() {
            return 3;
        }
    };

    private Integer level;

    LogLevel(Integer level) {
        this.level = level;
    }

    public Integer value() {
        return level;
    }
}

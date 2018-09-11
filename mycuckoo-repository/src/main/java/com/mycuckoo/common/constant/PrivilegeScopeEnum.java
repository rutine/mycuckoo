package com.mycuckoo.common.constant;

/**
 * 功能说明: 权限范围枚举
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 8, 2017 10:18:54 AM
 */
public enum PrivilegeScopeEnum {
    ORGAN("org"),
    ROLE("rol"),
    USER("usr"),
    INCLUDE("inc"),
    EXCLUDE("exc"),
    ALL("all");

    private String scope;

    PrivilegeScopeEnum(String scope) {
        this.scope = scope;
    }

    public String value() {
        return scope;
    }
}

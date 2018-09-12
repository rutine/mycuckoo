package com.mycuckoo.common.constant;

/**
 * 功能说明: 权限范围枚举
 *
 * @author rutine
 * @version 4.0.0
 * @time Jul 8, 2017 10:18:54 AM
 */
public enum PrivilegeScope {
    ORGAN("org", "机构"),
    ROLE("rol", "角色"),
    USER("usr", "用户"),
    INCLUDE("inc", "包含"),
    EXCLUDE("exc", "排除"),
    ALL("all", "全部");

    private String scope;
    private String desc;

    PrivilegeScope(String scope, String desc) {
        this.scope = scope;
        this.desc = desc;
    }

    public String value() {
        return scope;
    }

    public String desc() {return desc; }


    public static PrivilegeScope of(String scope) {
        PrivilegeScope[] enums = PrivilegeScope.values();
        for (PrivilegeScope myEnum : enums) {
            if (myEnum.value().equals(scope)) {
                return myEnum;
            }
        }

        return null;
    }
}

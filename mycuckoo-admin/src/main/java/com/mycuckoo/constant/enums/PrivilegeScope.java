package com.mycuckoo.constant.enums;

/**
 * 功能说明: 权限范围枚举
 *
 * @author rutine
 * @version 4.0.0
 * @time Jul 8, 2017 10:18:54 AM
 */
public enum PrivilegeScope {
    ORGAN("org", "机构"),
    DEPT("dept", "部门"),
    ROLE("rol", "角色"),
    USER("usr", "用户"),
    INCLUDE("inc", "包含"),
    EXCLUDE("exc", "排除"),
    ALL("all", "全部");

    public final String scope;
    public final String desc;

    PrivilegeScope(String scope, String desc) {
        this.scope = scope;
        this.desc = desc;
    }


    public static PrivilegeScope of(String scope) {
        PrivilegeScope[] enums = PrivilegeScope.values();
        for (PrivilegeScope myEnum : enums) {
            if (myEnum.scope.equals(scope)) {
                return myEnum;
            }
        }

        return null;
    }
}

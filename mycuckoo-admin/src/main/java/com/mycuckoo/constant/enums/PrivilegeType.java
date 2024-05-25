package com.mycuckoo.constant.enums;

/**
 * 功能说明: 权限类型
 *
 * @author rutine
 * @version 4.0.0
 * @time Sept 11, 2018 08:40:54 PM
 */
public enum  PrivilegeType {
    ROW("row", "行权限"),
    OPT("opt", "操作权限"),
    RES("res", "资源权限");

    private String type;
    private String desc;

    PrivilegeType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String value() {
        return type;
    }

    public String desc() {
        return desc;
    }

    public static PrivilegeType of(String privilegeType) {
        PrivilegeType[] enums = PrivilegeType.values();
        for (PrivilegeType myEnum : enums) {
            if (myEnum.value().equals(privilegeType)) {
                return myEnum;
            }
        }

        return null;
    }
}

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

    public final String code;
    public final String desc;

    PrivilegeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static PrivilegeType of(String privilegeType) {
        PrivilegeType[] enums = PrivilegeType.values();
        for (PrivilegeType myEnum : enums) {
            if (myEnum.code.equals(privilegeType)) {
                return myEnum;
            }
        }

        return null;
    }
}

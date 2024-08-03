package com.mycuckoo.constant.enums;

/**
 * 功能说明: 拥有者类型
 *
 * @author rutine
 * @version 4.0.0
 * @time Sept 12, 2018 09:07:34 AM
 */
public enum OwnerType {
    ROLE("rol", "角色者"),
    USR("usr", "用户者");

    public final String code;
    public final String desc;

    OwnerType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static OwnerType of(String ownerType) {
        OwnerType[] enums = OwnerType.values();
        for (OwnerType myEnum : enums) {
            if (myEnum.code.equals(ownerType)) {
                return myEnum;
            }
        }

        return null;
    }
}

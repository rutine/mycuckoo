package com.mycuckoo.common.constant;

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

    private String type;
    private String desc;

    OwnerType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String value() {
        return type;
    }

    public String desc() {
        return desc;
    }

    public static OwnerType of(String ownerType) {
        OwnerType[] enums = OwnerType.values();
        for (OwnerType myEnum : enums) {
            if (myEnum.value().equals(ownerType)) {
                return myEnum;
            }
        }

        return null;
    }
}

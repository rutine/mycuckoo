package com.mycuckoo.constant.enums;

/**
 * 功能说明: 菜单模块级别
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 2, 2017 5:54:33 PM
 */
public enum ModuleLevel {
    ONE(1, "一级菜单"),
    TWO(2, "二级菜单"),
    THREE(3, "三级菜单"),
    FOUR(4, "四级操作");

    public final Integer code;
    public final String desc;

    ModuleLevel(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ModuleLevel of(Integer level) {
        ModuleLevel[] levels = ModuleLevel.values();
        for (ModuleLevel levelEnum : levels) {
            if (levelEnum.code.equals(level)) {
                return levelEnum;
            }
        }

        return null;
    }
}

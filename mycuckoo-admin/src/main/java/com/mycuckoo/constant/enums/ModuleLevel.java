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

    private Integer level;
    private String desc;

    ModuleLevel(Integer level, String desc) {
        this.level = level;
        this.desc = desc;
    }

    public Integer value() {
        return level;
    }

    public static ModuleLevel of(Integer level) {
        ModuleLevel[] levels = ModuleLevel.values();
        for (ModuleLevel levelEnum : levels) {
            if (levelEnum.value().equals(level)) {
                return levelEnum;
            }
        }

        return null;
    }
}

package com.mycuckoo.admin.constant.enums;

/**
 * 功能说明: 表单字段类型
 *
 * @author rutine
 * @version 5.0.0
 * @date 2026-11-01
 */
public enum FieldType {
    GROUP(0, "分组"),
    TEXT(1, "单行文本"),
    TEXTAREA(2, "多行文本"),
    RADIO(3, "单选"),
    CHECKBOX(4, "多选"),
    SWITCH(5, "开关"),
    SELECT(6, "选择框"),
    CASCADER(7, "级联选择"),
    NUMBER(8, "数字"),
    DECIMAL(9, "小数"),
    DATE(10, "日期"),
    DATETIME(11, "日期时间"),
    MOBILE(12, "手机"),
    EMAIL(13, "邮箱"),
    FILE(14, "文件"),
    IMAGE(15, "图片"),
    RATE(16, "评分");

    public final Integer code;
    public final String desc;

    FieldType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FieldType of(Integer code) {
        FieldType[] types = FieldType.values();
        for (FieldType type : types) {
            if (type.code.equals(code)) {
                return type;
            }
        }

        return null;
    }

    /**
     * 是否分组
     */
    public static boolean isGroup(Integer code) {
        return GROUP.code.equals(code);
    }
}
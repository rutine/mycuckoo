package com.mycuckoo.flow.constant.enums;

public enum CommentType {
    NORMAL("1", "正常意见"),
    REJECT("2", "驳回意见"),
    RETURN("3", "退回意见");

    public final String code;
    public final String desc;

    CommentType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static CommentType of(String type) {
        CommentType[] enums = CommentType.values();
        for (CommentType myEnum : enums) {
            if (myEnum.code.equals(type)) {
                return myEnum;
            }
        }

        return null;
    }
}

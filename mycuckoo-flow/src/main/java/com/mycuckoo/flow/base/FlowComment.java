package com.mycuckoo.flow.base;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/7/31 20:42
 */
public class FlowComment {
    private String type; // 意见类别(1:正常意见 2:驳回意见 3:退回意见 )
    private String comment; //意见内容

    public FlowComment() {}
    public FlowComment(String type, String comment) {
        this.type = type;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
package com.mycuckoo.vo.platform;

import com.mycuckoo.vo.OrderTree;

import java.util.Date;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/19 15:52
 */
public class DeptTreeVo extends OrderTree {
    private Integer level;
    private String memo; //备注
    private String status; //状态
    private String creator; //创建人
    private Date createDate; //创建时间

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 9:05:15 PM
 */
public class Operate implements Serializable {

    private Long operateId; //模块操作ID
    private String optName; //模块操作名称
    private String optLink; //模块操作功能链接
    private String optIconCls; //模块图片
    private Integer optOrder; //模块操作顺序
    private Integer optGroup; //模块操作组
    private String memo; //备注
    private String status; //模块操作状态
    private String creator; //创建人
    private Date createDate; //创建时间
//	private List<ModOptRef> modOptRefs = Lists.newArrayList();

    /**
     * default constructor
     */
    public Operate() {
    }

    /**
     * minimal constructor
     */
    public Operate(Long operateId, String status) {
        this.operateId = operateId;
        this.status = status;
    }

    /**
     * full constructor
     */
    public Operate(Long operateId, String optName, String optLink, String optIconCls,
                   Integer optOrder, Integer optGroup, String memo, String status,
                   String creator, Date createDate) {
        this.operateId = operateId;
        this.optName = optName;
        this.optLink = optLink;
        this.optIconCls = optIconCls;
        this.optOrder = optOrder;
        this.optGroup = optGroup;
        this.memo = memo;
        this.status = status;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getOperateId() {
        return this.operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getOptLink() {
        return optLink;
    }

    public void setOptLink(String optLink) {
        this.optLink = optLink;
    }

    public String getOptIconCls() {
        return optIconCls;
    }

    public void setOptIconCls(String optIconCls) {
        this.optIconCls = optIconCls;
    }

    public Integer getOptOrder() {
        return this.optOrder;
    }

    public void setOptOrder(Integer optOrder) {
        this.optOrder = optOrder;
    }

    public Integer getOptGroup() {
        return this.optGroup;
    }

    public void setOptGroup(Integer optGroup) {
        this.optGroup = optGroup;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return this.creator;
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

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Operate operate = (Operate) obj;
        if (operateId != null && operate.getOperateId() != null &&
                this.operateId.longValue() == operate.getOperateId().longValue()) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (operateId == null ? 0 : operateId.hashCode());

        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

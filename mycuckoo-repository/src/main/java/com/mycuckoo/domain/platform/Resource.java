package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 4.1.0
 * @time May 5, 2024 10:12:15 AM
 */
public class Resource implements Serializable {
    private Long resourceId; //操作ID
    private Long moduleId; //上级模块ID
    private Long operateId; //操作ID
    private String name; //名称
    private String method; //请求方法
    private String path; //请求路径
    private Integer order; //顺序
    private String memo; //备注
    private String status; //状态
    private String creator; //创建人
    private Date createDate; //创建时间

    /**
     * default constructor
     */
    public Resource() {
    }

    /**
     * minimal constructor
     */
    public Resource(Long resourceId, String status) {
        this.resourceId = resourceId;
        this.status = status;
    }

    /**
     * full constructor
     */
    public Resource(Long resourceId, Long moduleId, Long operateId, String name,
                    String method, String path, Integer order, String memo, String status,
                    String creator, Date createDate) {
        this.resourceId = resourceId;
        this.moduleId = moduleId;
        this.operateId = operateId;
        this.name = name;
        this.path = path;
        this.method = method;
        this.order = order;
        this.memo = memo;
        this.status = status;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Resource resource = (Resource) obj;
        if ((resourceId != null && resource.getResourceId() != null
                && this.resourceId.longValue() == resource.getResourceId().longValue())
                || (moduleId != null && resource.getResourceId() != null
                && this.moduleId.longValue() == resource.getModuleId().longValue()
                && operateId != null && resource.getOperateId() != null
                && this.operateId.longValue() == resource.getOperateId().longValue())) {
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

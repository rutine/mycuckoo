package com.mycuckoo.admin.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/14 20:40
 */
public class BasicEntity<T> implements Serializable {
    protected T id; //ID
    protected Long tenantId; //租户id
    protected String updater; //更新人
    protected LocalDateTime updateTime; //更新时间
    protected String creator; //创建人
    protected LocalDateTime createTime; //创建时间

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}

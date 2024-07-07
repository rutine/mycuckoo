package com.mycuckoo.domain.uum;

import com.sun.scenario.effect.impl.prism.ps.PPSBlend_ADDPeer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 23, 2014 10:48:29 AM
 */
public class Privilege implements Serializable {
    private Long orgId;
    private Long privilegeId;
    private String resourceId;
    private Long ownerId;
    private String ownerType;
    private String privilegeScope;
    private String privilegeType;
    private String creator;
    private LocalDateTime createTime;

    /**
     * default constructor
     */
    public Privilege() {
    }

    /**
     * minimal constructor
     */
    public Privilege(Long privilegeId) {
        this.privilegeId = privilegeId;
    }



    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getPrivilegeId() {
        return this.privilegeId;
    }

    public void setPrivilegeId(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getPrivilegeScope() {
        return this.privilegeScope;
    }

    public void setPrivilegeScope(String privilegeScope) {
        this.privilegeScope = privilegeScope;
    }

    public String getPrivilegeType() {
        return this.privilegeType;
    }

    public void setPrivilegeType(String privilegeType) {
        this.privilegeType = privilegeType;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

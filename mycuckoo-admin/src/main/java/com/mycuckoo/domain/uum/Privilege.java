package com.mycuckoo.domain.uum;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 23, 2014 10:48:29 AM
 */
public class Privilege implements Serializable {

    private Long privilegeId;
    private String resourceId;
    private Long ownerId;
    private String ownerType;
    private String privilegeScope;
    private String privilegeType;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}

package com.mycuckoo.domain.uum;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 4.0.0
 * @time Sep 23, 2014 10:49:42 AM
 */
public class UserOrgRoleRef {
    private Long userOrgRoleId;
    private User user;
    private OrgRoleRef orgRoleRef;
    private String isDefault; // 默认角色

    /**
     * default constructor
     */
    public UserOrgRoleRef() {
    }

    /**
     * minimal constructor
     */
    public UserOrgRoleRef(Long userOrgRoleId) {
        this.userOrgRoleId = userOrgRoleId;
    }

    /**
     * full constructor
     */
    public UserOrgRoleRef(Long userOrgRoleId, OrgRoleRef orgRoleRef, User user) {
        this.userOrgRoleId = userOrgRoleId;
        this.orgRoleRef = orgRoleRef;
        this.user = user;
    }

    public Long getUserOrgRoleId() {
        return userOrgRoleId;
    }

    public void setUserOrgRoleId(Long userOrgRoleId) {
        this.userOrgRoleId = userOrgRoleId;
    }

    public OrgRoleRef getOrgRoleRef() {
        return this.orgRoleRef;
    }

    public void setOrgRoleRef(OrgRoleRef orgRoleRef) {
        this.orgRoleRef = orgRoleRef;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}

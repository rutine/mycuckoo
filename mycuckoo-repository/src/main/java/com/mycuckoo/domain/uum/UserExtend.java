package com.mycuckoo.domain.uum;

/**
 * 功能说明: 用户属性扩展
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 9, 2017 6:11:51 PM
 */
public class UserExtend extends User {
    private Long orgRoleId;
    private String roleName;
    private String orgName;

    public Long getOrgRoleId() {
        return orgRoleId;
    }

    public void setOrgRoleId(Long orgRoleId) {
        this.orgRoleId = orgRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}

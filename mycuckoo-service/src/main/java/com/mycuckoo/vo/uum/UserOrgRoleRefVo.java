package com.mycuckoo.vo.uum;

import com.mycuckoo.domain.uum.UserOrgRoleRef;

/**
 * 功能说明: 角色用户vo
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 9, 2017 5:50:43 PM
 */
public class UserOrgRoleRefVo extends UserOrgRoleRef {
    private Long orgRoleId;
    private Long organId;
    private String organName;
    private Long roleId;
    private String roleName;

    public Long getOrgRoleId() {
        return orgRoleId;
    }

    public void setOrgRoleId(Long orgRoleId) {
        this.orgRoleId = orgRoleId;
    }

    public Long getOrganId() {
        return organId;
    }

    public void setOrganId(Long organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

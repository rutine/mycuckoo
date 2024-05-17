package com.mycuckoo.web.vo.res;

import com.mycuckoo.vo.CheckBoxTree;

import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-09-10 15:21
 */
public class RolePrivilegeVo {
    private String privilegeScope;
    private String rowPrivilege;
    private List<CheckBoxTree> privileges;
    private List<String> assign;

    public RolePrivilegeVo(String privilegeScope, String rowPrivilege,
                           List<CheckBoxTree> privileges, List<String> assign) {
        this.privilegeScope = privilegeScope;
        this.rowPrivilege = rowPrivilege;
        this.privileges = privileges;
        this.assign = assign;
    }

    public String getPrivilegeScope() {
        return privilegeScope;
    }

    public void setPrivilegeScope(String privilegeScope) {
        this.privilegeScope = privilegeScope;
    }

    public String getRowPrivilege() {
        return rowPrivilege;
    }

    public void setRowPrivilege(String rowPrivilege) {
        this.rowPrivilege = rowPrivilege;
    }

    public List<CheckBoxTree> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<CheckBoxTree> privileges) {
        this.privileges = privileges;
    }

    public List<String> getAssign() {
        return assign;
    }

    public void setAssign(List<String> assign) {
        this.assign = assign;
    }
}

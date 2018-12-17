package com.mycuckoo.vo;

import java.util.List;

/**
 * 功能说明: 已分配和未分配的操作
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 2, 2017 6:32:38 PM
 */
public class AssignVo<O, T> {
    private List<O> assign;
    private List<T> unassign;
    private String privilegeScope;

    public AssignVo(List<O> assign, List<T> unassign) {
        this(assign, unassign, null);
    }

    public AssignVo(List<O> assign, List<T> unassign, String privilegeScope) {
        this.assign = assign;
        this.unassign = unassign;
        this.privilegeScope = privilegeScope;
    }

    public List<O> getAssign() {
        return assign;
    }

    public void setAssign(List<O> assign) {
        this.assign = assign;
    }

    public List<T> getUnassign() {
        return unassign;
    }

    public void setUnassign(List<T> unassign) {
        this.unassign = unassign;
    }

    public String getPrivilegeScope() {
        return privilegeScope;
    }

    public void setPrivilegeScope(String privilegeScope) {
        this.privilegeScope = privilegeScope;
    }
}

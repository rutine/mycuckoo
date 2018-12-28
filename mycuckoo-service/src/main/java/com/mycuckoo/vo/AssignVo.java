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
    private List<O> source;
    private List<T> assign;
    private String privilegeScope;

    public AssignVo(List<O> source, List<T> assign) {
        this(source, assign, null);
    }

    public AssignVo(List<O> source, List<T> assign, String privilegeScope) {
        this.source = source;
        this.assign = assign;
        this.privilegeScope = privilegeScope;
    }

    public List<O> getSource() {
        return source;
    }

    public void setSource(List<O> source) {
        this.source = source;
    }

    public List<T> getAssign() {
        return assign;
    }

    public void setAssign(List<T> assign) {
        this.assign = assign;
    }

    public String getPrivilegeScope() {
        return privilegeScope;
    }

    public void setPrivilegeScope(String privilegeScope) {
        this.privilegeScope = privilegeScope;
    }
}

package com.mycuckoo.vo;

import java.util.List;

/**
 * 功能说明: 已分配和未分配的操作
 *
 * @author rutine
 * @time Jul 2, 2017 6:32:38 PM
 * @version 3.0.0
 */
public class AssignVo<O> {
	private List<O> assign;
	private List<O> unassign;
	private String privilegeScope;
	
	public AssignVo(List<O> assign, List<O> unassign) {
		this(assign, unassign, null);
	}
	
	public AssignVo(List<O> assign, List<O> unassign, String privilegeScope) {
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

	public List<O> getUnassign() {
		return unassign;
	}

	public void setUnassign(List<O> unassign) {
		this.unassign = unassign;
	}

	public String getPrivilegeScope() {
		return privilegeScope;
	}
	public void setPrivilegeScope(String privilegeScope) {
		this.privilegeScope = privilegeScope;
	}
}

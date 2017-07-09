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
	private List<O> assigneds;
	private List<O> unassigneds;
	private String privilegeScope;
	
	public AssignVo(List<O> assigneds, List<O> unassigneds) {
		this(assigneds, unassigneds, null);
	}
	
	public AssignVo(List<O> assigneds, List<O> unassigneds, String privilegeScope) {
		this.assigneds = assigneds;
		this.unassigneds = unassigneds;
		this.privilegeScope = privilegeScope;
	}

	public List<O> getAssigneds() {
		return assigneds;
	}
	public void setAssigneds(List<O> assigneds) {
		this.assigneds = assigneds;
	}
	public List<O> getUnassigneds() {
		return unassigneds;
	}
	public void setUnassigneds(List<O> unassigneds) {
		this.unassigneds = unassigneds;
	}
	public String getPrivilegeScope() {
		return privilegeScope;
	}
	public void setPrivilegeScope(String privilegeScope) {
		this.privilegeScope = privilegeScope;
	}
}

package com.mycuckoo.vo;

import java.util.List;

import com.mycuckoo.domain.platform.Operate;

/**
 * 功能说明: 已分配和未分配的操作
 *
 * @author rutine
 * @time Jul 2, 2017 6:32:38 PM
 * @version 3.0.0
 */
public class AssignOperationVo {
	private List<Operate> assignedModOpts;
	private List<Operate> unassignedModOpts;
	
	public AssignOperationVo(List<Operate> assignedModOpts, List<Operate> unassignedModOpts) {
		this.assignedModOpts = assignedModOpts;
		this.unassignedModOpts = unassignedModOpts;
	}
	
	
	public List<Operate> getAssignedModOpts() {
		return assignedModOpts;
	}
	public void setAssignedModOpts(List<Operate> assignedModOpts) {
		this.assignedModOpts = assignedModOpts;
	}
	public List<Operate> getUnassignedModOpts() {
		return unassignedModOpts;
	}
	public void setUnassignedModOpts(List<Operate> unassignedModOpts) {
		this.unassignedModOpts = unassignedModOpts;
	}
	
}

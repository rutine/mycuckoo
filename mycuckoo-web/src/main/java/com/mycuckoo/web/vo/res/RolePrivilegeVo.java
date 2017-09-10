package com.mycuckoo.web.vo.res;

import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.TreeVoExtend;

import java.util.List;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-09-10 15:21
 */
public class RolePrivilegeVo extends AssignVo<TreeVoExtend> {
	private String rowPrivilege;

	public RolePrivilegeVo(List<TreeVoExtend> assign, List<TreeVoExtend> unassign, String privilegeScope, String rowPrivilege) {
		super(assign, unassign, privilegeScope);

		this.rowPrivilege = rowPrivilege;
	}

	public String getRowPrivilege() {
		return rowPrivilege;
	}

	public void setRowPrivilege(String rowPrivilege) {
		this.rowPrivilege = rowPrivilege;
	}
}

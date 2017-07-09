package com.mycuckoo.vo.uum;

import com.mycuckoo.domain.uum.User;

/**
 * 功能说明: 用户vo
 *
 * @author rutine
 * @time Jul 9, 2017 6:16:58 PM
 * @version 3.0.0
 */
public class UserVo extends User {
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

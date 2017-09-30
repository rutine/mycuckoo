package com.mycuckoo.vo;

import com.mycuckoo.vo.uum.RoleUserRefVo;

import java.util.List;

public class LoginRoleVo {
	private List<RoleUserRefVo> roles;
	
	public LoginRoleVo(List<RoleUserRefVo> roles) {
		this.roles = roles;
	}

	public List<RoleUserRefVo> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleUserRefVo> roles) {
		this.roles = roles;
	}
}

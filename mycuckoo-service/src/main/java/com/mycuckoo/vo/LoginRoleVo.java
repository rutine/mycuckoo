package com.mycuckoo.vo;

import java.util.List;

import com.mycuckoo.vo.uum.RoleUserRefVo;
import com.mycuckoo.vo.uum.UserAgentVo;

public class LoginRoleVo {
	private List<RoleUserRefVo> roles;
	private List<UserAgentVo> agents;
	
	public LoginRoleVo(List<RoleUserRefVo> roles, List<UserAgentVo> agents) {
		this.roles = roles;
		this.agents = agents;
	}

	public List<RoleUserRefVo> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleUserRefVo> roles) {
		this.roles = roles;
	}
	public List<UserAgentVo> getAgents() {
		return agents;
	}
	public void setAgents(List<UserAgentVo> agents) {
		this.agents = agents;
	}
}

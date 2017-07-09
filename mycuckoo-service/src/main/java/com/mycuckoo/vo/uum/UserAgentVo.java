package com.mycuckoo.vo.uum;

import com.mycuckoo.domain.uum.UserAgent;

/**
 * 功能说明: 用户代理vo
 *
 * @author rutine
 * @time Jul 9, 2017 5:50:09 PM
 * @version 3.0.0
 */
public class UserAgentVo extends UserAgent {
	private String userCode;
	private String userName;
	private String userPhotoUrl;
	private Long organId;
	private String organName;
	private Long roleId;
	private String roleName;
	
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhotoUrl() {
		return userPhotoUrl;
	}
	public void setUserPhotoUrl(String userPhotoUrl) {
		this.userPhotoUrl = userPhotoUrl;
	}
	public Long getOrganId() {
		return organId;
	}
	public void setOrganId(Long organId) {
		this.organId = organId;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}

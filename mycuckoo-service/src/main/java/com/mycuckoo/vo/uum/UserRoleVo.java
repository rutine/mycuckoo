package com.mycuckoo.vo.uum;

/**
 * @author rutine
 * @date 2017/9/29 17:55
 */
/**
 * 功能说明: 用户角色vo
 *
 * @author rutine
 * @time Sept 29, 2017 17:55:43 PM
 * @version 3.0.0
 */
public class UserRoleVo {
	private Long organRoleId;
	private Long organId;
	private String organName;
	private Long roleId;
	private String roleName;
	private Long userId;
	private String userName;
	private String userPhotoUrl;
	private String isDefault; // 默认角色

	public Long getOrganRoleId() {
		return organRoleId;
	}

	public void setOrganRoleId(Long organRoleId) {
		this.organRoleId = organRoleId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
}

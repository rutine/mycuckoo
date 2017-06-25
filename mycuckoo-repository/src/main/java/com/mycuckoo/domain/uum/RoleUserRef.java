package com.mycuckoo.domain.uum;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:49:42 AM
 * @version 3.0.0
 */
public class RoleUserRef {
	
	private Long orgRoleUserId;
	private OrgRoleRef orgRoleRef;
	private User user;
	private String isDefault; // 默认角色
	
	
	private Long orgRoleId;
	private String organName;
	private String roleName;
	private Long organId;
	private Long roleId;

	/** default constructor */
	public RoleUserRef() {
	}

	/** minimal constructor */
	public RoleUserRef(Long orgRoleUserId) {
		this.orgRoleUserId = orgRoleUserId;
	}

	/** full constructor */
	public RoleUserRef(Long orgRoleUserId, OrgRoleRef orgRoleRef, User user) {
		this.orgRoleUserId = orgRoleUserId;
		this.orgRoleRef = orgRoleRef;
		this.user = user;
	}

	public Long getOrgRoleUserId() {
		return this.orgRoleUserId;
	}

	public void setOrgRoleUserId(Long orgRoleUserId) {
		this.orgRoleUserId = orgRoleUserId;
	}

	public OrgRoleRef getOrgRoleRef() {
		return this.orgRoleRef;
	}

	public void setOrgRoleRef(OrgRoleRef orgRoleRef) {
		this.orgRoleRef = orgRoleRef;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	
	
	public Long getOrgRoleId() {
		return orgRoleId;
	}

	public void setOrgRoleId(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getOrganId() {
		return organId;
	}

	public void setOrganId(Long organId) {
		this.organId = organId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}	
	
}

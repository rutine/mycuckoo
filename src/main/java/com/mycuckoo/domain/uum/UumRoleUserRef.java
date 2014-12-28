package com.mycuckoo.domain.uum;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:49:42 AM
 * @version 2.0.0
 */
public class UumRoleUserRef {
	
	private Long orgRoleUserId;
	private UumOrgRoleRef uumOrgRoleRef;
	private UumUser uumUser;
	private String isDefault; // 默认角色
	
	
	private Long orgRoleId;
	private String organName;
	private String roleName;
	private Long organId;
	private Long roleId;

	/** default constructor */
	public UumRoleUserRef() {
	}

	/** minimal constructor */
	public UumRoleUserRef(Long orgRoleUserId) {
		this.orgRoleUserId = orgRoleUserId;
	}

	/** full constructor */
	public UumRoleUserRef(Long orgRoleUserId, UumOrgRoleRef uumOrgRoleRef,
			UumUser uumUser) {
		this.orgRoleUserId = orgRoleUserId;
		this.uumOrgRoleRef = uumOrgRoleRef;
		this.uumUser = uumUser;
	}

	public Long getOrgRoleUserId() {
		return this.orgRoleUserId;
	}

	public void setOrgRoleUserId(Long orgRoleUserId) {
		this.orgRoleUserId = orgRoleUserId;
	}

	public UumOrgRoleRef getUumOrgRoleRef() {
		return this.uumOrgRoleRef;
	}

	public void setUumOrgRoleRef(UumOrgRoleRef uumOrgRoleRef) {
		this.uumOrgRoleRef = uumOrgRoleRef;
	}

	public UumUser getUumUser() {
		return this.uumUser;
	}

	public void setUumUser(UumUser uumUser) {
		this.uumUser = uumUser;
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

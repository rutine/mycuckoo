package com.mycuckoo.domain.uum;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:47:39 AM
 * @version 2.0.0
 */
public class UumOrgRoleRef implements java.io.Serializable {

	private Long orgRoleId;
	private UumRole uumRole;
	private UumOrgan uumOrgan;
	private List<UumRoleUserRef> uumRoleUserRefs = Lists.newArrayList();

	/** default constructor */
	public UumOrgRoleRef() {
	}

	/** minimal constructor */
	public UumOrgRoleRef(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	/** full constructor */
	public UumOrgRoleRef(Long orgRoleId, UumRole uumRole, UumOrgan uumOrgan,
			List<UumRoleUserRef> uumRoleUserRefs) {
		this.orgRoleId = orgRoleId;
		this.uumRole = uumRole;
		this.uumOrgan = uumOrgan;
		this.uumRoleUserRefs = uumRoleUserRefs;
	}

	public Long getOrgRoleId() {
		return this.orgRoleId;
	}

	public void setOrgRoleId(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	public UumRole getUumRole() {
		return this.uumRole;
	}

	public void setUumRole(UumRole uumRole) {
		this.uumRole = uumRole;
	}

	public UumOrgan getUumOrgan() {
		return this.uumOrgan;
	}

	public void setUumOrgan(UumOrgan uumOrgan) {
		this.uumOrgan = uumOrgan;
	}
	
//	@JSON(serialize= false)
	public List<UumRoleUserRef> getUumRoleUserRefs() {
		return this.uumRoleUserRefs;
	}

	public void setUumRoleUserRefs(List<UumRoleUserRef> uumRoleUserRefs) {
		this.uumRoleUserRefs = uumRoleUserRefs;
	}
}

package com.mycuckoo.domain.uum;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:47:39 AM
 * @version 3.0.0
 */
public class OrgRoleRef implements java.io.Serializable {

	private Long orgRoleId;
	private Role role;
	private Organ organ;

	/** default constructor */
	public OrgRoleRef() {
	}

	/** minimal constructor */
	public OrgRoleRef(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	/** full constructor */
	public OrgRoleRef(Long orgRoleId, Role role, Organ organ) {
		this.orgRoleId = orgRoleId;
		this.role = role;
		this.organ = organ;
	}

	public Long getOrgRoleId() {
		return this.orgRoleId;
	}

	public void setOrgRoleId(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Organ getOrgan() {
		return this.organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}

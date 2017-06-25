package com.mycuckoo.domain.uum;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

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
	private List<RoleUserRef> roleUserRefs = Lists.newArrayList();

	/** default constructor */
	public OrgRoleRef() {
	}

	/** minimal constructor */
	public OrgRoleRef(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	/** full constructor */
	public OrgRoleRef(Long orgRoleId, Role role, Organ organ, List<RoleUserRef> roleUserRefs) {
		this.orgRoleId = orgRoleId;
		this.role = role;
		this.organ = organ;
		this.roleUserRefs = roleUserRefs;
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
	
	public List<RoleUserRef> getRoleUserRefs() {
		return this.roleUserRefs;
	}

	public void setUumRoleUserRefs(List<RoleUserRef> roleUserRefs) {
		this.roleUserRefs = roleUserRefs;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}

package com.mycuckoo.domain.uum;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:48:50 AM
 * @version 2.0.0
 */
public class UumRole implements java.io.Serializable {

	private Long roleId;		//角色ID
	private String roleName;	//角色名称
	private String status;		//角色状态
	private String memo;		//备注
	private String creator;	//创建人
	private Date createDate;	//创建时间
	private Short roleLevel;	//角色级别
	private List<UumOrgRoleRef> uumOrgRoleRefs = Lists.newArrayList();

	/** default constructor */
	public UumRole() {
	}
	
	/** minimal constructor */
	public UumRole(Long roleId, String status) {
		this.roleId = roleId;
		this.status = status;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Short getRoleLevel() {
		return this.roleLevel;
	}

	public void setRoleLevel(Short roleLevel) {
		this.roleLevel = roleLevel;
	}
	
//	@JSON(serialize= false)
	public List<UumOrgRoleRef> getUumOrgRoleRefs() {
		return this.uumOrgRoleRefs;
	}

	public void setUumOrgRoleRefs(List<UumOrgRoleRef> uumOrgRoleRefs) {
		this.uumOrgRoleRefs = uumOrgRoleRefs;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj) return true; // quict check
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		UumRole uumRole = (UumRole)obj;
		if(roleId != null && uumRole.getRoleId() != null && 
			roleId.longValue() ==  uumRole.getRoleId().longValue()){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int hashcode = 7;
		
		hashcode = hashcode * 37 + (roleId == null ? 0 : roleId.hashCode());
		
		return hashcode;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

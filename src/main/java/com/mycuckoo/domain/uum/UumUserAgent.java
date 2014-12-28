package com.mycuckoo.domain.uum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:50:14 AM
 * @version 2.0.0
 */
public class UumUserAgent implements Serializable {

	private static final long serialVersionUID = 1000000L;
	
	
	private Long agentId; // 代理id
	private Long userId; // 被代理用户id
	private Long agentUserId; // 代理用户id
	private Long orgRoleId; // 被代理机构角色id
	private Date beginDate; // 代理开始时间
	private Date endDate; // 代理结束时间
	private String reason; // 代理理由
	private List uumUserAgentPrivileges = Lists.newArrayList();
	
	private String userPhotoUrl;
	private String userName;
	private String userCode;
	private String organName;
	private String roleName;
	private Long organId;
	private Long roleId;

	/** default constructor */
	public UumUserAgent() {
	}

	/** minimal constructor */
	public UumUserAgent(Long agentId) {
		this.agentId = agentId;
	}

	/** full constructor */
	public UumUserAgent(Long agentId, Long userId, Long agentUserId,
			String askDirection, Date beginDate, Date endDate, String reason,
			String askResult) {
		this.agentId = agentId;
		this.userId = userId;
		this.agentUserId = agentUserId;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.reason = reason;
	}

	public Long getAgentId() {
		return this.agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAgentUserId() {
		return this.agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getUserPhotoUrl() {
		return userPhotoUrl;
	}

	public void setUserPhotoUrl(String userPhotoUrl) {
		this.userPhotoUrl = userPhotoUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
	
//	@JSON(serialize=false)
	public List getUumUserAgentPrivileges() {
		return uumUserAgentPrivileges;
	}

	public void setUumUserAgentPrivileges(List uumUserAgentPrivileges) {
		this.uumUserAgentPrivileges = uumUserAgentPrivileges;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

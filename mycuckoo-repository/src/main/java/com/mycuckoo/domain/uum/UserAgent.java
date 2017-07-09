package com.mycuckoo.domain.uum;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:50:14 AM
 * @version 3.0.0
 */
public class UserAgent implements Serializable {
	private static final long serialVersionUID = 1000000L;
	
	private Long agentId; // 代理id
	private Long userId; // 代理用户id
	private Long agentUserId; // 被代理用户id
	private Long orgRoleId; // 被代理机构角色id
	private Date beginDate; // 代理开始时间
	private Date endDate; // 代理结束时间
	private String reason; // 代理理由

	/** default constructor */
	public UserAgent() {
	}

	/** minimal constructor */
	public UserAgent(Long agentId) {
		this.agentId = agentId;
	}

	/** full constructor */
	public UserAgent(Long agentId, Long userId, Long agentUserId,
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
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(Long agentUserId) {
		this.agentUserId = agentUserId;
	}

	public Long getOrgRoleId() {
		return orgRoleId;
	}

	public void setOrgRoleId(Long orgRoleId) {
		this.orgRoleId = orgRoleId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

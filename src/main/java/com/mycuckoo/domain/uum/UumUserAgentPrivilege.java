package com.mycuckoo.domain.uum;

import java.io.Serializable;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:50:41 AM
 * @version 2.0.0
 */
public class UumUserAgentPrivilege implements Serializable {
	
	private Long userAgentPriId;
	private UumUserAgent uumUserAgent;
	private String privilegeId;
	private String privilegeType;

	/** default constructor */
	public UumUserAgentPrivilege() {
	}

	/** minimal constructor */
	public UumUserAgentPrivilege(Long userAgentPriId) {
		this.userAgentPriId = userAgentPriId;
	}

	/** full constructor */
	public UumUserAgentPrivilege(Long userAgentPriId, UumUserAgent uumUserAgent,
			String privilegeId, String privilegeType) {
		this.userAgentPriId = userAgentPriId;
		this.uumUserAgent = uumUserAgent;
		this.privilegeId = privilegeId;
		this.privilegeType = privilegeType;
	}

	public Long getUserAgentPriId() {
		return this.userAgentPriId;
	}

	public void setUserAgentPriId(Long userAgentPriId) {
		this.userAgentPriId = userAgentPriId;
	}

	public String getPrivilegeId() {
		return this.privilegeId;
	}

	public void setPrivilegeId(String privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getPrivilegeType() {
		return this.privilegeType;
	}

	public void setPrivilegeType(String privilegeType) {
		this.privilegeType = privilegeType;
	}

	public UumUserAgent getUumUserAgent() {
		return uumUserAgent;
	}

	public void setUumUserAgent(UumUserAgent uumUserAgent) {
		this.uumUserAgent = uumUserAgent;
	}
}

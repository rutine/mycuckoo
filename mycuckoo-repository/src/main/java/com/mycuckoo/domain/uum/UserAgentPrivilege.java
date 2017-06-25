package com.mycuckoo.domain.uum;

import java.io.Serializable;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:50:41 AM
 * @version 3.0.0
 */
public class UserAgentPrivilege implements Serializable {
	
	private Long userAgentPriId;
	private UserAgent userAgent;
	private String privilegeId;
	private String privilegeType;

	/** default constructor */
	public UserAgentPrivilege() {
	}

	/** minimal constructor */
	public UserAgentPrivilege(Long userAgentPriId) {
		this.userAgentPriId = userAgentPriId;
	}

	/** full constructor */
	public UserAgentPrivilege(Long userAgentPriId, UserAgent userAgent,
			String privilegeId, String privilegeType) {
		this.userAgentPriId = userAgentPriId;
		this.userAgent = userAgent;
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

	public UserAgent getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(UserAgent userAgent) {
		this.userAgent = userAgent;
	}
}

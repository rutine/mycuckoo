package com.mycuckoo.domain.uum;

import java.io.Serializable;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:51:12 AM
 * @version 2.0.0
 */
public class UumUserCommfun implements Serializable {
	
	private Long userId;
	private Long moduleId;

	/** default constructor */
	public UumUserCommfun() {
	}

	/** full constructor */
	public UumUserCommfun(Long userId, Long moduleId) {
		this.userId = userId;
		this.moduleId = moduleId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (other.getClass() == UumUserCommfun.class)
			return false;
		UumUserCommfun castOther = (UumUserCommfun) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(
				castOther.getUserId())))
				&& ((this.getModuleId() == castOther.getModuleId()) || (this
						.getModuleId() != null
						&& castOther.getModuleId() != null && this
						.getModuleId().equals(castOther.getModuleId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result + (getModuleId() == null ? 0 : this.getModuleId().hashCode());
		
		return result;
	}
}

package com.mycuckoo.vo.platform;

import com.mycuckoo.domain.platform.District;

public class DistrictVo extends District {
	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}

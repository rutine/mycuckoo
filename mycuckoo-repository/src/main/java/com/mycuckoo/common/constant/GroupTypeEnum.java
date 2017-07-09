package com.mycuckoo.common.constant;

/**
 * 功能说明: 组相关信息
 *
 * @author rutine
 * @time Jul 8, 2017 10:18:54 AM
 * @version 3.0.0
 */
public enum GroupTypeEnum {
	USER_GROUP("userGroup"),
	ROLE_GROUP("roleGroup"),
	GENERAL_GROUP("generalGroup");
	
	private String group;
	
	GroupTypeEnum(String group) {
		this.group = group;
	}
	
	public String value() {return group;}
}

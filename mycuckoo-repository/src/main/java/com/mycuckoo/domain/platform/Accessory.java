package com.mycuckoo.domain.platform;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 8:58:08 PM
 * @version 3.0.0
 */
public class Accessory implements Serializable {

	private Long accessoryId;
	private Long infoId;
	private String accessoryName;

	/** default constructor */
	public Accessory() {
	}

	/** minimal constructor */
	public Accessory(Long accessoryId) {
		this.accessoryId = accessoryId;
	}

	/** full constructor */
	public Accessory(Long accessoryId, Long infoId, String accessoryName) {
		this.accessoryId = accessoryId;
		this.infoId = infoId;
		this.accessoryName = accessoryName;
	}

	public Long getAccessoryId() {
		return this.accessoryId;
	}

	public void setAccessoryId(Long accessoryId) {
		this.accessoryId = accessoryId;
	}

	public Long getInfoId() {
		return this.infoId;
	}

	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}

	public String getAccessoryName() {
		return this.accessoryName;
	}

	public void setAccessoryName(String accessoryName) {
		this.accessoryName = accessoryName;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}

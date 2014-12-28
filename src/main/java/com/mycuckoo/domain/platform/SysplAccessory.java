package com.mycuckoo.domain.platform;

import java.io.Serializable;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 8:58:08 PM
 * @version 2.0.0
 */
public class SysplAccessory implements Serializable {

	private Long accessoryId;
	private Long infoId;
	private String accessoryName;

	/** default constructor */
	public SysplAccessory() {
	}

	/** minimal constructor */
	public SysplAccessory(Long accessoryId) {
		this.accessoryId = accessoryId;
	}

	/** full constructor */
	public SysplAccessory(Long accessoryId, Long infoId, String accessoryName) {
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
}

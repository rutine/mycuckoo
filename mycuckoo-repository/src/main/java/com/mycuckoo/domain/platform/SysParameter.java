package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:06:28 PM
 * @version 3.0.0
 */
public class SysParameter implements Serializable {

	private Long paraId;
	private String paraName;
	private String paraKeyName;
	private String paraValue;
	private String paraType;
	private String memo;
	private String status;
	private String creator;
	private Date createDate;

	/** default constructor */
	public SysParameter() {
	}

	/** minimal constructor */
	public SysParameter(Long paraId, String status) {
		this.paraId = paraId;
		this.status = status;
	}

	/** full constructor */
	public SysParameter(Long paraId, String paraName, String paraKeyName,
			String paraValue, String paraType, String memo, String status,
			String creator, Date createDate) {
		this.paraId = paraId;
		this.paraName = paraName;
		this.paraKeyName = paraKeyName;
		this.paraValue = paraValue;
		this.paraType = paraType;
		this.memo = memo;
		this.status = status;
		this.creator = creator;
		this.createDate = createDate;
	}

	public Long getParaId() {
		return this.paraId;
	}

	public void setParaId(Long paraId) {
		this.paraId = paraId;
	}

	public String getParaName() {
		return this.paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	public String getParaKeyName() {
		return this.paraKeyName;
	}

	public void setParaKeyName(String paraKeyName) {
		this.paraKeyName = paraKeyName;
	}

	public String getParaValue() {
		return this.paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}

	public String getParaType() {
		return this.paraType;
	}

	public void setParaType(String paraType) {
		this.paraType = paraType;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}

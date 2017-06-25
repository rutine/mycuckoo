package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:03:22 PM
 * @version 3.0.0
 */
public class DicBigType implements Serializable { 

	private Long bigTypeId;
	private String bigTypeName;
	private String bigTypeCode;
	private String memo;
	private String status;
	private String creator;
	private Date createDate;
	private List<DicSmallType> dicSmallTypes = Lists.newArrayList();

	/** default constructor */
	public DicBigType() {
	}

	/** minimal constructor */
	public DicBigType(Long bigTypeId, String status) {
		this.bigTypeId = bigTypeId;
		this.status = status;
	}
	
	/** full constructor */
	public DicBigType(Long bigTypeId, String bigTypeName, String bigTypeCode,
			String memo, String status, String creator, Date createDate, List<DicSmallType> dicSmallTypes) {
		this.bigTypeId = bigTypeId;
		this.bigTypeName = bigTypeName;
		this.bigTypeCode = bigTypeCode;
		this.memo = memo;
		this.status = status;
		this.creator = creator;
		this.createDate = createDate;
		this.dicSmallTypes = dicSmallTypes;
	}

	public Long getBigTypeId() {
		return this.bigTypeId;
	}
	
	public void setBigTypeId(Long bigTypeId) {
		this.bigTypeId = bigTypeId;
	}

	public String getBigTypeName() {
		return this.bigTypeName;
	}
	
	public void setBigTypeName(String bigTypeName) {
		this.bigTypeName = bigTypeName;
	}

	public String getBigTypeCode() {
		return this.bigTypeCode;
	}
	
	public void setBigTypeCode(String bigTypeCode) {
		this.bigTypeCode = bigTypeCode;
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

	public List<DicSmallType> getDicSmallTypes() {
		return this.dicSmallTypes;
	}
	
	public void setDicSmallTypes(List<DicSmallType> dicSmallTypes) {
		this.dicSmallTypes = dicSmallTypes;
	}
}

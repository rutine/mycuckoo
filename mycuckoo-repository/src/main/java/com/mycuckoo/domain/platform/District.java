package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:04:00 PM
 * @version 3.0.0
 */
public class District implements Serializable {
	
	private static final long serialVersionUID = 1000000L;

	private Long districtId;
	private District sysplDistrict;
	private String districtName;
	private String districtCode;
	private String districtPostal;
	private String districtTelcode;
	private String districtLevel;
	private String memo;
	private String status;
	private String creator;
	private Date createDate;
	private List<District> districts = Lists.newArrayList();

	
	private Long upDistrictId;
	private String upDistrictName;
	
	/** default constructor */
	public District() {
	}

	/** minimal constructor */
	public District(Long districtId, String status) {
		this.districtId = districtId;
		this.status = status;
	}

	/** full constructor */
	public District(Long districtId, District sysplDistrict,
			String districtName, String districtCode, String districtPostal,
			String districtTelcode, String districtLevel, String memo,
			String status, String creator, Date createDate,  List<District> districts) {
		this.districtId = districtId;
		this.sysplDistrict = sysplDistrict;
		this.districtName = districtName;
		this.districtCode = districtCode;
		this.districtPostal = districtPostal;
		this.districtTelcode = districtTelcode;
		this.districtLevel = districtLevel;
		this.memo = memo;
		this.status = status;
		this.creator = creator;
		this.createDate = createDate;
		this.districts = districts;
	}

	public Long getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public District getSysplDistrict() {
		return this.sysplDistrict;
	}

	public void setSysplDistrict(District sysplDistrict) {
		this.sysplDistrict = sysplDistrict;
	}

	public String getDistrictName() {
		return this.districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDistrictCode() {
		return this.districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictPostal() {
		return this.districtPostal;
	}

	public void setDistrictPostal(String districtPostal) {
		this.districtPostal = districtPostal;
	}

	public String getDistrictTelcode() {
		return this.districtTelcode;
	}

	public void setDistrictTelcode(String districtTelcode) {
		this.districtTelcode = districtTelcode;
	}

	public String getDistrictLevel() {
		return this.districtLevel;
	}

	public void setDistrictLevel(String districtLevel) {
		this.districtLevel = districtLevel;
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

	public  List<District> getDistricts() {
		return this.districts;
	}

	public void setDistricts( List<District> districts) {
		this.districts = districts;
	}
	
	public Long getUpDistrictId() {
		return upDistrictId;
	}

	public void setUpDistrictId(Long upDistrictId) {
		this.upDistrictId = upDistrictId;
	}

	public String getUpDistrictName() {
		return upDistrictName;
	}

	public void setUpDistrictName(String upDistrictName) {
		this.upDistrictName = upDistrictName;
	}

	
	public boolean equals(Object obj ){
			if(this == obj) return true;
			if(obj == null) return false;
		if(this.getClass() != obj.getClass()) return false;
		District district = (District)obj;
		if(district.getDistrictId() != null && getDistrictId() != null && 
				district.getDistrictId().longValue() ==
			this.getDistrictId().longValue()){
			return true;
		}else{
			return false;
		}
	}
	
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getDistrictId() == null ? 0 : getDistrictId().hashCode());

		return result;
	}
}

package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:03:38 PM
 * @version 2.0.0
 */
public class SysplDicSmallType implements Serializable {

	private Long smallTypeId;
	private SysplDicBigType sysplDicBigType;
	private String smallTypeName;
	private String smallTypeCode;

	/** default constructor */
	public SysplDicSmallType() {
	}

	/** minimal constructor */
	public SysplDicSmallType(Long smallTypeId) {
		this.smallTypeId = smallTypeId;
	}

	/** full constructor */
	public SysplDicSmallType(Long smallTypeId, SysplDicBigType sysplDicBigType,
			String smallTypeName, String smallTypeCode) {
		this.smallTypeId = smallTypeId;
		this.sysplDicBigType = sysplDicBigType;
		this.smallTypeName = smallTypeName;
		this.smallTypeCode = smallTypeCode;
	}

	public Long getSmallTypeId() {
		return this.smallTypeId;
	}

	public void setSmallTypeId(Long smallTypeId) {
		this.smallTypeId = smallTypeId;
	}

	public SysplDicBigType getSysplDicBigType() {
		return this.sysplDicBigType;
	}

	public void setSysplDicBigType(SysplDicBigType sysplDicBigType) {
		this.sysplDicBigType = sysplDicBigType;
	}

	public String getSmallTypeName() {
		return this.smallTypeName;
	}

	public void setSmallTypeName(String smallTypeName) {
		this.smallTypeName = smallTypeName;
	}

	public String getSmallTypeCode() {
		return this.smallTypeCode;
	}

	public void setSmallTypeCode(String smallTypeCode) {
		this.smallTypeCode = smallTypeCode;
	}
	
	public boolean equals(Object obj){
	   	if(this == obj) return true;
	   	if(obj == null) return false;
 		if(this.getClass() != obj.getClass()) return false;
 		
		SysplDicSmallType sysplDicSmallType = (SysplDicSmallType)obj;
		
		if(sysplDicSmallType.getSmallTypeId() != null && this.smallTypeId != null && 
			sysplDicSmallType.getSmallTypeId().longValue() == this.smallTypeId.longValue()){
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		int result = 17;

		result = 37 * result + (smallTypeId == null ? 0 : smallTypeId.hashCode());
		return result;
	}
	
	public SysplDicSmallTypeComp getSysplDicSmallTypeInst(){
		SysplDicSmallTypeComp sysplDicSmallTypeComp = new SysplDicSmallTypeComp();
		return sysplDicSmallTypeComp;
	}
	
	class SysplDicSmallTypeComp implements Comparator {
		public int compare(Object arg0, Object arg1) {
			SysplDicSmallType sysplDicSmallType1 = (SysplDicSmallType)arg0;
			SysplDicSmallType sysplDicSmallType2 = (SysplDicSmallType)arg1;
			int flag = sysplDicSmallType1.getSmallTypeId().compareTo(sysplDicSmallType2.getSmallTypeId());
			return flag;
		}
		
	}
}

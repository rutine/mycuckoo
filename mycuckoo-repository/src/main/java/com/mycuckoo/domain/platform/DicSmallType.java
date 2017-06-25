package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:03:38 PM
 * @version 3.0.0
 */
public class DicSmallType implements Serializable {

	private Long smallTypeId;
	private DicBigType sysplDicBigType;
	private String smallTypeName;
	private String smallTypeCode;

	/** default constructor */
	public DicSmallType() {
	}

	/** minimal constructor */
	public DicSmallType(Long smallTypeId) {
		this.smallTypeId = smallTypeId;
	}

	/** full constructor */
	public DicSmallType(Long smallTypeId, DicBigType sysplDicBigType,
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

	public DicBigType getSysplDicBigType() {
		return this.sysplDicBigType;
	}

	public void setSysplDicBigType(DicBigType sysplDicBigType) {
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
 		
		DicSmallType sysplDicSmallType = (DicSmallType)obj;
		
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
	
	public DicSmallTypeComp getDicSmallTypeInst(){
		return new DicSmallTypeComp();
	}
	
	class DicSmallTypeComp implements Comparator {
		public int compare(Object arg0, Object arg1) {
			DicSmallType dicSmallType1 = (DicSmallType)arg0;
			DicSmallType dicSmallType2 = (DicSmallType)arg1;
			int flag = dicSmallType1.getSmallTypeId().compareTo(dicSmallType2.getSmallTypeId());
			
			return flag;
		}
		
	}
}

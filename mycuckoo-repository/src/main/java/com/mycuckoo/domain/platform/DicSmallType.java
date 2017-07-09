package com.mycuckoo.domain.platform;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 22, 2014 9:03:38 PM
 * @version 3.0.0
 */
public class DicSmallType implements Serializable {

	private Long smallTypeId;
	private Long bigTypeId;
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
	public DicSmallType(Long smallTypeId, Long bigTypeId,
			String smallTypeName, String smallTypeCode) {
		this.smallTypeId = smallTypeId;
		this.bigTypeId = bigTypeId;
		this.smallTypeName = smallTypeName;
		this.smallTypeCode = smallTypeCode;
	}

	public Long getSmallTypeId() {
		return this.smallTypeId;
	}

	public void setSmallTypeId(Long smallTypeId) {
		this.smallTypeId = smallTypeId;
	}

	public Long getBigTypeId() {
		return bigTypeId;
	}

	public void setBigTypeId(Long bigTypeId) {
		this.bigTypeId = bigTypeId;
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
 		
		DicSmallType dicSmallType = (DicSmallType)obj;
		
		if(dicSmallType.getSmallTypeId() != null && this.smallTypeId != null && 
			dicSmallType.getSmallTypeId().longValue() == this.smallTypeId.longValue()){
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
	
	public DicSmallTypeComp getDicSmallTypeInst() {
		return new DicSmallTypeComp();
	}
	
	class DicSmallTypeComp implements Comparator<DicSmallType> {
		public int compare(DicSmallType dicSmallType1, DicSmallType dicSmallType2) {
			int flag = dicSmallType1.getSmallTypeId().compareTo(dicSmallType2.getSmallTypeId());
			
			return flag;
		}
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}

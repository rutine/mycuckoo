package com.mycuckoo.domain.uum;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @time Sep 23, 2014 10:44:33 AM
 * @version 3.0.0
 */
public class Organ implements java.io.Serializable {

	private Long orgId; 			// 主键
	private Long parentId; 			// 上一级
	private String orgSimpleName; 	// 机构简称
	private String orgFullName; 	// 机构全称
	private String orgCode; 		// 机构代码
	private String orgAddress1; 	// 联系地址1
	private String orgAddress2; 	// 联系地址2
	private String orgTel1; 		// 联系电话1
	private String orgTel2; 		// 联系电话2
	private Date orgBeginDate; 		// 成立日期
	private String orgType; 		// 机构类型
	private String orgFax; 			// 传真
	private String orgPostal; 		// 机构邮编
	private String orgLegal; 		// 法人代表
	private String orgTaxNo; 		// 税务号
	private String orgRegNo; 		// 注册登记号
	private Long orgBelongDist; 	// 所属地区
	private String status; 			// 机构状态
	private String memo; 			// 备注
	private String creator; 		// 创建人
	private Date createDate; 		// 创建时间
	
//	private List<OrgRoleRef> orgRoleRefs = Lists.newArrayList(); // 
//	private List<User> users =  Lists.newArrayList(); // 
//	private List<Organ> organs = Lists.newArrayList(); // 
//	
//	
//	private Long upOrgId;				//上级机构ID
//	private String upOrgName; 			//上级机构
//	private boolean isLeaf;
//	private String dataIconCls;
//	
//	private String orgBelongDistName;	// 所属地区
	
	/** default constructor */
	public Organ() {
	}

	/** minimal constructor */
	public Organ(Long orgId, String status) {
		this.orgId = orgId;
		this.status = status;
	}

	/** full constructor */
	public Organ(Long orgId, Long parentId, String orgSimpleName,
			String orgFullName, String orgCode, String orgAddress1,
			String orgAddress2, String orgTel1, String orgTel2,
			Date orgBeginDate, String orgType, String orgFax, String orgPostal,
			String orgLegal, String orgTaxNo, String orgRegNo,
			Long orgBelongDist, String status, String memo, String creator,
			Date createDate) {
		this.orgId = orgId;
		this.parentId = parentId;
		this.orgSimpleName = orgSimpleName;
		this.orgFullName = orgFullName;
		this.orgCode = orgCode;
		this.orgAddress1 = orgAddress1;
		this.orgAddress2 = orgAddress2;
		this.orgTel1 = orgTel1;
		this.orgTel2 = orgTel2;
		this.orgBeginDate = orgBeginDate;
		this.orgType = orgType;
		this.orgFax = orgFax;
		this.orgPostal = orgPostal;
		this.orgLegal = orgLegal;
		this.orgTaxNo = orgTaxNo;
		this.orgRegNo = orgRegNo;
		this.orgBelongDist = orgBelongDist;
		this.status = status;
		this.memo = memo;
		this.creator = creator;
		this.createDate = createDate;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getOrgSimpleName() {
		return this.orgSimpleName;
	}

	public void setOrgSimpleName(String orgSimpleName) {
		this.orgSimpleName = orgSimpleName;
	}

	public String getOrgFullName() {
		return this.orgFullName;
	}

	public void setOrgFullName(String orgFullName) {
		this.orgFullName = orgFullName;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgAddress1() {
		return this.orgAddress1;
	}

	public void setOrgAddress1(String orgAddress1) {
		this.orgAddress1 = orgAddress1;
	}

	public String getOrgAddress2() {
		return this.orgAddress2;
	}

	public void setOrgAddress2(String orgAddress2) {
		this.orgAddress2 = orgAddress2;
	}

	public String getOrgTel1() {
		return this.orgTel1;
	}

	public void setOrgTel1(String orgTel1) {
		this.orgTel1 = orgTel1;
	}

	public String getOrgTel2() {
		return this.orgTel2;
	}

	public void setOrgTel2(String orgTel2) {
		this.orgTel2 = orgTel2;
	}

	public Date getOrgBeginDate() {
		return this.orgBeginDate;
	}

	public void setOrgBeginDate(Date orgBeginDate) {
		this.orgBeginDate = orgBeginDate;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgFax() {
		return this.orgFax;
	}

	public void setOrgFax(String orgFax) {
		this.orgFax = orgFax;
	}

	public String getOrgPostal() {
		return this.orgPostal;
	}

	public void setOrgPostal(String orgPostal) {
		this.orgPostal = orgPostal;
	}

	public String getOrgLegal() {
		return this.orgLegal;
	}

	public void setOrgLegal(String orgLegal) {
		this.orgLegal = orgLegal;
	}

	public String getOrgTaxNo() {
		return this.orgTaxNo;
	}

	public void setOrgTaxNo(String orgTaxNo) {
		this.orgTaxNo = orgTaxNo;
	}

	public String getOrgRegNo() {
		return this.orgRegNo;
	}

	public void setOrgRegNo(String orgRegNo) {
		this.orgRegNo = orgRegNo;
	}

	public Long getOrgBelongDist() {
		return this.orgBelongDist;
	}

	public void setOrgBelongDist(Long orgBelongDist) {
		this.orgBelongDist = orgBelongDist;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
	public boolean equals(Object obj){
		if(this == obj) return true; 	// quict check
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Organ uumOrgan = (Organ)obj;
		if(orgId != null && uumOrgan.getOrgId() != null && 
			orgId.longValue() ==  uumOrgan.getOrgId().longValue()){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int hashcode = 7;
		
		hashcode = hashcode * 37 + (orgId == null ? 0 : orgId.hashCode());
		
		return hashcode;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
	
}

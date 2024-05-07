package com.mycuckoo.domain.uum;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 23, 2014 10:44:33 AM
 */
public class Organ implements java.io.Serializable {

    private Long orgId;            // 主键
    private Long parentId;            // 上一级
    private String code;        // 机构代码
    private String simpleName;    // 机构简称
    private String fullName;    // 机构全称
    private String address1;    // 联系地址1
    private String address2;    // 联系地址2
    private String tel1;        // 联系电话1
    private String tel2;        // 联系电话2
    private Date beginDate;        // 成立日期
    private String type;        // 机构类型
    private String fax;            // 传真
    private String postal;        // 机构邮编
    private String legal;        // 法人代表
    private String taxNo;        // 税务号
    private String regNo;        // 注册登记号
    private Long belongDist;    // 所属地区
    private String status;            // 机构状态
    private String memo;            // 备注
    private String updater;        // 更新人
    private Date updateDate;        // 更新时间
    private String creator;        // 创建人
    private Date createDate;        // 创建时间

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
//	private String belongDistName;	// 所属地区

    /**
     * default constructor
     */
    public Organ() {
    }

    /**
     * minimal constructor
     */
    public Organ(Long orgId, String status) {
        this.orgId = orgId;
        this.status = status;
    }

    /**
     * full constructor
     */
    public Organ(Long orgId, Long parentId, String code,
                 String simpleName, String fullName, String address1,
                 String address2, String tel1, String tel2,
                 Date beginDate, String type, String fax, String postal,
                 String legal, String taxNo, String regNo,
                 Long belongDist, String status, String memo,
                 String updater, Date updateDate, String creator, Date createDate) {
        this.orgId = orgId;
        this.parentId = parentId;
        this.code = code;
        this.simpleName = simpleName;
        this.fullName = fullName;
        this.address1 = address1;
        this.address2 = address2;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.beginDate = beginDate;
        this.type = type;
        this.fax = fax;
        this.postal = postal;
        this.legal = legal;
        this.taxNo = taxNo;
        this.regNo = regNo;
        this.belongDist = belongDist;
        this.status = status;
        this.memo = memo;
        this.updater = updater;
        this.updateDate = updateDate;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public Long getBelongDist() {
        return belongDist;
    }

    public void setBelongDist(Long belongDist) {
        this.belongDist = belongDist;
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

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
    public boolean equals(Object obj) {
        if (this == obj) return true;    // quict check
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Organ uumOrgan = (Organ) obj;
        if (orgId != null && uumOrgan.getOrgId() != null &&
                orgId.longValue() == uumOrgan.getOrgId().longValue()) {
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

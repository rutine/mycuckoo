package com.mycuckoo.domain.uum;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 23, 2014 10:44:33 AM
 */
public class Organ extends BasicDomain<Long> {

    private Long orgId;             // 主键
    private Long parentId;          // 上一级
    private String treeId;
    private Long roleId;
    private Integer level;
    private String code;            // 机构代码
    private String simpleName;      // 机构简称
    private String fullName;        // 机构全称
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

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId == null ? treeId : treeId.trim();
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? code : code.trim();
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName == null ? simpleName : simpleName.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? fullName : fullName.trim();
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1 == null ? address1 : address1.trim();
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2 == null ? address2 : address2.trim();
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1 == null ? tel1 : tel1.trim();
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2 == null ? tel2 : tel2.trim();
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
        this.type = type == null ? type : type.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? fax : fax.trim();
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal == null ? postal : postal.trim();
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal == null ? legal : legal.trim();
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo == null ? taxNo : taxNo.trim();
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo == null ? regNo : regNo.trim();
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
        this.status = status == null ? status : status.trim();
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? memo : memo.trim();
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

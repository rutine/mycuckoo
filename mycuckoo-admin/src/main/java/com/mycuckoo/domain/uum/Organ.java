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

    /**
     * full constructor
     */
    public Organ(Long orgId, Long parentId, String treeId, Long roleId, Integer level, String code,
                 String simpleName, String fullName, String address1,
                 String address2, String tel1, String tel2,
                 Date beginDate, String type, String fax, String postal,
                 String legal, String taxNo, String regNo,
                 Long belongDist, String status, String memo,
                 String updator, LocalDateTime updateTime,
                 String creator, LocalDateTime createTime) {
        this.orgId = orgId;
        this.parentId = parentId;
        this.treeId = treeId;
        this.roleId = roleId;
        this.level = level;
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
        this.updator = updator;
        this.updateTime = updateTime;
        this.creator = creator;
        this.createTime = createTime;
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
        this.treeId = treeId;
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

package com.mycuckoo.domain.uum;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 8:38:53 PM
 */
public class User implements Serializable {

    private Long userId; //用户ID
    private Long orgId;
    private Long accountId;
    private Long roleId;
    private Long deptId;
    private String code; //用户号
    private String name; //用户名
    private String pinyin;
    private String phone; //用户手机
    private String email; //用户邮件
    private String password; //用户密码
    private String gender; //用户性别
    private String position; //用户职位
    private String photoUrl; //用户照片
    private String qq; //用户QQ
    private String wechat; //用户wechat
    private String officeTel; //办公电话
    private String familyTel; //家庭电话
    private Date avidate; //用户有效期
    private String address; //家庭住址
    private String memo; //备注
    private String status; //用户状态
    private String updater; //更新人
    private Date updateDate; //更新时间
    private String creator; //创建人
    private Date createDate; //创建时间

    /**
     * default constructor
     */
    public User() {
    }

    /**
     * minimal constructor
     */
    public User(Long userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    /**
     * full constructor
     */
    public User(Long userId,
                Long orgId, Long accountId,
                Long roleId, Long deptId,
                String code, String name,
                String phone, String email,
                String password, String gender,
                String position, String photoUrl,
                String qq, String wechat,
                String officeTel, String familyTel,
                Date avidate, String address, Long belongOrg,
                String memo, String status,
                String updater, Date updateDate, String creator, Date createDate) {
        this.userId = userId;
        this.orgId = orgId;
        this.accountId = accountId;
        this.roleId = roleId;
        this.deptId = deptId;
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.position = position;
        this.photoUrl = photoUrl;
        this.qq = qq;
        this.wechat = wechat;
        this.officeTel = officeTel;
        this.familyTel = familyTel;
        this.avidate = avidate;
        this.address = address;
        this.memo = memo;
        this.status = status;
        this.updater = updater;
        this.updateDate = updateDate;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getOfficeTel() {
        return officeTel;
    }

    public void setOfficeTel(String officeTel) {
        this.officeTel = officeTel;
    }

    public String getFamilyTel() {
        return familyTel;
    }

    public void setFamilyTel(String familyTel) {
        this.familyTel = familyTel;
    }

    public Date getAvidate() {
        return avidate;
    }

    public void setAvidate(Date avidate) {
        this.avidate = avidate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;        // (1) same object?
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User user = (User) obj;
        if (code != null && code.equals(user.getCode()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hashcode = 7;

        hashcode = hashcode * 37 + (userId == null ? 0 : userId.hashCode());
        hashcode = hashcode * 37 + (code == null ? 0 : code.hashCode());

        return hashcode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

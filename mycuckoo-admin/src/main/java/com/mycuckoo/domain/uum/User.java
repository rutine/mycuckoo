package com.mycuckoo.domain.uum;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 8:38:53 PM
 */
public class User extends BasicDomain<Long> {

    private Long userId; //用户ID
    private Long accountId;
    private Long roleId;
    private Long deptId;
    private String name; //用户名
    private String pinyin;
    private String phone; //用户手机
    private String email; //用户邮件
    private String gender; //用户性别
    private String position; //用户职位
    private String photoUrl; //用户照片
    private Date avidate; //用户有效期
    private String address; //家庭住址
    private String memo; //备注
    private String status; //用户状态

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

    public Long getUserId() {
        return userId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? name : name.trim();
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
        this.phone = phone == null ? phone : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? email : email.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? gender : gender.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? position : position.trim();
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? photoUrl : photoUrl.trim();
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
        this.address = address == null ? address : address.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? memo : memo.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? status : status.trim();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;        // (1) same object?
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User user = (User) obj;
        if (userId != null && userId.equals(user.getUserId()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hashcode = 7;

        hashcode = hashcode * 37 + (userId == null ? 0 : userId.hashCode());
        hashcode = hashcode * 37 + (userId == null ? 0 : userId.hashCode());

        return hashcode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

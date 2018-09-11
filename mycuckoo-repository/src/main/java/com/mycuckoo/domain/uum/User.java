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
    private Long userBelongtoOrg;
    private String userCode; //用户号
    private String userNamePy;
    private String userName; //用户名
    private String userPassword; //用户密码
    private String userGender; //用户性别
    private String userPosition; //用户职位
    private String userPhotoUrl; //用户照片
    private String userQq; //用户QQ
    private String userMsn; //用户MSN
    private String userMobile; //用户手机
    private String userMobile2; //用户手机2
    private String userOfficeTel; //办公电话
    private String userFamilyTel; //家庭电话
    private String userEmail; //用户邮件
    private Date userAvidate; //用户有效期
    private String userIsAgent;
    private String memo; //备注
    private String status; //用户状态
    private String creator; //创建人
    private Date createDate; //创建时间
    private String userAddress; //家庭住址

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
    public User(Long userId, Long userBelongtoOrg, String userCode,
                String userName, String userPassword, String userGender,
                String userPosition, String userPhotoUrl, String userQq,
                String userMsn, String userMobile, String userMobile2,
                String userOfficeTel, String userFamilyTel, String userEmail,
                Date userAvidate, String userIsAgent, String memo, String status,
                String creator, Date createDate) {
        this.userId = userId;
        this.userBelongtoOrg = userBelongtoOrg;
        this.userCode = userCode;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userGender = userGender;
        this.userPosition = userPosition;
        this.userPhotoUrl = userPhotoUrl;
        this.userQq = userQq;
        this.userMsn = userMsn;
        this.userMobile = userMobile;
        this.userMobile2 = userMobile2;
        this.userOfficeTel = userOfficeTel;
        this.userFamilyTel = userFamilyTel;
        this.userEmail = userEmail;
        this.userAvidate = userAvidate;
        this.userIsAgent = userIsAgent;
        this.memo = memo;
        this.status = status;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserBelongtoOrg() {
        return userBelongtoOrg;
    }

    public void setUserBelongtoOrg(Long userBelongtoOrg) {
        this.userBelongtoOrg = userBelongtoOrg;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserGender() {
        return this.userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserPosition() {
        return this.userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public String getUserPhotoUrl() {
        return this.userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserQq() {
        return this.userQq;
    }

    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    public String getUserMsn() {
        return this.userMsn;
    }

    public void setUserMsn(String userMsn) {
        this.userMsn = userMsn;
    }

    public String getUserMobile() {
        return this.userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserMobile2() {
        return this.userMobile2;
    }

    public void setUserMobile2(String userMobile2) {
        this.userMobile2 = userMobile2;
    }

    public String getUserOfficeTel() {
        return this.userOfficeTel;
    }

    public void setUserOfficeTel(String userOfficeTel) {
        this.userOfficeTel = userOfficeTel;
    }

    public String getUserFamilyTel() {
        return this.userFamilyTel;
    }

    public void setUserFamilyTel(String userFamilyTel) {
        this.userFamilyTel = userFamilyTel;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getUserAvidate() {
        return this.userAvidate;
    }

    public void setUserAvidate(Date userAvidate) {
        this.userAvidate = userAvidate;
    }

    public String getUserIsAgent() {
        return this.userIsAgent;
    }

    public void setUserIsAgent(String userIsAgent) {
        this.userIsAgent = userIsAgent;
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

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserNamePy() {
        return userNamePy;
    }

    public void setUserNamePy(String userNamePy) {
        this.userNamePy = userNamePy;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;        // (1) same object?
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User user = (User) obj;
        if (userCode != null && userCode.equals(user.getUserCode()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hashcode = 7;

        hashcode = hashcode * 37 + (userId == null ? 0 : userId.hashCode());
        hashcode = hashcode * 37 + (userCode == null ? 0 : userCode.hashCode());

        return hashcode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

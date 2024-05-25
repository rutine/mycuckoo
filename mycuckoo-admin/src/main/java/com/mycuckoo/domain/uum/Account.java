package com.mycuckoo.domain.uum;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 4.1.0
 * @time May 18, 2024 8:40:53 PM
 */
public class Account implements Serializable {

    private Long accountId; //ID
    private String phone; //手机
    private String email; //邮件
    private String password; //密码
    private Integer errorCount; //密码错误次数
    private String ip; //
    private Date loginDate; //最近登录时间
    private Date updateDate; //更新时间
    private Date createDate; //创建时间

    /**
     * default constructor
     */
    public Account() {
    }

    /**
     * minimal constructor
     */
    public Account(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * full constructor
     */
    public Account(Long accountId,
                   String phone, String email,
                   String password,
                   Integer errorCount, String ip,
                   Date loginDate, Date updateDate, Date createDate) {
        this.accountId = accountId;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.errorCount = errorCount;
        this.ip = ip;
        this.loginDate = loginDate;
        this.updateDate = updateDate;
        this.createDate = createDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
        Account act = (Account) obj;
        if (phone != null && phone.equals(act.getPhone()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hashcode = 7;

        hashcode = hashcode * 37 + (accountId == null ? 0 : accountId.hashCode());
        hashcode = hashcode * 37 + (phone == null ? 0 : phone.hashCode());

        return hashcode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

package com.mycuckoo.domain.uum;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 4.1.0
 * @time May 18, 2024 8:40:53 PM
 */
public class Account implements Serializable {

    private Long accountId; //ID
    private String account; //用户号
    private String phone; //手机
    private String email; //邮件
    private String password; //密码
    private Integer errorCount; //密码错误次数
    private String ip; //
    private LocalDateTime loginTime; //最近登录时间
    private LocalDateTime updateTime; //更新时间
    private LocalDateTime createTime; //创建时间

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


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? account : account.trim();;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? password : password.trim();
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

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
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

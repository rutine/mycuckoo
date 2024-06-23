package com.mycuckoo.web.vo.res;

import java.time.LocalDateTime;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/6/22 13:07
 */
public class AccountInfo {
    private Long accountId;
    private String account;
    private LocalDateTime loginTime;

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
        this.account = account;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}

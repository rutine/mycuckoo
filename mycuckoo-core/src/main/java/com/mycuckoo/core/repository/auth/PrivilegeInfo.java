package com.mycuckoo.core.repository.auth;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:21
 */
public class PrivilegeInfo {
    private Integer orgId;
    private Integer userId;
    private boolean skip;

    public PrivilegeInfo(Integer orgId, Integer userId) {
        this.orgId = orgId;
        this.userId = userId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public Integer getUserId() {
        return userId;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
}

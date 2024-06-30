package com.mycuckoo.core.repository.auth;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 4.1.0
 * @time 2024/5/25 10:21
 */
public class RowInfo {
    private Long orgId;
    private Long userId;
    private boolean skip;

    public RowInfo(Long orgId, Long userId) {
        this.orgId = orgId;
        this.userId = userId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }
}

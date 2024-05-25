package com.mycuckoo.web.vo.res.uum;

import com.mycuckoo.domain.uum.User;

/**
 * 功能说明: 用户vo
 *
 * @author rutine
 * @version 3.0.0
 * @time Jul 9, 2017 6:16:58 PM
 */
public class UserVo extends User {
    private String roleName;
    private String orgName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}

package com.mycuckoo.web.vo.res;

import com.mycuckoo.web.vo.res.platform.HierarchyModuleVo;
import com.mycuckoo.core.UserInfo;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-10-06 15:48
 */
public class LoginUserInfo {
    private HierarchyModuleVo menu;
    private UserInfo user;

    public HierarchyModuleVo getMenu() {
        return menu;
    }

    public void setMenu(HierarchyModuleVo menu) {
        this.menu = menu;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}

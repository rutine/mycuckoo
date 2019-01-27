package com.mycuckoo.web.vo.res;

import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.uum.UserRoleVo;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-10-06 15:48
 */
public class LoginUserInfo {
    private HierarchyModuleVo menu;
    private UserRoleVo user;

    public HierarchyModuleVo getMenu() {
        return menu;
    }

    public void setMenu(HierarchyModuleVo menu) {
        this.menu = menu;
    }

    public UserRoleVo getUser() {
        return user;
    }

    public void setUser(UserRoleVo user) {
        this.user = user;
    }
}

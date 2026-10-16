package com.mycuckoo.admin.entity.dto;

import com.mycuckoo.admin.entity.dto.platform.HierarchyModuleDto;
import com.mycuckoo.core.UserInfo;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 3.0.0
 * @time 2017-10-06 15:48
 */
public class LoginUserInfo {
    private HierarchyModuleDto menu;
    private UserInfo user;

    public HierarchyModuleDto getMenu() {
        return menu;
    }

    public void setMenu(HierarchyModuleDto menu) {
        this.menu = menu;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}

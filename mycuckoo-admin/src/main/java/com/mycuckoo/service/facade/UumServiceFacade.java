package com.mycuckoo.service.facade;

import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserExtend;
import com.mycuckoo.service.uum.AccountService;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.core.CheckboxTree;
import com.mycuckoo.web.vo.res.platform.HierarchyModuleVo;
import com.mycuckoo.web.vo.res.platform.ModuleMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能说明: 用户常用业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:15:12 AM
 */
@Service
@Transactional(readOnly = true)
public class UumServiceFacade {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private PrivilegeService privilegeService;


    public void deletePrivilegeByModOptId(String[] modOptRefIds) {
        this.privilegeService.deletePrivilegeByModOptId(modOptRefIds);
    }

    public void deletePrivilegeByModResId(String[] modOptRefIds) {
        this.privilegeService.deletePrivilegeByModResId(modOptRefIds);
    }

    public Long getAccountByPhoneAndPwd(String phone, String password, String ip) {
        return accountService.getAccountIdByPhoneOrEmail(phone, null, password, ip);
    }

    public User getUserByUserCodeAndPwd(String userCode, String password) {
        return userService.getUserByUserCodeAndPwd(userCode, password);
    }

    public UserExtend getUserByAccountIdAndUserId(Long accountId, Long userId) {
        return userService.getByAccountIdAndUserId(accountId, userId);
    }

    public List<UserExtend> findByAccountId(Long accountId) {
        return userService.findByAccountId(accountId);
    }

    public HierarchyModuleVo findPrivilegesForAdminLogin() {
        return privilegeService.findPrivilegesForAdminLogin();
    }

    public HierarchyModuleVo findPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId) {
        return privilegeService.findPrivilegesForUserLogin(userId, roleId, organId, organRoleId);
    }

    public boolean existsSpecialPrivilegeForUser(long userId) {
        return privilegeService.existsSpecialPrivilegeByUserId(userId);
    }

    public List<CheckboxTree> convertToTree(List<ModuleMenuVo> vos) {
        return privilegeService.convertToTree(vos);
    }
}

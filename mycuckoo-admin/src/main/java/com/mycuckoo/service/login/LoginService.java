package com.mycuckoo.service.login;

import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.util.PwdCrypt;
import com.mycuckoo.core.util.SystemConfigXmlParse;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.core.web.filter.PrivilegeFilter;
import com.mycuckoo.domain.uum.Account;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.domain.uum.UserExtend;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.facade.UumServiceFacade;
import com.mycuckoo.service.uum.AccountService;
import com.mycuckoo.service.uum.OrganService;
import com.mycuckoo.service.uum.RoleService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.web.vo.res.platform.HierarchyModuleVo;
import com.mycuckoo.web.vo.res.platform.ResourceVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能说明: 用户登录service
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:16:53 AM
 */
@Service
@Transactional(readOnly = true)
public class LoginService {

    @Autowired
    private OrganService organService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private UumServiceFacade uumServiceFacade;
    @Autowired
    private PlatformServiceFacade platformServiceFacade;


    @Transactional
    public void register(String username, String phone, String password, String orgName) {
        Role defaultRole = roleService.getRegDefault("web");
        Assert.notNull(defaultRole, "暂不支持注册");
        long accountId = accountService.save(null, phone, null, password);
        long orgId = organService.save(orgName, defaultRole.getRoleId());
        long userId = userService.save(orgId, accountId, -1L, username, phone, null);
    }


    public boolean isAdmin(String account) {
        // 通过配置XML获得管理员用户，管理员则不需要权限过滤
        List<String> adminCodes = SystemConfigXmlParse
                .getInstance().getSystemConfigBean().getSystemMgr();
        // 管理员
        if (adminCodes.contains(account)) return true;

        return false;
    }

    public Account getAccountBy(String account, String password) {
        password = PwdCrypt.getInstance().encrypt(password);//明文加密成密文

        return accountService.getBy(account, password, SessionContextHolder.getIP());
    }

    public UserInfo getUserByAccountIdAndUserId(Long accountId, Long userId) {
        UserExtend user = userService.getByAccountIdAndUserId(accountId, userId);
        if (user == null) {
            return null;
        }

        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(user, info);
        info.setId(user.getUserId());
        info.setOrgId(user.getOrgId());
        info.setUserName(user.getName());

        return info;
    }

    public List<UserExtend> login(Long accountId) {
        return userService.findByAccountId(accountId);
    }

    public HierarchyModuleVo filterPrivilege(Long userId, Long roleId, Long organId, String account) {
        // ====== 2 加载菜单 ======
        HierarchyModuleVo moduleVo = null;
        if (isAdmin(account)) {// 是管理员
            // 加载全部权限
            moduleVo = uumServiceFacade.findPrivilegesForAdminLogin();
        } else {// 非管理员
            // 模块权限过滤，用户是否有特殊权限，并过滤特殊权限
            moduleVo = uumServiceFacade.findPrivilegesForUserLogin(userId, roleId, organId);
        }

        return moduleVo;
    }

    // 加载用户资源
    public List<String> getUserResources(Long userId, Long roleId, Long organId, String account) {
        return this.filterPrivilege(userId, roleId, organId, account)
                .getFourth().values().stream()
                .flatMap(o -> o.stream())
                .map(ResourceVo::getId)
                .map(String::valueOf)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<PrivilegeFilter.ResourceInfo> getAllResources() {
        return platformServiceFacade.findAllModResRefs().stream()
                .map(o -> new PrivilegeFilter.ResourceInfo(o.getPath(), o.getMethod(), o.getId().toString()))
                .collect(Collectors.toList());
    }
}

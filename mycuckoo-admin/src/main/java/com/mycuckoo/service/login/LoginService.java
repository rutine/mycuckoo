package com.mycuckoo.service.login;

import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserExtend;
import com.mycuckoo.service.facade.UumServiceFacade;
import com.mycuckoo.util.PwdCrypt;
import com.mycuckoo.util.web.SessionUtil;
import com.mycuckoo.util.SystemConfigXmlParse;
import com.mycuckoo.web.vo.res.platform.HierarchyModuleVo;
import com.mycuckoo.core.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private UumServiceFacade uumServiceFacade;


    public boolean isAdmin(String userCode) {
        // 通过配置XML获得管理员用户，管理员则不需要权限过滤
        List<String> adminCodes = SystemConfigXmlParse
                .getInstance().getSystemConfigBean().getSystemMgr();
        // 管理员
        if (adminCodes.contains(userCode)) return true;

        return false;
    }

    public Long getAccountByPhoneAndPwd(String phone, String password) {
        password = PwdCrypt.getInstance().encrypt(password);//明文加密成密文

        return uumServiceFacade.getAccountByPhoneAndPwd(phone, password, SessionUtil.getIP());
    }

    public User getUserByUserCodePwd(String userCode, String password) {
        password = PwdCrypt.getInstance().encrypt(password);//明文加密成密文

        return uumServiceFacade.getUserByUserCodeAndPwd(userCode, password);
    }

    public UserInfo getUserByAccountIdAndUserId(Long accountId, Long userId) {
        UserExtend user = uumServiceFacade.getUserByAccountIdAndUserId(accountId, userId);
        if (user == null) {
            return null;
        }

        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(user, info);
        info.setId(user.getUserId());
        info.setUserName(user.getName());
        info.setUserCode(user.getCode());

        return info;
    }

    public List<UserExtend> preLogin(Long accountId) {
        return uumServiceFacade.findByAccountId(accountId);
    }

    public HierarchyModuleVo filterPrivilege(Long userId, Long roleId, Long organId, Long organRoleId, String userCode) {
        // ====== 2 加载菜单 ======
        HierarchyModuleVo moduleVo = null;
        if (isAdmin(userCode)) {// 是管理员
            // 加载全部权限
            moduleVo = uumServiceFacade.findPrivilegesForAdminLogin();
        } else {// 非管理员
            // 模块权限过滤，用户是否有特殊权限，并过滤特殊权限
            moduleVo = uumServiceFacade.findPrivilegesForUserLogin(userId, roleId, organId, organRoleId);
        }

        return moduleVo;
    }
}

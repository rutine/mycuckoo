package com.mycuckoo.web.login;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.AjaxResponse;
import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserExtend;
import com.mycuckoo.service.login.LoginService;
import com.mycuckoo.util.web.SessionUtil;
import com.mycuckoo.web.vo.res.LoginUserInfo;
import com.mycuckoo.web.vo.res.platform.HierarchyModuleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static com.mycuckoo.constant.BaseConst.*;

/**
 * 功能说明: 登陆系统Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:52:03 PM
 */
@RestController
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    public final static String ADMIN_ORGNAME = "总部";
    public final static String ADMIN_ROLENAME = "管理员";


    @Autowired
    private LoginService loginService;


    /**
     * 功能说明 : 登录系统第一阶段 代理 多角色
     *
     * @param account
     * @param password
     * @param session
     * @return
     * @author rutine
     * @time Nov 21, 2012 8:00:26 PM
     */
    @PostMapping("/login")
    public AjaxResponse<List<UserExtend>> login(
            @RequestParam String account,
            @RequestParam String password,
            HttpSession session) {
        
        /*
         * 1. 验证用户是否存在 并得到用户对象
         * 2. 用户没有相应角色则提示没有使用系统权限
         * 3. 用户状态是否可用
         * 4. 用户有效期是否已到
         * 5. 存在并已经登录则踢出
         * 6. 获得系统定义的角色切换方式，如果用户拥有多个角色则提示用户选择角色
         */
        boolean isAdmin = loginService.isAdmin(account);
//        User user = loginService.getUserByUserCodePwd(userCode, password);
//        if (user == null) {
//            throw new ApplicationException(1, "用户不存在");
//        } else if (!isAdmin && DISABLE.equals(user.getStatus())) {
//            throw new ApplicationException(3, "用户已被停用");
//        } else if (!isAdmin && (user.getAvidate() == null || (new Date()).after(user.getAvidate()))) {
//            throw new ApplicationException(4, "用户过期");
//        }
        Long accountId = loginService.getAccountByPhoneAndPwd(account, password);

        List<UserExtend> vos = loginService.preLogin(accountId);
        if (!isAdmin) {
            int len = vos.size();
            Iterator<UserExtend> it = vos.iterator();
            while (it.hasNext()) {
                User vo = it.next();
                if (vo.getRoleId() == null || vo.getRoleId() == 0) {
                    if (len == 1) {
                        throw new ApplicationException(4, "用户为无角色用户没有使用权限");
                    }
                    it.remove();
                }
            }

            if (vos.isEmpty()) {
                throw new ApplicationException(2, "用户没有权限");
            }
        }

        session.setAttribute(ACCOUNT_ID, accountId);
        session.setAttribute(ACCOUNT_ORG, vos);

        return AjaxResponse.create(vos);
    }

    /**
     * 功能说明 : 登录系统第二阶段, 设置用户会话信息
     *
     * @param userId
     * @param session
     * @return
     * @author rutine
     * @time Nov 21, 2012 8:00:41 PM
     */
    @PostMapping("/login/orgs")
    public AjaxResponse<?> listOrg(
            @RequestBody Long userId,
            HttpSession session) {
        /*
         * 7. 用户机构名称及ID、用户角色名称及ID角色级别、用户名称及ID、放入session
         */
        Long accountId = (Long) session.getAttribute(ACCOUNT_ID);
        UserInfo user = loginService.getUserByAccountIdAndUserId(accountId, userId);
        Assert.notNull(user, "所选组织不存在, 请选择正确组织登录!");
        String userName = user.getUserName();
        
        /*
         * 用户ID及名称、用户机构ID及名称、用户角色ID及名称角色级别、放入session
         * ID 均为Long
         */
        Long organId = user.getOrgId() == null ? -1L : user.getOrgId();
        String organName = user.getOrgName() == null ? ADMIN_ORGNAME : user.getOrgName();
        Long roleId = user.getRoleId() == null ? -1L : user.getRoleId();
        String roleName = user.getRoleName() == null ? ADMIN_ROLENAME : user.getRoleName();

        session.setAttribute(USER_INFO, user);

        logger.info("organId:   {}  -  organName:  {}", organId, organName);
        logger.info("roleId:    {}  -  roleName:   {}", roleId, roleName);
        logger.info("userId:    {}  -  userName:  {}", userId, userName);

        return AjaxResponse.create("登录成功");
    }

    /**
     * 功能说明 : 登录系统第三阶段, 加载用户菜单
     *
     * @return
     * @author rutine
     * @time Nov 21, 2012 8:01:00 PM
     */
    @PostMapping("/login/menus")
    public AjaxResponse<LoginUserInfo> listMenu(HttpServletRequest request, HttpSession session) {
        /*
         *  8  通过配置XML获得管理员用户，管理员则不需要权限过滤
         *  9 模块权限过滤，用户是否有特殊权限，并过滤特殊权限
         * 10 portal?
         */
        UserInfo user = SessionUtil.getUserInfo();
        Long organId = user.getOrgId();
        Long roleId = user.getRoleId();
        Long userId = user.getId();
        String userCode = user.getUserCode();

        HierarchyModuleVo moduleVo = (HierarchyModuleVo) session.getAttribute(MODULE_MENU);
        if (moduleVo == null) {
            // 加载用户菜单
            moduleVo = loginService.filterPrivilege(userId, roleId, organId, -1L, userCode);
            logger.info("user row privilege : 【{}】", moduleVo.getRow());

            session.setAttribute(MODULE_MENU, moduleVo);

            // 记录登录日志
            LogOperator.begin()
                    .module(ModuleName.USER_LOGIN)
                    .operate(OptName.USER_LOGIN)
                    .id(userId)
                    .title(null)
                    .content("组织：%s, 角色：%s, 用户: %s",
                            SessionUtil.getOrganName(), SessionUtil.getRoleName(), SessionUtil.getUserName())
                    .level(LogLevel.THIRD)
                    .emit();
        }

        LoginUserInfo userInfo = new LoginUserInfo();
        userInfo.setMenu(moduleVo);
        userInfo.setUser(user);

        return AjaxResponse.create(userInfo);
    }

    @GetMapping("/login/logout")
    public AjaxResponse<String> logout(HttpSession session) {
        session.invalidate();

        return AjaxResponse.create("成功退出登录");
    }


    @GetMapping("/test/{var}")
    public String test(
            @CookieValue("JSESSIONID") String cookie,
            @PathVariable String var) throws IOException {
        logger.debug("--------------------hello world!----------------");
        logger.debug("path variable --> " + var);
        logger.debug("sessionid : " + cookie);

        return "default";
    }
}

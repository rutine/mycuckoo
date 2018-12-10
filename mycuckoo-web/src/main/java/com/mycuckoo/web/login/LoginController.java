package com.mycuckoo.web.login;

import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.login.LoginService;
import com.mycuckoo.vo.HierarchyModuleVo;
import com.mycuckoo.vo.uum.UserRoleVo;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import com.mycuckoo.web.vo.res.LoginUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.mycuckoo.common.constant.Common.*;
import static com.mycuckoo.common.constant.ServiceVariable.DISABLE;

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
     * @param userCode
     * @param password
     * @param session
     * @return
     * @author rutine
     * @time Nov 21, 2012 8:00:26 PM
     */
    @PostMapping("/login/step/first")
    public AjaxResponse<List<UserRoleVo>> stepFirst(
            @RequestParam String userCode,
            @RequestParam String password,
            HttpSession session) {

        logger.debug("userCode --> " + userCode);
        logger.debug("password --> " + password);
        
        /*
         * 1. 验证用户是否存在 并得到用户对象
         * 2. 用户没有相应角色则提示没有使用系统权限
         * 3. 用户状态是否可用
         * 4. 用户有效期是否已到
         * 5. 存在并已经登录则踢出
         * 6. 获得系统定义的角色切换方式，如果用户拥有多个角色则提示用户选择角色
         */
        User user = null;
        boolean isAdmin = loginService.isAdmin(userCode);
        user = loginService.getUserByUserCodePwd(userCode, password);
        if (user == null) {
            throw new ApplicationException(1, "用户不存在");
        } else if (!isAdmin && DISABLE.equals(user.getStatus())) {
            throw new ApplicationException(3, "用户用户已被停用");
        } else if (!isAdmin && (user.getUserAvidate() == null || (new Date()).after(user.getUserAvidate()))) {
            throw new ApplicationException(4, "用户过期");
        }

        List<UserRoleVo> vos = loginService.preLogin(user);
        if (!isAdmin) {
            int len = vos.size();
            Iterator<UserRoleVo> it = vos.iterator();
            while (it.hasNext()) {
                UserRoleVo vo = it.next();
                if (vo.getOrganRoleId() == 0) {
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

        logger.debug("json --> " + JsonUtils.toJson(vos));
        session.setAttribute(USER_CODE, userCode);
        session.setAttribute("loginRoles", vos);

        return AjaxResponse.create(vos);
    }

    /**
     * 功能说明 : 登录系统第二阶段, 设置用户会话信息
     *
     * @param role
     * @param session
     * @return
     * @author rutine
     * @time Nov 21, 2012 8:00:41 PM
     */
    @PostMapping("/login/step/second")
    public AjaxResponse<?> stepSecond(
            @RequestBody UserRoleVo role,
            HttpSession session) {

        logger.debug("role --> {}", JsonUtils.toJson(role));
        
        /*
         * 7. 用户机构名称及ID、用户角色名称及ID角色级别、用户名称及ID、放入session
         */
        Long userId = null;
        String userCode = (String) session.getAttribute(USER_CODE);
        String userName = null;
        String userPhotoUrl = null;
        if (role != null) {
            userId = role.getUserId();
            userName = role.getUserName();
            userPhotoUrl = role.getUserPhotoUrl();
        }
        
        /*
         * 用户ID及名称、用户机构ID及名称、用户角色ID及名称角色级别、放入session
         * ID 均为Long
         */
        Long organId = role.getOrganId() == null ? -1L : role.getOrganId();
        String organName = role.getOrganName() == null ? ADMIN_ORGNAME : role.getOrganName();
        Long organRoleId = role.getOrganRoleId() == null ? -1L : role.getOrganRoleId();
        Long roleId = role.getRoleId() == null ? -1L : role.getRoleId();
        String roleName = role.getRoleName() == null ? ADMIN_ROLENAME : role.getRoleName();


        session.setAttribute(ORGAN_ID, organId);
        session.setAttribute(ORGAN_NAME, organName);
        session.setAttribute(ORGAN_ROLE_ID, organRoleId);
        session.setAttribute(ROLE_ID, roleId);
        session.setAttribute(ROLE_NAME, roleName);
        session.setAttribute(USER_ID, userId);
        session.setAttribute(USER_CODE, userCode);
        session.setAttribute(USER_NAME, userName);
        session.setAttribute(USER_PHOTO_URL, userPhotoUrl);

        logger.info("organId :      {}  -  organName : {}", organId, organName);
        logger.info("organRoleId :  {}  -  roleId :    {}  -  roleName : {}", organRoleId, roleId, roleName);
        logger.info("userId :       {}  -  userCode :  {}  -  userName : {}", userId, userCode, userName);

        return AjaxResponse.create("登录成功");
    }

    /**
     * 功能说明 : 登录系统第三阶段, 加载用户菜单
     *
     * @return
     * @author rutine
     * @time Nov 21, 2012 8:01:00 PM
     */
    @PostMapping("/login/step/third")
    public AjaxResponse<LoginUserInfo> stepThird(HttpServletRequest request, HttpSession session) {
        /*
         *  8  通过配置XML获得管理员用户，管理员则不需要权限过滤
         *  9 模块权限过滤，用户是否有特殊权限，并过滤特殊权限
         * 10 portal?
         */
        Long userId = (Long) session.getAttribute(USER_ID);
        Long roleId = (Long) session.getAttribute(ROLE_ID);
        Long organRoleId = (Long) session.getAttribute(ORGAN_ROLE_ID);
        Long organId = (Long) session.getAttribute(ORGAN_ID);
        String userCode = (String) session.getAttribute(USER_CODE);
        String userName = (String) session.getAttribute(USER_NAME);
        String userPhotoUrl = (String) session.getAttribute(USER_PHOTO_URL);

        // 加载用户菜单
        HierarchyModuleVo moduleVo = loginService.filterPrivilege(userId, roleId, organId, organRoleId, userCode);
        logger.info("user row privilege : 【{}】", moduleVo.getRow());

        session.setAttribute(MODULE_MENU, moduleVo);

        // 记录登录日志
        StringBuilder optContent = new StringBuilder();
        optContent.append(session.getAttribute(ORGAN_NAME) + "-")
                .append(session.getAttribute(ROLE_NAME) + "-")
                .append(session.getAttribute(USER_NAME));

        loginService.saveLog(LogLevelEnum.THIRD, OptNameEnum.USER_LOGIN,
                OptNameEnum.USER_LOGIN.value(), optContent.toString(), "");

        LoginUserInfo.UserInfo info = new LoginUserInfo.UserInfo();
        info.setUserCode(userCode);
        info.setUserName(userName);
        info.setUserPhotoUrl(userPhotoUrl);
        LoginUserInfo userInfo = new LoginUserInfo();
        userInfo.setMenu(moduleVo);
        userInfo.setUser(info);

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

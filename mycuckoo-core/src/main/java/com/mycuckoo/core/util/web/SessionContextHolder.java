package com.mycuckoo.core.util.web;

import com.mycuckoo.core.UserInfo;
import com.mycuckoo.core.exception.MyCuckooException;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static com.mycuckoo.constant.BaseConst.*;

/**
 * 功能说明: 取会话信息工具类，主要是统一获取会话中的信息
 *
 * @author rutine
 * @version 3.0.0
 * @time Nov 1, 2014 1:19:16 PM
 */
public final class SessionContextHolder {
    private static Logger logger = LoggerFactory.getLogger(SessionContextHolder.class);

    private static NamedThreadLocal<HttpServletRequest> localRequest = new NamedThreadLocal<>("Session Util");


    public static void setRequest(HttpServletRequest request) {
        if (request == null) {
            logger.debug("清理请求对象: {}", localRequest.get());
            localRequest.remove();
        } else {
            localRequest.set(request);
            logger.debug("缓存请求对象: {}", localRequest.get());
        }
    }

    private static HttpSession getSession(boolean b) {
        return localRequest.get().getSession(b);
    }

    /**
     * 功能说明 : 获取计算机IP
     *
     * @return 计算机IP
     * @author rutine
     * @time Jul 2, 2017 8:47:25 AM
     */
    public static String getIP() {
        RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = localRequest.get();
        if (request == null) {
            logger.error("request is null.");
            throw new MyCuckooException("request is null.");
        }

        return InetUtils.getIp(request);
    }

    /**
     * 功能说明 : 获取计算机名称
     *
     * @return 计算机名称
     * @author rutine
     * @time Jul 2, 2017 8:47:25 AM
     */
    public static String getHostName() {
        InetAddress IP = null;
        try {
            IP = InetAddress.getByName(getIP());
        } catch (UnknownHostException e) {
            logger.error("unknown IP: {}", IP, e);
        }
        //得到计算机名称
        String computName = IP.getHostName();

        return computName;
    }

    public static void setUserInfo(UserInfo user) {
        getSession(true).setAttribute(SESSION_USER_INFO, user);
    }

    /**
     * 功能说明 : 获取用户信息, 此值登录后存入会话
     *
     * @return 用户信息
     * @author rutine
     * @time Jan 27, 2019 3:16:53 PM
     */
    public static UserInfo getUserInfo() {
        return getSession(false) == null ? null : (UserInfo) getSession(false).getAttribute(SESSION_USER_INFO);
    }

    /**
     * 功能说明 : 获取用户所属的机构id, 此值登录后存入会话
     *
     * @return 机构id
     * @author rutine
     * @time Nov 1, 2014 1:24:53 PM
     */
    public static Long getOrganId() {
        return getUserInfo() != null ? getUserInfo().getOrgId() : null;
    }

    /**
     * 功能说明 : 获取用户所属的机构名称, 此值登录后存入会话
     *
     * @return 机构名称
     * @author rutine
     * @time Nov 1, 2014 1:27:53 PM
     */
    public static String getOrganName() {
        return getUserInfo() != null ? getUserInfo().getOrgName() : null;
    }

    /**
     * 功能说明 : 获取用户所属的角色id, 此值登录后存入会话
     *
     * @return 角色id
     * @author rutine
     * @time Nov 1, 2014 1:32:22 PM
     */
    public static Long getRoleId() {
        return getUserInfo() != null ? getUserInfo().getRoleId() : null;
    }

    /**
     * 功能说明 : 获取用户所属的角色名称, 此值登录后存入会话
     *
     * @return
     * @author rutine
     * @time Nov 1, 2014 1:33:19 PM
     */
    public static String getRoleName() {
        return getUserInfo() != null ? getUserInfo().getRoleName() : null;
    }

    /**
     * 功能说明 : 获取用户id, 此值登录后存入会话
     *
     * @return 用户id
     * @author rutine
     * @time Nov 1, 2014 1:35:39 PM
     */
    public static Long getUserId() {
        return getUserInfo() != null ? getUserInfo().getId() : null;
    }

    /**
     * 功能说明 : 获取账号id, 此值登录后存入会话
     */
    public static Long getAccountId() {
        return getSession(false) != null ? (Long) getSession(false).getAttribute(SESSION_ACCOUNT_ID) : null;
    }

    /**
     * 功能说明 : 获取账号, 此值登录后存入会话
     */
    public static String getAccountCode() {
        return getSession(false) != null ? (String) getSession(false).getAttribute(SESSION_ACCOUNT_CODE) : null;
    }

    /**
     * 功能说明 : 获取用户编码, 此值登录后存入会话
     *
     * @return 用户编码
     * @author rutine
     * @time Nov 1, 2014 1:36:38 PM
     */
    public static String getUserCode() {
        return (String) getUserInfo().getPinyin();
    }

    /**
     * 功能说明 : 获取用户名称, 此值登录后存入会话
     *
     * @return 用户名称
     * @author rutine
     * @time Nov 1, 2014 1:37:30 PM
     */
    public static String getUserName() {
        return getUserInfo() != null ? getUserInfo().getUserName() : null;
    }

    /**
     * 功能说明 : 获取用户头像, 此值登录后存入会话
     *
     * @return 用户头像
     * @author rutine
     * @time Nov 1, 2014 1:38:39 PM
     */
    public static String getUserPhotoUrl() {
        return getUserInfo() != null ? getUserInfo().getPhotoUrl() : null;
    }

//    /**
//     * 功能说明 : 获取用户菜单, 此值登录后存入会话
//     *
//     * @return
//     * @author rutine
//     * @time Nov 2, 2014 12:11:23 PM
//     */
//    public static HierarchyModuleVo getHierarchyModule() {
//        return (HierarchyModuleVo) getSession().getAttribute(SESSION_MODULE_MENU);
//    }

    /**
     * 功能说明 : 获取用户资源, 此值登录后存入会话
     */
    public static List<String> getResources() {
        return getSession(false) == null ? Lists.newArrayList() : (List < String >) getSession(false).getAttribute(SESSION_RES_CODES);
    }

}

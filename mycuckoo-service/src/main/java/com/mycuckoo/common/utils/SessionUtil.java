package com.mycuckoo.common.utils;

import static com.mycuckoo.common.constant.Common.ORGAN_ID;
import static com.mycuckoo.common.constant.Common.ORGAN_NAME;
import static com.mycuckoo.common.constant.Common.ORGAN_ROLE_ID;
import static com.mycuckoo.common.constant.Common.ROLE_ID;
import static com.mycuckoo.common.constant.Common.ROLE_NAME;
import static com.mycuckoo.common.constant.Common.USER_CODE;
import static com.mycuckoo.common.constant.Common.USER_ID;
import static com.mycuckoo.common.constant.Common.USER_NAME;
import static com.mycuckoo.common.constant.Common.USER_PHOTO_URL;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

import com.mycuckoo.domain.platform.ModuleMemu;
import com.mycuckoo.exception.SystemException;

/**
 * 功能说明: 取会话信息工具类，主要是统一获取会话中的信息
 *
 * @author rutine
 * @time Nov 1, 2014 1:19:16 PM
 * @version 3.0.0
 */
public final class SessionUtil {
	private static Logger logger = LoggerFactory.getLogger(SessionUtil.class);
	
	private static NamedThreadLocal<HttpServletRequest> localRequest = new NamedThreadLocal<>("Session Util");
	
	
	public static void setRequest(HttpServletRequest request) {
		localRequest.set(request);
	}
	
	private static HttpSession getSession() {
		return localRequest.get().getSession();
	}
	
	/**
	 * 功能说明 : 获取计算机IP
	 * 
	 * @return 计算机IP
	 * @author rutine
	 * @time Jul 2, 2017 8:47:25 AM
	 */
	public static String getIP() {
		HttpServletRequest request = localRequest.get();
		if(request == null) {
			logger.error("request is null.");
			throw new SystemException("request is null.");
		}
		
		String ipAddr = request.getRemoteAddr();
		
		return ipAddr;
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
	
	/**
	 * 功能说明 : 获取用户所属的机构id, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 机构id
	 * @author rutine
	 * @time Nov 1, 2014 1:24:53 PM
	 */
	public static Long getOrganId() {
		return (Long) getSession().getAttribute(ORGAN_ID);
	}
	
	/**
	 * 功能说明 : 获取用户所属的机构名称, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 机构名称
	 * @author rutine
	 * @time Nov 1, 2014 1:27:53 PM
	 */
	public static String getOrganName() {
		return (String) getSession().getAttribute(ORGAN_NAME);
	}
	
	/**
	 * 功能说明 : 获取用户所属的机构角色id, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 机构角色id
	 * @author rutine
	 * @time Nov 1, 2014 1:30:06 PM
	 */
	public static Long getOrganRoleId() {
		return (Long) getSession().getAttribute(ORGAN_ROLE_ID);
	}
	
	/**
	 * 功能说明 : 获取用户所属的角色id, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 角色id
	 * @author rutine
	 * @time Nov 1, 2014 1:32:22 PM
	 */
	public static Long getRoleId() {
		return (Long) getSession().getAttribute(ROLE_ID);
	}
	
	/**
	 * 功能说明 : 获取用户所属的角色名称, 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 1, 2014 1:33:19 PM
	 */
	public static String getRoleName() {
		return (String) getSession().getAttribute(ROLE_NAME);
	}
	
	/**
	 * 功能说明 : 获取用户id, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 用户id
	 * @author rutine
	 * @time Nov 1, 2014 1:35:39 PM
	 */
	public static Long getUserId() {
		return (Long) getSession().getAttribute(USER_ID);
	}

	/**
	 * 功能说明 : 获取用户编码, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 用户编码
	 * @author rutine
	 * @time Nov 1, 2014 1:36:38 PM
	 */
	public static String getUserCode() {
		return (String) getSession().getAttribute(USER_CODE);
	}
	
	/**
	 * 功能说明 : 获取用户名称, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 用户名称
	 * @author rutine
	 * @time Nov 1, 2014 1:37:30 PM
	 */
	public static String getUserName() {
		return (String) getSession().getAttribute(USER_NAME);
	}
	
	/**
	 * 功能说明 : 获取用户头像, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 用户头像
	 * @author rutine
	 * @time Nov 1, 2014 1:38:39 PM
	 */
	public static String getUserPhotoUrl() {
		return (String) getSession().getAttribute(USER_PHOTO_URL);
	}
	
	/**
	 * 功能说明 : 获取用户一级菜单, 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 12:11:23 PM
	 */
	public static List<ModuleMemu> getFirstMenu() {
		return (List<ModuleMemu>) getSession().getAttribute("firstList");
	}
	
	/**
	 * 功能说明 : 获取用户二级菜单, 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 12:13:21 PM
	 */
	public static Map<String, List<ModuleMemu>> getSecondMenu() {
		return (Map<String, List<ModuleMemu>>) getSession().getAttribute("secondMap");
	}
	
	/**
	 * 功能说明 : 获取用户三级菜单, 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 12:13:53 PM
	 */
	public static Map<String, List<ModuleMemu>> getThirdMenu() {
		return (Map<String, List<ModuleMemu>>) getSession().getAttribute("thirdMap");
	}
	
	/**
	 * 功能说明 : 获取用户四级菜单(操作按钮), 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 4, 2014 8:36:48 PM
	 */
	public static Map<String, List<ModuleMemu>> getFourthMenu() {
		return (Map<String, List<ModuleMemu>>) getSession().getAttribute("fourthMap");
	}

}

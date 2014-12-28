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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.mycuckoo.domain.platform.SysplModuleMemu;

/**
 * 功能说明: 取会话信息工具类，主要是统一获取会话中的信息
 *
 * @author rutine
 * @time Nov 1, 2014 1:19:16 PM
 * @version 2.0.0
 */
public final class SessionUtil {
	
	/**
	 * 功能说明 : 获取用户所属的机构id, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 机构id
	 * @author rutine
	 * @time Nov 1, 2014 1:24:53 PM
	 */
	public static Long getOrganId(HttpSession session) {
		return (Long) session.getAttribute(ORGAN_ID);
	}
	
	/**
	 * 功能说明 : 获取用户所属的机构名称, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 机构名称
	 * @author rutine
	 * @time Nov 1, 2014 1:27:53 PM
	 */
	public static String getOrganName(HttpSession session) {
		return (String) session.getAttribute(ORGAN_NAME);
	}
	
	/**
	 * 功能说明 : 获取用户所属的机构角色id, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 机构角色id
	 * @author rutine
	 * @time Nov 1, 2014 1:30:06 PM
	 */
	public static Long getOrganRoleId(HttpSession session) {
		return (Long) session.getAttribute(ORGAN_ROLE_ID);
	}
	
	/**
	 * 功能说明 : 获取用户所属的角色id, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 角色id
	 * @author rutine
	 * @time Nov 1, 2014 1:32:22 PM
	 */
	public static Long getRoleId(HttpSession session) {
		return (Long) session.getAttribute(ROLE_ID);
	}
	
	/**
	 * 功能说明 : 获取用户所属的角色名称, 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 1, 2014 1:33:19 PM
	 */
	public static String getRoleName(HttpSession session) {
		return (String) session.getAttribute(ROLE_NAME);
	}
	
	/**
	 * 功能说明 : 获取用户id, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 用户id
	 * @author rutine
	 * @time Nov 1, 2014 1:35:39 PM
	 */
	public static Long getUserId(HttpSession session) {
		return (Long) session.getAttribute(USER_ID);
	}

	/**
	 * 功能说明 : 获取用户编码, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 用户编码
	 * @author rutine
	 * @time Nov 1, 2014 1:36:38 PM
	 */
	public static String getUserCode(HttpSession session) {
		return (String) session.getAttribute(USER_CODE);
	}
	
	/**
	 * 功能说明 : 获取用户名称, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 用户名称
	 * @author rutine
	 * @time Nov 1, 2014 1:37:30 PM
	 */
	public static String getUserName(HttpSession session) {
		return (String) session.getAttribute(USER_NAME);
	}
	
	/**
	 * 功能说明 : 获取用户头像, 此值登录后存入会话
	 * 
	 * @param session
	 * @return 用户头像
	 * @author rutine
	 * @time Nov 1, 2014 1:38:39 PM
	 */
	public static String getUserPhotoUrl(HttpSession session) {
		return (String) session.getAttribute(USER_PHOTO_URL);
	}
	
	/**
	 * 功能说明 : 获取用户一级菜单, 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 12:11:23 PM
	 */
	public static List<SysplModuleMemu> getFirstMenu(HttpSession session) {
		return (List<SysplModuleMemu>) session.getAttribute("firstList");
	}
	
	/**
	 * 功能说明 : 获取用户二级菜单, 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 12:13:21 PM
	 */
	public static Map<String, List<SysplModuleMemu>> getSecondMenu(HttpSession session) {
		return (Map<String, List<SysplModuleMemu>>) session.getAttribute("secondMap");
	}
	
	/**
	 * 功能说明 : 获取用户三级菜单, 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 12:13:53 PM
	 */
	public static Map<String, List<SysplModuleMemu>> getThirdMenu(HttpSession session) {
		return (Map<String, List<SysplModuleMemu>>) session.getAttribute("thirdMap");
	}
	
	/**
	 * 功能说明 : 获取用户四级菜单(操作按钮), 此值登录后存入会话
	 * 
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 4, 2014 8:36:48 PM
	 */
	public static Map<String, List<SysplModuleMemu>> getFourthMenu(HttpSession session) {
		return (Map<String, List<SysplModuleMemu>>) session.getAttribute("fourthMap");
	}

}

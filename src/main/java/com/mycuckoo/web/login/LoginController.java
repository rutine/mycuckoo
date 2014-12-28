package com.mycuckoo.web.login;

import static com.mycuckoo.common.constant.ActionVariable.ADMIN_ORGNAME;
import static com.mycuckoo.common.constant.ActionVariable.ADMIN_ROLENAME;
import static com.mycuckoo.common.constant.Common.AGENT_ID;
import static com.mycuckoo.common.constant.Common.COMMON_FUN;
import static com.mycuckoo.common.constant.Common.DISABLE;
import static com.mycuckoo.common.constant.Common.FIRST;
import static com.mycuckoo.common.constant.Common.FOURTH;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_THIRD;
import static com.mycuckoo.common.constant.Common.ORGAN_ID;
import static com.mycuckoo.common.constant.Common.ORGAN_NAME;
import static com.mycuckoo.common.constant.Common.ORGAN_ROLE_ID;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_ROW;
import static com.mycuckoo.common.constant.Common.ROLE_ID;
import static com.mycuckoo.common.constant.Common.ROLE_NAME;
import static com.mycuckoo.common.constant.Common.SECOND;
import static com.mycuckoo.common.constant.Common.THIRD;
import static com.mycuckoo.common.constant.Common.USER_CODE;
import static com.mycuckoo.common.constant.Common.USER_ID;
import static com.mycuckoo.common.constant.Common.USER_LOGIN;
import static com.mycuckoo.common.constant.Common.USER_NAME;
import static com.mycuckoo.common.constant.Common.USER_PHOTO_URL;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.login.LoginService;

/**
 * 功能说明: 登陆系统Controller
 *
 * @author rutine
 * @time Sep 24, 2014 10:52:03 PM
 * @version 2.0.0
 */
@Controller
public class LoginController {
	
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	
	@RequestMapping(value={"", "/login"})
	public String login() throws IOException {
		logger.info("--------------------默认地址转向登陆页面----------------");
		
		return "default";
	}
	
	/**
	 * 功能说明 : 预登录 代理 多角色
	 *
	 * @param userCode
	 * @return
	 * @author rutine
	 * @time Nov 21, 2012 8:00:26 PM
	 */
	@RequestMapping(value="/login/preLogin", produces="application/json;charset=utf-8", method=RequestMethod.GET)
	@ResponseBody
	public Map preLogin(@RequestParam String userCode) {
		logger.debug("userCode --> " + userCode);
		
		Map resultJson = Maps.newHashMap();
		try {
			resultJson = loginService.preLogin(userCode);
		} catch (ApplicationException e) {
			logger.error("预登陆错误!", e);
		}
		
		logger.debug("json --> " + JsonUtils.toJson(resultJson));
		
		return resultJson;
	}
	
	/**
	 * 功能说明 : 登录系统第一阶段, 设置用户会话信息
	 *
	 * @param userCode
	 * @param password
	 * @param roleInfoJson
	 * @param agentInfoJson
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 21, 2012 8:00:41 PM
	 */
	@RequestMapping(value="/login/loginSysFirstDo", produces="application/json;charset=utf-8", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult loginSysFirstDo(
			@RequestParam String userCode, 
			@RequestParam String password,
			@RequestParam String roleInfoJson,
			@RequestParam String agentInfoJson,
			HttpSession session) {
		
		logger.debug("userCode --> " + userCode);
		logger.debug("password --> " + password);
		logger.debug("roles --> " + roleInfoJson);
		logger.debug("userAgent --> " + agentInfoJson);
		
		/*
		 * 1. 验证用户是否存在 并得到用户对象
		 * 2. 用户没有相应角色则提示没有使用系统权限
		 * 3. 用户状态是否可用
		 * 4. 用户有效期是否已到
		 * 5. 存在并已经登录则踢出
		 * 6. 获得系统定义的角色切换方式，如果用户拥有多个角色则提示用户选择角色
		 * 7. 用户机构名称及ID、用户角色名称及ID角色级别、用户名称及ID、放入session
		 */
		AjaxResult ajaxModel = new AjaxResult();
		UumRoleUserRef uumRoleUserRef = (UumRoleUserRef) JsonUtils.fromJson(roleInfoJson, UumRoleUserRef.class);
		UumUserAgent uumUserAgent = (UumUserAgent) JsonUtils.fromJson(agentInfoJson, UumUserAgent.class);
		Long userId = null;
		String userName = null;
		String userPhotoUrl = null;
		try {
			// 非管理员
			if(!loginService.isAdministrator(userCode)) {
				if(uumRoleUserRef == null) { // 用户没有权限
					ajaxModel.setCode((short) 2);
					return ajaxModel;
				} else if(uumRoleUserRef.getOrgRoleId() == 0) { // 用户为无角色用户没有使用权限
					ajaxModel.setCode((short) 2);
					return ajaxModel;
				}
				
				UumUser uumUser = loginService.getUserByUserCodePwd(userCode, password);
				if(uumUser == null) { // 用户不存在
					ajaxModel.setCode((short) 1);
					return ajaxModel;
				} else if(DISABLE.equals(uumUser.getStatus())) { // 用户已被停用
					ajaxModel.setCode((short) 3);
					return ajaxModel;
				} else if (uumUser.getUserAvidate() == null || (new Date()).after(uumUser.getUserAvidate())) {
					// 用户过期
					ajaxModel.setCode((short) 4);
					return ajaxModel;
				}
			}
			
			userId = uumRoleUserRef.getUumUser().getUserId();
			userName = uumRoleUserRef.getUumUser().getUserName();
			userPhotoUrl = uumRoleUserRef.getUumUser().getUserPhotoUrl();
			if(uumUserAgent != null) { // 有代理
				BeanUtils.copyProperties(uumUserAgent, uumRoleUserRef);
				userId = uumUserAgent.getUserId();
				userCode = uumUserAgent.getUserCode();
				userName = uumUserAgent.getUserName() + "(" + userName + "D)";
				userPhotoUrl = uumUserAgent.getUserPhotoUrl();
				session.setAttribute(AGENT_ID, uumUserAgent.getAgentId()); // 用户代理主键ID
			} else {
				session.removeAttribute(AGENT_ID); // 清除用户代理主键ID
			}
		} catch (ApplicationException e) {
			logger.error("login occur error. ", e);
			
			ajaxModel.setCode((short) 500);
			ajaxModel.setMsg(e.toString());
			return ajaxModel;
		} catch (SystemException e) {
			logger.error("login occur error. ", e);
			
			ajaxModel.setCode((short) 500);
			ajaxModel.setMsg(e.toString());
			return ajaxModel;
		} catch (Exception e) {
			logger.error("login occur error. ", e);
			
			ajaxModel.setCode((short) 500);
			ajaxModel.setMsg(e.toString());
			return ajaxModel;
		}
		
		/*
		 * 用户ID及名称、用户机构ID及名称、用户角色ID及名称角色级别、放入session
		 * ID 均为Long
		 */ 
		Long organId = uumRoleUserRef.getOrganId() == null ? -1L : uumRoleUserRef.getOrganId();
		String organName = uumRoleUserRef.getOrganName() == null ? ADMIN_ORGNAME : uumRoleUserRef.getOrganName();
		Long organRoleId = uumRoleUserRef.getOrgRoleId() == null ? -1L : uumRoleUserRef.getOrgRoleId();
		Long roleId = uumRoleUserRef.getRoleId() == null ? -1L : uumRoleUserRef.getRoleId();
		String roleName = uumRoleUserRef.getRoleName() == null ? ADMIN_ROLENAME : uumRoleUserRef.getRoleName();
		
		
		session.setAttribute(ORGAN_ID, organId);
		session.setAttribute(ORGAN_NAME, organName);
		session.setAttribute(ORGAN_ROLE_ID, organRoleId);
		session.setAttribute(ROLE_ID, roleId);
		session.setAttribute(ROLE_NAME, roleName);
		session.setAttribute(USER_ID, userId);
		session.setAttribute(USER_CODE, userCode);
		session.setAttribute(USER_NAME, userName);
		session.setAttribute(USER_PHOTO_URL, userPhotoUrl);
		
		logger.info("organId :      " + organId     + "  -  organName : " + organName);
		logger.info("organRoleId :  " + organRoleId + "  -  roleId :    " + roleId   +  "  -  roleName : " + roleName);
		logger.info("userId :       " + userId      + "  -  userCode :  " + userCode +  "  -  userName : " + userName);
		 
		return ajaxModel;
	}
	
	/**
	 * 功能说明 : 登录系统第二阶段, 加载用户菜单
	 *
	 * @return
	 * @author rutine
	 * @time Nov 21, 2012 8:01:00 PM
	 */
	@RequestMapping(value="/login/loginSysSecondDo", method=RequestMethod.GET)
	public String loginSysSecondDo(HttpServletRequest request, HttpSession session, Model model) {
		Object obj = request.getAttribute("refresh-page");
		boolean refresh = (obj == null ? true : BooleanUtils.toBoolean(obj.toString()));
		
	   logger.debug("---------------------------- refresh page {} ----------------------------------", refresh);
		
		if(refresh) {
			return "index";
		}
		
		/*
		 *  8  得到用户自定义的页面布局模板 -- viewportfactory implement
		 *  9  通过配置XML获得管理员用户，管理员则不需要权限过滤
		 * 10 模块权限过滤，用户是否有特殊权限，并过滤特殊权限
		 * 11 用户代理模块操作
		 * 12 常用功能
		 * 13 portal?
		 */
		try {
			Long userId = (Long) session.getAttribute(USER_ID);
			Long roleId = (Long) session.getAttribute(ROLE_ID);
			Long organRoleId = (Long) session.getAttribute(ORGAN_ROLE_ID);
			Long organId = (Long) session.getAttribute(ORGAN_ID);
			String userCode = (String) session.getAttribute(USER_CODE);
			Long agentId = (Long) session.getAttribute(AGENT_ID);
	
			// 加载用户菜单
			Map map = loginService.filterPrivilege(userId, roleId, organId, organRoleId, userCode, agentId, request);
			List<SysplModuleMemu> firstList = (List<SysplModuleMemu>) map.get(FIRST); // 第一级
			Map secondMap = (Map) map.get(SECOND); // 第二级
			Map thirdMap = (Map) map.get(THIRD); // 第三级
			Map fourthMap = (Map) map.get(FOURTH); // 第四级
			List<SysplModuleMemu> assignUumUserCommfunList = (List<SysplModuleMemu>) map.get(COMMON_FUN);
			String userRowPrivilege = (String) map.get(PRIVILEGE_TYPE_ROW);
			
			logger.info("user row privilege : 【" + userRowPrivilege + "】");
			
			session.setAttribute("firstList", firstList);
			session.setAttribute("secondMap", secondMap);
			session.setAttribute("thirdMap", thirdMap);
			session.setAttribute("fourthMap", fourthMap);
			session.setAttribute("assignUumUserCommfunList", assignUumUserCommfunList);
			
//			// 记录登录日志
			StringBuilder optContent = new StringBuilder();
			optContent.append(session.getAttribute(ORGAN_NAME) + "-")
				.append(session.getAttribute(ROLE_NAME) + "-")
				.append(session.getAttribute(USER_NAME));
			loginService.saveLog(LOG_LEVEL_THIRD, USER_LOGIN, USER_LOGIN, optContent.toString(), "", request);
		} catch (ApplicationException e) {
			logger.error("login occur error. ", e);
			
			model.addAttribute("failMSG", e.toString());
		} catch (SystemException e) {
			logger.error("login occur error. ", e);
			
			model.addAttribute("failMSG", e.toString());
		}
		
		return "index";
	}
	
	@RequestMapping(value="/login/loginForward", method=RequestMethod.GET)
	public String loginForward(
			@RequestParam String multiRole, 
			@RequestParam String userCode,
			RedirectAttributes redirectAttributes) {
		
		logger.info("多角色用户: " + (isNullOrEmpty(multiRole) ? "否" : "是") + ", usrCode: " + userCode);
		
		redirectAttributes.addFlashAttribute("refresh-page", false);
		
		return "redirect:/" + userCode + "/index";
	}
	
	@RequestMapping(value={"/{usr}/index"}, method=RequestMethod.GET)
	public String index(@PathVariable String usr, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute(USER_ID) == null) { // session过期, 要求重新登录
			return "redirect:/login";
		}
		
		return "forward:/login/loginSysSecondDo";
	}
	
	@RequestMapping(value="/login/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/login";
	}
	
	
	
	
	@RequestMapping("/test")
	public String test(@CookieValue("JSESSIONID") String cookie, Model model) throws IOException {
		logger.debug("--------------------hello world!----------------");
		logger.debug("sessionid : " + cookie);
		
		return "default";
	}
	
	// url: "/login/test/string"
	@RequestMapping("/test/{var}")
	public String test1(@PathVariable String var) {
		logger.debug("path variable --> " + var);
		
		return "defualt";
	}
}

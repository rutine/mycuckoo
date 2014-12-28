package com.mycuckoo.web.platform.system;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.domain.vo.SystemConfigBean;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.SystemConfigService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 系统配置Controller
 * 
 * @author rutine
 * @time Oct 13, 2014 9:28:46 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/systemConfigMgr")
public class SystemConfigController {
	private static Logger logger = LoggerFactory.getLogger(SystemConfigController.class);

	@Autowired
	private SystemConfigService systemConfigService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getCreateForm(Model model) {

		logger.info("---------------- 请求系统配置管理界面 -----------------");

		SystemConfigBean systemConfigBean = new SystemConfigBean();
		try {
			systemConfigBean = systemConfigService.getSystemConfigInfo();
		} catch (ApplicationException e) {
			logger.error("请求系统配置页错误", e);
			
			model.addAttribute("error", "请求系统配置页错误");
		}
		model.addAttribute("systemConfigBean", systemConfigBean);

		return "business/platform/system/systemConfigMgrForm";
	}

	/**
	 * 功能说明 : 设置系统配置信息
	 * 
	 * @param systemConfig 设置系统配置信息
	 * @param userAddDelFlag
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 23, 2013 9:36:00 PM
	 */
	@RequestMapping(value = "/setSystemConfigInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postSetSystemConfigInfo(
			SystemConfigBean systemConfig,
			@RequestParam(defaultValue = "-1") String userAddDelFlag,
			HttpServletRequest request) {
		
		AjaxResult ajaxDone = new AjaxResult();
		ajaxDone.setStatus(true);
		ajaxDone.setMsg("设置成功");
		try {
			systemConfigService.setSystemConfigInfo(systemConfig, userAddDelFlag, request);
		} catch (ApplicationException e) {
			logger.error("1. 设置系统配置出错", e);
			
			ajaxDone.setStatus(false);
			ajaxDone.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 设置系统配置出错", e);
			
			ajaxDone.setStatus(false);
			ajaxDone.setMsg("设置失败!");
		}

		return ajaxDone;
	}

	/**
	 * 功能说明 : 查询用户信息为管理员分配
	 * 
	 * @param userCode
	 * @param userName
	 * @param pageNo
	 * @param pageSize
	 * @param model
	 * @return
	 * @author rutine
	 * @time Nov 23, 2013 11:03:33 PM
	 */
	@RequestMapping(value = "/getUserForSetAdmin")
	public String getUserForSetAdmin(
			@RequestParam(value = "userCode", defaultValue = "") String userCode,
			@RequestParam(value = "userName", defaultValue = "") String userName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			@RequestParam String userAddDelFlag, Model model) {

		Page<UumUser> page = null;
		if ("add".equalsIgnoreCase(userAddDelFlag)) {
			page = userService.findUsersForSetAdmin(userName, userCode, new PageRequest(pageNo - 1, pageSize));
		} else {
			page = userService.findAdminUsers();
		}
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "userAddDelFlag=" + userAddDelFlag + "&userCode=" + userCode + "&userName=" + userName);

		return "business/platform/system/systemConfigMgrSelectUser";
	}

	/**
	 * 功能说明 : 启动tomcat监控
	 * 
	 * @author rutine
	 * @time Nov 24, 2013 1:20:00 PM
	 */
	@RequestMapping(value = "/startJConsole")
	public AjaxResult startJconsole() {
		Runtime runtime = Runtime.getRuntime();
		
		AjaxResult ajaxDone = new AjaxResult();
		ajaxDone.setStatus(true);
		ajaxDone.setMsg("启动成功.");
		try {
			runtime.exec("cmd.exe /c jconsole ");
		} catch (IOException e) {
			logger.error("启动tomcat监控失败!", e);
			
			ajaxDone.setStatus(true);
			ajaxDone.setMsg("启动失败!");
		}
		
		return ajaxDone;
	}
}

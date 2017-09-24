package com.mycuckoo.web.platform.system;

import com.mycuckoo.exception.SystemException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.SystemConfigService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.vo.SystemConfigBean;
import com.mycuckoo.vo.uum.UserVo;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 系统配置Controller
 * 
 * @author rutine
 * @time Oct 13, 2014 9:28:46 PM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/platform/system/config/mgr")
public class SystemConfigController {
	private static Logger logger = LoggerFactory.getLogger(SystemConfigController.class);

	@Autowired
	private SystemConfigService systemConfigService;
	@Autowired
	private UserService userService;



	/**
	 * 功能说明 : 查询用户信息为管理员分配
	 *
	 * @param userCode
	 * @param userName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @author rutine
	 * @time Nov 23, 2013 11:03:33 PM
	 */
	@GetMapping(value = "/list")
	public AjaxResponse<Page<UserVo>> list(
			@RequestParam(value = "userCode", defaultValue = "") String userCode,
			@RequestParam(value = "userName", defaultValue = "") String userName,
			@RequestParam String userAddDelFlag,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

		Page<UserVo> page = null;
		if ("add".equalsIgnoreCase(userAddDelFlag)) {
			page = userService.findUsersForSetAdmin(userName, userCode, new PageRequest(pageNo - 1, pageSize));
		} else {
			page = userService.findAdminUsers();
		}

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 设置系统配置信息
	 * 
	 * @param systemConfig 设置系统配置信息
	 * @return
	 * @author rutine
	 * @time Nov 23, 2013 9:36:00 PM
	 */
	@PutMapping(value = "update")
	public AjaxResponse<String> putUpdate(@RequestBody SystemConfigBean systemConfig) {

		try {
			systemConfigService.setSystemConfigInfo(systemConfig, systemConfig.getUserAddDelFlag());
		} catch (SystemException e) {
			AjaxResponse.create(500, "设置失败");
		}

		return AjaxResponse.create("设置成功");
	}

	@GetMapping(value = "/view")
	public AjaxResponse<SystemConfigBean> getView() {
		SystemConfigBean systemConfigBean = systemConfigService.getSystemConfigInfo();

		return AjaxResponse.create(systemConfigBean);
	}

	/**
	 * 功能说明 : 启动tomcat监控
	 * 
	 * @author rutine
	 * @time Nov 24, 2013 1:20:00 PM
	 */
	@GetMapping(value = "/start/jconsole")
	public AjaxResponse<String> startJconsole() {
		try {
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("cmd.exe /c jconsole ");
		} catch (IOException e) {
			logger.error("启动tomcat监控失败!", e);
			AjaxResponse.create(500, "启动失败");
		}

		
		return AjaxResponse.create("启动成功");
	}
}

package com.mycuckoo.web.platform.system;


import com.google.common.collect.Maps;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.SystemParameterService;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 系统参数Controller
 *
 * @author rutine
 * @time Oct 14, 2014 8:49:02 PM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/platform/system/parameter/mgr")
public class SystemParameterController {
	private static Logger logger = LoggerFactory.getLogger(SystemParameterController.class);

	@Autowired
	private SystemParameterService systemParameterService;



	@GetMapping(value = "/list")
	public AjaxResponse<Page<SysParameter>> list(
			@RequestParam(value = "paraName", defaultValue = "") String paraName,
			@RequestParam(value = "paraKeyName", defaultValue = "") String paraKeyName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

		Map<String, Object> params = Maps.newHashMap();
		params.put("paraName", StringUtils.isBlank(paraName)
				? null : "%" + paraName + "%");
		params.put("optName", StringUtils.isBlank(paraKeyName)
				? null : "%" + paraKeyName + "%");
		Page<SysParameter> page = systemParameterService.findByPage(params, new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 创建系统参数
	 * 
	 * @param sysParameter
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 10:10:06 AM
	 */
	@PutMapping(value = "/create")
	public AjaxResponse<String> putCreate(@RequestBody SysParameter sysParameter) {

		logger.debug(JsonUtils.toJson(sysParameter));

		sysParameter.setCreateDate(new Date());
		sysParameter.setCreator(SessionUtil.getUserCode());
		systemParameterService.save(sysParameter);

		return AjaxResponse.create("保存系统参数成功");
	}

	/**
	 * 功能说明 : 停用/启用系统参数
	 * 
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 10:14:48 AM
	 */
	@GetMapping(value = "/disEnable")
	public AjaxResponse<String> disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag) {

		systemParameterService.disEnable(id, disEnableFlag);

		return AjaxResponse.create("操作成功");
	}

	/**
	 * 功能说明 : 修改系统参数
	 * 
	 * @param sysParameter
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 10:21:10 AM
	 */
	@PutMapping(value = "/update")
	public AjaxResponse<String> putUpdate(@RequestBody SysParameter sysParameter) {
		systemParameterService.update(sysParameter);

		return AjaxResponse.create("修改系统参数成功");
	}

	@GetMapping(value = "/view")
	public AjaxResponse<SysParameter> getView(@RequestParam long id) {
		SysParameter sysParameter = systemParameterService.get(id);

		return AjaxResponse.create(sysParameter);
	}
}

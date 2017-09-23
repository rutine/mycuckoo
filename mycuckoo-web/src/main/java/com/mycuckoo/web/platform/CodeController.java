package com.mycuckoo.web.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.platform.Code;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.CodeService;
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
 * 功能说明: 系统编码Controller
 *
 * @author rutine
 * @time Oct 14, 2014 3:16:02 PM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/platform/code/mgr")
public class CodeController {
	private static Logger logger = LoggerFactory.getLogger(CodeController.class);

	@Autowired
	private CodeService codeService;



	@GetMapping(value = "/list")
	public AjaxResponse<Page<Code>> codeMgr(
			@RequestParam(value = "codeName", defaultValue = "") String codeName,
			@RequestParam(value = "codeEngName", defaultValue = "") String codeEngName,
			@RequestParam(value = "moduleName", defaultValue = "") String moduleName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {
		

		Map<String, Object> params = Maps.newHashMap();
		params.put("codeName", StringUtils.isBlank(codeName) ? null : "%" + codeName + "%");
		params.put("codeEngName", StringUtils.isBlank(codeEngName) ? null : "%" + codeEngName + "%");
		params.put("moduleName", StringUtils.isBlank(moduleName) ? null : "%" + moduleName + "%");
		Page<Code> page = codeService.findByPage(params, new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 创建新编码
	 * 
	 * @param code
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:48:33 PM
	 */
	@PutMapping(value = "/createForm")
	public AjaxResponse<String> putCreate(@RequestBody Code code) {
		
		logger.debug(JsonUtils.toJson(code));

		code.setCreator(SessionUtil.getUserCode());
		code.setCreateDate(new Date());
		codeService.saveCode(code);

		return AjaxResponse.create("保存系统编码成功");
	}

	/**
	 * 功能说明 : 删除系统编码
	 * 
	 * @param id
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:59:46 PM
	 */
	@DeleteMapping(value = "/delete")
	public AjaxResponse<String> delete(@RequestParam long id) {
		
		throw new ApplicationException("找不到删除记录的方法!");

//		return AjaxResponse.create("删除系统编码成功");
	}

	/**
	 * 功能说明 : 停用/启用编码
	 * 
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:57:53 PM
	 */
	@GetMapping(value = "/disEnable")
	public AjaxResponse<String> disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag) {

		boolean disEnableBol = codeService.disEnable(id, disEnableFlag);

		return AjaxResponse.create("操作成功");
	}

	/**
	 * 功能说明 : 修改编码
	 * 
	 * @param code
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:53:05 PM
	 */
	@PutMapping(value = "/update")
	public AjaxResponse<String> putUpdate(@RequestBody Code code) {
		codeService.update(code);

		return AjaxResponse.create("修改系统编码成功");
	}

	@GetMapping(value = "/view")
	public AjaxResponse<Code> getView(@RequestParam long id) {
		Code code = codeService.get(id);

		logger.debug(JsonUtils.toJson(code));

		return AjaxResponse.create(code);
	}

}

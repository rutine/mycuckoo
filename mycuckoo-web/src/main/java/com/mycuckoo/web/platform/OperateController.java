package com.mycuckoo.web.platform;


import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.OperateService;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 操作按钮Controller
 * 
 * @author rutine
 * @time Oct 12, 2014 3:41:26 PM
 * @version 3.0.0
 */
@RestController
@RequestMapping(value = "/platform/operate/mgr")
public class OperateController {
	private static Logger logger = LoggerFactory.getLogger(OperateController.class);

	@Autowired
	private OperateService optService;



	/**
	 * 功能说明 : 操作按钮管理界面入口
	 * 
	 * @param operateName 操作名称
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 5:52:09 PM
	 */
	@GetMapping(value = "/list")
	public AjaxResponse<Page<Operate>> operateMgr(
			@RequestParam(value = "operateName", defaultValue = "") String operateName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {
		
		logger.info("---------------- 请求操作按钮管理界面 -----------------");

		Page<Operate> page = optService.findByPage(operateName, new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 创建模块操作
	 *
	 * @param operate
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 8:30:06 PM
	 */
	@PutMapping(value = "/create")
	public AjaxResponse<String> putCreate(@RequestBody Operate operate) {
		
		logger.debug(JsonUtils.toJson(operate));

		operate.setCreateDate(new Date());
		operate.setCreator(SessionUtil.getUserCode());
		optService.save(operate);

		return AjaxResponse.create("保存成功");
	}


	/**
	 * 功能说明 : 停用/启用模块操作
	 * 
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 9:00:07 PM
	 */
	@GetMapping(value = "/disEnable")
	public AjaxResponse<String> disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag) {

		optService.disEnable(id, disEnableFlag);

		return AjaxResponse.create("操作成功");
	}

	/**
	 * 功能说明 : 修改模块操作
	 * 
	 * @param operate
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 8:52:32 PM
	 */
	@PutMapping(value = "/update")
	public AjaxResponse<String> putUpdate(@RequestBody Operate operate) {

		optService.update(operate);

		return AjaxResponse.create("保存成功");
	}

	@GetMapping(value = "/view")
	public AjaxResponse<Operate> getView(@RequestParam long id, Model model) {
		Operate operate = optService.get(id);
		return AjaxResponse.create(operate);
	}

}
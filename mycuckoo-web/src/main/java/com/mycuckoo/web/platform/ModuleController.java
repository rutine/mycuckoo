package com.mycuckoo.web.platform;


import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.domain.platform.Operate;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.platform.ModuleService;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.TreeVo;
import com.mycuckoo.vo.platform.ModuleMenuVo;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 模块菜单Controller
 *
 * @author rutine
 * @time Oct 2, 2014 7:37:26 AM
 * @version 3.0.0
 */
@RestController
@RequestMapping("/platform/module/mgr")
public class ModuleController {
	private static Logger logger = LoggerFactory.getLogger(ModuleController.class);
	
	@Autowired
	private ModuleService moduleService;


	
	/**
	 * 功能说明 : 列表展示页面
	 *
	 * @param treeId 查找指定节点下的模块`
	 * @param modName 模块名称
	 * @param modEnId 模块id
	 * @param pageNo 第几页
	 * @param pageSize 页面大小, 暂时没有使用
	 * @return
	 * @author rutine
	 * @time Dec 2, 2012 8:22:41 PM
	 */
	@GetMapping(value="/list")
	public AjaxResponse<Page<ModuleMenuVo>> list(@RequestParam(value="treeId", defaultValue="-1") long treeId,
                                                 @RequestParam(value="modName", defaultValue="") String modName,
                                                 @RequestParam(value="modEnId", defaultValue="") String modEnId,
                                                 @RequestParam(value="pageNo", defaultValue="1") int pageNo,
                                                 @RequestParam(value="pageSize", defaultValue=LIMIT + "") int pageSize) {
		
		Page<ModuleMenuVo> page = moduleService.findByPage(treeId, modName, modEnId,
				new PageRequest(pageNo - 1, pageSize));
		
		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 获得模块已经分配和未分配的操作列表
	 *
	 * @param modName
	 * @param id
	 * @return
	 * @author rutine
	 * @time May 9, 2013 8:52:56 PM
	 */
	@GetMapping(value="/list/operation")
	public AjaxResponse<AssignVo<Operate>> listOperation(
			@RequestParam(value="moduleId") long id,
			@RequestParam(value="modName") String modName) {

		AssignVo<Operate> vo = moduleService.findAssignedAndUnAssignedOperatesByModuleId(id);

		return AjaxResponse.create(vo);
	}
	
	/**
	 * 功能说明 : 创建模块
	 *
	 * @return
	 * @author rutine
	 * @time Jun 1, 2013 9:13:49 AM
	 */
	@PutMapping(value="/create")
	public AjaxResponse<String> putCreate(@RequestBody ModuleMenu moduleMemu) {
		
		logger.debug(JsonUtils.toJson(moduleMemu));
		
		moduleMemu.setCreateDate(new Date());
		moduleMemu.setCreator(SessionUtil.getUserCode());
		moduleService.save(moduleMemu);
		
		return AjaxResponse.create("保存模块成功");
	}

	
	/**
	 * 功能说明 : 保存模块操作关系
	 *
	 * @param id 模块ID
	 * @param operateIds 模块操作列表
	 * @return
	 * @author rutine
	 * @time May 12, 2013 5:44:16 PM
	 */
	@GetMapping(value="/create/module/operation/ref")
	public AjaxResponse<String> putCreateModuleOptRefs(
			@RequestParam(value="moduleId") long id,
			@RequestParam(value="operateIds") List<Long> operateIds) {
	
		moduleService.saveModuleOptRefs(id, operateIds);
		
		return AjaxResponse.create("分配模块权限成功");
	}
	
	/**
	 * 功能说明 : 删除模块
	 *
	 * @param id
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 11:09:30 AM
	 */
	@DeleteMapping(value="/delete")
	public AjaxResponse<String> delete(@RequestParam long id) {
		
		moduleService.delete(id);
		
		return AjaxResponse.create("模块菜单删除成功");
	}
	
	/**
	 * 功能说明 : 停用/启用模块
	 *
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 1:53:04 PM
	 */
	@GetMapping(value="/disEnable")
	public AjaxResponse<String> disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag) {

		boolean disEnableBol = moduleService.disEnable(id, disEnableFlag);

		return AjaxResponse.create("操作成功");
	}
	
	/**
	 * 功能说明 : 获取模块的下级模块
	 *
	 * @param id 模块id
	 * @param filterOutModId
	 * @author rutine
	 * @time Dec 1, 2012 1:45:37 PM
	 */
	@GetMapping(value="/get/child/nodes")
	public AjaxResponse<List<TreeVo>> getChildNodes(
			@RequestParam(value = "treeId", defaultValue = "0") long id,
			@RequestParam(value = "filterModId", defaultValue = "0") long filterOutModId) {

		List<TreeVo> asyncTreeList = moduleService.findByParentIdAndFilterOutModuleIds(id, filterOutModId);
		
		logger.debug("json --> " + JsonUtils.toJson(asyncTreeList));
		
		return AjaxResponse.create(asyncTreeList);
	}
	
	/**
	 * 功能说明 : 修改模块
	 *
	 * @return
	 * @author rutine
	 * @time Jun 1, 2013 3:45:56 PM
	 */
	@PutMapping(value="/update")
	public AjaxResponse<String> putUpdate(@RequestBody ModuleMenu moduleMemu) {
		moduleService.update(moduleMemu);
		
		return AjaxResponse.create("修改模块成功");
	}
			
	@GetMapping(value="/view")
	public AjaxResponse<ModuleMenuVo> getView(@RequestParam long id) {
		ModuleMenuVo moduleMemu = moduleService.get(id);
		ModuleMenuVo parentMemu  = moduleService.get(moduleMemu.getParentId());
		moduleMemu.setParentName(parentMemu.getModName());
		
		return AjaxResponse.create(moduleMemu);
	}
}
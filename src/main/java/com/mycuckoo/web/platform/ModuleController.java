package com.mycuckoo.web.platform;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.ModuleService;

/**
 * 功能说明: 模块菜单Controller
 *
 * @author rutine
 * @time Oct 2, 2014 7:37:26 AM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/moduleMgr")
public class ModuleController {
	private static Logger logger = LoggerFactory.getLogger(ModuleController.class);
	
	@Autowired
	private ModuleService moduleService;
	
	@RequestMapping(value="/index")
	public String moduleMgr() {
		logger.info("---------------- 请求模块菜单管理界面 -----------------");
		
		return "business/platform/module/moduleMgr";
	}
	
	/**
	 * 功能说明 : 列表展示页面
	 *
	 * @param treeId 查找指定节点下的模块`
	 * @param modName 模块名称
	 * @param modEnId 模块id
	 * @param pageNo 第几页
	 * @param pageSize 页面大小, 暂时没有使用
	 * @param model 业务数据
	 * @return
	 * @author rutine
	 * @time Dec 2, 2012 8:22:41 PM
	 */
	@RequestMapping(value="/list")
	public String list(@RequestParam(value="treeId", defaultValue="-1") long treeId,
			@RequestParam(value="modName", defaultValue="") String modName,
			@RequestParam(value="modEnId", defaultValue="") String modEnId,
			@RequestParam(value="pageNo", defaultValue="1") int pageNo,
			@RequestParam(value="pageSize", defaultValue=LIMIT + "") int pageSize,
			HttpServletRequest request,
			Model model) {
		
		Page<SysplModuleMemu> page = moduleService.findModulesByCon(treeId, modName, modEnId, 
				new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "treeId=" + treeId + "&modName=" + modName  + "&modEnId=" + modEnId);
		
		return "business/platform/module/moduleMgrList";
	}
	
	/**
	 * 功能说明 : 创建模块
	 *
	 * @param sysplModuleMemu
	 * @param session
	 * @return
	 * @author rutine
	 * @time Jun 1, 2013 9:13:49 AM
	 */
	@RequestMapping(value="/createForm", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult postCreateForm(
			SysplModuleMemu sysplModuleMemu, 
			HttpServletRequest request,
			HttpSession session) {
		
		logger.debug(JsonUtils.toJson(sysplModuleMemu));
		
		AjaxResult ajaxModel = new AjaxResult(true, "保存模块成功");
		try {
			SysplModuleMemu sysplModuleMemuParent = new SysplModuleMemu();
			sysplModuleMemuParent.setModuleId(sysplModuleMemu.getUpModId());
			sysplModuleMemu.setCreateDate(new Date());
			sysplModuleMemu.setCreator(SessionUtil.getUserCode(session));
			sysplModuleMemu.setSysplModuleMemu(sysplModuleMemuParent);
			moduleService.saveModule(sysplModuleMemu, request);
		} catch (ApplicationException e) {
			logger.error("保存模块失败！", e);
			
			ajaxModel.setStatus(false);
			ajaxModel.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("保存模块失败！", e);
			
			ajaxModel.setStatus(false);
			ajaxModel.setMsg("保存模块失败");
		}
		
		return ajaxModel;
	}
	
	@RequestMapping(value="/createForm", method=RequestMethod.GET)
	public String getCreateForm(Model model) {
	
		model.addAttribute("module", new SysplModuleMemu());
		model.addAttribute("action", "create");
		
		return "business/platform/module/moduleMgrForm";
	}
	
	/**
	 * 功能说明 : 保存模块操作关系
	 *
	 * @param id 模块ID
	 * @param operateIdList 模块操作列表
	 * @param request
	 * @return
	 * @author rutine
	 * @time May 12, 2013 5:44:16 PM
	 */
	@RequestMapping(value="/createModuleOptRefs", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult postCreateModuleOptRefs(
			@RequestParam(value="moduleId") long id,
			@RequestParam(value="operateIdList[]") List<Long> operateIdList,
			ServletRequest request) {
		
//		String[] ids = request.getParameterValues("operateIdList[]");
//		DefaultConversionService conversionService = new DefaultConversionService();
//		List<Long> idList = (List<Long>) conversionService.convert(ids, List.class);
	
		AjaxResult ajaxModel = new AjaxResult(true, "分配模块权限成功");
		try {
			moduleService.saveModuleOptRefs(id, operateIdList, (HttpServletRequest) request);
		} catch (ApplicationException e) {
			logger.error("分配模块权限失败!", e);
			
			ajaxModel.setStatus(false);
			ajaxModel.setMsg("分配模块权限失败");
		}
		
		return ajaxModel;
	}
	
	/**
	 * 功能说明 : 删除模块
	 *
	 * @param id
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 11:09:30 AM
	 */
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	@ResponseBody
	public AjaxResult deleteModuleById(
			@RequestParam long id, 
			HttpServletRequest request) {
		
		AjaxResult ajaxModel = new AjaxResult(true, "模块菜单删除成功");
		try {
			moduleService.deleteModuleByModuleId(id, request);
		} catch (ApplicationException e) {
			logger.error("模块菜单删除失败！", e);
			
			ajaxModel.setStatus(false);
			ajaxModel.setMsg("模块菜单删除失败");
		} catch (SystemException e) {
			logger.error("模块菜单删除失败！", e);
			
			ajaxModel.setStatus(false);
			ajaxModel.setMsg("模块菜单删除失败");
		}
		
		return ajaxModel;
	}
	
	/**
	 * 功能说明 : 停用/启用模块
	 *
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 1:53:04 PM
	 */
	@RequestMapping(value="/disEnable", method=RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag,
			HttpServletRequest request) {
		
		AjaxResult ajaxModel = new AjaxResult(true, "");
		try {
			boolean disEnableBol = moduleService.disEnableModule(id, disEnableFlag, request);
			ajaxModel.setStatus(disEnableBol);
		}  catch (ApplicationException e) {
			logger.error("停用启用模块失败！", e);
			
			ajaxModel.setStatus(false);
		} catch (SystemException e) {
			logger.error("停用启用模块失败！", e);
			
			ajaxModel.setStatus(false);
		}
		
		return ajaxModel;
	}
	
	/**
	 * 功能说明 : 获取模块的下级模块
	 *
	 * @param id 模块id
	 * @param filterModId
	 * @param model
	 * @author rutine
	 * @time Dec 1, 2012 1:45:37 PM
	 */
	@RequestMapping(value="/getChildNodes", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	@ResponseBody
	public List<TreeVo> getChildNodes(
			@RequestParam(value = "moduleId", defaultValue = "0") long id,
			@RequestParam(value = "filterModId", defaultValue = "0") long filterModId) {
		
		List<TreeVo> asyncTreeList = moduleService.findModulesByUpModId(id, filterModId);
		
		logger.debug("json --> " + JsonUtils.toJson(asyncTreeList));
		
		return asyncTreeList;
	}
	
	/**
	 * 功能说明 : 修改模块
	 *
	 * @return
	 * @author rutine
	 * @time Jun 1, 2013 3:45:56 PM
	 */
	@RequestMapping(value="/updateForm", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult postUpdateForm(
			@ModelAttribute("preload") SysplModuleMemu sysplModuleMemu, 
			HttpServletRequest request) {
		
		AjaxResult ajaxModel = new AjaxResult(true, "修改模块成功");
		try {
			SysplModuleMemu sysplModuleMemuParent = new SysplModuleMemu();
			// upmodid为0参数传不过来
			sysplModuleMemuParent .setModuleId(sysplModuleMemu.getUpModId() == null ? 0 : sysplModuleMemu.getUpModId());
			sysplModuleMemu.setSysplModuleMemu(sysplModuleMemuParent);
			moduleService.updateModule(sysplModuleMemu, request);
		} catch (NumberFormatException e) {
			logger.error("upModId is not number. ", e);
			
			ajaxModel.setStatus(false);
			ajaxModel.setMsg("修改模块失败");
		} catch (ApplicationException e) {
			logger.error("修改模块失败!", e);
			
			ajaxModel.setStatus(false);
			ajaxModel.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("修改模块失败!", e);
			
			ajaxModel.setStatus(false);
			ajaxModel.setMsg(e.getMessage());
		}
		
		return ajaxModel;
	}
	
	@RequestMapping(value="/updateForm", method=RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id,  Model model) {
		SysplModuleMemu moduleMemu = moduleService.getModuleByModuleId(id);
		SysplModuleMemu parentMemu  = moduleService.getModuleByModuleId(moduleMemu.getSysplModuleMemu().getModuleId());
		moduleMemu.setUpModId(parentMemu.getModuleId());
		moduleMemu.setUpModName(parentMemu.getModName());
		
		model.addAttribute("module", moduleMemu);
		model.addAttribute("action", "update");
		
		return "business/platform/module/moduleMgrForm";
	}
	
	/**
	 * 功能说明 : 获得模块已经分配和未分配的操作列表
	 *
	 * @param modName
	 * @param id
	 * @param model
	 * @return
	 * @author rutine
	 * @time May 9, 2013 8:52:56 PM
	 */
	@RequestMapping(value="/getAssignedAUnAssignedOpt")
	public String getAssignedAUnAssignedOpt(
			@RequestParam(value="moduleId") long id,
			@RequestParam(value="modName") String modName,
			Model model) {
	
		Map<String, List<SysplOperate>> map = moduleService.findAssignedAUnAssignedOperatesByModuleId(id);
		model.addAllAttributes(map);
	
		return "business/platform/module/moduleMgrAssignOpts";
	}
			
	@RequestMapping(value="/viewForm", method=RequestMethod.GET)
	public String getViewForm(@RequestParam long id,  Model model) {
		SysplModuleMemu moduleMemu = moduleService.getModuleByModuleId(id);
		SysplModuleMemu parentMemu  = moduleService.getModuleByModuleId(moduleMemu.getSysplModuleMemu().getModuleId());
		moduleMemu.setUpModId(parentMemu.getModuleId());
		moduleMemu.setUpModName(parentMemu.getModName());
		
		model.addAttribute("module", moduleMemu);
		model.addAttribute("action", "view");
		
		return "business/platform/module/moduleMgrForm";
	}
	
	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preload")
	public SysplModuleMemu get(@RequestParam(value = "moduleId", required = false) Long id) {
		if (id != null) {
			return moduleService.getModuleByModuleId(id);
		}
		return null;
	}
	
}
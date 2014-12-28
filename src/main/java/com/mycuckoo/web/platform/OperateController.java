package com.mycuckoo.web.platform;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.util.Date;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.OperateService;

/**
 * 功能说明: 操作按钮Controller
 * 
 * @author rutine
 * @time Oct 12, 2014 3:41:26 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping(value = "/platform/operateMgr")
public class OperateController {
	private static Logger logger = LoggerFactory.getLogger(OperateController.class);

	@Autowired
	private OperateService optService;

	/**
	 * 功能说明 : 操作按钮管理界面入口
	 * 
	 * @param modCode 菜单编码
	 * @param operateName 操作名称
	 * @param pageNo 页码
	 * @param pageSize 每页大小
	 * @param model
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 5:52:09 PM
	 */
	@RequestMapping(value = "/index")
	public String operateMgr(
			@RequestParam(value = "operateName", defaultValue = "") String operateName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {
		
		logger.info("---------------- 请求操作按钮管理界面 -----------------");

		Page<SysplOperate> page = optService.findOperatesByCon(operateName, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "operateName=" + operateName);

		return "business/platform/module/operateMgr";
	}

	/**
	 * 功能说明 : 创建模块操作
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param sysplOperate
	 * @param request
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 8:30:06 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			SysplOperate sysplOperate, 
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		logger.debug(JsonUtils.toJson(sysplOperate));

		boolean success = true;
		try {
			sysplOperate.setCreateDate(new Date());
			sysplOperate.setCreator(SessionUtil.getUserCode(session));
			optService.saveOperate(sysplOperate, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存操作按钮失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存操作按钮失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "保存操作按钮失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("operate", new SysplOperate());
		model.addAttribute("action", "create");

		return "business/platform/module/operateMgrForm";
	}

	/**
	 * 功能说明 : 停用/启用模块操作
	 * 
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 9:00:07 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {

		AjaxResult ajaxResult = new AjaxResult(true, "操作失败!");
		try {
			boolean disEnableBol = optService.disEnableOperate(id, disEnableFlag, request);
			ajaxResult.setStatus(disEnableBol);
		} catch (ApplicationException e) {
			logger.error("1. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
		} catch (SystemException e) {
			logger.error("2. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 修改模块操作
	 * 
	 * @param sysplOperate
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 2, 2013 8:52:32 PM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(
			@ModelAttribute("preload") SysplOperate sysplOperate,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		boolean success = true;
		try {
			optService.updateOperate(sysplOperate, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改操作按钮失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改操作按钮失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "修改操作按钮失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		SysplOperate operate = optService.findOperatesByOperateId(id);
		
		model.addAttribute("operate", operate);
		model.addAttribute("action", "update");

		return "business/platform/module/operateMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		SysplOperate operate = optService.findOperatesByOperateId(id);
		
		model.addAttribute("operate", operate);
		model.addAttribute("action", "view");

		return "business/platform/module/operateMgrForm";
	}

	@ModelAttribute("preload")
	public SysplOperate get(@RequestParam(value = "operateId", required = false) Long id) {
		if (id != null) {
			logger.debug("------------------- preload before update--------------------------");
			
			return optService.findOperatesByOperateId(id);
		}
		return null;
	}

}

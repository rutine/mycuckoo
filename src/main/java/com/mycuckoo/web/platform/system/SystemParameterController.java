package com.mycuckoo.web.platform.system;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;
import static com.mycuckoo.common.constant.Common.USER_CODE;

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
import com.mycuckoo.domain.platform.SysplSysParameter;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.SystemParameterService;

/**
 * 功能说明: 系统参数Controller
 *
 * @author rutine
 * @time Oct 14, 2014 8:49:02 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/systemParameterMgr")
public class SystemParameterController {
	private static Logger logger = LoggerFactory.getLogger(SystemParameterController.class);

	@Autowired
	private SystemParameterService systemParameterService;

	@RequestMapping(value = "/index")
	public String systemParaMgr(
			@RequestParam(value = "paraName", defaultValue = "") String paraName,
			@RequestParam(value = "paraKeyName", defaultValue = "") String paraKeyName,
			@RequestParam(value = "page", defaultValue = "1") int start,
			@RequestParam(value = "limit", defaultValue = LIMIT + "") int limit,
			Model model) {

		logger.info("---------------- 请求系统参数管理界面 -----------------");

		Page<SysplSysParameter> page = systemParameterService.findSystemParametersByCon(paraName, 
				paraKeyName, new PageRequest(start - 1, limit));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "paraName=" + paraName + "&paraKeyName=" + paraKeyName);

		return "business/platform/system/systemParameterMgr";
	}

	/**
	 * 功能说明 : 创建系统参数
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param sysplSysParameter
	 * @param request
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 10:10:06 AM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			SysplSysParameter sysplSysParameter,
			HttpServletRequest request, 
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		logger.debug(JsonUtils.toJson(sysplSysParameter));

		boolean success = true;
		try {
			sysplSysParameter.setCreateDate(new Date());
			sysplSysParameter.setCreator(SessionUtil.getUserCode(session));
			systemParameterService.saveSystemParameter(sysplSysParameter, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存系统参数失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存系统参数失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "保存系统参数失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {

		model.addAttribute("systemPara", new SysplSysParameter());
		model.addAttribute("action", "create");

		return "business/platform/system/systemParameterMgrForm";
	}

	/**
	 * 功能说明 : 停用/启用系统参数
	 * 
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 10:14:48 AM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "操作失败!");
		try {
			boolean disEnableBol = systemParameterService.disEnableSystemParameter(id, disEnableFlag, request);
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
	 * 功能说明 : 修改系统参数
	 * 
	 * @param sysplSysParameter
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jul 2, 2013 10:21:10 AM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(
			@ModelAttribute("preload") SysplSysParameter sysplSysParameter,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		boolean success = true;
		try {
			systemParameterService.updateSystemParameter(sysplSysParameter, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改系统参数失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改系统参数失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "修改系统参数失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		SysplSysParameter sysplSysParameter = systemParameterService.getSystemParameterById(id);

		model.addAttribute("systemPara", sysplSysParameter);
		model.addAttribute("action", "update");

		return "business/platform/system/systemParameterMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		SysplSysParameter sysplSysParameter = systemParameterService.getSystemParameterById(id);

		model.addAttribute("systemPara", sysplSysParameter);
		model.addAttribute("action", "view");

		return "business/platform/system/systemParameterMgrForm";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preload")
	public SysplSysParameter get(@RequestParam(value = "paraId", required = false) Long id) {
		if (id != null) {
			return systemParameterService.getSystemParameterById(id);
		}
		return null;
	}

}

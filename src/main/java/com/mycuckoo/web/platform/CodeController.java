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
import com.mycuckoo.domain.platform.SysplCode;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.CodeService;

/**
 * 功能说明: 系统编码Controller
 *
 * @author rutine
 * @time Oct 14, 2014 3:16:02 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/codeMgr")
public class CodeController {
	private static Logger logger = LoggerFactory.getLogger(CodeController.class);

	@Autowired
	private CodeService codeService;

	@RequestMapping(value = "/index")
	public String codeMgr(
			@RequestParam(value = "codeName", defaultValue = "") String codeName,
			@RequestParam(value = "codeEngName", defaultValue = "") String codeEngName,
			@RequestParam(value = "moduleName", defaultValue = "") String moduleName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {
		
		logger.info("---------------- 请求系统编码管理界面 -----------------");

		Page<SysplCode> page = codeService.findCodesByCon(codeEngName,
				codeName, moduleName, new PageRequest(pageNo - 1, pageSize));

		model.addAttribute("page", page);
		model.addAttribute("searchParams", "codeEngName=" + codeEngName
				+ "&codeName=" + codeName + "&moduleName=" + moduleName);

		return "business/platform/module/codeMgr";
	}

	/**
	 * 功能说明 : 创建新编码
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param sysplCode
	 * @param request
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:48:33 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			SysplCode sysplCode, 
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		logger.debug(JsonUtils.toJson(sysplCode));

		boolean success = true;
		try {
			sysplCode.setCreator(SessionUtil.getUserCode(session));
			sysplCode.setCreateDate(new Date());
			codeService.saveCode(sysplCode, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存系统编码失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("1. 保存系统编码失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "保存系统编码失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("code", new SysplCode());
		model.addAttribute("action", "create");

		return "business/platform/module/codeMgrForm";
	}

	/**
	 * 功能说明 : 删除系统编码
	 * 
	 * @param id
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:59:46 PM
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult delete(
			@RequestParam long id,
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "删除系统编码成功");
		try {
			throw new ApplicationException("找不到删除记录的方法!");
		} catch (ApplicationException e) {
			logger.error("1. 删除系统编码失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("1. 删除系统编码失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("删除系统编码失败!");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 停用/启用编码
	 * 
	 * @param id
	 * @param disEnableFlag 停用/启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:57:53 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "");
		try {
			boolean disEnableBol = codeService.disEnableCode(id, disEnableFlag, request);
			ajaxResult.setStatus(disEnableBol);
		} catch (ApplicationException e) {
			logger.error("1. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
		} catch (SystemException e) {
			logger.error("1. 停用启用失败!", e);
			
			ajaxResult.setStatus(false);
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 修改编码
	 * 
	 * @param sysplCode
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:53:05 PM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(
			@ModelAttribute("preload") SysplCode sysplCode,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		boolean success = true;
		try {
			codeService.updateCode(sysplCode, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改系统编码失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改系统编码失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "修改系统编码失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		SysplCode sysplCode = codeService.getCodeByCodeId(id);

		logger.debug(JsonUtils.toJson(sysplCode));

		model.addAttribute("code", sysplCode);
		model.addAttribute("action", "update");

		return "business/platform/module/codeMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		SysplCode sysplCode = codeService.getCodeByCodeId(id);

		logger.debug(JsonUtils.toJson(sysplCode));

		model.addAttribute("code", sysplCode);
		model.addAttribute("action", "view");

		return "business/platform/module/codeMgrForm";
	}

	@ModelAttribute("preload")
	public SysplCode get(@RequestParam(value = "codeId", required = false) Long id) {
		if (id != null) {
			return codeService.getCodeByCodeId(id);
		}
		return null;
	}

}

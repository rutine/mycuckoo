package com.mycuckoo.web.platform;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplAffiche;
import com.mycuckoo.domain.platform.SysplCode;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.AfficheService;

/**
 * 功能说明: 系统公告Controller
 * 
 * @author rutine
 * @time Oct 14, 2014 9:25:37 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/platform/afficheMgr")
public class AfficheController {
	private static Logger logger = LoggerFactory.getLogger(AfficheController.class);

	@Autowired
	private AfficheService afficheService;

	@RequestMapping(value = "/index")
	public String afficheMgr(
			@RequestParam(value = "afficheTitle", defaultValue = "") String afficheTitle,
			@RequestParam(value = "affichePulish", defaultValue = "0") Short affichePulish,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {
		
		logger.info("---------------- 请求公告菜单管理界面 -----------------");

		Page<SysplAffiche> page = afficheService.findAffichesByCon(afficheTitle, 
				affichePulish, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "afficheTitle=" + afficheTitle + "&affichePulish=" + affichePulish);

		return "business/platform/module/afficheMgr";
	}

	/**
	 * 功能说明 : 创建新公告
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param sysplAffiche
	 * @param request
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 29, 2013 8:39:57 AM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			@RequestParam(required = false) List<String> accessoryNameList,
			SysplAffiche sysplAffiche, 
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		logger.debug(JsonUtils.toJson(sysplAffiche));

		boolean success = true;
		try {
			afficheService.saveAffiche(sysplAffiche, accessoryNameList, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存公告失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存公告失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "保存公告失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("code", new SysplCode());
		model.addAttribute("action", "create");

		return "business/platform/module/afficheMgrForm";
	}

	/**
	 * 功能说明 : 删除公告
	 * 
	 * @param afficheIdList 公告IDs
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 25, 2013 8:59:46 PM
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult delete(
			@RequestParam(value = "ids") List<Long> afficheIdList,
			HttpServletRequest request) {

		AjaxResult ajaxResult = new AjaxResult(true, "删除公告成功");
		try {
			afficheService.deleteAffichesByIds(afficheIdList, request);
		} catch (ApplicationException e) {
			logger.error("1. 删除公告失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 删除公告失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("删除公告失败!");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 修改公告
	 * 
	 * @param sysplAffiche
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 29, 2013 8:55:38 AM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(
			@ModelAttribute("preload") SysplAffiche sysplAffiche,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		boolean success = true;
		try {
			afficheService.updateAffiche(sysplAffiche, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改公告失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改公告失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "修改公告失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		SysplAffiche sysplAffiche = afficheService.getAfficheByAfficheId(id);

		logger.debug(JsonUtils.toJson(sysplAffiche));

		model.addAttribute("affiche", sysplAffiche);
		model.addAttribute("action", "update");

		return "business/platform/module/afficheMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		SysplAffiche sysplAffiche = afficheService.getAfficheByAfficheId(id);

		logger.debug(JsonUtils.toJson(sysplAffiche));

		model.addAttribute("affiche", sysplAffiche);
		model.addAttribute("action", "view");

		return "business/platform/module/afficheMgrForm";
	}

	@ModelAttribute("preload")
	public SysplAffiche get(@RequestParam(value = "afficheId", required = false) Long id) {
		if (id != null) {
			return afficheService.getAfficheByAfficheId(id);
		}
		return null;
	}

	/**
	 * 设置自定义属性转换器
	 * 
	 * @param request the current request
	 * @param binder the data binder
	 */
	@InitBinder
	public void InitBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		// 不要删除下行注释! 将来"yyyy-MM-dd"将配置到properties文件中
		// SimpleDateFormat dateFormat = new
		// SimpleDateFormat(getText("date.format", request.getLocale()));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}

}

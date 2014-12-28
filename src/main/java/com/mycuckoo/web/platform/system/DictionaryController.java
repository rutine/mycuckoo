package com.mycuckoo.web.platform.system;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplDicBigType;
import com.mycuckoo.domain.platform.SysplDicSmallType;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.platform.DictionaryService;

/**
 * 功能说明: 字典Controller
 * 
 * @author rutine
 * @time Oct 18, 2014 7:50:17 AM
 * @version 2.0.0
 */
@Controller
@RequestMapping(value = "/platform/typeDictionaryMgr")
public class DictionaryController {
	private static Logger logger = LoggerFactory.getLogger(DictionaryController.class);

	@Autowired
	private DictionaryService dictionaryService;

	@RequestMapping(value = "/index")
	public String dictionaryMgr(
			@RequestParam(value = "bigTypeName", defaultValue = "") String dictName,
			@RequestParam(value = "bigTypeCode", defaultValue = "") String dictCode,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {

		logger.info("---------------- 请求字典菜单管理界面 -----------------");

		Page<SysplDicBigType> page = dictionaryService
				.findDicBigTypesByCon(dictName, dictCode, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "bigTypeName=" + dictName + "&bigTypeCode=" + dictCode);

		return "business/platform/system/dictionaryMgr";
	}

	/**
	 * 功能说明 : 创建新字典大类
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param dicBigType
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jun 11, 2013 4:29:28 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			SysplDicBigType dicBigType,
			HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		
		for (SysplDicSmallType sysplDicSmallType : dicBigType.getSysplDicSmallTypes()) {
			sysplDicSmallType.setSysplDicBigType(dicBigType);
		}

		logger.debug(JsonUtils.toJson(dicBigType.getSysplDicSmallTypes(), SysplDicSmallType.class));
		
		boolean success = true;
		try {
			dictionaryService.saveDicBigType(dicBigType, request);
			dictionaryService.saveDicSmallTypes(dicBigType.getSysplDicSmallTypes(), request);
		} catch (ApplicationException e) {
			logger.error("1. 保存字典类失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存字典类失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "保存字典类失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("dictionary", new SysplDicBigType());
		model.addAttribute("action", "create");

		return "business/platform/system/dictionaryMgrForm";
	}

	/**
	 * 功能说明 : 停用启用
	 * 
	 * @param id
	 * @param disEnableFlag true为停用启用成功，false不能停用
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 11, 2013 6:08:04 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {

		AjaxResult ajaxResult = new AjaxResult(true, "操作失败!");
		try {
			boolean disEnableBol = dictionaryService.disEnableDicBigType(id, disEnableFlag, request);
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
	 * 功能说明 : 根据大类代码查询所有小类
	 * 
	 * @return
	 * @author rutine
	 * @time Dec 15, 2012 4:21:10 PM
	 */
	@RequestMapping(value = "/getSmallTypeDicByBigTypeCode", method = RequestMethod.GET)
	public String getSmallTypeDicByBigTypeCode(
			@RequestParam String bigTypeCode, 
			Model model) {

		List<SysplDicSmallType> sysplDicSmallTypeList = dictionaryService.findDicSmallTypesByBigTypeCode(bigTypeCode);
		model.addAttribute(sysplDicSmallTypeList);

		return "business/platform/system/dictionarySelect";
	}

	/**
	 * 功能说明 : 修改字典, 直接删除字典大类关联的字典小类，保存字典小类
	 * 
	 * @param dicBigType 字典大类对象
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jun 11, 2013 5:43:50 PM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(
			@ModelAttribute("preload") SysplDicBigType dicBigType,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		for (SysplDicSmallType sysplDicSmallType : dicBigType.getSysplDicSmallTypes()) {
			sysplDicSmallType.setSysplDicBigType(dicBigType);
		}

		logger.debug(JsonUtils.toJson(dicBigType.getSysplDicSmallTypes(), SysplDicSmallType.class));
		
		boolean success = true;
		try {
			dictionaryService.updateDicBigType(dicBigType, request);
			dictionaryService.saveDicSmallTypes(dicBigType.getSysplDicSmallTypes(), request);
		} catch (ApplicationException e) {
			logger.error("1. 修改字典失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改字典失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "修改字典失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		SysplDicBigType bigDictionary = dictionaryService.getDicBigTypeByBigTypeId(id);
		List<SysplDicSmallType> smallDictionarys = dictionaryService
				.findDicSmallTypesByBigTypeCode(bigDictionary.getBigTypeCode());

		model.addAttribute("dictionary", bigDictionary);
		model.addAttribute("smallDictionarys", smallDictionarys);
		model.addAttribute("action", "update");

		return "business/platform/system/dictionaryMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		SysplDicBigType bigDictionary = dictionaryService.getDicBigTypeByBigTypeId(id);
		List<SysplDicSmallType> smallDictionarys = dictionaryService
				.findDicSmallTypesByBigTypeCode(bigDictionary.getBigTypeCode());

		model.addAttribute("dictionary", bigDictionary);
		model.addAttribute("smallDictionarys", smallDictionarys);
		model.addAttribute("action", "view");

		return "business/platform/system/dictionaryMgrForm";
	}

	@ModelAttribute("preload")
	public SysplDicBigType get(@RequestParam(value = "bigTypeId", required = false) Long id) {
		if (id != null) {
			logger.info("--------------- preload before update data ----------------");
			return dictionaryService.getDicBigTypeByBigTypeId(id);
		}
		return null;
	}
}

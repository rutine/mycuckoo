package com.mycuckoo.web.commonused;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.commonused.UserCommFunService;
import com.mycuckoo.service.iface.commonused.UumCommonService;

/**
 * 功能说明: 用户常用功能Controller
 *
 * @author rutine
 * @time Nov 2, 2014 11:35:34 AM
 * @version 2.0.0
 */
@Controller
@RequestMapping(value = "/userCommFunMgr")
public class UserCommFunController {
	private static Logger logger = LoggerFactory.getLogger(UserCommFunController.class);
	
	@Autowired
	private UserCommFunService userCommFunService;
	@Autowired
	private UumCommonService commonService;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getCreateForm(HttpSession session, Model model) {
		List<TreeVoExtend> userMenuList = Lists.newArrayList();
		
		userMenuList.addAll(commonService.convertToTree(SessionUtil.getFirstMenu(session)));
		for(List<SysplModuleMemu> list : SessionUtil.getSecondMenu(session).values()) {
			userMenuList.addAll(commonService.convertToTree(list));
		}
		for(List<SysplModuleMemu> list : SessionUtil.getThirdMenu(session).values()) {
			userMenuList.addAll(commonService.convertToTree(list));
		}
		model.addAttribute("userMenuList", JsonUtils.toJson(userMenuList));
		
		return "business/commonused/userCommFunMgrForm";
	}
	
	/**
	 * 功能说明 : 保存用户常用功能
	 * 
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 1:45:30 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult postCreateForm(
			@RequestParam(value = "moduleIdList") Set<Long> moduleIdList,
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "成功保存用户常用功能.");
		try {
			long userId = SessionUtil.getUserId(request.getSession());
			List<Long> list = Lists.newArrayList();
			list.addAll(moduleIdList);
			userCommFunService.saveUserCommFun(userId, list, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存用户常用功能失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存用户常用功能失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("保存用户常用功能失败!");
		}
		
		return ajaxResult;
	}

}

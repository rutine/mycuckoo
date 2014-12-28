package com.mycuckoo.web.commonused;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.commonused.UumCommonService;
import com.mycuckoo.service.iface.uum.UserAgentService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 用户授权代理设置Controller
 *
 * @author rutine
 * @time Nov 2, 2014 3:57:52 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping(value = "/userAgentMgr")
public class UserAgentController {
	private static Logger logger = LoggerFactory.getLogger(UserAgentController.class);
	
	@Autowired
	private UserAgentService userAgentService;
	@Autowired
	private UserService userService;
	@Autowired
	private UumCommonService commonService;
	
	/**
	 * 功能说明 : 用户代理页面
	 * 
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 6:23:54 PM
	 */
	@RequestMapping(value = "/index")
	public String index(HttpSession session, Model model) {
		logger.info("---------------- 请求用户代理界面 -----------------");
		
		this.list(1, 10, session, model);
		this.getCreateForm(session, model);
		
		model.addAttribute("searchParams", "tab=tabList");
		
		return "business/commonused/userAgentMgr";
	}
	
	
	/**
	 * 功能说明 : 用户代理列表页面
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param session
	 * @param model
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 6:28:49 PM
	 */
	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			HttpSession session,
			Model model) {
		
		Long userId = SessionUtil.getUserId(session);
		Page<UumUserAgent> page = userAgentService.findUserAgentByCon(userId, 
				new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		
		return "business/commonused/userAgentMgrList";
	}
	
	/**
	 * 功能说明 : 用户代理form页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 8:22:09 PM
	 */
	@RequestMapping(value = "/creatForm", method = RequestMethod.GET)
	public String getCreateForm(HttpSession session, Model model) {
		List<TreeVoExtend> userMenuList = Lists.newArrayList();
		
		userMenuList.addAll(commonService.convertToTree(SessionUtil.getFirstMenu(session)));
		for(List<SysplModuleMemu> list : SessionUtil.getSecondMenu(session).values()) {
			userMenuList.addAll(commonService.convertToTree(list));
		}
		for(List<SysplModuleMemu> list : SessionUtil.getThirdMenu(session).values()) {
			Iterator<SysplModuleMemu> it = list.iterator();
			while(it.hasNext()) {
				if(!SessionUtil.getFourthMenu(session).containsKey(it.next().getModuleId())) {
					it.remove();
				}
			}
			userMenuList.addAll(commonService.convertToTree(list));
		}
		for(List<SysplModuleMemu> list : SessionUtil.getFourthMenu(session).values()) {
			userMenuList.addAll(commonService.convertToTree(list));
		}
		model.addAttribute("userMenuList", JsonUtils.toJson(userMenuList));
		
		return "business/commonused/userAgentMgrForm";
	}
	
	
	/**
	 * 功能说明 : 删除用户代理
	 * 
	 * @param agentId
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 2, 2014 6:32:25 PM
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(
			@RequestParam(value = "id") long agentId,
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "成功删除用户代理.");
		try{
			List<Long> agentIds = Lists.newArrayList();
			agentIds.add(agentId);
			userAgentService.deleteUserAgentsByAgentIds(agentIds, request);
		} catch (ApplicationException e) {
			logger.error("1. 删除用户代理失败!");
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 删除用户代理失败!");
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("删除用户代理失败!");
		}
		
		return ajaxResult;
	}
	
	/**
	 * 功能说明：查找用户分配的模块操作代理权限
	 * @author jacobliang
	 * @return
	 * @time Feb 22, 2012 9:53:18 AM
	 */
	@RequestMapping(value = "getAssignedUserAgentPrivilegeList")
	@ResponseBody
	public List<TreeVo> getAssignedUserAgentPrivilegeList(@RequestParam(value = "id") long agentId) {
		return userAgentService.findAgentPrivilegeForAgentUserByAgentId(agentId);
	}

	/**
	 * 功能说明 : 根据拼音代码查询用户信息
	 * 
	 * @param userNamePy
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 3, 2014 9:17:55 PM
	 */
	@RequestMapping(value = "/queryUsersByUserNamePy")
	@ResponseBody
	public List queryUsersByUserNamePy(
			@RequestParam(value = "q") String userNamePy,
			HttpSession session) {
		
		long userId = SessionUtil.getUserId(session);
		List list = userService.findUsersByUserNamePy(CommonUtils.getFirstLetters(userNamePy), userId);
		
		return list;
	}
	
	/**
	 * 功能说明 : 判断是否已经为此用户授过权
	 * 
	 * @param userAgentId
	 * @param session
	 * @return
	 * @author rutine
	 * @time Nov 5, 2014 3:06:11 PM
	 */
	@RequestMapping(value = "/hasAgentUser")
	@ResponseBody
	public AjaxResult hasAgentUser(
			@RequestParam(value = "userAgentId") Long userAgentId,
			HttpSession session) {
		Long userId = SessionUtil.getUserId(session);
		
		AjaxResult ajaxResult = new AjaxResult(true, "查询成功.");
		try {
			boolean hasExists = userAgentService.existsAgentUser(userId, userAgentId);
			ajaxResult.getData().put("exists", hasExists);
		} catch (SystemException e) {
			logger.error("判断是否已经为 『{}』 用户授权查询异常！", userAgentId);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("查询失败!");
		}
		
		return ajaxResult;
	}
	
	/**
	 * 功能说明 : 保存用户代理
	 * 
	 * @param agentUserId 代理用户id
	 * @param beginDate 代理开始时间
	 * @param endDate 代理结束时间
	 * @param reason 代理原因
	 * @param optIdList 代理权限ids
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 5, 2014 2:53:25 PM
	 */
	@RequestMapping(value = "/saveUserAgent", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveUserAgent(
			@RequestParam(value = "agentUserId") Long agentUserId,
			@RequestParam(value = "beginDate") Date beginDate,
			@RequestParam(value = "endDate") Date endDate,
			@RequestParam(value = "reason") String reason,
			@RequestParam(value = "optIdList") Set<String> optIdList,
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = this.hasAgentUser(agentUserId, request.getSession());
		if(!ajaxResult.isStatus() || (boolean) ajaxResult.getData().get("exists")) {
			return ajaxResult;
		}
		
		ajaxResult = new AjaxResult(true, "保存用户代理成功.");
		try {
			UumUserAgent userAgent = new UumUserAgent();
			userAgent.setOrgRoleId(SessionUtil.getOrganRoleId(request.getSession()));
			userAgent.setUserId(SessionUtil.getUserId(request.getSession()));
			userAgent.setAgentUserId(agentUserId);
			userAgent.setBeginDate(beginDate);
			userAgent.setEndDate(endDate);
			userAgent.setReason(reason);
			List<String> list = Lists.newArrayList();
			list.addAll(optIdList);
			userAgentService.saveUserAgent(userAgent, list, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存用户代理失败!");
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存用户代理失败!");
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("保存用户代理失败!");
		}
		
		return ajaxResult;
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
		// SimpleDateFormat dateFormat = new SimpleDateFormat(getText("date.format", request.getLocale()));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
}
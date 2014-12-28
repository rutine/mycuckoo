package com.mycuckoo.web.uum.group;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;
import static com.mycuckoo.common.constant.Common.USER;
import static com.mycuckoo.common.constant.Common.USER_CODE;
import static com.mycuckoo.common.constant.Common.USER_GROUP;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumGroupMember;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.uum.RoleOrganService;
import com.mycuckoo.service.iface.uum.group.UserGroupService;

/**
 * 功能说明: 用户组Controller
 *
 * @author rutine
 * @time Oct 23, 2014 7:46:16 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping(value = "/uum/userGroupMgr")
public class UserGroupController {
	private static Logger logger = LoggerFactory.getLogger(UserGroupController.class);

	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private RoleOrganService roleOrganService;

	/**
	 * 功能说明 : 根据组名称查询组信息
	 * 
	 * @param groupName 组名称
	 * @param pageNo 分页第几页
	 * @param pageSize 分页每页记录数
	 * @param model 业务数据
	 * @return
	 * @author rutine
	 * @time Oct 27, 2013 10:44:18 AM
	 */
	@RequestMapping("/index")
	public String userGroupMgr(
			@RequestParam(value = "groupName", defaultValue = "") String groupName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {
		logger.info("---------------- 请求用户组管理界面 -----------------");

		Page<UumGroup> page = userGroupService.findUserGroupsByCon(groupName, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "groupName=" + groupName);

		return "/business/uum/group/userGroupMgr";
	}

	/**
	 * 功能说明 : 保存用户组
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param group 用户组对象
	 * @param userIdList
	 * @param request
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Nov 9, 2013 5:24:53 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			UumGroup group,
			@RequestParam Set<Long> userIdList, 
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		boolean success = true;
		try {
			List<UumGroupMember> uumGroupMemberSet = new ArrayList<UumGroupMember>();
			if (!userIdList.isEmpty()) {
				for (Long memberResourceId : userIdList) {
					if (memberResourceId != null) {
						UumGroupMember uumGroupMember = new UumGroupMember();
						uumGroupMember.setUumGroup(group);
						uumGroupMember.setGroupMemberType(USER);
						uumGroupMember.setMemberResourceId(memberResourceId);
						uumGroupMemberSet.add(uumGroupMember);
					}
				}
				group.setUumGroupMembers(uumGroupMemberSet);
			}
			group.setGroupType(USER_GROUP);
			group.setCreateDate(new Date());
			group.setCreator((String) session.getAttribute(USER_CODE));
			userGroupService.saveUserGroup(group, request);
		} catch (ApplicationException e) {
			logger.error("1. 添加用户组失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 添加用户组失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "添加用户组失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {

		model.addAttribute("module", new UumGroup());
		model.addAttribute("action", "create");

		return "/business/uum/group/userGroupMgrForm";
	}

	/**
	 * 功能说明 : 停用启用
	 * 
	 * @param id
	 * @param disEnableFlag 停用启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 3, 2013 6:12:42 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult();
		try {
			boolean disEnableBol = userGroupService.disEnableUserGroup(id, disEnableFlag, request);
			ajaxResult.setStatus(disEnableBol);
		} catch (ApplicationException e) {
			logger.error("1. 停用启用普通组失败!", e);
		} catch (SystemException e) {
			logger.error("2. 停用启用普通组失败!", e);
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 修改用户组
	 * 
	 * @param group
	 * @param userIdList
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Nov 3, 2013 6:01:42 PM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(@ModelAttribute("preload") UumGroup group,
			@RequestParam Set<Long> userIdList, 
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		boolean success = true;
		try {
			List<UumGroupMember> uumGroupMemberSet = new ArrayList<UumGroupMember>();
			if (!userIdList.isEmpty()) {
				for (Long memberResourceId : userIdList) {
					if (memberResourceId != null) {
						UumGroupMember uumGroupMember = new UumGroupMember();
						uumGroupMember.setUumGroup(group);
						uumGroupMember.setGroupMemberType(USER);
						uumGroupMember.setMemberResourceId(memberResourceId);
						uumGroupMemberSet.add(uumGroupMember);
					}
				}
				group.setUumGroupMembers(uumGroupMemberSet);
			}
			group.setUumGroupMembers(uumGroupMemberSet);
			group.setGroupType(USER_GROUP);
			userGroupService.updateUserGroup(group, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改用户组失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("id", group.getGroupId());
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改用户组失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("id", group.getGroupId());
			redirectAttributes.addAttribute("error", "修改用户组失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		UumGroup group = userGroupService.getUserGroupByGroupId(id);

		model.addAttribute("group", group);
		model.addAttribute("action", "update");

		return "/business/uum/group/userGroupMgrForm";
	}

	/**
	 * 功能说明 : 根据机构ID查询角色列表
	 * 
	 * @param orgId
	 * @return
	 * @author rutine
	 * @time Nov 3, 2013 2:54:46 PM
	 */
	@RequestMapping("/queryRoleList")
	@ResponseBody
	public List<UumRole> queryRoleList(@RequestParam long orgId,
			@RequestParam(value = "page", defaultValue = "1") int start,
			@RequestParam(value = "limit", defaultValue = LIMIT + "") int limit) {
		
		Page<UumRole> page = roleOrganService.findSelectedRolesByOrgId(orgId, null, new PageRequest(start - 1, limit));

		return page.getContent();
	}

	/**
	 * 功能说明 : 根据机构ID角色ID和用户ID查询用户信息
	 * 
	 * @param orgId 机构ID
	 * @param roleId 角色ID
	 * @return
	 * @author rutine
	 * @time Nov 3, 2013 3:28:13 PM
	 */
	@RequestMapping("/queryUserList")
	@ResponseBody
	public List<UumUser> queryUserList(@RequestParam long orgId,
			@RequestParam long roleId) {
		List<UumUser> users = new ArrayList<UumUser>(0);
		try {
			users = userGroupService.findUsersByOrgRolId(orgId, roleId);
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		return users;
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		UumGroup group = userGroupService.getUserGroupByGroupId(id);

		model.addAttribute("group", group);
		model.addAttribute("action", "view");

		return "/business/uum/group/userGroupMgrForm";
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preload")
	public UumGroup get(
			@RequestParam(value = "groupId", required = false) Long id) {
		if (id != null) {
			return userGroupService.getUserGroupByGroupId(id);
		}
		return null;
	}
}

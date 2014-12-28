package com.mycuckoo.web.uum.group;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;
import static com.mycuckoo.common.constant.Common.ROLE;
import static com.mycuckoo.common.constant.Common.ROLE_GROUP;
import static com.mycuckoo.common.constant.Common.USER_CODE;

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
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.uum.group.RoleGroupService;

/**
 * 功能说明: 角色组管理Controller
 * 
 * @author rutine
 * @time Oct 21, 2014 9:09:36 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/uum/roleGroupMgr")
public class RoleGroupController {
	private static Logger logger = LoggerFactory.getLogger(RoleGroupController.class);

	@Autowired
	private RoleGroupService roleGroupService;

	@RequestMapping("/index")
	public String roleGroupMgr(
			@RequestParam(value = "groupName", defaultValue = "") String groupName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {
		
		logger.info("---------------- 请求角色组管理界面 -----------------");

		Page<UumGroup> page = roleGroupService.findRoleGroupsByCon(groupName, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "groupName=" + groupName);

		return "/business/uum/group/roleGroupMgr";
	}

	/**
	 * 功能说明 : 保存角色组
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param group 角色组对象
	 * @param roleIdList
	 * @param request
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Nov 9, 2013 5:23:51 PM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			UumGroup group,
			@RequestParam Set<Long> roleIdList, 
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		boolean success = true;
		try {
			List<UumGroupMember> uumGroupMemberSet = new ArrayList<UumGroupMember>();
			if (!roleIdList.isEmpty()) {
				for (Long memberResourceId : roleIdList) {
					if (memberResourceId != null) {
						UumGroupMember uumGroupMember = new UumGroupMember();
						uumGroupMember.setUumGroup(group);
						uumGroupMember.setGroupMemberType(ROLE);
						uumGroupMember.setMemberResourceId(memberResourceId);
						uumGroupMemberSet.add(uumGroupMember);
					}
				}
				group.setUumGroupMembers(uumGroupMemberSet);
			}
			group.setGroupType(ROLE_GROUP);
			group.setCreateDate(new Date());
			group.setCreator((String) session.getAttribute(USER_CODE));
			roleGroupService.saveRoleGroup(group, request);
		} catch (ApplicationException e) {
			logger.error("1. 添加角色组失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 添加角色组失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "添加角色组失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("module", new UumGroup());
		model.addAttribute("action", "create");

		return "/business/uum/group/roleGroupMgrForm";
	}

	/**
	 * 功能说明 : 停用启用
	 * 
	 * @param id
	 * @param disEnableFlag 停用启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Nov 9, 2013 5:28:50 PM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(
			@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult();
		try {
			boolean disEnableBol = roleGroupService.disEnableGroup(id, disEnableFlag, request);
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
	 * 功能说明 : 修改角色组
	 * 
	 * @param group
	 * @param userIdList
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Nov 9, 2013 7:24:05 PM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(
			@ModelAttribute("preload") UumGroup group,
			@RequestParam Set<Long> roleIdList, 
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		boolean success = true;
		try {
			List<UumGroupMember> uumGroupMemberSet = new ArrayList<UumGroupMember>();
			if (!roleIdList.isEmpty()) {
				for (Long memberResourceId : roleIdList) {
					if (memberResourceId != null) {
						UumGroupMember uumGroupMember = new UumGroupMember();
						uumGroupMember.setUumGroup(group);
						uumGroupMember.setGroupMemberType(ROLE);
						uumGroupMember.setMemberResourceId(memberResourceId);
						uumGroupMemberSet.add(uumGroupMember);
					}
				}
				group.setUumGroupMembers(uumGroupMemberSet);
			}
			group.setUumGroupMembers(uumGroupMemberSet);
			group.setGroupType(ROLE_GROUP);
			roleGroupService.updateRoleGroup(group, request);
		} catch (ApplicationException e) {
			logger.error("1. 添加角色组失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("id", group.getGroupId());
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 添加角色组失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("id", group.getGroupId());
			redirectAttributes.addAttribute("error", "添加角色组失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		UumGroup group = roleGroupService.getRoleGroupById(id);

		model.addAttribute("group", group);
		model.addAttribute("action", "update");

		return "/business/uum/group/roleGroupMgrForm";
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
	public List<UumRole> queryRoleList(
			@RequestParam long orgId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {
		
		List<UumRole> roleList = new ArrayList<UumRole>();
		try {
			roleList = roleGroupService.findRolesByOrgId(orgId);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		return roleList;
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preload")
	public UumGroup get(@RequestParam(value = "groupId", required = false) Long id) {
		if (id != null) {
			return roleGroupService.getRoleGroupById(id);
		}
		return null;
	}
}

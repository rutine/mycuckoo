package com.mycuckoo.web.uum;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;
import static com.mycuckoo.common.constant.Common.OWNER_TYPE_ROL;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_OPT;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_ROW;
import static com.mycuckoo.common.constant.Common.USER_CODE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleService;
import com.mycuckoo.service.iface.uum.RoleUserService;

/**
 * 功能说明: 角色管理Controller
 * 
 * @author rutine
 * @time Oct 18, 2014 3:25:00 PM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/uum/roleMgr")
public class RoleController {
	private static Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private PrivilegeService privilegeService;

	@RequestMapping(value = "/index")
	public String roleMgr(
			@RequestParam(value = "roleName", defaultValue = "") String roleName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {
		
		logger.info("---------------- 请求角色菜单管理界面 -----------------");

		Page<UumRole> page = roleService.findRoleByCon(roleName, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "roleName=" + roleName);

		return "business/uum/user/roleMgr";
	}

	/**
	 * 功能说明 : 分配角色对模块的操作权限
	 * 
	 * @param id 角色id
	 * @param model
	 * @return
	 * @author rutine
	 * @time Jul 14, 2013 4:27:11 PM
	 */
	@RequestMapping(value = "/getRolePrivilege", method = RequestMethod.POST)
	public String getRolePrivilege(@RequestParam long id, Model model) {
		Map moduleMemuMap = privilegeService.findSelectAUnselectModOptByOwnIdAOwnType(id, OWNER_TYPE_ROL);

		model.addAttribute("assignedModList", JsonUtils.toJson(moduleMemuMap.get("assignedModOpts")));
		model.addAttribute("unassignedModList", JsonUtils.toJson(moduleMemuMap.get("unassignedModOpts")));
		model.addAttribute("privilegeScope", moduleMemuMap.get("privilegeScope"));
		model.addAttribute("rowPrivilege", privilegeService.findRowPrivilegeByRoleIdAPriType(id));

		return "business/uum/user/roleMgrAssignModOpt";
	}

	/**
	 * 功能说明 : 保存角色
	 * 
	 * @param opt 操作(保存, 保存添加)
	 * @param uumRole
	 * @param request
	 * @param session
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jul 14, 2013 9:24:10 AM
	 */
	@RequestMapping(value = "/createForm", method = RequestMethod.POST)
	public String postCreateForm(
			@RequestParam(required = false) String opt,
			UumRole uumRole, 
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		logger.debug(JsonUtils.toJson(uumRole));

		boolean success = true;
		try {
			uumRole.setCreateDate(new Date());
			uumRole.setCreator((String) session.getAttribute(USER_CODE));
			roleService.saveRole(uumRole, request);
		} catch (ApplicationException e) {
			logger.error("1. 保存角色失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 保存角色失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "保存角色失败!");
		}

		return success && !"saveadd".equals(opt) ? "redirect:index" : "redirect:createForm";
	}

	@RequestMapping(value = "/createForm", method = RequestMethod.GET)
	public String getCreateForm(Model model) {
		model.addAttribute("role", new UumRole());
		model.addAttribute("action", "create");

		return "business/uum/user/roleMgrForm";
	}

	/**
	 * 功能说明 : 停用启用模块
	 * 
	 * @param id
	 * @param disEnableFlag 停用启用标志
	 * @param request
	 * @return
	 * @author rutine
	 * @time Jul 14, 2013 9:32:21 AM
	 */
	@RequestMapping(value = "/disEnable", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult disEnable(@RequestParam long id,
			@RequestParam String disEnableFlag, 
			HttpServletRequest request) {
		
		AjaxResult ajaxResult = new AjaxResult(true, "");
		/**
		 * 角色停用时检查其下是否有用户 1. 角色下有相应的用户, 不能被停用
		 */
		int userTotal = roleUserService.countRoleUserRefsByRoleId(id); // 是否已经分配用户
		if (userTotal > 0) {
			ajaxResult.setStatus(false);
			return ajaxResult;
		}
		try {
			roleService.disEnableRole(id, disEnableFlag, request);
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
	 * 功能说明 : 修改角色
	 * 
	 * @param uumRole角色对象
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @author rutine
	 * @time Jul 14, 2013 9:39:46 AM
	 */
	@RequestMapping(value = "/updateForm", method = RequestMethod.POST)
	public String postUpdateForm(
			@ModelAttribute("preload") UumRole uumRole,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		boolean success = true;
		try {
			roleService.updateRole(uumRole, request);
		} catch (ApplicationException e) {
			logger.error("1. 修改角色失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 修改角色失败!", e);
			
			success = false;
			redirectAttributes.addAttribute("error", "修改角色失败!");
		}

		return success ? "redirect:index" : "redirect:updateForm";
	}

	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String getUpdateForm(@RequestParam long id, Model model) {
		UumRole uumRole = roleService.getRoleByRoleId(id);
		
		model.addAttribute("role", uumRole);
		model.addAttribute("action", "update");

		return "business/uum/user/roleMgrForm";
	}

	@RequestMapping(value = "/viewForm", method = RequestMethod.GET)
	public String getViewForm(@RequestParam long id, Model model) {
		UumRole uumRole = roleService.getRoleByRoleId(id);

		model.addAttribute("role", uumRole);
		model.addAttribute("action", "view");

		return "business/uum/user/roleMgrForm";
	}

	/**
	 * 功能说明 : 为角色分配操作权限
	 * 
	 * @param id 角色id
	 * @param privilegeScope 权限范围
	 * @param optIdList 模块id集合
	 * @param model 业务对象
	 * @return json 数据
	 * @author rutine
	 * @time Sep 15, 2013 9:47:05 AM
	 */
	@RequestMapping(value = "/saveOptPrivilege", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveOptPrivilege(@RequestParam long id,
			@RequestParam String privilegeScope,
			@RequestParam Set<String> optIdList, HttpServletRequest request) {

		AjaxResult ajaxResult = new AjaxResult(true, "分配角色操作权限成功");
		try {
			List<String> list = new ArrayList<String>(optIdList.size());
			list.addAll(optIdList);
			privilegeService.savePrivilege(list, id, PRIVILEGE_TYPE_OPT, OWNER_TYPE_ROL, privilegeScope, request);
		} catch (ApplicationException e) {
			logger.error("1. 分配角色操作权限成功失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 分配角色操作权限成功失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("分配角色操作权限成功失败!");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 为角色设置行权限
	 * 
	 * @param id 角色id
	 * @param rowPrivilege 行权限类型
	 * @param request
	 * @return json 数据
	 * @author rutine
	 * @time Sep 15, 2013 1:18:53 PM
	 */
	@RequestMapping(value = "/saveRowPrivilege", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveRowPrivilege(
			@RequestParam long id,
			@RequestParam String rowPrivilege, 
			HttpServletRequest request) {

		AjaxResult ajaxResult = new AjaxResult(true, "设置角色行权限成功");
		try {
			final String rowPriv = rowPrivilege;
			List<String> optIdList = new ArrayList<String>() {
				{
					add(rowPriv);
				}
			};
			privilegeService.savePrivilege(optIdList, id, PRIVILEGE_TYPE_ROW, OWNER_TYPE_ROL, rowPrivilege, request);
		} catch (ApplicationException e) {
			logger.error("1. 设置角色行权限失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 设置角色行权限失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("设置角色行权限失败!");
		}

		return ajaxResult;
	}

	/**
	 * 使用@ModelAttribute, 实现Struts2
	 * Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preload")
	public UumRole get(@RequestParam(value = "roleId", required = false) Long id) {
		if (id != null) {
			return roleService.getRoleByRoleId(id);
		}
		return null;
	}

}

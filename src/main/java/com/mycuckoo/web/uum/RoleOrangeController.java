package com.mycuckoo.web.uum;

import static com.mycuckoo.common.constant.ActionVariable.LIMIT;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.domain.vo.AjaxResult;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.service.iface.uum.RoleOrganService;

/**
 * 功能说明: 角色机构Controller
 * 
 * @author rutine
 * @time Oct 19, 2014 12:16:38 AM
 * @version 2.0.0
 */
@Controller
@RequestMapping("/uum/roleAssignMgr")
public class RoleOrangeController {
	private static Logger logger = LoggerFactory.getLogger(RoleOrangeController.class);

	@Autowired
	private RoleOrganService roleOrganService;

	@RequestMapping(value = "/index")
	public String roleOrganMgr() {
		logger.info("---------------- 请求角色分配菜单管理界面 -----------------");

		return "business/uum/user/roleOrganMgr";
	}

	/**
	 * 列表展示页面
	 *
	 * @param treeId 树ID
	 * @param roleName 角色名称
	 * @param pageNo 第几页
	 * @param pageSize 每页显示数量
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(
			@RequestParam(value = "treeId", defaultValue = "-1") long treeId,
			@RequestParam(value = "roleName", defaultValue = "") String roleName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {

		Page<UumRole> page = roleOrganService.findSelectedRolesByOrgId(treeId,
				roleName, new PageRequest(pageNo - 1, pageSize));
		model.addAttribute("page", page);
		model.addAttribute("searchParams", "treeId=" + treeId + "&roleName=" + roleName);

		return "business/uum/user/roleOrganMgrList";
	}

	/**
	 * 功能说明 : 创建新的机构角色关系
	 * 
	 * @param id 机构id
	 * @param roleIdList 角色id集合
	 * @param request
	 * @return json 消息
	 * @author rutine
	 * @time Sep 15, 2013 6:07:06 PM
	 */
	@RequestMapping("/save")
	@ResponseBody
	public AjaxResult save(
			@RequestParam long id,
			@RequestParam Set<Long> roleIdList, 
			HttpServletRequest request) {

		logger.debug("orgId ---> {}, roleIdList ---> {}", id, roleIdList);

		AjaxResult ajaxResult = new AjaxResult(true, "成功为机构分配角色.");
		try {
			// 去掉重复
			List<Long> list = new ArrayList<Long>();
			list.addAll(roleIdList);
			roleOrganService.saveOrgRoleRef(id, list, request);
		} catch (ApplicationException e) {
			logger.error("1. 为机构分配角色失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("1. 为机构分配角色失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("为机构分配角色失败!");
		}

		return ajaxResult;
	}

	@RequestMapping("/delete")
	@ResponseBody
	// 删除为机构分配的角色
	public AjaxResult delete(
			@RequestParam long id, 
			@RequestParam List<Long> roleIdList, 
			HttpServletRequest request) {

		logger.debug("orgId ---> {}, roleIdList ---> {}", id, roleIdList);

		AjaxResult ajaxResult = new AjaxResult(true, "成功删除机构角色.");
		try {
			boolean roleHasUser = roleOrganService.deleteOrgRoleRef(id, roleIdList, request);
			if(roleHasUser) {
				ajaxResult.setCode((short) 1);
			}
		} catch (ApplicationException e) {
			logger.error("1. 删除机构角色失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg(e.getMessage());
		} catch (SystemException e) {
			logger.error("2. 删除机构角色失败!", e);
			
			ajaxResult.setStatus(false);
			ajaxResult.setMsg("删除机构角色失败!");
		}

		return ajaxResult;
	}

	/**
	 * 功能说明 : 机构未分配角色页面
	 * 
	 * @param treeId 机构id
	 * @param pageNo 第几页
	 * @param pageSize 每页显示数量
	 * @param model 业务对象
	 * @return
	 * @author rutine
	 * @time Sep 15, 2013 4:05:34 PM
	 */
	@RequestMapping(value = "/queryUnselectedRoleList")
	public String queryUnselectedRoleList(
			@RequestParam(value = "treeId", defaultValue = "-1") long treeId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
			Model model) {

		logger.debug("orgId ---> {}", treeId);

		try {
			Page<UumRole> page = roleOrganService.findUnselectedRolesByOrgId(treeId, new PageRequest(pageNo - 1, pageSize));
			model.addAttribute("page", page);
			model.addAttribute("searchParams", "treeId=" + treeId);
		} catch (SystemException e) {
			logger.error("unselectedRoleList methode occur error. ", e);
		}

		return "business/uum/user/roleOrganMgrListWithUnselectRole";
	}

}

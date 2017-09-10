package com.mycuckoo.web.uum;


import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.uum.OrganRoleService;
import com.mycuckoo.web.vo.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 角色机构Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 19, 2014 12:16:38 AM
 */
@RestController
@RequestMapping("/uum/role/assign/mgr")
public class OrganRoleController {
	private static Logger logger = LoggerFactory.getLogger(OrganRoleController.class);

	@Autowired
	private OrganRoleService roleOrganService;


	/**
	 * 列表展示页面
	 *
	 * @param treeId   树ID
	 * @param roleName 角色名称
	 * @param pageNo   第几页
	 * @param pageSize 每页显示数量
	 * @return
	 */
	@GetMapping(value = "/list")
	public AjaxResponse<Page<Role>> list(
			@RequestParam(value = "treeId", defaultValue = "-1") long treeId,
			@RequestParam(value = "roleName", defaultValue = "") String roleName,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

		Page<Role> page = roleOrganService.findSelectedRolesByOrgId(treeId,
				roleName, new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 机构未分配角色页面
	 *
	 * @param treeId   机构id
	 * @param pageNo   第几页
	 * @param pageSize 每页显示数量
	 * @return
	 * @author rutine
	 * @time Sep 15, 2013 4:05:34 PM
	 */
	@GetMapping(value = "/list/unselect/role")
	public AjaxResponse<Page<Role>> listUnselectRole(
			@RequestParam(value = "treeId", defaultValue = "-1") long treeId,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

		logger.debug("orgId ---> {}", treeId);

		Page<Role> page = roleOrganService.findUnselectedRolesByOrgId(treeId,
				new PageRequest(pageNo - 1, pageSize));

		return AjaxResponse.create(page);
	}

	/**
	 * 功能说明 : 创建新的机构角色关系
	 *
	 * @param id         机构id
	 * @param roleIdList 角色id集合
	 * @return json 消息
	 * @author rutine
	 * @time Sep 15, 2013 6:07:06 PM
	 */
	@PutMapping(value = "/save")
	public AjaxResponse<String> save(
			@RequestParam long id,
			@RequestParam Set<Long> roleIdList) {

		logger.debug("orgId ---> {}, roleIdList ---> {}", id, roleIdList);

		// 去掉重复
		List<Long> list = new ArrayList<Long>();
		list.addAll(roleIdList);
		roleOrganService.save(id, list);

		return AjaxResponse.create("成功为机构分配角色");
	}

	/**
	 * 删除为机构分配的角色
	 *
	 * @param id
	 * @param roleIdList
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public AjaxResponse<String> delete(
			@RequestParam long id,
			@RequestParam List<Long> roleIdList) {

		logger.debug("orgId ---> {}, roleIdList ---> {}", id, roleIdList);

		roleOrganService.deleteOrgRoleRef(id, roleIdList);

		return AjaxResponse.create("成功删除机构角色");
	}

}

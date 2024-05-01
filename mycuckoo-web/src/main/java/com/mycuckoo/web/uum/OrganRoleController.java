package com.mycuckoo.web.uum;


import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.uum.OrganRoleService;
import com.mycuckoo.web.vo.AjaxResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mycuckoo.web.constant.ActionConst.LIMIT;

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
    @GetMapping
    public AjaxResponse<Page<Role>> list(
            @RequestParam(value = "treeId", defaultValue = "-1") long treeId,
            @RequestParam(value = "roleName", defaultValue = "") String roleName,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

        roleName = StringUtils.isNotBlank(roleName) ? "%" + roleName + "%" : null;

        Page<Role> page = roleOrganService.findSelectedRolesByOrgId(treeId,
                roleName, new PageRequest(pageNo - 1, pageSize));

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 机构未分配角色页面
     *
     * @param orgId   机构id
     * @param pageNo   第几页
     * @param pageSize 每页显示数量
     * @return
     * @author rutine
     * @time Sep 15, 2013 4:05:34 PM
     */
    @GetMapping("/{orgId}/unselect/role")
    public AjaxResponse<Page<Role>> listUnselectRole(
            @PathVariable long orgId,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

        logger.debug("orgId ---> {}", orgId);

        Page<Role> page = roleOrganService.findUnselectedRolesByOrgId(orgId,
                new PageRequest(pageNo - 1, pageSize));

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 创建新的机构角色关系
     *
     * @param putVo 机构id 角色id集合
     * @return json 消息
     * @author rutine
     * @time Sep 15, 2013 6:07:06 PM
     */
    @PutMapping
    public AjaxResponse<String> save(@RequestBody AssignRolePutVo putVo) {
        logger.debug("orgId ---> {}, roleIdList ---> {}", putVo.getId(), putVo.getRoleIdList());

        // 去掉重复
        List<Long> list = new ArrayList<Long>();
        list.addAll(putVo.roleIdList);
        roleOrganService.save(putVo.id, list);

        return AjaxResponse.create("成功为机构分配角色");
    }

    /**
     * 删除为机构分配的角色
     *
     * @param orgId
     * @param roleIdList
     * @return
     */
    @DeleteMapping("/{orgId}")
    public AjaxResponse<String> delete(
            @PathVariable long orgId,
            @RequestBody List<Long> roleIdList) {

        logger.debug("orgId ---> {}, roleIdList ---> {}", orgId, roleIdList);

        roleOrganService.delete(orgId, roleIdList);

        return AjaxResponse.create("成功删除机构角色");
    }


    static class AssignRolePutVo {
        private long id;
        private Set<Long> roleIdList;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Set<Long> getRoleIdList() {
            return roleIdList;
        }

        public void setRoleIdList(Set<Long> roleIdList) {
            this.roleIdList = roleIdList;
        }
    }
}

package com.mycuckoo.web.uum;


import com.mycuckoo.constant.enums.OwnerType;
import com.mycuckoo.constant.enums.PrivilegeType;
import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.RoleService;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.CheckBoxTree;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import com.mycuckoo.web.vo.res.RolePrivilegeVo;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.mycuckoo.web.constant.ActionConst.LIMIT;

/**
 * 功能说明: 角色管理Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 18, 2014 3:25:00 PM
 */
@RestController
@RequestMapping("/uum/role/mgr")
public class RoleController {
    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;
    @Autowired
    private PrivilegeService privilegeService;


    @GetMapping
    public AjaxResponse<Page<Role>> list(
            @RequestParam(value = "roleName", defaultValue = "") String roleName,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize,
            Model model) {

        logger.info("---------------- 请求角色菜单管理界面 -----------------");

        Page<Role> page = roleService.findByPage(roleName, new PageRequest(pageNo - 1, pageSize));

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 分配角色对模块的操作权限
     *
     * @param id 角色id
     * @return
     * @author rutine
     * @time Jul 14, 2013 4:27:11 PM
     */
    @GetMapping(value = "/{id}/role-privilege")
    public AjaxResponse<RolePrivilegeVo> listRolePrivilege(@PathVariable long id) {
        AssignVo<CheckBoxTree, Long> baseVo = privilegeService.findModOptByOwnIdAOwnTypeWithCheck(id, OwnerType.ROLE);

        RolePrivilegeVo vo = new RolePrivilegeVo(
                baseVo.getPrivilegeScope(),
                privilegeService.findRowPrivilegeByRoleIdAPriType(id),
                baseVo.getSource(),
                baseVo.getAssign());

        return AjaxResponse.create(vo);
    }

    /**
     * 功能说明 : 保存角色
     *
     * @param role
     * @return
     * @author rutine
     * @time Jul 14, 2013 9:24:10 AM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody Role role) {

        logger.debug(JsonUtils.toJson(role));

        role.setCreateDate(new Date());
        role.setCreator(SessionUtil.getUserCode());
        roleService.save(role);

        return AjaxResponse.create("保存角色成功");
    }


    /**
     * 功能说明 : 修改角色
     *
     * @param role 角色对象
     * @return
     * @author rutine
     * @time Jul 14, 2013 9:39:46 AM
     */
    @PutMapping
    public AjaxResponse<String> update(@RequestBody Role role) {
        roleService.update(role);

        return AjaxResponse.create("修改角色成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<Role> get(@PathVariable long id) {
        Role role = roleService.get(id);

        return AjaxResponse.create(role);
    }

    /**
     * 功能说明 : 停用启用模块
     *
     * @param id
     * @param disEnableFlag 停用启用标志
     * @return
     * @author rutine
     * @time Jul 14, 2013 9:32:21 AM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        roleService.disEnable(id, disEnableFlag);

        return AjaxResponse.create("停用启用成功");
    }

    /**
     * 功能说明 : 为角色分配操作权限
     *
     * @param id             角色id
     * @param privilegeScope 权限范围
     * @param operationIds   模块id集合
     * @return json 数据
     * @author rutine
     * @time Sep 15, 2013 9:47:05 AM
     */
    @PostMapping("/{id}/operation-privilege/{privilegeScope}")
    public AjaxResponse<String> saveOptPrivilege(
            @PathVariable long id,
            @PathVariable String privilegeScope,
            @RequestBody Set<String> operationIds) {

        privilegeService.save(Lists.newArrayList(operationIds), id, PrivilegeType.OPT, OwnerType.ROLE, privilegeScope);

        return AjaxResponse.create("分配角色操作权限成功");
    }

    /**
     * 功能说明 : 为角色设置行权限
     *
     * @param id           角色id
     * @param rowPrivilege 行权限类型
     * @return json 数据
     * @author rutine
     * @time Sep 15, 2013 1:18:53 PM
     */
    @PostMapping("/{id}/row-privilege")
    public AjaxResponse saveRowPrivilege(
            @PathVariable long id,
            @RequestBody String rowPrivilege) {

        List<String> optIdList = Lists.newArrayList(rowPrivilege);
        privilegeService.save(optIdList, id, PrivilegeType.ROW, OwnerType.ROLE, rowPrivilege);

        return AjaxResponse.create("设置角色行权限成功");
    }

}

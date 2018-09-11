package com.mycuckoo.web.uum;


import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.RoleService;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.TreeVoExtend;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import com.mycuckoo.web.vo.res.RolePrivilegeVo;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.mycuckoo.common.constant.Common.*;
import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

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


    @GetMapping(value = "/list")
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
    @GetMapping(value = "/list/row/privilege")
    public AjaxResponse<RolePrivilegeVo> listRolePrivilege(@RequestParam long id) {
        AssignVo<TreeVoExtend> baseVo = privilegeService.findSelectAUnselectModOptByOwnIdAOwnType(id, OWNER_TYPE_ROL);

        RolePrivilegeVo vo = new RolePrivilegeVo(
                baseVo.getAssign(),
                baseVo.getUnassign(),
                baseVo.getPrivilegeScope(),
                privilegeService.findRowPrivilegeByRoleIdAPriType(id));

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
    @PutMapping(value = "/create")
    public AjaxResponse<String> putCreate(@RequestBody Role role) {

        logger.debug(JsonUtils.toJson(role));

        role.setCreateDate(new Date());
        role.setCreator(SessionUtil.getUserCode());
        roleService.save(role);

        return AjaxResponse.create("保存角色成功");
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
    @GetMapping(value = "/disEnable")
    public AjaxResponse<String> disEnable(
            @RequestParam long id,
            @RequestParam String disEnableFlag) {

        roleService.disEnable(id, disEnableFlag);

        return AjaxResponse.create("停用启用成功");
    }

    /**
     * 功能说明 : 修改角色
     *
     * @param role 角色对象
     * @return
     * @author rutine
     * @time Jul 14, 2013 9:39:46 AM
     */
    @PutMapping(value = "/update")
    public AjaxResponse<String> putUpdate(@RequestBody Role role) {
        roleService.update(role);

        return AjaxResponse.create("修改角色成功");
    }

    @GetMapping(value = "/view")
    public AjaxResponse<Role> getView(@RequestParam long id) {
        Role role = roleService.getByRoleId(id);

        return AjaxResponse.create(role);
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
    @GetMapping(value = "/save/operation/privilege")
    public AjaxResponse<String> saveOptPrivilege(
            @RequestParam long id,
            @RequestParam String privilegeScope,
            @RequestParam Set<String> operationIds) {

        List<String> list = new ArrayList<String>(operationIds.size());
        list.addAll(operationIds);
        privilegeService.save(list, id, PRIVILEGE_TYPE_OPT, OWNER_TYPE_ROL, privilegeScope);

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
    @GetMapping(value = "/save/row/privilege")
    public AjaxResponse saveRowPrivilege(
            @RequestParam long id,
            @RequestParam String rowPrivilege) {

        List<String> optIdList = Lists.newArrayList(rowPrivilege);
        privilegeService.save(optIdList, id, PRIVILEGE_TYPE_ROW, OWNER_TYPE_ROL, rowPrivilege);

        return AjaxResponse.create("设置角色行权限成功");
    }

}

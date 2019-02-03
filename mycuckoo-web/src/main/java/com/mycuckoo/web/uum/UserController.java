package com.mycuckoo.web.uum;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.OwnerType;
import com.mycuckoo.common.constant.PrivilegeType;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.common.utils.FirstLetter;
import com.mycuckoo.common.utils.SessionUtil;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.UserOrgRoleService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.CheckBoxTree;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.uum.RowPrivilegeVo;
import com.mycuckoo.vo.uum.UserRoleVo;
import com.mycuckoo.vo.uum.UserVo;
import com.mycuckoo.web.util.JsonUtils;
import com.mycuckoo.web.vo.AjaxResponse;
import com.mycuckoo.web.vo.req.UserPasswordUvo;
import com.mycuckoo.web.vo.req.UserPhotoUvo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mycuckoo.common.constant.Common.USER_DEFAULT_PWD;
import static com.mycuckoo.web.constant.ActionVariable.LIMIT;

/**
 * 功能说明: 用户管理Controller
 *
 * @author rutine
 * @version 3.0.0
 * @time Oct 19, 2014 10:04:42 AM
 */
@RestController
@RequestMapping("/uum/user/mgr")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserOrgRoleService userOrgRoleService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private PlatformServiceFacade platformServiceFacade;


    @GetMapping
    public AjaxResponse<Page<UserVo>> list(
            @RequestParam(value = "treeId", defaultValue = "-1") String treeId,
            @RequestParam(value = "userCode", defaultValue = "") String userCode,
            @RequestParam(value = "userName", defaultValue = "") String userName,
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = LIMIT + "") int pageSize) {

        userCode = StringUtils.isBlank(userCode) ? null : "%" + userCode + "%";
        userName = StringUtils.isBlank(userName) ? null : "%" + userName + "%";
        Page<UserVo> page = userService.findByPage(treeId, userName,
                userCode, new PageRequest(pageNo - 1, pageSize));

        return AjaxResponse.create(page);
    }

    /**
     * 功能说明 : 用户分配特殊操作权限
     *
     * @param id 用户id
     * @author rutine
     * @time Oct 13, 2013 1:05:30 PM
     */
    @GetMapping("/{id}/user-privilege")
    public AjaxResponse<AssignVo<CheckBoxTree, Long>> listUserPrivilege(@PathVariable long id) {
        AssignVo<CheckBoxTree, Long> vo = privilegeService.findModOptByOwnIdAOwnTypeWithCheck(id, OwnerType.USR);

        return AjaxResponse.create(vo);
    }

    /**
     * 功能说明 : 查询用户的所有角色
     *
     * @param id 用户id
     * @author rutine
     * @time Oct 13, 2013 4:53:00 PM
     */
    @GetMapping("/{id}/role-privilege")
    public AjaxResponse<Map<String, Object>> listRolePrivilege(@PathVariable long id) {
        List<UserRoleVo> userRoles = userOrgRoleService.findByUserId(id);
        List<? extends SimpleTree> orgRoles = this.getChildNodes(0L, "Y").getData();

        Map<String, Object> map = Maps.newHashMap();
        map.put("userRoles", userRoles);
        map.put("orgRoles", orgRoles);

        return AjaxResponse.create(map);
    }

    /**
     * 功能说明 : 根据用户ID 已分配行权限
     *
     * @param id
     * @return
     * @author rutine
     * @time Oct 20, 2013 3:03:59 PM
     */
    @GetMapping("/{id}/row-privilege")
    public AjaxResponse<RowPrivilegeVo> listRowPrivilege(@PathVariable long id) {
        RowPrivilegeVo vo = privilegeService.findRowPrivilegeByUserId(id);

        return AjaxResponse.create(vo);
    }

    /**
     * 功能说明 : 保存用户
     *
     * @param user
     * @author rutine
     * @time Oct 6, 2013 8:26:57 PM
     */
    @PostMapping
    public AjaxResponse<String> create(@RequestBody UserVo user) {
        logger.debug(JsonUtils.toJson(user));

        Assert.hasText(user.getUserCode(), "用户编码必填");
        Assert.isTrue(user.getUserCode().length() <= 10
                        || StringUtils.isAlphanumeric(user.getUserCode()),
                "用户编码长度最大10的字符或数字");
        Assert.isTrue(StringUtils.isNumeric(user.getUserMobile()), "必须有效电话号");
        Assert.isTrue(StringUtils.isNumeric(user.getUserMobile2()), "必须有效电话号");
        Assert.isTrue(StringUtils.isNumeric(user.getUserFamilyTel()), "必须有效电话号");
        Assert.isTrue(StringUtils.isNumeric(user.getUserOfficeTel()), "必须有效电话号");
        Assert.notNull(user.getUserAvidate(), "用户有效期不能为空");
        Assert.notNull(user.getOrgRoleId(), "角色不能为空");
        Assert.hasText(user.getRoleName(), "角色不能为空");


        user.setCreator(SessionUtil.getUserCode());
        user.setCreateDate(new Date());
        user.setUserNamePy(CommonUtils.getFirstLetters(user.getUserName()));

        userService.save(user);

        return AjaxResponse.create("保存用户成功");
    }

    @PutMapping
    public AjaxResponse<String> update(@RequestBody UserVo user) {
        user.setUserNamePy(FirstLetter.getFirstLetters(user.getUserName()));
        userService.update(user);

        return AjaxResponse.create("修改用户成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<User> get(@PathVariable long id) {
        User user = userService.getUserByUserId(id);

        return AjaxResponse.create(user);
    }

    /**
     * 功能说明 : 停用启用用户
     *
     * @param id
     * @param disEnableFlag 停用启用标志
     * @author rutine
     * @time Oct 6, 2013 9:16:06 PM
     */
    @PutMapping("/{id}/disEnable/{disEnableFlag}")
    public AjaxResponse<String> disEnable(
            @PathVariable long id,
            @PathVariable String disEnableFlag) {

        userService.disEnable(id, disEnableFlag);

        return AjaxResponse.create("停用启用成功");
    }

    /**
     * 功能说明 : 根据用户名模糊查询
     *
     * @param userName
     * @author rutine
     * @time Oct 20, 2013 3:07:34 PM
     */
    @GetMapping("/query/users")
    public AjaxResponse<List<UserVo>> queryUsersByUserName(@RequestParam(defaultValue = "") String userName) {
        List<UserVo> vos = userService.findByUserName(userName);

        return AjaxResponse.create(vos);
    }

    /**
     * 功能说明 : 查找指定节点的下一级子节点
     *
     * @param orgId         机构id
     * @param isCheckbox
     * @author rutine
     * @time Dec 1, 2012 1:45:37 PM
     */
    @GetMapping("/{orgId}/child/nodes")
    public AjaxResponse<List<? extends SimpleTree>> getChildNodes(
            @PathVariable Long orgId,
            @RequestParam(value = "isCheckbox", defaultValue = "N") String isCheckbox) {

        List<? extends SimpleTree> asyncTreeList = userService.findChildNodes(orgId, isCheckbox);

        logger.debug("json --> {}", JsonUtils.toJson(asyncTreeList));

        return AjaxResponse.create(asyncTreeList);
    }

    /**
     * 功能说明 : 保存为用户分配的模块操作权限
     *
     * @param id             用户id
     * @param privilegeScope 权限范围
     * @param operationIds   模块id集合
     * @return json消息
     * @author rutine
     * @time Oct 13, 2013 2:13:42 PM
     */
    @PostMapping("/{id}/operation-privilege/{privilegeScope}")
    public AjaxResponse<String> saveOperationPrivilege(@PathVariable long id,
                                                       @PathVariable String privilegeScope,
                                                       @RequestBody Set<String> operationIds) {

        List<String> list = Lists.newArrayList(operationIds);
        privilegeService.save(list, id, PrivilegeType.OPT, OwnerType.USR, privilegeScope);

        return AjaxResponse.create("为用户分配操作权限成功");
    }

    /**
     * 功能说明 : 保存为用户分配的角色
     *
     * @param id            用户id
     * @param defaultRoleId 默认角色id
     * @param roleIds       角色id集合
     * @author rutine
     * @time Oct 13, 2013 9:17:05 PM
     */
    @PostMapping("/{id}/role-privilege/{defaultRoleId}")
    public AjaxResponse<String> saveRolePrivilege(
            @PathVariable long id,
            @PathVariable long defaultRoleId,
            @RequestBody Set<Long> roleIds) {

        List<Long> list = new ArrayList<>(roleIds);
        userOrgRoleService.save(id, list, defaultRoleId);

        return AjaxResponse.create("为用户分配角色成功");
    }

    /**
     * 功能说明 : 保存为用户分配的行权限
     *
     * @param id
     * @param privilegeScope
     * @param rowIds
     * @author rutine
     * @time Oct 20, 2013 4:25:02 PM
     */
    @PostMapping("/{id}/row-privilege/{privilegeScope}")
    public AjaxResponse<String> saveRowPrivilege(
            @PathVariable long id,
            @PathVariable String privilegeScope,
            @RequestBody Set<String> rowIds) {

        List<String> list = new ArrayList<>(rowIds);
        privilegeService.save(list, id, PrivilegeType.ROW, OwnerType.USR, privilegeScope);

        return AjaxResponse.create("为用户分配行权限成功");
    }

    /**
     * 功能说明 : 重置用户密码
     *
     * @param id
     * @author rutine
     * @time Oct 20, 2013 4:48:55 PM
     */
    @PutMapping("/{id}/reset-password")
    public AjaxResponse<String> resetPwd(
            @PathVariable long id,
            @RequestBody String userName) {

        // 系统参数用户默认密码
        String userDefaultPwd = platformServiceFacade.findSystemParaByParaName(USER_DEFAULT_PWD);
        userService.resetPwdByUserId(userDefaultPwd, userName, id);

        return AjaxResponse.create("重置密码成功");
    }


    /**
     * 功能说明 : 修改用户密码
     *
     * @param vo
     * @author rutine
     * @time Nov 6, 2014 9:13:20 PM
     */
    @PutMapping("/update/password")
    public AjaxResponse<String> updatePassword(@RequestBody UserPasswordUvo vo) {
        Assert.state(vo.getNewPassword().equals(vo.getConfirmPassword()), "两次输入的新密码不一致");

        String password = vo.getPassword();
        User user = userService.getUserByUserId(SessionUtil.getUserId());
        Assert.state(password.equals(user.getUserPassword()), "密码错误");

        user.setUserPassword(vo.getNewPassword());
        userService.updateUserInfo(user);

        return AjaxResponse.create("修改成功");
    }

    /**
     * 功能说明 : 上传头像
     *
     * @return
     * @author rutine
     * @time Nov 1, 2014 8:28:55 AM
     */
    @PutMapping("/update/photo")
    public AjaxResponse<String> updatePhoto(@RequestBody UserPhotoUvo vo, HttpServletRequest request) {
        Assert.state(StringUtils.startsWith(vo.getPhoto(), "http://"), "无效头像地址");

        userService.updateUserPhotoUrl(vo.getPhoto(), SessionUtil.getUserId());
        SessionUtil.getUserInfo().setUserPhotoUrl(vo.getPhoto());

        return AjaxResponse.create("上传头像成功");
    }
}

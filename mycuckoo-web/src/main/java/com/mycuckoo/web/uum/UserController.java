package com.mycuckoo.web.uum;

import com.google.common.collect.Lists;
import com.mycuckoo.constant.enums.OwnerType;
import com.mycuckoo.constant.enums.PrivilegeType;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import com.mycuckoo.service.facade.PlatformServiceFacade;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.utils.CommonUtils;
import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.vo.AssignVo;
import com.mycuckoo.vo.CheckboxTree;
import com.mycuckoo.vo.uum.RowPrivilegeVo;
import com.mycuckoo.vo.uum.UserVo;
import com.mycuckoo.vo.uum.UserVos;
import com.mycuckoo.web.vo.AjaxResponse;
import com.mycuckoo.web.vo.req.UserPasswordUvo;
import com.mycuckoo.web.vo.req.UserPhotoUvo;
import com.mycuckoo.web.vo.req.UserReqVos;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.mycuckoo.constant.BaseConst.USER_DEFAULT_PWD;
import static com.mycuckoo.web.constant.ActionConst.LIMIT;

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
    private PrivilegeService privilegeService;
    @Autowired
    private PlatformServiceFacade platformServiceFacade;


    @GetMapping
    public AjaxResponse<Page<UserVo>> list(
           @RequestParam(defaultValue = "") String code,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = LIMIT + "") int pageSize) {

        code = StringUtils.isBlank(code) ? null : "%" + code + "%";
        name = StringUtils.isBlank(name) ? null : "%" + name + "%";
        Page<UserVo> page = userService.findByPage(code, name, new PageRequest(pageNo - 1, pageSize));

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
    public AjaxResponse<AssignVo<CheckboxTree, String>> listUserPrivilege(@PathVariable long id) {
        //todo
        AssignVo<CheckboxTree, String> vo = privilegeService.findModOptByOwnIdAOwnTypeWithCheck(id, OwnerType.USR);
//        AssignVo<CheckboxTree, String> vo = privilegeService.findModResByOwnIdAOwnTypeWithCheck(id, OwnerType.USR);

        return AjaxResponse.create(vo);
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
        Assert.hasText(user.getCode(), "用户编码必填");
        Assert.state(user.getCode().length() <= 10
                        || StringUtils.isAlphanumeric(user.getCode()),
                "用户编码长度最大10的字符或数字");
        Assert.state(StringUtils.isNumeric(user.getPhone()), "必须有效电话号");
        Assert.state(StringUtils.isNumeric(user.getFamilyTel()), "必须有效电话号");
        Assert.state(StringUtils.isNumeric(user.getOfficeTel()), "必须有效电话号");
        Assert.notNull(user.getAvidate(), "用户有效期不能为空");

        userService.save(user);

        return AjaxResponse.create("保存用户成功");
    }

    @PutMapping
    public AjaxResponse<String> update(@RequestBody UserVo user) {
        userService.update(user);

        return AjaxResponse.create("修改用户成功");
    }

    @GetMapping("/{id}")
    public AjaxResponse<User> get(@PathVariable long id) {
        User user = userService.getByUserId(id);

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
    @GetMapping("/selector")
    public AjaxResponse<List<UserVos.UProfile>> listByName(@RequestParam(defaultValue = "") String userName) {
        List<UserVos.UProfile> vos = userService.findByName(userName);

        return AjaxResponse.create(vos);
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
    @PostMapping("/{id}/opt-privilege/{privilegeScope}")
    public AjaxResponse<String> saveOptPrivilege(@PathVariable long id,
                                                 @PathVariable String privilegeScope,
                                                 @RequestBody Set<String> operationIds) {

        List<String> list = Lists.newArrayList(operationIds);
        privilegeService.save(list, id, PrivilegeType.OPT, OwnerType.USR, privilegeScope);

        return AjaxResponse.create("为用户分配操作权限成功");
    }

    /**
     * 功能说明 : 保存为用户分配资源权限
     *
     * @param id             用户id
     * @param privilegeScope 权限范围
     * @param resIds         资源id集合
     * @return json消息
     * @author rutine
     * @time May 17 2024 18:24:15 PM
     */
    @PostMapping("/{id}/res-privilege/{privilegeScope}")
    public AjaxResponse<String> saveResPrivilege(@PathVariable long id,
                                                 @PathVariable String privilegeScope,
                                                 @RequestBody Set<String> resIds) {

        List<String> list = Lists.newArrayList(resIds);
        privilegeService.save(list, id, PrivilegeType.RES, OwnerType.USR, privilegeScope);

        return AjaxResponse.create("为用户分配资源权限成功");
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
        String userDefaultPwd = platformServiceFacade.findSystemParaByKey(USER_DEFAULT_PWD);
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
        userService.updateUserInfo(password, vo.getNewPassword());

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
        SessionUtil.getUserInfo().setPhotoUrl(vo.getPhoto());

        return AjaxResponse.create("上传头像成功");
    }

    /**
     * 功能说明 : 更新用户角色
     */
    @PutMapping("/update/role")
    public AjaxResponse<String> updateRole(@RequestBody UserReqVos.URole vo) {
        Assert.notNull(vo.getUserId(), "用户id不能为空");
        Assert.notNull(vo.getRoleId(), "角色id不能为空");

        userService.updateRole(vo.getUserId(), vo.getRoleId());

        return AjaxResponse.create("设置角色成功");
    }
}

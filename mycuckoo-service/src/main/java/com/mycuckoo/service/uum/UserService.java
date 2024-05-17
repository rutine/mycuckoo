package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.constant.enums.OwnerType;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserOrgRoleRef;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.operator.LogOperator;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.UserMapper;
import com.mycuckoo.utils.CommonUtils;
import com.mycuckoo.utils.SystemConfigXmlParse;
import com.mycuckoo.vo.CheckBoxTree;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.uum.UserRoleVo;
import com.mycuckoo.vo.uum.UserVo;
import com.mycuckoo.vo.uum.UserVos;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.mycuckoo.constant.ServiceConst.*;

/**
 * 功能说明: 用户业务类
 *
 * @author rutine
 * @version 4.0.0
 * @time Sep 25, 2014 11:37:54 AM
 */
@Service
@Transactional(readOnly = true)
public class UserService {
    static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserOrgRoleService userOrgRoleService;
    @Autowired
    private OrganService organService;
    @Autowired
    private OrganRoleService organRoleService;
    @Autowired
    private PrivilegeService privilegeService;


    /**
     * 停用/启用用户 删除用户所属角色并移到无角色用户 删除用户所拥有操作权限
     *
     * @param userId        用户ID
     * @param disEnableFlag 停用启用标志
     * @return boolean true成功
     * @throws ApplicationException
     * @author rutine
     * @time Oct 20, 2012 3:50:29 PM
     * <p>
     * 1 移除用户角色
     * 2 移除用户操作行权限
     */
    @Transactional
    public boolean disEnable(long userId, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        if (!enable) {
            User user = new User(userId, DISABLE);
            user.setBelongOrg(0L);
            userMapper.update(user); // 更改用户所属机构为0
            userOrgRoleService.deleteByUserId(userId); // 1 移除用户角色
            privilegeService.deleteByOwnerIdAndOwnerType(userId, OwnerType.USR.value()); // 2 移除用户所拥有操作、行权限

            OrgRoleRef orgRoleRef = new OrgRoleRef(0L);
            UserOrgRoleRef userOrgRoleRef = new UserOrgRoleRef(null, orgRoleRef, user);
            userOrgRoleRef.setIsDefault(Y);
            userOrgRoleService.save(userOrgRoleRef); // 设置无角色用户
        } else {
            userMapper.update(new User(userId, ENABLE));
        }

        User user = userMapper.get(userId);
        writeLog(user, LogLevel.SECOND, enable ? OptName.ENABLE : OptName.DISABLE);
        return true;
    }

    public List<UserVos.UProfile> findByName(String userName) {
        userName = "%" + userName + "%";
        List<User> list = userMapper.findByCodeAndName(null, userName);

        List<UserVos.UProfile> vos = Lists.newArrayList();
        list.forEach(entity -> {
            UserVos.UProfile vo = new UserVos.UProfile();
            vo.setUserId(entity.getUserId());
            vo.setCode(entity.getCode());
            vo.setName(entity.getName());
            vos.add(vo);
        });

        return vos;
    }

    public List findByPinyin(String userNamePy, long userId) {
        return userMapper.findByPinyin(userNamePy, userId);
    }

    public List<? extends SimpleTree> findChildNodes(Long organId, String isCheckbox) {
        List<? extends SimpleTree> simpleTrees = organService.findChildNodes(organId, isCheckbox);

        return this.buildTree(simpleTrees, isCheckbox);
    }
    private List<? extends CheckBoxTree> buildTree(List<? extends SimpleTree> simpleTrees, String isCheckbox) {
        if (simpleTrees == null) {
            return null;
        }

        List<CheckBoxTree> boxTrees = Lists.newArrayList();
        for (SimpleTree tree : simpleTrees) {
            String parentId = "orgId_" + tree.getParentId();
            CheckBoxTree boxTree = new CheckBoxTree();
            boxTree.setId("orgId_" + tree.getId());
            boxTree.setParentId(parentId);
            boxTree.setText(tree.getText());
            boxTree.setIcon(tree.getIcon());
            boxTree.setIconSkin(tree.getIconSkin());
            boxTree.setIsLeaf(false);
            boxTree.setCheckBox(null); // 机构无checkbox
            boxTree.setNocheck(true); // 机构无checkbox
            boxTree.setChildren(this.buildTree(tree.getChildren(), isCheckbox));
            boxTrees.add(boxTree);

            List<OrgRoleRef> orgRoleList = organRoleService.findRolesByOrgId(Long.valueOf(tree.getId()));
            for (OrgRoleRef orgRoleRef : orgRoleList) {
                Role role = orgRoleRef.getRole();
                CheckBoxTree roleBoxTree = new CheckBoxTree();
                roleBoxTree.setId("orgRoleId_" + orgRoleRef.getOrgRoleId());
                roleBoxTree.setParentId(parentId);
                roleBoxTree.setText(role.getName());
                roleBoxTree.setIsLeaf(true);
                if (!Y.equals(isCheckbox)) {// 角色无checkbox
                    roleBoxTree.setCheckBox(null);
                    roleBoxTree.setNocheck(true);
                    roleBoxTree.setIconSkin(ROLE_CSS);
                } else {
                    roleBoxTree.setCheckBox(new CheckBoxTree.CheckBox(0));
                }
                boxTrees.add(roleBoxTree);
            }
        }

        return boxTrees;
    }

    public Page<UserVo> findByPage(String treeId, String code, String name, Pageable page) {
        Long orgRoleId = null;
        String flag = null;
        if (!CommonUtils.isNullOrEmpty(treeId) && treeId.indexOf("_") != -1) {
            int index = treeId.indexOf("_");
            orgRoleId = Long.parseLong(treeId.substring(index + 1));
            flag = treeId.substring(0, index);
        }

        Page<User> page2 = null;
        if (flag == null) { // 根据用户代码和用户名称模糊、分页查询用户记录
            page2 = userMapper.findByPage2(null, null, code, name, page);
        } else if (StringUtils.equals(flag, "orgId")) { // 分页查询属于机构ids的用户记录
            List<Long> orgIds = organService.findChildIds(orgRoleId, 0);
            Long[] arr = orgIds.isEmpty() ? null : orgIds.toArray(new Long[]{});
            page2 = userMapper.findByPage2(null, arr, code, name, page);
        } else { // 分页查询属于某个角色id的用户记录
            page2 = userMapper.findByPage2(orgRoleId, null, code, name, page);
        }


        List<UserVo> vos = Lists.newArrayList();
        page2.forEach(entity -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        });

        return new PageImpl<>(vos, page, page2.getTotalElements());
    }

    public UserVo getUserByUserCodeAndPwd(String userCode, String password) {
        if (CommonUtils.isNullOrEmpty(userCode) || CommonUtils.isNullOrEmpty(userCode)) return null;

        User user = userMapper.getByUserCode(userCode);
        if (user == null) {
            throw new ApplicationException("用户不存在错误!");
        }
        if (!user.getPassword().equals(password)) {
            throw new ApplicationException("用户密码不正确!");
        }

        UserVo vo = new UserVo();
        BeanUtils.copyProperties(user, vo);

        return vo;
    }

    public UserVo getUserByUserId(long userId) {
        User user = userMapper.get(userId);
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(user, vo);
        vo.setPassword(CommonUtils.decrypt(user.getPassword()));

        List<UserRoleVo> roleUserVos = userOrgRoleService.findByUserId(user.getUserId());
        for (UserRoleVo refVo : roleUserVos) {
            if (Y.equals(refVo.getIsDefault())) {
                vo.setOrgRoleId(refVo.getOrgRoleId());
                vo.setOrgName(refVo.getOrgName());
                vo.setRoleName(refVo.getRoleName());
                break;
            }
        }

        return vo;
    }

    public List<User> findByUserIds(Long[] userIds) {
        if (userIds == null || userIds.length == 0) return null;

        return userMapper.findByUserIds(userIds);
    }

    public Page<UserVo> findUsersForSetAdmin(String userName, String userCode, Pageable page) {
        Page<User> page2 = userMapper.findByPage2(null, null, userCode, userName, page);
        List<UserVo> vos = Lists.newArrayList();
        List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
        int count = 0;
        for (User user : page2.getContent()) {
            if (!systemAdminCode.contains(user.getCode())) {
                count++;
                UserVo vo = new UserVo();
                BeanUtils.copyProperties(user, vo);
                vos.add(vo);
            }
        }

        return new PageImpl<>(vos, page, page2.getTotalElements() - count);
    }

    public Page<UserVo> findAdminUsers() {
        List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
        List<UserVo> vos = new ArrayList<UserVo>();
        for (String userCode : systemAdminCode) {
            try {
                User entity = userMapper.getByUserCode(userCode);
                UserVo vo = new UserVo();
                BeanUtils.copyProperties(entity, vo);
                vos.add(vo);
            } catch (Exception e) {
                logger.warn("用户编码'{}'存在重复!", userCode);
            }
        }

        return new PageImpl<>(vos);
    }

    public boolean existsByUserCode(String userCode) {
        return userMapper.existsByUserCode(userCode);
    }

    @Transactional
    public void update(UserVo user) {
        user.setCode(null); //用户号不更改

        // 设置用户所属机构
        OrgRoleRef orgRoleRef = organRoleService.get(user.getOrgRoleId() == null ? 0 : user.getOrgRoleId());
        user.setBelongOrg(orgRoleRef.getOrgan().getOrgId());

        user.setPassword(CommonUtils.encrypt(user.getPassword())); // 加密
        userMapper.update(user); // 保存用户

        writeLog(user, LogLevel.SECOND, OptName.MODIFY);
    }

    public void updateBelongOrgIdForAssignRole(long organId, long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setBelongOrg(organId);
        userMapper.update(user);
    }

    @Transactional
    public void updateUserInfo(User user) {
        User user2 = new User();
        user2.setUserId(user.getUserId());
        user2.setCode(user.getCode());
        user2.setName(user.getName());
        user2.setPassword(CommonUtils.encrypt(user.getPassword()));
        userMapper.update(user2); // 保存用户
    }

    @Transactional
    public void updateUserPhotoUrl(String photoUrl, long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setPhotoUrl(photoUrl);
        userMapper.update(user);
    }

    @Transactional
    public void resetPwdByUserId(String userDefaultPwd, String userName, long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(CommonUtils.encrypt(userDefaultPwd));
        userMapper.update(user);

        LogOperator.begin()
                .module(ModuleName.ROLE_MGR)
                .operate(OptName.RESET_PWD)
                .id(userId)
                .title(null)
                .content("重置密码用户：%s", userName)
                .level(LogLevel.SECOND)
                .emit();
    }

    @Transactional
    public void save(UserVo user) {
        Assert.state(!existsByUserCode(user.getCode()), "用户编码[" + user.getCode() + "]已存在!");

        // 设置用户所属机构
        OrgRoleRef orgRoleRef = organRoleService.get(user.getOrgRoleId());
        user.setBelongOrg(orgRoleRef.getOrgan().getOrgId());
        user.setPassword(CommonUtils.encrypt(user.getPassword())); // 加密
        user.setStatus(ENABLE);
        userMapper.save(user);

        UserOrgRoleRef roleUserRef = new UserOrgRoleRef(null, orgRoleRef, user); // 默认角色
        roleUserRef.setIsDefault(Y);
        userOrgRoleService.save(roleUserRef); // 保存用户的默认角色

        writeLog(user, LogLevel.FIRST, OptName.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     *
     * @param entity
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 20, 2012 4:17:57 PM
     */
    private void writeLog(User entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.USER_MGR)
                .operate(opt)
                .id(entity.getUserId())
                .title(null)
                .content("用户编码：%s, 用户名称: %s, 所属机构: %s",
                        entity.getCode(), entity.getName(), entity.getBelongOrg())
                .level(logLevel)
                .emit();
    }

}

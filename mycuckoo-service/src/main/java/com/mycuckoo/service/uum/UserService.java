package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.constant.OwnerType;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserOrgRoleRef;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageImpl;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.UserMapper;
import com.mycuckoo.service.platform.SystemOptLogService;
import com.mycuckoo.vo.SimpleTree;
import com.mycuckoo.vo.CheckBoxTree;
import com.mycuckoo.vo.uum.UserRoleVo;
import com.mycuckoo.vo.uum.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

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
    @Autowired
    private SystemOptLogService sysOptLogService;


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
    public boolean disEnable(long userId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
            User user = new User(userId, DISABLE);
            user.setUserBelongtoOrg(0L);
            userMapper.update(user); // 更改用户所属机构为0
            userOrgRoleService.deleteByUserId(userId); // 1 移除用户角色
            privilegeService.deleteByOwnerIdAndOwnerType(userId, OwnerType.USR.value()); // 2 移除用户所拥有操作、行权限

            OrgRoleRef orgRoleRef = new OrgRoleRef(0L);
            UserOrgRoleRef userOrgRoleRef = new UserOrgRoleRef(null, orgRoleRef, user);
            userOrgRoleRef.setIsDefault(Y);
            userOrgRoleService.save(userOrgRoleRef); // 设置无角色用户

            user = userMapper.get(userId);
            writeLog(user, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
            return true;
        } else {
            User user = new User(userId, ENABLE);
            userMapper.update(user);

            user = userMapper.get(userId);
            writeLog(user, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
            return true;
        }
    }

    public List<UserVo> findByUserName(String userName) {
        userName = "%" + userName + "%";
        List<User> list = userMapper.findByCodeAndName(null, userName);

        List<UserVo> vos = Lists.newArrayList();
        list.forEach(entity -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        });

        return vos;
    }

    public List findByUserNamePy(String userNamePy, long userId) {
        return userMapper.findByUserNamePy(userNamePy, userId);
    }

    public List<? extends SimpleTree> findNextLevelChildNodes(String treeId, String isCheckbox) {
        int index = treeId.indexOf("_");
        int orgId = 0;
        if (index != -1) {
            orgId = Integer.parseInt(treeId.substring(index + 1));
        }

        List<CheckBoxTree> vos = Lists.newArrayList();
        List<CheckBoxTree> orgList = organService.findNextLevelChildNodesWithCheckbox(orgId, 0);
        for (SimpleTree treeVo : orgList) {
            CheckBoxTree treeVoExt = new CheckBoxTree();
            BeanUtils.copyProperties(treeVo, treeVoExt);
            // 机构无checkbox
            treeVoExt.setNocheck(true);
            treeVoExt.setId("orgId_" + treeVoExt.getId());
            vos.add(treeVoExt);
        }

        List<OrgRoleRef> orgRoleList = organRoleService.findRolesByOrgId(orgId);
        for (OrgRoleRef orgRoleRef : orgRoleList) {
            Role role = orgRoleRef.getRole();
            CheckBoxTree treeVoExt = new CheckBoxTree();
            if (!Y.equals(isCheckbox)) {
                // 角色无checkbox
                treeVoExt.setNocheck(true);
                treeVoExt.setIconSkin(ROLE_CSS);
            }
            treeVoExt.setId("orgRoleId_" + orgRoleRef.getOrgRoleId());
            treeVoExt.setText(role.getRoleName());
            treeVoExt.setLeaf(true);
            vos.add(treeVoExt);
        }

        return vos;
    }

    public Page<UserVo> findByPage(String treeId, String userName, String userCode, Pageable page) {
        Long orgRoleId = null;
        String flag = null;
        if (!CommonUtils.isNullOrEmpty(treeId) && treeId.indexOf("_") != -1) {
            int index = treeId.indexOf("_");
            orgRoleId = Long.parseLong(treeId.substring(index + 1));
            flag = treeId.substring(0, index);
        }

        Page<User> page2 = null;
        if (flag == null) { // 根据用户代码和用户名称模糊、分页查询用户记录
            page2 = userMapper.findByPage2(null, null, userCode, userName, page);
        } else if (StringUtils.equals(flag, "orgId")) { // 分页查询属于机构ids的用户记录
            List<Long> orgIds = organService.findChildIds(orgRoleId, 0);
            Long[] arr = orgIds.isEmpty() ? null : orgIds.toArray(new Long[]{});
            page2 = userMapper.findByPage2(null, arr, userCode, userName, page);
        } else { // 分页查询属于某个角色id的用户记录
            page2 = userMapper.findByPage2(orgRoleId, null, userCode, userName, page);
        }


        List<UserVo> vos = Lists.newArrayList();
        page2.forEach(entity -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        });

        return new PageImpl<>(vos, page, page2.getTotalElements());
    }

    public List<UserVo> findByOrgId(long organId) {
        List<User> list = userMapper.findByOrgId(organId);
        List<UserVo> vos = Lists.newArrayList();
        list.forEach(entity -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(entity, vo);
            vos.add(vo);
        });

        return vos;
    }

    public UserVo getUserByUserCodeAndPwd(String userCode, String password) {
        if (CommonUtils.isNullOrEmpty(userCode) || CommonUtils.isNullOrEmpty(userCode)) return null;

        User user = userMapper.getByUserCode(userCode);
        if (user == null) {
            throw new ApplicationException("用户不存在错误!");
        }
        if (!user.getUserPassword().equals(password)) {
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

        List<UserRoleVo> roleUserVos = userOrgRoleService.findByUserId(user.getUserId());
        for (UserRoleVo refVo : roleUserVos) {
            if (Y.equals(refVo.getIsDefault())) {
                long orgRoleId = refVo.getOrganRoleId();
                String roleName = refVo.getRoleName();

                vo.setOrgRoleId(orgRoleId);
                vo.setRoleName(roleName);
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
//        Map<String, Object> map = new WeakHashMap<String, Object>();
//        Page<User> page2 = userMapper.findByPage2(null, null, userCode, userName, page);
//        List<User> userList = page2.getContent();
//        int count = (int) page2.getTotalElements();
//        List<User> systemAdminUserList = Lists.newArrayList();
//        List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
//        int systemAdminUserCount = 0;
//        for (User user : userList) {
//            for (String adminCode : systemAdminCode) {
//                if (user.getUserCode().equals(adminCode)) {
//                    systemAdminUserList.add(user);
//                    systemAdminUserCount++;
//                }
//            }
//        }
//        userList.removeAll(systemAdminUserList);
//        count = count - systemAdminUserCount;
//        // 可分配为管理员的用户
//        map.put("userList", userList);
//        map.put("userCount", count);
//
//        // 已分配为管理员的用户
//        map.put("systemAdminUserList", systemAdminUserList);
//        map.put("systemAdminUserCount", systemAdminUserCount);
//
//        return map;

        Page<User> page2 = userMapper.findByPage2(null, null, userCode, userName, page);
        List<UserVo> vos = Lists.newArrayList();
        List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
        int count = 0;
        for (User user : page2.getContent()) {
            if (!systemAdminCode.contains(user.getUserCode())) {
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

    public void update(UserVo user) {
        // 设置用户所属机构
        OrgRoleRef orgRoleRef = organRoleService.get(user.getOrgRoleId() == null ? 0 : user.getOrgRoleId());
        user.setUserBelongtoOrg(orgRoleRef.getOrgan().getOrgId());

        user.setUserPassword(CommonUtils.encrypt(user.getUserPassword())); // 加密
        userMapper.update(user); // 保存用户

        writeLog(user, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
    }

    public void updateBelongOrgIdForAssignRole(long organId, long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserBelongtoOrg(organId);
        userMapper.update(user);
    }

    public void updateUserInfo(User user) {
        User user2 = new User();
        user2.setUserId(user.getUserId());
        user2.setUserCode(user.getUserCode());
        user2.setUserName(user.getUserName());
        user2.setUserPassword(user.getUserPassword());
        userMapper.update(user2); // 保存用户
    }

    @Transactional
    public void updateUserPhotoUrl(String photoUrl, long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserPhotoUrl(photoUrl);
        userMapper.update(user);
    }

    @Transactional
    public void resetPwdByUserId(String userDefaultPwd, String userName, long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setUserPassword(CommonUtils.encrypt(userDefaultPwd));
        userMapper.update(user);

        String optContent = "重置密码用户：" + userName;
        sysOptLogService.saveLog(LogLevelEnum.SECOND, OptNameEnum.RESET_PWD, USER_MGR, optContent, userId + "");
    }

    public void save(UserVo user) {
        // 设置用户所属机构
        OrgRoleRef orgRoleRef = organRoleService.get(user.getOrgRoleId());
        user.setUserBelongtoOrg(orgRoleRef.getOrgan().getOrgId());
        user.setUserPassword(CommonUtils.encrypt(user.getUserPassword())); // 加密
        userMapper.save(user);

        UserOrgRoleRef roleUserRef = new UserOrgRoleRef(null, orgRoleRef, user); // 默认角色
        roleUserRef.setIsDefault(Y);
        userOrgRoleService.save(roleUserRef); // 保存用户的默认角色

        writeLog(user, LogLevelEnum.FIRST, OptNameEnum.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     *
     * @param user
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 20, 2012 4:17:57 PM
     */
    private void writeLog(User user, LogLevelEnum logLevel, OptNameEnum opt) {
        StringBuilder optContent = new StringBuilder();
        optContent.append("用户编码：").append(user.getUserCode()).append(SPLIT);
        optContent.append("用户名称: ").append(user.getUserName()).append(SPLIT);
        optContent.append("所属机构: ").append(user.getUserBelongtoOrg()).append(SPLIT);

        sysOptLogService.saveLog(logLevel, opt, USER_MGR, optContent.toString(), user.getUserId() + "");
    }

}

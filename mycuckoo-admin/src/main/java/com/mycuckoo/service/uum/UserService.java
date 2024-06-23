package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.constant.enums.OwnerType;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.CommonUtils;
import com.mycuckoo.core.util.web.SessionUtil;
import com.mycuckoo.domain.uum.Department;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserExtend;
import com.mycuckoo.repository.uum.UserMapper;
import com.mycuckoo.web.vo.res.uum.UserVo;
import com.mycuckoo.web.vo.res.uum.UserVos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.mycuckoo.constant.AdminConst.DISABLE;
import static com.mycuckoo.constant.AdminConst.ENABLE;

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
    private DepartmentService departmentService;
    @Autowired
    private RoleService roleService;
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
            // 移除用户所拥有操作、行权限
            privilegeService.deleteByOwnerIdAndOwnerType(userId, OwnerType.USR.value());
        } else {
            userMapper.update(new User(userId, ENABLE));
        }

        User user = userMapper.get(userId);
        writeLog(user, LogLevel.SECOND, enable ? OptName.ENABLE : OptName.DISABLE);
        return true;
    }

    public List<UserVos.Profile> findByName(String userName) {
        userName = "%" + userName + "%";
        List<User> list = userMapper.findByName(userName);

        List<UserVos.Profile> vos = Lists.newArrayList();
        list.forEach(entity -> {
            UserVos.Profile vo = new UserVos.Profile();
            vo.setUserId(entity.getUserId());
            vo.setName(entity.getName());
            vos.add(vo);
        });

        return vos;
    }

    public List findByPinyin(String userNamePy, long userId) {
        return userMapper.findByPinyin(userNamePy, userId);
    }

    public Page<User> findByPage(Querier querier) {
        Page<User> page2 = userMapper.findByPage(querier.getQ(), querier);;

        return page2;
    }

    public List<UserExtend> findByAccountId(Long accountId) {
        return userMapper.findByAccountId(accountId);
    }

    public UserExtend getByAccountIdAndUserId(Long accountId, Long userId) {
        if (accountId == null || userId == null) return null;

        return userMapper.getByAccountIdAndUserId(accountId, userId);
    }

    public UserVo getByUserId(long userId) {
        User user = userMapper.get(userId);
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(user, vo);

        if (user.getRoleId() != null) {
            Role role = roleService.get(user.getRoleId());
            vo.setRoleName(role == null ? null : role.getName());
        }
        if (user.getDeptId() != null) {
            Department department = departmentService.get(user.getDeptId());
            vo.setDeptName(department == null ? null : department.getName());
        }

        return vo;
    }

    public List<User> findByUserIds(Long[] userIds) {
        if (userIds == null || userIds.length == 0) return null;

        return userMapper.findByUserIds(userIds);
    }

    @Transactional
    public void update(User user) {
        user.setOrgId(null);
        user.setAccountId(null);
        user.setPinyin(CommonUtils.getFirstLetters(user.getName()));
        user.setUpdator(SessionUtil.getUserId().toString());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(null);

        userMapper.update(user); // 保存用户

        writeLog(user, LogLevel.SECOND, OptName.MODIFY);
    }

    public void updateBelongOrgIdForAssignRole(long organId, long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setOrgId(organId);
        userMapper.update(user);
    }

    @Transactional
    public void updateUserPhotoUrl(String photoUrl, long userId) {
        User user = new User();
        user.setUserId(userId);
        user.setPhotoUrl(photoUrl);
        user.setUpdator(SessionUtil.getUserId().toString());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Transactional
    public void updateRole(long userId, long roleId) {
        User user = new User();
        user.setUserId(userId);
        user.setRoleId(roleId);
        user.setUpdator(SessionUtil.getUserId().toString());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

//    @Transactional
//    public void resetPwdByUserId(String userDefaultPwd, String userName, long userId) {
//        User user = new User();
//        user.setUserId(userId);
//        user.setPassword(CommonUtils.encrypt(userDefaultPwd));
//        user.setUpdator(SessionUtil.getUserId().toString());
//        user.setUpdateTime(LocalDateTime.now());
//        userMapper.update(user);
//
//        LogOperator.begin()
//                .module(ModuleName.ROLE_MGR)
//                .operate(OptName.RESET_PWD)
//                .id(userId)
//                .title(null)
//                .content("重置密码用户：%s", userName)
//                .level(LogLevel.SECOND)
//                .emit();
//    }

    @Transactional
    public void save(User user) {
        user.setOrgId(SessionUtil.getOrganId());
        user.setPinyin(CommonUtils.getFirstLetters(user.getName()));
        user.setStatus(ENABLE);
        user.setUpdator(SessionUtil.getUserId().toString());
        user.setUpdateTime(LocalDateTime.now());
        user.setCreator(SessionUtil.getUserId().toString());
        user.setCreateTime(LocalDateTime.now());
        userMapper.save(user);

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
                .content("用户名称: %s, 所属机构: %s", entity.getName(), entity.getOrgId())
                .level(logLevel)
                .emit();
    }

}

package com.mycuckoo.service.facade;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.uum.DepartmentExtend;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.service.uum.DepartmentService;
import com.mycuckoo.service.uum.PrivilegeService;
import com.mycuckoo.service.uum.UserService;
import com.mycuckoo.web.vo.res.platform.HierarchyModuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能说明: 用户常用业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:15:12 AM
 */
@Service
@Transactional(readOnly = true)
public class UumServiceFacade {

    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService deptService;


    public void deletePrivilegeByModOptId(String[] modOptRefIds) {
        this.privilegeService.deletePrivilegeByModOptId(modOptRefIds);
    }

    public void deletePrivilegeByModResId(String[] modOptRefIds) {
        this.privilegeService.deletePrivilegeByModResId(modOptRefIds);
    }

    public HierarchyModuleVo findPrivilegesForAdminLogin() {
        return privilegeService.findPrivilegesForAdminLoginNew();
    }

    public HierarchyModuleVo findPrivilegesForUserLogin(long userId, long roleId, long organId) {
        return privilegeService.findPrivilegesForUserLogin(userId, roleId, organId);
    }

    public boolean existsSpecialPrivilegeForUser(long userId) {
        return privilegeService.existsSpecialPrivilegeByUserId(userId);
    }

    @Transactional
    public void deleteRowPrivilegeByDeptId(String deptId) {
        privilegeService.deleteRowPrivilegeByDeptId(deptId);
    }


    public List<User> findByUserIds(Long[] userIds) {
        return userService.findByUserIds(userIds);
    }

    public List<DepartmentExtend> findByDeptIds(Long[] deptIds) {
        return deptService.findByDeptIds(deptIds);
    }

    public List<Long> findChildIds(long deptId, int flag) {
        return deptService.findChildIds(deptId, flag);
    }
}

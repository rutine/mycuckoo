package com.mycuckoo.admin.service.facade;

import com.mycuckoo.admin.entity.uum.DepartmentExtend;
import com.mycuckoo.admin.entity.uum.User;
import com.mycuckoo.admin.service.uum.DepartmentService;
import com.mycuckoo.admin.service.uum.PrivilegeService;
import com.mycuckoo.admin.service.uum.UserService;
import com.mycuckoo.admin.entity.dto.platform.HierarchyModuleDTO;
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



    public void deletePrivilegeByModResId(String[] modResRefIds) {
        this.privilegeService.deletePrivilegeByModResId(modResRefIds);
    }

    public HierarchyModuleDTO findPrivilegesForAdminLogin() {
        return privilegeService.findPrivilegesForAdminLoginNew();
    }

    public HierarchyModuleDTO findPrivilegesForUserLogin(long userId, long roleId, long organId) {
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

package com.mycuckoo.service.uum;

import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.LogLevelEnum;
import com.mycuckoo.common.constant.OptNameEnum;
import com.mycuckoo.common.constant.OwnerType;
import com.mycuckoo.common.utils.CommonUtils;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.RoleMapper;
import com.mycuckoo.service.platform.SystemOptLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.*;

/**
 * 功能说明:  角色业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 11:31:44 AM
 */
@Service
@Transactional(readOnly = true)
public class RoleService {
    static Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private OrganRoleService organRoleService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private SystemOptLogService sysOptLogService;


    @Transactional
    public void disEnable(long roleId, String disEnableFlag) {
        if (DISABLE.equals(disEnableFlag)) {
            organRoleService.deleteByRoleId(roleId); //根据角色ID删除机构角色关系记录，为停用角色所用
            privilegeService.deleteByOwnerIdAndOwnerType(roleId, OwnerType.ROLE.value());  //删除用户所拥有操作、行权限

            Role role = new Role(roleId, DISABLE);
            roleMapper.update(role);

            role = get(roleId);
            writeLog(role, LogLevelEnum.SECOND, OptNameEnum.DISABLE);
        } else {
            Role role = new Role(roleId, ENABLE);
            roleMapper.update(role);

            role = get(roleId);
            writeLog(role, LogLevelEnum.SECOND, OptNameEnum.ENABLE);
        }
    }

    public boolean existByRoleName(String roleName) {
        int count = roleMapper.countByRoleName(roleName);

        logger.debug("find Role total is {}", count);

        return count > 0;
    }

    public Page<Role> findByPage(String roleName, Pageable page) {
        logger.debug("start={} limit={} roleName={}",
                page.getOffset(), page.getPageSize(), roleName);

        Map<String, Object> params = Maps.newHashMap();
        params.put("roleName", CommonUtils.isNullOrEmpty(roleName) ? null : "%" + roleName + "%");

        return roleMapper.findByPage(params, page);
    }

    public Role get(long roleId) {
        logger.debug("will find Role id is {}", roleId);

        return roleMapper.get(roleId);
    }

    @Transactional
    public void update(Role role) {
        Role old = get(role.getRoleId());
        Assert.notNull(old, "角色不存在!");
        Assert.state(old.getRoleName().equals(role.getRoleName())
                || !existByRoleName(role.getRoleName()), "角色[" + role.getRoleName() + "]已存在!");

        roleMapper.update(role);

        writeLog(role, LogLevelEnum.SECOND, OptNameEnum.MODIFY);
    }

    @Transactional
    public void save(Role role) {
        Assert.state(!existByRoleName(role.getRoleName()), "角色[" + role.getRoleName() + "]已存在!");

        role.setStatus(ENABLE);
        roleMapper.save(role);

        writeLog(role, LogLevelEnum.FIRST, OptNameEnum.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     *
     * @param role     角色对象
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 17, 2012 7:39:34 PM
     */
    private void writeLog(Role role, LogLevelEnum logLevel, OptNameEnum opt) {
        String optContent = "角色名称 : " + role.getRoleName() + SPLIT;
        sysOptLogService.saveLog(logLevel, opt, ROLE_MGR, optContent, role.getRoleId() + "");
    }

}

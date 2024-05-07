package com.mycuckoo.service.uum;

import com.google.common.collect.Maps;
import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.constant.enums.OwnerType;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.operator.LogOperator;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.uum.RoleMapper;
import com.mycuckoo.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Map;

import static com.mycuckoo.constant.ServiceConst.DISABLE;
import static com.mycuckoo.constant.ServiceConst.ENABLE;

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


    @Transactional
    public void disEnable(long roleId, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        if (!enable) {
            organRoleService.deleteByRoleId(roleId); //根据角色ID删除机构角色关系记录，为停用角色所用
            privilegeService.deleteByOwnerIdAndOwnerType(roleId, OwnerType.ROLE.value());  //删除用户所拥有操作、行权限

            roleMapper.update(new Role(roleId, DISABLE));
        } else {
            roleMapper.update(new Role(roleId, ENABLE));
        }

        Role role = get(roleId);
        writeLog(role, LogLevel.SECOND, enable ? OptName.ENABLE : OptName.DISABLE);
    }

    public boolean existByRoleName(String roleName) {
        int count = roleMapper.countByRoleName(roleName);

        logger.debug("find Role total is {}", count);

        return count > 0;
    }

    public Page<Role> findByPage(String name, Pageable page) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("name", CommonUtils.isNullOrEmpty(name) ? null : "%" + name + "%");

        return roleMapper.findByPage(params, page);
    }

    public Role get(long roleId) {
        return roleMapper.get(roleId);
    }

    @Transactional
    public void update(Role role) {
        Role old = get(role.getRoleId());
        Assert.notNull(old, "角色不存在!");
        Assert.state(old.getName().equals(role.getName())
                || !existByRoleName(role.getName()), "角色[" + role.getName() + "]已存在!");

        roleMapper.update(role);

        writeLog(role, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Role role) {
        Assert.state(!existByRoleName(role.getName()), "角色[" + role.getName() + "]已存在!");

        role.setStatus(ENABLE);
        roleMapper.save(role);

        writeLog(role, LogLevel.FIRST, OptName.SAVE);
    }


    // --------------------------- 私有方法 -------------------------------

    /**
     * 公用模块写日志
     *
     * @param entity     角色对象
     * @param logLevel
     * @param opt
     * @throws ApplicationException
     * @author rutine
     * @time Oct 17, 2012 7:39:34 PM
     */
    private void writeLog(Role entity, LogLevel logLevel, OptName opt) {
        LogOperator.begin()
                .module(ModuleName.ROLE_MGR)
                .operate(opt)
                .id(entity.getRoleId())
                .title(null)
                .content("角色名称：%s", entity.getName())
                .level(logLevel)
                .emit();
    }

}

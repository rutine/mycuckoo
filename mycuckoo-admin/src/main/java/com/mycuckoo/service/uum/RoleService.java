package com.mycuckoo.service.uum;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.constant.enums.OwnerType;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.ApplicationException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.repository.uum.RoleMapper;
import com.mycuckoo.util.web.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;

import static com.mycuckoo.constant.AdminConst.DISABLE;
import static com.mycuckoo.constant.AdminConst.ENABLE;

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
    private PrivilegeService privilegeService;


    @Transactional
    public void disEnable(long roleId, String disEnableFlag) {
        boolean enable = ENABLE.equals(disEnableFlag);
        if (!enable) {
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

    public Page<Role> findByPage(Querier querier) {
        return roleMapper.findByPage(querier.getQ(), querier);
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

        role.setUpdateDate(new Date());
        role.setUpdater(SessionUtil.getUserCode());
        roleMapper.update(role);

        writeLog(role, LogLevel.SECOND, OptName.MODIFY);
    }

    @Transactional
    public void save(Role role) {
        Assert.state(!existByRoleName(role.getName()), "角色[" + role.getName() + "]已存在!");

        role.setOrgId(SessionUtil.getOrganId());
        role.setUpdateDate(new Date());
        role.setUpdater(SessionUtil.getUserCode());
        role.setCreateDate(new Date());
        role.setCreator(SessionUtil.getUserCode());
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

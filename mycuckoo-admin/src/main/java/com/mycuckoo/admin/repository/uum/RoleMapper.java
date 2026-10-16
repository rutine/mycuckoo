package com.mycuckoo.admin.repository.uum;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.core.annotation.RowPermission;
import com.mycuckoo.admin.entity.uum.Role;

/**
 * 功能说明: 角色持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:46:52 AM
 */
@RowPermission(table = "uum_role")
public interface RoleMapper extends Repository<Role, Long> {

    /**
     * 根据角色名称的统计角色记录
     *
     * @param roleName
     * @return
     */
    int countByRoleName(String roleName);

    @RowPermission(table = "uum_role", row = RowPermission.Row.NONE)
    Role getByRegDefault(String regDefault);
}

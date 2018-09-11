package com.mycuckoo.repository.uum;

import com.mycuckoo.domain.uum.Role;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 角色持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:46:52 AM
 */
public interface RoleMapper extends Repository<Role, Long> {

    /**
     * 根据角色名称的统计角色记录
     *
     * @param roleName
     * @return
     */
    int countByRoleName(String roleName);

}

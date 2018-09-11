package com.mycuckoo.repository.uum;

import com.mycuckoo.domain.uum.RoleUserRef;
import com.mycuckoo.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 角色用户持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:47:28 AM
 */
public interface RoleUserRefMapper extends Repository<RoleUserRef, Long> {

    /**
     * 根据用户ID查询角色用户关系
     *
     * @param userId 用户ID
     * @return 用户所有关联的角色用户关系
     */
    List<RoleUserRef> findByUserId(Long userId);

    /**
     * 根据userId删除角色用户关系
     *
     * @param userId
     */
    void deleteByUserId(Long userId);

    /**
     * 根据机构角色ID统计角色用户关系记录
     *
     * @param orgRoleId
     * @return
     */
    int countByOrgRoleId(Long orgRoleId);

    /**
     * 根据角色ID统计角色用户关系记录
     *
     * @param roleId
     * @return
     */
    int countByRoleId(Long roleId);

    /**
     * 根据用户ID和机构角色ID获取角色
     *
     * @param userId    用户ID
     * @param orgRoleId 机构角色ID
     * @return
     */
    RoleUserRef findByUserIdAndOrgRoleId(@Param("userId") Long userId, @Param("orgRoleId") Long orgRoleId);

    /**
     * 根据机构和角色ID查询用户
     *
     * @param orgId  机构id
     * @param roleId 角色id
     * @return 主要是UumUser对象
     * @author rutine
     */
    List<RoleUserRef> findByOrgRoleId(@Param("orgId") Long orgId, @Param("roleId") Long roleId);

}

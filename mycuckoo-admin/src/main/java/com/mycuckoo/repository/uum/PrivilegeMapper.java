package com.mycuckoo.repository.uum;

import com.mycuckoo.domain.uum.Privilege;
import com.mycuckoo.core.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 权限持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:45:55 AM
 */
public interface PrivilegeMapper extends Repository<Privilege, Long> {

    /**
     * <p>根据资源拥有者ID，拥有者类型和权限类型查询权限记录</p>
     *
     * @param ownerIds       用户或角色ID
     * @param ownerTypes     为用户或角色标识
     * @param privilegeTypes 行或操作权限
     * @return
     */
    List<Privilege> findByOwnIdAndPrivilegeType(
            @Param("ownerIds") Long[] ownerIds,
            @Param("ownerTypes") String[] ownerTypes,
            @Param("privilegeTypes") String[] privilegeTypes);

    /**
     * <p>统计用户特殊权限记录</p>
     *
     * @param userId
     * @param ownerType {@link com.mycuckoo.constant.enums.OwnerType#USR}
     * @return
     */
    int countByUserIdAndOwnerType(@Param("userId") Long userId, @Param("ownerType") String ownerType);

    /**
     * <p>根据拥有者ID和拥有者类型删除权限记录</p>
     *
     * @param ownerId   拥有者ID
     * @param ownerType 拥有者类型
     */
    int deleteByOwnerIdAndOwnerType(
            @Param("ownerId") Long ownerId,
            @Param("ownerType") String ownerType);

    /**
     * <p>根据资源拥有者ID，拥有者类型和权限类型删除已分配的权限</p>
     *
     * @param ownerId       用户或角色ID
     * @param ownerType     为用户或角色标识
     * @param privilegeType 行或操作权限
     */
    int deleteByOwnerIdAndPrivilegeType(
            @Param("ownerId") Long ownerId,
            @Param("ownerType") String ownerType,
            @Param("privilegeType") String privilegeType);

    /**
     * <p>停用机构时根据机构ID删除行权限</p>
     *
     * @param orgId
     * @param privilegeType  {@link com.mycuckoo.constant.enums.PrivilegeType#ROW}
     * @param privilegeScope {@link com.mycuckoo.constant.enums.PrivilegeScope#DEPT}
     */
    int deleteRowPrivilegeByDeptId(
            @Param("resourceId") String orgId,
            @Param("privilegeType") String privilegeType,
            @Param("privilegeScope") String privilegeScope);

    /**
     * <p>删除模块操作关系时同时, 也删除模块操作权限</p>
     *
     * @param modOptRefIds
     * @param privilegeType {@link com.mycuckoo.constant.enums.PrivilegeType#OPT}
     */
    int deleteByModOptId(
            @Param("modOptRefIds") String[] modOptRefIds,
            @Param("privilegeType") String privilegeType);

}

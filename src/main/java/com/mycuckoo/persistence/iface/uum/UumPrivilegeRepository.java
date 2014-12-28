package com.mycuckoo.persistence.iface.uum;

import java.util.List;

import com.mycuckoo.domain.uum.UumPrivilege;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 权限持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:45:55 AM
 * @version 2.0.0
 */
public interface UumPrivilegeRepository extends Repository<UumPrivilege, Long> {

	/**
	 * <p>根据资源拥有者ID，拥有者类型和权限类型查询权限记录</p>
	 * 
	 * @param ownerIds 用户或角色ID
	 * @param ownerTypes 为用户或角色标识
	 * @param privilegeTypes 行或操作权限
	 * @return
	 */
	List<UumPrivilege> findPrivilegesByOwnIdAPriType(Long[] ownerIds,
			String[] ownerTypes, String[] privilegeTypes);

	/**
	 * <p>统计用户特殊权限记录</p>
	 * 
	 * @param userId
	 * @return
	 */
	int countSpecialPrivilegesByUserId(Long userId);

	/**
	 * <p>根据拥有者ID和拥有者类型删除权限记录</p>
	 * 
	 * @param ownerId 拥有者ID
	 * @param ownerType 拥有者类型
	 */
	void deletePrivilegeByOwnerIdAType(Long ownerId, String ownerType);

	/**
	 * <p>根据资源拥有者ID，拥有者类型和权限类型删除已分配的权限</p>
	 * 
	 * @param ownerId 用户或角色ID
	 * @param ownerType 为用户或角色标识
	 * @param privilegeType 行或操作权限
	 */
	void deletePrivilegeByOwnerIdAPriType(Long ownerId, String ownerType, String privilegeType);

	/**
	 * <p>停用机构时根据机构ID删除行权限</p>
	 * 
	 * @param orgId
	 */
	void deleteRowPrivilegeByOrgId(String orgId);

	/**
	 * <p>删除模块操作关系时同时, 也删除模块操作权限</p>
	 * 
	 * @param modOptRefIds
	 */
	void deletePrivilegeByModOptId(String[] modOptRefIds);

}

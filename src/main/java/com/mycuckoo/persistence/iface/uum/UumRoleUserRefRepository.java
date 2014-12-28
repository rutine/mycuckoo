package com.mycuckoo.persistence.iface.uum;

import java.util.List;

import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 角色用户持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:47:28 AM
 * @version 2.0.0
 */
public interface UumRoleUserRefRepository extends Repository<UumRoleUserRef, Long> {

	/**
	 * 
	 * 根据用户ID查询角色用户关系
	 * 
	 * @param user_id 用户ID
	 * @return 用户所有关联的角色用户关系
	 */
	List<UumRoleUserRef> findRoleUserRefsByUserId(Long userId);

	/**
	 * 根据userId删除角色用户关系
	 * 
	 * @param userId
	 */
	void deleteRoleUserRefByUserId(Long userId);

	/**
	 * 根据机构角色ID统计角色用户关系记录
	 * 
	 * @param orgRoleId
	 * @return
	 */
	int countRoleUserRefsByOrgRoleId(Long orgRoleId);

	/**
	 * 根据角色ID统计角色用户关系记录
	 * 
	 * @param orgRoleId
	 * @return
	 */
	int countRoleUserRefsByRoleId(Long roleId);

	/**
	 * 根据用户ID和机构角色ID获取角色
	 * 
	 * @param userId 用户ID
	 * @param orgRoleId 机构角色ID
	 * @return
	 */
	UumRoleUserRef getRoleUserRefByUserIdAOrgRoleId(Long userId, Long orgRoleId);

	/**
	 * 根据机构和角色ID查询用户
	 * 
	 * @param orgId 机构id
	 * @param roleId 角色id
	 * @return 主要是UumUser对象
	 * @author rutine
	 */
	List<UumRoleUserRef> findRoleUserRefsByOrgRoleId(Long orgId, Long roleId);

}

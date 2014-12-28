package com.mycuckoo.persistence.iface.uum;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 机构角色关系持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:44:43 AM
 * @version 2.0.0
 */
public interface UumOrgRoleRefRepository extends Repository<UumOrgRoleRef, Long> {

	/**
	 * <p>根据机构ID统计角色数量</p>
	 * 
	 * @param orgId
	 * @return
	 */
	int countOrgRoleRefsByOrgId(Long orgId);

	/**
	 * <p>根据角色ID删除机构角色关系记录，为停用角色所用</p>
	 * 
	 * @param roleId
	 */
	void deleteOrgRoleRefsByRoleId(Long roleId);

	/**
	 * <p>根据roleId查询所有机构.</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[
	 * 		{
	 * 			"orgRoleId"    : {@code Long}, 
	 * 			"uumOrgan"     : {@code List<UumOrgan>}
	 *		},
	 *		...
	 *	]
	 * </pre>
	 * 
	 * @param roleId
	 * @return 机构角色关系列表
	 **/
	List<UumOrgRoleRef> findOrgansByRoleId(Long roleId);

	/**
	 * <p>根据角色ID查询所有机构角色关系ID</p>
	 * 
	 * @param roleId
	 * @return
	 */
	List<Long> findOrgRoleIdsByRoleId(Long roleId);

	/**
	 * <p>根据机构角色IDs查询机构角色关系</p>
	 * 
	 * @param orgRoleRefs
	 * @return
	 */
	List<UumOrgRoleRef> findOrgRoleRefsByOrgRoleIds(Long[] orgRoleRefIds);

	/**
	 * <p>根据机构ID和角色名称分页查询已分配的角色</p>
	 * 
	 * @param orgId 机构ID
	 * @param roleName 角色名称
	 * @param page
	 * @return
	 */
	Page<UumOrgRoleRef> findRolesByCon(Long orgId, String roleName, Pageable page);

	/**
	 * <p>根据orgId查询所有角色.</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[
	 * 		{
	 * 			"orgRoleId"    : {@code Long}, 
	 * 			"uumRole"      : {@code List<UumRole>}
	 *		},
	 *		...
	 *	]
	 * </pre>
	 * 
	 * @param orgId
	 * @return 机构角色关系列表
	 **/
	List<UumOrgRoleRef> findRolesByOrgId(Long orgId);

	/**
	 * <p>根据机构ID和角色ID获取机构角色关系</p>
	 * 
	 * @param orgId 机构ID
	 * @param roleId 角色ID
	 * @return
	 */
	UumOrgRoleRef getOrgRoleRefByOrgRoleId(Long orgId, Long roleId);
}

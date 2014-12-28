package com.mycuckoo.persistence.iface.uum.group;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumGroupMember;

/**
 * 功能说明: 角色组管理持久层接口
 *
 * @author rutine
 * @time Sep 24, 2014 10:54:05 AM
 * @version 2.0.0
 */
public interface UumRoleGroupRepository extends UumGroupRepository {

	/**
	 * 根据组名称统计组数量
	 *
	 * @param groupName 组名称
	 * @return
	 */
	int countRoleGroupsByGroupName(String groupName);
	
	/**
	 * 根据组ID删除组成员
	 *
	 * @param groupId
	 */
	void deleteRoleGroupMembersByGroupId(Long groupId);
	
	/**
	 * 根据组ID查询所有组成员
	 *
	 * @param groupId
	 * @return
	 */
	List<UumGroupMember> findRoleGroupMembersByGroupId(Long groupId);

	/**
	 * 根据组名称查询组记录
	 *
	 * @param groupName 组名称
	 * @param page
	 * @return
	 */
	Page<UumGroup> findRoleGroupsByCon(String groupName, Pageable page);
	
	/**
	 * 根据组ID修改组状态
	 *
	 * @param groupId
	 * @param status
	 */
	void updateRoleGroupStatus(Long groupId, String status);
}

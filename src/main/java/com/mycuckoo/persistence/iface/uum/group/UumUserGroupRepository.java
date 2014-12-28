package com.mycuckoo.persistence.iface.uum.group;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;

/**
 * 功能说明: 角色组管理持久层接口
 *
 * @author rutine
 * @time Sep 24, 2014 10:54:34 AM
 * @version 2.0.0
 */
public interface UumUserGroupRepository extends UumGroupRepository {

	/**
	 * 根据组名称统计组数量
	 *
	 * @param groupName 组名称
	 * @return
	 */
	int countUserGroupsByGroupName(String groupName);
	
	/**
	 * 根据组ID删除组成员
	 *
	 * @param groupId
	 */
	void deleteUserGroupMembersByGroupId(Long groupId);

	/**
	 * 根据组名称查询组信息
	 *
	 * @param groupName 组名称
	 * @param page
	 * @return
	 */
	Page<UumGroup> findUserGroupsByCon(String groupName, Pageable page);
	
	/**
	 * 根据组ID修改组状态
	 *
	 * @param groupId
	 * @param status
	 */
	void updateUserGroupStatus(Long groupId, String status);
}

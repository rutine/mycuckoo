package com.mycuckoo.persistence.iface.uum.group;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 分组持久层接口
 *
 * @author rutine
 * @time Sep 24, 2014 10:53:49 AM
 * @version 2.0.0
 */
public interface UumGroupRepository extends Repository<UumGroup, Long> {
	
	/**
	 * 根据组名称统计组数量
	 *
	 * @param groupName 组名称
	 * @return
	 */
	int countGroupsByGroupName(String groupName);
	
	/**
	 * 根据组ID删除组成员
	 *
	 * @param groupId
	 */
	void deleteGroupMembersByGroupId(Long groupId);
	
	/**
	 * 根据组名称查询组信息
	 *
	 * @param groupName 组名称
	 * @param page
	 * @return
	 */
	Page<UumGroup> findGroupsByCon(String groupName, Pageable page);
	
	/**
	 * 根据组ID修改组状态
	 *
	 * @param groupId
	 * @param status
	 */
	void updateGroupStatus(Long groupId, String status);

}
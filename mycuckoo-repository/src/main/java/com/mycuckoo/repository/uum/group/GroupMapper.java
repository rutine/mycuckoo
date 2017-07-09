package com.mycuckoo.repository.uum.group;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.uum.Group;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 分组持久层接口
 *
 * @author rutine
 * @time Sep 24, 2014 10:53:49 AM
 * @version 3.0.0
 */
public interface GroupMapper extends Repository<Group, Long> {
	
	/**
	 * 根据组名称统计组数量
	 *
	 * @param groupName 组名称
	 * @param groupType 组类型
	 * @return
	 */
	int countByGroupName(@Param("groupName") String groupName, @Param("groupType") String groupType);
	
	/**
	 * 根据组ID修改组状态
	 *
	 * @param groupId
	 * @param status
	 */
	void updateStatus(@Param("groupId") Long groupId, @Param("status") String status);
}
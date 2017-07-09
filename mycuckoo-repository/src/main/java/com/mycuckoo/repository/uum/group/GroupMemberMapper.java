package com.mycuckoo.repository.uum.group;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.uum.GroupMember;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 组员持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:53:26 AM
 * @version 3.0.0
 */
public interface GroupMemberMapper extends Repository<GroupMember, Long> {
	
	/**
	 * 根据组ID删除组成员
	 *
	 * @param groupId
	 * @param groupType
	 */
	void deleteByGroupId(@Param("groupId") Long groupId, @Param("groupType") String groupType);
	
	/**
	 * 根据组ID查询所有组成员
	 *
	 * @param groupId
	 * @return
	 */
	List<GroupMember> findByGroupId(@Param("groupId") Long groupId, @Param("groupType") String groupType);

}

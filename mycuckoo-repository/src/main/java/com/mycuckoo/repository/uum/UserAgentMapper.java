package com.mycuckoo.repository.uum;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.uum.UserAgent;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 用户代理持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:48:55 AM
 * @version 3.0.0
 */
public interface UserAgentMapper extends Repository<UserAgent, Long> {

	/**
	 * 根据用户ID和代理用户ID统计代理记录<br>
	 * 
	 * @param userId
	 * @param agentUserId
	 * @return
	 */
	int countByUserIdAndAgentUserId(@Param("userId") long userId, @Param("agentUserId") long agentUserId);

	/**
	 * 根据代理用户ID查询有效期内的代理记录<br>
	 * 
	 * @param agentUserId
	 * @param now
	 * @return
	 */
	List<UserAgent> findByAgentUserId(@Param("agentUserId") long agentUserId, @Param("now") Date now);

	/**
	 * 根据机构角色ID查询用户代理<br>
	 * 
	 * @param ownerIds
	 * @return
	 */
	List<UserAgent> findByOwnerId(Long[] ownerIds);

}

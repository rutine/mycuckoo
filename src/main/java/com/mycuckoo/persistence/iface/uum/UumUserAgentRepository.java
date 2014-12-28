package com.mycuckoo.persistence.iface.uum;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 用户代理持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:48:55 AM
 * @version 2.0.0
 */
public interface UumUserAgentRepository extends Repository<UumUserAgent, Long> {

	/**
	 * 根据条件分页查询所有代理记录<br>
	 * 
	 * @param userId
	 * @param page
	 * @return
	 */
	Page<UumUserAgent> findUserAgentsByCon(Long userId, Pageable page);

	/**
	 * 根据用户ID和代理用户ID统计代理记录<br>
	 * 
	 * @param userId
	 * @param agentUserId
	 * @return
	 */
	int countUserAgentsByUserIdAAgentUserId(long userId, long agentUserId);

	/**
	 * 根据代理用户ID查询有效期内的代理记录<br>
	 * 
	 * @param agentUserId
	 * @return
	 */
	List<UumUserAgent> findUserAgentsByAgentUserId(long agentUserId);

	/**
	 * 根据用户ID查询代理记录<br>
	 * 
	 * @param userId
	 * @return
	 */
	List<UumUserAgent> findUserAgentsByUserId(long userId);

	/**
	 * 根据机构角色ID查询用户代理<br>
	 * 
	 * @param ownerIds
	 * @return
	 */
	List<UumUserAgent> findUserAgentsByOwnerId(Long[] ownerIds);

}

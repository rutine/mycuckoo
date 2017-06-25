package com.mycuckoo.repository.uum;

import java.util.List;

import com.mycuckoo.domain.uum.UserAgentPrivilege;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 用户代理权限持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:48:22 AM
 * @version 3.0.0
 */
public interface UserAgentPrivilegeMapper extends Repository<UserAgentPrivilege, Long> {

	/**
	 * 根据代理ID查询所有权限IDs
	 * 
	 * @param agentId
	 * @return 权限列表
	 */
	List<String> findPrivilegeIdsByAgentId(Long agentId);

}

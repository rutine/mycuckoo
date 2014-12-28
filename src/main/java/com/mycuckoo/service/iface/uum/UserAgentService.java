package com.mycuckoo.service.iface.uum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 用户代理业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:32:55 AM
 * @version 2.0.0
 */
public interface UserAgentService {

	/**
	 * <p>当停用用户时删除其所有代理</p>
	 * 
	 * @param userId
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 19, 2012 8:53:50 PM
	 */
	void deleteUserAgentsByUserId(long userId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>删除用户代理</p>
	 * 
	 * @param agentIds
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 19, 2012 8:54:30 PM
	 */
	void deleteUserAgentsByAgentIds(List<Long> agentIds,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>当用户重新分配权限时，删除用户代理</p>
	 * 
	 * @param ownerId
	 * @param ownerType
	 * @author rutine
	 * @time Oct 19, 2012 8:53:18 PM
	 */
	void deleteUserAgentsByOwnerId(Long ownerId, String ownerType,
			HttpServletRequest request);

	/**
	 * <p>根据代理<code>ID</code>查询权限<code>ID</code></p>
	 * 
	 * @param agentId
	 * @return
	 * @author rutine
	 * @time Oct 19, 2012 8:54:47 PM
	 */
	List<String> findPrivilegeIdsByAgentId(Long agentId);

	/**
	 * 查找用户分配的模块操作代理权限<br>
	 * 
	 * @param agentId
	 * @return
	 * @author rutine
	 * @time Oct 19, 2012 8:59:54 PM
	 */
	List<TreeVo> findAgentPrivilegeForAgentUserByAgentId(Long agentId);

	/**
	 * <p>根据用户ID查询代理记录</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		agentId, agentUserId, userId, userCode, userName, 
	 * 		orgRoleId, organId, organName, roleId, roleName, 
	 * 		beginDate, endDate, reason
	 *	}
	 * </pre>
	 * 
	 * @param agentUserId
	 * @return
	 * @author rutine
	 * @time Oct 19, 2012 8:56:06 PM
	 */
	List<UumUserAgent> findUserAgentsByAgentUserId(long agentUserId);

	/**
	 * <p>根据用户ID分页查询用户代理</p>
	 * 
	 * @param userId
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 19, 2012 8:55:40 PM
	 */
	Page<UumUserAgent> findUserAgentByCon(long userId, Pageable page);

	/**
	 * <p>判断是否已经为此用户授过权</p>
	 * 
	 * @param userId
	 * @param userAgentId
	 * @return
	 * @author rutine
	 * @time Oct 19, 2012 8:56:29 PM
	 */
	boolean existsAgentUser(long userId, long userAgentId);

	/**
	 * <p>修改用户代理</p>
	 * 
	 * @param uumUserAgent
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 19, 2012 8:56:46 PM
	 */
	void updateUserAgent(UumUserAgent uumUserAgent, HttpServletRequest request)
			throws ApplicationException;;

	/**
	 * <p>保存用户代理</p>
	 * 
	 * @param uumUserAgent
	 * @param modOptIdList
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 19, 2012 8:57:30 PM
	 */
	void saveUserAgent(UumUserAgent uumUserAgent, List<String> modOptIdList,
			HttpServletRequest request) throws ApplicationException;
}

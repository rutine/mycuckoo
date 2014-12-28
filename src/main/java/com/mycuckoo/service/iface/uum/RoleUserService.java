package com.mycuckoo.service.iface.uum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 角色用户业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:32:06 AM
 * @version 2.0.0
 */
public interface RoleUserService {

	/**
	 * <p>根据机构角色<code>ID</code>统计角色用户关系记录</p>
	 * 
	 * @param orgRoleId
	 * @return
	 * @author rutine
	 * @time Oct 18, 2012 9:59:37 PM
	 */
	int countRoleUserRefsByOrgRoleId(long orgRoleId);

	/**
	 * <p>根据角色<code>ID</code>统计已分配的用户记录</p>
	 * 
	 * @param roleId 角色<code>ID</code>
	 * @return
	 * @author rutine
	 * @time Oct 18, 2012 10:00:57 PM
	 */
	int countRoleUserRefsByRoleId(long roleId);

	/**
	 * <p>根据用户ID删除角色用户关系记录</p>
	 * 
	 * @param userId 用户ID
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 18, 2012 9:56:49 PM
	 */
	void deleteRoleUserRefByUserId(long userId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>根据用户<code>ID</code>和机构角色<code>ID</code>获取角色</p>
	 * 
	 * @param userId
	 * @param orgRoleId
	 * @return
	 * @author rutine
	 * @time Oct 18, 2012 9:57:44 PM
	 */
	UumRoleUserRef getRoleUserRefByUserIdAOrgRoleId(long userId, long orgRoleId);

	/**
	 * <p>根据用户ID查找用户的所有角色</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[
	 * 		{
	 * 			orgRoleUserId, isDefault, orgRoleId, organId, organName, 
	 * 			roleId, roleName, uumUser.userId, uumUser.userName
	 * 		},
	 * 		...
	 * 	]
	 * </pre>
	 * 
	 * @param userId 用户主键ID
	 * @return List<UumRoleUserRef> 用户角色关系记录
	 * @author rutine
	 * @time Oct 18, 2012 9:58:04 PM
	 */
	List<UumRoleUserRef> findRoleUserRefsByUserId(long userId);

	/**
	 * <p>保存角色用户关系记录</p>
	 * 
	 * @param userId 用户id
	 * @param orgRoleIds 机构角色关系id
	 * @param defaultOrgRoleId 默认机构角色ID
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 18, 2012 10:03:59 PM
	 */
	void saveRoleUserRef(long userId, List<Long> orgRoleIds,
			long defaultOrgRoleId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>保存为用户分配的角色</p>
	 * 
	 * @param userId 用户ID
	 * @param orgRoleIds 机构角色id数组
	 * @param defaultOrgRoleId 默认角色
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 18, 2012 10:02:10 PM
	 */
	void saveRoleUserRef2(long userId, List<Long> orgRoleIds,
			long defaultOrgRoleId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>保存角色用户关系记录</p>
	 * 
	 * @param uumRoleUserRef 角色用户关系对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 8:28:08 PM
	 */
	void saveRoleUserRef(UumRoleUserRef uumRoleUserRef)
			throws ApplicationException;

	/**
	 * <p>根据机构<code>ID</code>和角色<code>ID</code>查询用户</p>
	 * 
	 * @param orgId
	 * @param roleId
	 * @return UumRoleUserRef 主要用于得到用户对象
	 * @author rutine
	 * @time Oct 31, 2012 8:21:20 PM
	 */
	List<UumRoleUserRef> findRoleUserRefsByOrgRoleId(Long orgId, Long roleId);
}

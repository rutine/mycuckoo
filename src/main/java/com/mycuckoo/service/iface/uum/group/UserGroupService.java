package com.mycuckoo.service.iface.uum.group;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 用户组业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:25:48 AM
 * @version 2.0.0
 */
public interface UserGroupService {

	/**
	 * <p>停用启用用户组</p>
	 * 
	 * @param groupId 用户组ID
	 * @param disEnableFlag 停用启用标志
	 * @return boolean true为停用启用成功，false不能停用
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 30, 2012 9:24:50 PM
	 */
	boolean disEnableUserGroup(long groupId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据机构<code>ID</code>和角色<code>ID</code>查询用户信息</p>
	 * 
	 * @param organId 机构ID
	 * @param roleId 角色ID
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 30, 2012 9:27:19 PM
	 */
	List<UumUser> findUsersByOrgRolId(long organId, long roleId) throws ApplicationException;

	/**
	 * <p>根据组名称查询组信息</p>
	 * 
	 * @param groupName 组名称
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 30, 2012 9:28:16 PM
	 */
	Page<UumGroup> findUserGroupsByCon(String groupName, Pageable page);

	/**
	 * <p>根据用户组<code>ID</code>获取用户组</p>
	 * 
	 * @param groupId 用户组ID
	 * @return
	 * @author rutine
	 * @time Oct 30, 2012 9:30:52 PM
	 */
	UumGroup getUserGroupByGroupId(long groupId);

	/**
	 * <p>判断用户组是否存在</p>
	 * 
	 * @param groupName 组名称
	 * @return
	 * @author rutine
	 * @time Oct 30, 2012 9:26:56 PM
	 */
	boolean existsUserGroupByGroupName(String groupName);

	/**
	 * <p>更新用户组信息</p>
	 * 
	 * @param uumGroup 用户组对象
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 30, 2012 9:31:15 PM
	 */
	void updateUserGroup(UumGroup uumGroup, HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>更新用户组状态</p>
	 * 
	 * @param groupId
	 * @param status
	 * @param request
	 * @author rutine
	 * @time Oct 30, 2012 9:31:03 PM
	 */
	void updateUserGroupStatus(long groupId, String status, HttpServletRequest request);

	/**
	 * <p>保存用户组</p>
	 * 
	 * @param uumGroup 用户组对象
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 30, 2012 9:31:27 PM
	 */
	void saveUserGroup(UumGroup uumGroup, HttpServletRequest request) throws ApplicationException;
}

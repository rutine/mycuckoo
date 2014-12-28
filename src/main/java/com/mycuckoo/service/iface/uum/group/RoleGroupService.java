package com.mycuckoo.service.iface.uum.group;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 角色组业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:24:49 AM
 * @version 2.0.0
 */
public interface RoleGroupService {

	/**
	 * <p>停用启用角色组<p>
	 * 
	 * @param groupId 角色组ID
	 * @param disEnableFlag 停用启用标志
	 * @return boolean true为停用启用成功，false不能停用
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 31, 2012 8:32:10 PM
	 */
	boolean disEnableGroup(long groupId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据机构<code>ID</code>查询角色信息</p>
	 * 
	 * @param organId 机构ID
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 31, 2012 8:33:19 PM
	 */
	List<UumRole> findRolesByOrgId(long organId) throws ApplicationException;

	/**
	 * <p>根据组名称查询组信息</p>
	 * 
	 * @param groupName 组名称
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 31, 2012 8:34:18 PM
	 */
	Page<UumGroup> findRoleGroupsByCon(String groupName, Pageable page);

	/**
	 * <p>根据用户组ID获取角色组</p>
	 * 
	 * @param groupId 角色组ID
	 * @return
	 * @author rutine
	 * @time Oct 31, 2012 8:43:30 PM
	 */
	UumGroup getRoleGroupById(long groupId);

	/**
	 * <p>判断角色组是否存在</p>
	 * 
	 * @param groupName 组名称
	 * @return
	 * @author rutine
	 * @time Oct 31, 2012 8:32:56 PM
	 */
	boolean existsRoleGroupByGroupName(String groupName);

	/**
	 * <p>更新角色组</p>
	 * 
	 * @param uumGroup 角色组对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 31, 2012 8:35:01 PM
	 */
	void updateRoleGroup(UumGroup uumGroup, HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存角色组</p>
	 * 
	 * @param uumGroup 角色组对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 31, 2012 8:35:21 PM
	 */
	void saveRoleGroup(UumGroup uumGroup, HttpServletRequest request) throws ApplicationException;

}

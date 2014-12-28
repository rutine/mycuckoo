package com.mycuckoo.service.iface.uum.group;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 组管理一般业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:24:00 AM
 * @version 2.0.0
 */
public interface GeneralGroupService {

	/**
	 * <p>停用启用用户组</p>
	 * 
	 * @param groupId 用户组ID
	 * @param disEnableFlag 停用启用标志
	 * @return boolean true为停用启用成功，false不能停用
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 31, 2012 9:06:25 PM
	 */
	boolean disEnableGroup(long groupId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据机构<code>ID</code> 角色<code>ID</code> 和组成员类型查询用户信息
	 * 
	 * @param organId 机构ID
	 * @param roleId 角色ID
	 * @param memberType user或role
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 31, 2012 9:06:52 PM
	 */
	List<UumUser> findGeneralByOrgRolId(long organId, long roleId, String memberType) throws ApplicationException;

	/**
	 * <p>根据组名称查询组信息</p>
	 * 
	 * @param groupName 组名称
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 31, 2012 9:07:34 PM
	 */
	Page<UumGroup> findGeneralGroupByCon(String groupName, Pageable page);

	/**
	 * <p>根据组<code>ID</code>获取普通组</p>
	 * 
	 * @param groupId
	 * @return
	 * @author rutine
	 * @time Nov 10, 2013 2:19:04 PM
	 */
	UumGroup getGeneralGroupByGroupId(long groupId);

	/**
	 * <p>判断普通组是否存在</p>
	 * 
	 * @param groupName 组名称
	 * @return
	 * @author rutine
	 * @time Oct 31, 2012 9:08:10 PM
	 */
	boolean existsGeneralGroupByGroupName(String groupName);

	/**
	 * <p>更新普通组</p>
	 * 
	 * @param uumGroup 用户组对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 31, 2012 9:08:33 PM
	 */
	void updateGeneralGroup(UumGroup uumGroup, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>保存普通组</p>
	 * 
	 * @param uumGroup 用户组对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 31, 2012 9:08:48 PM
	 */
	void saveGeneralGroup(UumGroup uumGroup, HttpServletRequest request)
			throws ApplicationException;

}

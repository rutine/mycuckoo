package com.mycuckoo.service.iface.uum;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 权限业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:28:45 AM
 * @version 2.0.0
 */
public interface PrivilegeService {

	/**
	 * <p>根据资源拥有者ID，拥有者类型和权限类型删除已分配的权限(模块操作)</p>
	 * 
	 * @param ownerId 资源拥有者ID
	 * @param ownerType 拥有者类型
	 * @param privilegeType 权限类型
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 8:41:04 PM
	 */
	void deletePrivilege(long ownerId, String ownerType, String privilegeType,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>删除模块操作关系时同时, 也删除模块操作权限</p>
	 * 
	 * @param modOptRefIds
	 * @author rutine
	 * @time Oct 20, 2012 8:42:09 PM
	 */
	void deletePrivilegeByModOptId(String[] modOptRefIds, HttpServletRequest request);

	/**
	 * <p>根据拥有者ID和拥有者类型删除权限</p>
	 * 
	 * @param ownerId 拥有者ID
	 * @param ownerType 拥有者类型
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 8:43:33 PM
	 */
	void deletePrivilegeByOwnerIdAType(long ownerId, String ownerType,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>停用机构时根据机构ID删除行权限</p>
	 * 
	 * @param resourceId 资源ID
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 8:44:05 PM
	 */
	void deleteRowPriByResourceId(String resourceId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>根据用户名模糊查询用户记录</p>
	 * 
	 * @param userName
	 * @return
	 * @author rutine
	 * @time Oct 20, 2012 8:44:31 PM
	 */
	List<UumUser> findUsersByUserName(String userName);

	/**
	 * <p>过滤所有的模块操作，构成模块树，并获得相应模块下的操作</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		"moduleMenu"    : {@code List<SysplModuleMemu>}(包含1,2,3级模块的列表), 
	 * 		"moduleOperate" : {@code Map<String, List<SysplModuleMemu>>}
	 *	}
	 * </pre>
	 * 
	 * @param sysplModOptRefList 系统模块操作关系列表
	 * @param isTreeFlag 是否为树标志
	 * @return Map {"moduleMenu" : List<SysplModuleMemu>, "moduleOperate" : Map<String, List<SysplModuleMemu>>}
	 * @author rutine
	 * @time Oct 20, 2012 8:45:28 PM
	 */
	Map filterModOpt(List<SysplModOptRef> sysplModOptRefList, boolean isTreeFlag);

	/**
	 * <p>获取角色的行权限</p>
	 * 
	 * @param roleId 角色ID
	 * @return
	 * @author rutine
	 * @time Oct 20, 2012 8:46:09 PM
	 */
	String findRowPrivilegeByRoleIdAPriType(long roleId);

	/**
	 * <p>根据用户或角色<code>ID</code>和类型查询已分配和未分配的操作按钮
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		{@code "assignedModOpts"}   : {@code List<SysplModuelMenu>},
	 * 		{@code "unassignedModOpts"} : {@code List<SysplModuleMemu>},
	 * 		{@code "privilegeScope"}    : {@code String}
	 * 	}
	 * </pre>
	 * 
	 * @param ownerId 用户或角色ID
	 * @param ownerType 权限类型
	 * @return Map 1.list 已分配 2.list 未分配 3.string 权限范围
	 * @author rutine
	 * @time Oct 20, 2012 8:50:02 PM
	 * @see {@link PrivilegeService#filterModOpt}
	 */
	Map findSelectAUnselectModOptByOwnIdAOwnType(long ownerId, String ownerType);

	/**
	 * 根据用户ID查询已分配行权限
	 * 
	 * @param userId 用户ID
	 * @return Map 1.list 已分配 2.string 权限范围
	 * @author rutine
	 * @time Oct 20, 2012 8:50:48 PM
	 */
	Map findSelectRowPrivilegeByUserId(long userId);

	/**
	 * 判断用户是否存在特殊操作权限
	 * 
	 * @param userId 用户或角色ID
	 * @return true 有 false 无
	 * @author rutine
	 * @time Oct 20, 2012 8:52:06 PM
	 */
	boolean existsSpecialPrivilegeByUserId(long userId);
	
	/**
	 * <p>当登录用户为管理员时查询所有模块操作</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		"first"  : {@code List<SysplModuleMemu>}(一级菜单),
	 * 		"second" : {@code Map<String, List<SysplModuleMemu>>}(二级菜单),
	 * 		"third"  : {@code Map<String, List<SysplModuleMemu>>}(三级菜单),
	 * 		"fourth" : {@code Map<String, List<SysplModuleMemu>>}(四级操作按钮)
	 * 	}
	 * </pre>
	 * 
	 * @return moduleMenuMap {"first" : List, "second" : Map, "third" : Map, "fourth" : Map
	 * @author rutine
	 * @time Nov 7, 2012 8:40:49 PM
	 */
	Map findUserPrivilegesForAdminLogin();

	/**
	 * <p>根据代理权限{@code id}查询代理用户的权限</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		{@code "first"}   : {@code List<SysplModuelMenu>},
	 * 		{@code "second"}  : {@code Map<String, SysplModuleMemu>}(key键值是上级模块id),
	 * 		{@code "third"}   : {@code Map<String, SysplModuleMemu>}(key键值是上级模块id),
	 * 		{@code "fourth"}  : {@code Map<String, List<SysplModuleMemu>>}(四级操作按钮),
	 * 		{@code "row"}     : {@code String}(sql条件语句: and id = '')
	 * 	}
	 * </pre>
	 * 
	 * @param userId
	 * @param roleId
	 * @param organId
	 * @param organRoleId
	 * @param privilegeIdList 代理权限{@code id}
	 * @return modMenuMap:{"first" : List, "second" : Map, "third" : Map, "row" : String}
	 * @author rutine
	 * @time Oct 20, 2012 8:53:27 PM
	 */
	Map findUserPrivilegesForAgentLogin(long userId, long roleId, long organId,
			long organRoleId, List privilegeIdList);

	/**
	 * <p>根据登录的用户或角色{@code id} 和 标识查询所有模块操作</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		{@code "first"}  : {@code List<SysplModuelMenu>},
	 * 		{@code "second"} : {@code Map<String, SysplModuleMemu>}(key键值是上级模块id),
	 * 		{@code "third"}  : {@code Map<String, SysplModuleMemu>}(key键值是上级模块id),
	 * 		{@code "fourth"} : {@code Map<String, List<SysplModuleMemu>>}(四级操作按钮),
	 * 		{@code "row"}    : {@code String}(sql条件语句: and id = '')
	 * 	}
	 * </pre>
	 * 
	 * @param userId
	 * @param roleId
	 * @param organId
	 * @param organRoleId
	 * @return modMenuMap:{"first" : List, "second" : Map, "third" : Map, "row" : String}
	 * @author rutine
	 * @time Oct 20, 2012 8:53:48 PM
	 */
	Map findUserPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId);

	/**
	 * <p>保存角色或用户的权限</p>
	 * 
	 * @param modOptIds 模块操作关系IDS
	 * @param ownerId 用户或角色ID
	 * @param privilegeType 权限类型
	 * @param ownerType 为用户或角色标识
	 * @param privilegeScope
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 8:54:41 PM
	 */
	void savePrivilege(List<String> modOptIds, long ownerId,
			String privilegeType, String ownerType, String privilegeScope,
			HttpServletRequest request) throws ApplicationException;
	
	/**
	 * 转换模块菜单对象为模块菜单树节点
	 *
	 * @param moduleMemuList 模块菜单对象集合
	 * @return List 模块菜单树节点集合
	 * @author rutine
	 * @time Sep 8, 2013 2:19:14 PM
	 */
	List<TreeVoExtend> convertToTree(List<SysplModuleMemu> moduleMemuList);
}

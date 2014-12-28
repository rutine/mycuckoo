package com.mycuckoo.service.iface.uum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 角色业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:31:12 AM
 * @version 2.0.0
 */
public interface RoleService {

	/**
	 * <p>停用/启用角色</p>
	 * 
	 * @param roleId 角色ID
	 * @param disEnableFlag 停用启用标志
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 9:25:09 PM
	 */
	void disEnableRole(long roleId, String disEnableFlag, HttpServletRequest request) 
			throws ApplicationException;

	/**
	 * <p>判断角色名称是否存在</p>
	 * 
	 * @param roleName 角色名称
	 * @return boolean true 存在 false不存在
	 * @author rutine
	 * @time Oct 17, 2012 9:27:21 PM
	 */
	boolean existRoleByRoleName(String roleName);

	/**
	 * <p>返回所有角色记录</p>
	 * 
	 * @return List<UumRole> 所有角色列表
	 * @author rutine
	 * @time Oct 17, 2012 9:25:30 PM
	 */
	List<UumRole> findAllRoles();

	/**
	 * 根据条件分页查询角色
	 * 
	 * @param roleName 角色名称
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 17, 2012 9:26:13 PM
	 */
	Page<UumRole> findRoleByCon(String roleName, Pageable page);

	/**
	 * <p>根据角色<code>ID</code>获取角色记录
	 * 
	 * @param roleId 角色ID
	 * @return UumRole 角色对象
	 * @author rutine
	 * @time Oct 17, 2012 9:26:39 PM
	 */
	UumRole getRoleByRoleId(long roleId);

	/**
	 * <p>修改角色</p>
	 * 
	 * @param uumRole 角色对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 9:27:38 PM
	 */
	void updateRole(UumRole uumRole, HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存角色</p>
	 * 
	 * @param uumRole 角色对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 9:27:53 PM
	 */
	void saveRole(UumRole uumRole, HttpServletRequest request) throws ApplicationException;
}

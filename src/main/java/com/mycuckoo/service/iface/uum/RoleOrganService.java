package com.mycuckoo.service.iface.uum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 机构角色业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:30:01 AM
 * @version 2.0.0
 */
public interface RoleOrganService {

	/**
	 * <p>统计机构下的角色数量</p>
	 * 
	 * @param orgId 机构ID
	 * @return int 机构下的角色数量
	 * @author rutine
	 * @time Oct 17, 2012 10:31:41 PM
	 */
	int countOrgRoleRefsByOrgId(long orgId);

	/**
	 * <p>根据机构<code>ID</code>和角色<code>ID</code>删除机构角色关系记录</p>
	 * 
	 * @param orgId 机构ID
	 * @param roleIds 角色ID
	 * @return boolean true 角色下有用户 false 角色下无用户
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 9:58:40 PM
	 */
	boolean deleteOrgRoleRef(long orgId, List<Long> roleIds,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据角色<code>ID</code>删除机构角色记录，为停用角色所用</p>
	 * 
	 * @param roleId 角色ID
	 * @author rutine
	 * @time Oct 17, 2012 10:01:26 PM
	 */
	void deleteOrgRoleRefByRoleId(long roleId, HttpServletRequest request);

	/**
	 * <p>根据机构<code>ID</code>和角色<code>ID</code>获取机构角色记录</p>
	 * 
	 * @param orgId 机构ID
	 * @param roleId 角色ID
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 10:11:46 PM
	 */
	UumOrgRoleRef getOrgRoleRefByOrgRoleId(long orgId, long roleId) throws ApplicationException;

	/**
	 * <p>根据机构角色<code>ID</code>获取机构角色记录</p>
	 * 
	 * @param orgRoleId
	 * @return UumOrgRoleRef 机构角色关系记录
	 * @author rutine
	 * @time Oct 17, 2012 10:23:45 PM
	 */
	UumOrgRoleRef getOrgRoleRefById(long orgRoleId);

	/**
	 * <p>根据机构角色<code>IDs</code>查询机构角色记录</p>
	 * 
	 * @param orgRoleIds
	 * @return
	 * @author rutine
	 * @time Oct 17, 2012 10:24:56 PM
	 */
	List<UumOrgRoleRef> findOrgRoleRefByOrgRoleIds(Long[] orgRoleRefIds);

	/**
	 * <p>根据角色<code>ID</code>查询所有机构角色<code>ID</code></p>
	 * 
	 * @param roleId
	 * @return
	 * @author rutine
	 * @time Oct 17, 2012 10:26:17 PM
	 */
	List<Long> findOrgRoleRefIdsByRoleId(Long roleId);

	/**
	 * <p>根据机构<code>ID</code>查询角色记录</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[
	 * 		{
	 * 			"orgRoleId"    : {@code Long}, 
	 * 			"uumRole"      : {@code List<UumRole>}
	 *		},
	 *		...
	 *	]
	 * </pre>
	 * 
	 * @param orgId 机构ID
	 * @return
	 * @author rutine
	 * @time Oct 17, 2012 10:12:32 PM
	 */
	List<UumOrgRoleRef> findRolesByOrgId(long orgId);

	/**
	 * <p>根据机构<code>ID</code>和角色名查询已分配的角色</p>
	 * 
	 * @param orgId 机构ID
	 * @param roleName 角色名称
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 17, 2012 10:18:49 PM
	 */
	Page<UumRole> findSelectedRolesByOrgId(long orgId, String roleName, Pageable page);

	/**
	 * <p>根据机构<code>ID</code>查询未分配的角色</p>
	 * 
	 * @param orgId 机构ID
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 17, 2012 10:22:28 PM
	 */
	Page<UumRole> findUnselectedRolesByOrgId(long orgId, Pageable page);

	/**
	 * <p>保存为机构分配的角色</p>
	 * 
	 * @param orgId 机构ID
	 * @param roleIds 角色ID
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 17, 2012 10:32:09 PM
	 */
	void saveOrgRoleRef(long orgId, List<Long> roleIds,
			HttpServletRequest request) throws ApplicationException;

}

package com.mycuckoo.service.iface.commonused;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.domain.vo.TreeVoExtend;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.iface.login.LoginService;
import com.mycuckoo.service.iface.uum.PrivilegeService;
import com.mycuckoo.service.iface.uum.RoleUserService;
import com.mycuckoo.service.iface.uum.UserAgentService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 统一用户对外公共接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:22:33 AM
 * @version 2.0.0
 */
public interface UumCommonService {

	/**
	 * 删除模块操作关系时同时也删除模块操作权限<br>
	 * 
	 * @param modOptRefIds
	 * @author rutine
	 * @time Oct 24, 2012 8:20:52 PM
	 */
	void deletePrivilegeByModOptId(String[] modOptRefIds, HttpServletRequest request);

	/**
	 * 删除用户无用常用功能<br>
	 * 
	 * @param uumUserCommfunList 用户常用功能列表
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 24, 2012 8:21:12 PM
	 */
	void deleteUserCommFunById(List<UumUserCommfun> uumUserCommfunList,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据代理ID查询代理的权限</p>
	 * 
	 * @param agentId
	 * @return
	 * @author rutine
	 * @time Oct 24, 2012 8:21:34 PM
	 */
	List<String> findAgentPrivilegeByAgentId(Long agentId);

	/**
	 * <p>根据用户ID查找用户的所有角色 
	 * 源自：{@link RoleUserService#findRoleUserRefsByUserId(long)} 
	 * 应用：{@link LoginService#obtainRoleUserRefByUserId(long)}</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	查看提供者 {@link RoleUserService#findRoleUserRefsByUserId(long)}
	 * </pre>
	 * 
	 * @param userId 用户主键ID
	 * @return 用户角色关系记录
	 * @author rutine
	 * @time Oct 24, 2012 8:21:56 PM
	 */
	List<UumRoleUserRef> findRoleUsersByUserId(long userId);

	/**
	 * <p>根据用户ID查询代理记录</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	查看提供者 {@link UserAgentService#findUserAgentsByAgentUserId(long)}
	 * </pre>
	 * 
	 * @param userId
	 * @return
	 * @author rutine
	 * @time Oct 24, 2012 8:26:09 PM
	 */
	List<UumUserAgent> findUserAgentsByAgentUserId(long userId);

	/**
	 * <p>根据用户code和密码获得用户信息, 
	 * 		源自：{@link UserService#getUserByUserCodeAndPwd(String, String)}, 
	 * 		应用：{@link LoginService#getUserByUserCodePwd(String, String)}</p>
	 * 
	 * <pre>
	 * 	<b>数据结构: </b> 
	 * 	查看提供者 {@link UserService#getUserByUserCodeAndPwd(String, String)}
	 * </pre>
	 * 
	 * @param userCode 用户登录名
	 * @param password 用户密码
	 * @return UumUser 用户对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 24, 2012 8:22:24 PM
	 */
	UumUser getUserByUserCodeAPwd(String userCode, String password) throws ApplicationException;

	/**
	 * 根据用户ID查询用户常用功能<br>
	 * 
	 * @param userId 用户ID
	 * @return
	 * @author rutine
	 * @time Oct 24, 2012 8:22:55 PM
	 */
	List<UumUserCommfun> findUserCommFunsByUserId(long userId);
	
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
	 * 	查看提供者 {@link PrivilegeService#findUserPrivilegesForAgentLogin(long, long, long, long, List)}
	 * </pre>
	 * 
	 * @param userId
	 * @param roleId
	 * @param organId
	 * @param organRoleId
	 * @param privilegeIdList
	 * @return
	 * @author rutine
	 * @time Oct 24, 2012 8:23:56 PM
	 */
	Map findUserPrivilegesForAgentLogin(long userId, long roleId, long organId,
			long organRoleId, List privilegeIdList);

	/**
	 * <p>查询用户的权限 
	 * 	源自：{@link PrivilegeService#findUserPrivilegesForUserLogin(long, long, long, long)} 
	 * 	应用：{@link LoginService#filterPrivilege(Long, Long, Long, Long, String, Long, HttpServletRequest)}</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	查看提供者 {@link PrivilegeService#findUserPrivilegesForUserLogin(long, long, long, long)}
	 * </pre>
	 * 
	 * @param userId 用户ID
	 * @param roleId 角色ID
	 * @param organId
	 * @param organRoleId
	 * @return
	 * @author rutine
	 * @time Oct 24, 2012 8:24:26 PM
	 */
	Map findUserPrivilegesForUserLogin(long userId, long roleId, long organId, long organRoleId);

	/**
	 * 查询判断用户是否有特殊操作权限 源自：PrivilegeService 应用：LoginServiceImpl<br>
	 * 
	 * @param userId 用户或角色ID
	 * @return true 有 false 无
	 * @author rutine
	 * @time Oct 24, 2012 8:23:27 PM
	 */
	boolean existsSpecialPrivilegeForUser(long userId);
	
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

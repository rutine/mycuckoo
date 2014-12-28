package com.mycuckoo.service.iface.login;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.iface.uum.RoleUserService;
import com.mycuckoo.service.iface.uum.UserAgentService;
import com.mycuckoo.service.iface.uum.UserService;

/**
 * 功能说明: 用户登录service
 * 
 * @author rutine
 * @time Sep 24, 2014 11:02:35 AM
 * @version 2.0.0
 */
public interface LoginService {

	/**
	 * 删除用户无用常用功能
	 * 
	 * @param uumUserCommfunList 用户常用功能列表
	 * @throws ApplicationException
	 * @author rutine
	 * @time Nov 7, 2012 8:40:02 PM
	 */
	void deleteUserCommFunById(List<UumUserCommfun> uumUserCommfunList,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * 根据用户ID查询用户常用功能
	 * 
	 * @param userId 用户ID
	 * @return
	 * @author rutine
	 * @time Nov 7, 2012 8:41:14 PM
	 */
	List<UumUserCommfun> findUserCommFunsByUserId(long userId);

	/**
	 * 判断用户是否为管理员,管理员信息指定在配置文件中
	 * 
	 * @param userCode
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Nov 7, 2012 8:41:33 PM
	 */
	boolean isAdministrator(String userCode) throws ApplicationException;

	/**
	 * <p>根据用户code和密码获得用户信息</p>
	 * 
	 * <pre>
	 * 	<b>有效的数据结构: </b> 
	 * 	查看提供者 {@link UserService#getUserByUserCodeAndPwd(String, String)}
	 * </pre>
	 * 
	 * @param userCode 用户登录输入的名称
	 * @param password 用户密码
	 * @return UumUser 查询用户对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Nov 7, 2012 8:42:23 PM
	 */
	UumUser getUserByUserCodePwd(String userCode, String password) throws ApplicationException;

	/**
	 * <p>预登录 代理 多角色</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		roleList: { 查看提供者 {@link RoleUserService#findRoleUserRefsByUserId(long)} },
	 * 		userAgentList: { 查看提供者 {@link UserAgentService#findUserAgentsByAgentUserId(long)} }
	 * 	}
	 * </pre>
	 * 
	 * @param userCode
	 * @throws ApplicationException
	 * @return
	 * @author rutine
	 * @time Nov 7, 2012 8:42:56 PM
	 */
	Map preLogin(String userCode) throws ApplicationException;

	/**
	 * <p>权限过滤</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	{
	 * 		{@code "first"}     : {@code List<SysplModuelMenu>},
	 * 		{@code "second"}    : {@code Map<String, List<SysplModuleMemu>>}(key键值是上级模块id),
	 * 		{@code "third"}     : {@code Map<String, List<SysplModuleMemu>>}(key键值是上级模块id),
	 * 		{@code "fourth"}    : {@code Map<String, List<SysplModuleMemu>>}(四级操作按钮)
	 * 		{@code "row"}       : {@code String}(sql条件语句: and id = '')
	 * 		{@code "commonFun"} : {@code List<SysplModuleMemu>}
	 * 	}
	 * </pre>
	 * 
	 * @param userId
	 * @param roleId
	 * @param organId
	 * @param organRoleId
	 * @param userCode
	 * @param agentId
	 * @return moduleMenuMap:{"first" : List, "second" : Map, "third" : Map, "fourth" : Map, "row" : String, "commonFun" : List}
	 * @throws ApplicationException
	 * @author rutine
	 * @time Nov 7, 2012 8:43:21 PM
	 */
	Map filterPrivilege(Long userId, Long roleId, Long organId,
			Long organRoleId, String userCode, Long agentId,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * 保存日志(公用)
	 *
	 * @param level 级别
	 * @param optModName 模块名称
	 * @param optName 操作名称
	 * @param optContent 内容
	 * @param optBusinessId 业务id
	 * @param request
	 * @throws ApplicationException
	 */
	void saveLog(String level, String optModName, String optName,
			String optContent, String optBusinessId, HttpServletRequest request)
			throws ApplicationException;

}

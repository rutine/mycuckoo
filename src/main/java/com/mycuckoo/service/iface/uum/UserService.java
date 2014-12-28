package com.mycuckoo.service.iface.uum;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 用户业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:33:32 AM
 * @version 2.0.0
 */
public interface UserService {

	/**
	 * <p>停用/启用用户 删除用户所属角色并移到无角色用户 删除用户所拥有操作权限</p>
	 * 
	 * @param userId 用户ID
	 * @param disEnableFlag 停用/启用标志
	 * @return boolean true成功
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 3:50:29 PM
	 */
	boolean disEnableUser(long userId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>返回所有用户信息</p>
	 * 
	 * @return List<UumUser> 所有用户列表
	 * @author rutine
	 * @time Oct 20, 2012 3:51:14 PM
	 */
	List<UumUser> findAllUsers();

	/**
	 * <p>根据用户名模糊用户信息</p>
	 * 
	 * @param userName
	 * @return
	 * @author rutine
	 * @time Oct 20, 2012 3:51:34 PM
	 */
	List<UumUser> findUsersByUserName(String userName);

	/**
	 * <p>根据拼音代码查询用户信息</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[
	 * 		{
	 * 			userId, userName
	 * 		}
	 * 	]
	 * </pre>
	 * 
	 * @param userNamePy
	 * @param userId
	 * @return
	 * @author rutine
	 * @time Oct 20, 2012 3:51:54 PM
	 */
	List findUsersByUserNamePy(String userNamePy, long userId);

	/**
	 * <p>根据机构/角色<code>ID</code>查询直接下级机构/角色</p>
	 * 
	 * @param treeId 机构或角色id
	 * @param isCheckbox 是否需要checkbox
	 * @return List<UumOrgan> 所有机构和角色
	 * @author rutine
	 * @time Oct 20, 2012 3:52:07 PM
	 */
	List<? extends TreeVo> findNextLevelChildNodes(String treeId, String isCheckbox);

	/**
	 * <p>根据条件分页查询用户</p>
	 * 
	 * @param treeId 格式：id_1:机构 id_2:角色 查询某个树的节点
	 * @param userName 用户名称
	 * @param userCode 用户code
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 20, 2012 3:53:19 PM
	 */
	Page<UumUser> findUsersByCon(String treeId, String userName, String userCode, Pageable page);

	/**
	 * <p>根据机构<code>ID</code>查询用户信息</p>
	 * 
	 * @param orgId 机构<code>ID</code>
	 * @return
	 * @author rutine
	 * @time Oct 20, 2012 3:56:46 PM
	 */
	List<UumUser> findUsersByOrgId(long orgId);

	/**
	 * <p>根据用户code和密码获得用户信息</p>
	 * 
	 * <pre>
	 * 	<b>有效的数据结构: </b> 
	 * 	{ 
	 * 		userId, uumOrgan.orgId, userCode, userNamePy, userName, 
	 * 		userPassword, userGender, userPosition, userPhotoUrl, userQq, 
	 * 		userMsn, userMobile, userMobile2, userOfficeTel, userFamilyTel, 
	 * 		userAddress, userEmail, userAvidate, userIsAgent, memo, 
	 * 		status, creator, createDate 
	 * 	} 
	 * </pre>
	 * 
	 * 
	 * @param userCode 用户登录名
	 * @param password 用户密码
	 * @return UumUser 用户对象
	 * @throws ApplicationException 用户编码重复异常
	 * @author rutine
	 * @time Oct 20, 2012 3:54:37 PM
	 */
	UumUser getUserByUserCodeAndPwd(String userCode, String password) throws ApplicationException;

	/**
	 * <p>根据用户<code>ID</code>获取用户</p>
	 * 
	 * @param userId 用户ID
	 * @return UumUser 用户对象
	 * @author rutine
	 * @time Oct 20, 2012 3:54:59 PM
	 */
	UumUser getUserByUserId(long userId);

	/**
	 * <p>根据用户IDs查询用户信息</p>
	 * 
	 * <pre>
	 * 	<b>数据结构:</b>
	 * 	[
	 * 		{
	 * 			userId, userName
	 * 		}
	 * 	]
	 * </pre>
	 * 
	 * @param userIds
	 * @return
	 * @author rutine
	 * @time Oct 20, 2012 3:57:16 PM
	 */
	List findUsersByUserIds(Long[] userIds);

	/**
	 * <p>查询用户信息为管理员分配</p>
	 * 
	 * @param userName 用户名称
	 * @param userCode 用户code
	 * @param page
	 * @return page
	 * 
	 * @author rutine
	 * @time Oct 20, 2012 3:55:54 PM
	 */
	// @return map 
	// 1. userList(key) -> List<UumUser>(value), 
	// 2. userCount -> Integer <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	// 3. systemAdminUserList -> List<UumUser>, 
	// 4. systemAdminUserCount -> Integer
	Page<UumUser> findUsersForSetAdmin(String userName, String userCode, Pageable page);

	/**
	 * <p>查询所有管理员</p>
	 * 
	 * @return
	 * @author rutine
	 * @time Nov 24, 2013 9:24:46 AM
	 */
	Page<UumUser> findAdminUsers();

	/**
	 * <p>判断用户号是否存在</p>
	 * 
	 * @param userCode 用户登录名
	 * @return boolean true用户名存在 false不存在
	 * @author rutine
	 * @time Oct 20, 2012 3:54:13 PM
	 */
	boolean existsUserByUserCode(String userCode);

	/**
	 * <p>修改用户</p>
	 * 
	 * @param uumUser 用户对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 3:57:34 PM
	 */
	void updateUser(UumUser uumUser, HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>当为用户分配多个角色时，更改用户的默认角色</p>
	 * 
	 * @param organId
	 * @param userId
	 * @author rutine
	 * @time Oct 20, 2012 3:57:52 PM
	 */
	void updateUserBelongOrgIdAssignRole(long organId, long userId, HttpServletRequest request);

	/**
	 * <p>根据用户ID修改用户号 用户名 用户密码</p>
	 * 
	 * @param userId 用户ID
	 * @param userCode 用户号
	 * @param userName 用户名
	 * @param password 用户密码
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 3:58:30 PM
	 */
	void updateUserInfo(UumUser uumUser, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>修改用户的照片URL</p>
	 * 
	 * @param photoUrl 用户照片URL
	 * @param userId 用户ID
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 3:59:00 PM
	 */
	void updateUserPhotoUrl(String photoUrl, long userId, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>根据用户<code>ID</code>重置用户密码</p>
	 * 
	 * @param userDefaultPwd 用户默认密码
	 * @param userName 用户名
	 * @param userId 用户ID
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 3:59:29 PM
	 */
	void resetPwdByUserId(String userDefaultPwd, String userName, long userId, 
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存用户</p>
	 * 
	 * @param uumUser 用户对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 20, 2012 3:59:47 PM
	 */
	void saveUser(UumUser uumUser, HttpServletRequest request) throws ApplicationException;
}

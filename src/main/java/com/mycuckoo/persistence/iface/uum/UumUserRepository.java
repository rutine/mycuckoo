package com.mycuckoo.persistence.iface.uum;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 用户持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:51:46 AM
 * @version 2.0.0
 */
public interface UumUserRepository extends Repository<UumUser, Long> {

	/**
	 * <p>根据机构ID查询用户</p>
	 * 
	 * @param belongOrgId 用户属于的机构ID
	 * @return 机构关联的所有用户
	 */
	List<UumUser> findUsersByOrgId(Long belongOrgId);

	/**
	 * 
	 * <p>通过用户代码和用户名称进行模糊查询用户记录</p>
	 * 
	 * @param userCode 用户代码
	 * @param userName 用户名称
	 * @return 用户对象列表
	 */
	List<UumUser> findUsersByCodeAndName(String userCode, String userName);

	/**
	 * 
	 * <p>根据条件分页查询用户</p>
	 * 
	 * @param treeId 树节点, 如: 1-0
	 * @param orgIds 机构ids
	 * @param userCode 用户代码
	 * @param userName 用户名称
	 * @param page 分页
	 * @return
	 */
	Page<UumUser> findUsersByCon(String treeId, Long[] orgIds, 
			String userCode, String userName, Pageable page);

	/**
	 * 
	 * <p>判断用户号是否存在</p>
	 * 
	 * @param userCode 用户代码
	 * @return true 用户号重复
	 */
	boolean existsUserByUserCode(String userCode);

	/**
	 * 
	 * <p>根据用户号和用户密码获取用户信息</p>
	 * 
	 * @param userCode 用户号
	 * @param password 用户密码
	 * @return 用户
	 */
	UumUser getUserByUserCodeAndPwd(String userCode, String password);

	/**
	 * <p>当为用户分配多个角色时，更改用户的默认角色</p>
	 * 
	 * @param organId
	 * @param userId
	 */
	void updateUserBelongOrgIdByUserId(long organId, long userId);

	/**
	 * <p>根据用户ID重置用户密码</p>
	 * 
	 * @param defaultPassword
	 * @param userId
	 */
	void updateUserPwdByUserId(String defaultPassword, long userId);

	/**
	 * <p>修改用户照片URL</p>
	 * 
	 * @param photoUrl 用户照片
	 * @param userId
	 */
	void updateUserPhotoUrlByUserId(String photoUrl, long userId);

	/**
	 * <p>根据用户名模糊查询用户信息</p>
	 * 
	 * @param userName
	 * @return
	 */
	List<UumUser> findUsersByUserName(String userName);

	/**
	 * <p>根据拼音代码查询用户信息, 返回值只有用户id和用户名称</p>
	 * 
	 * <pre>
	 * 	[
	 * 		{
	 * 			userId, userName
	 * 		},
	 * 		...
	 * 	]
	 * </pre>
	 * 
	 * @param userNamePy
	 * @param userId
	 * @return
	 */
	List findUsersByUserNamePy(String userNamePy, long userId);

	/**
	 * <p>根据用户IDs查询用户信息, 返回值只有用户id和用户名称</p>
	 * 
	 * <pre>
	 * 	[
	 * 		{
	 * 			userId, userName
	 * 		},
	 * 		...
	 * 	]
	 * </pre>
	 * 
	 * @param userIds
	 * @return
	 */
	List findUsersByUserIds(Long[] userIds);
}

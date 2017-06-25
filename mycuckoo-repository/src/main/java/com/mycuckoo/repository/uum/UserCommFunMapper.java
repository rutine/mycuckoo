package com.mycuckoo.repository.uum;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.uum.UserCommfun;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;

/**
 * 功能说明: 用户常用功能持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:49:28 AM
 * @version 3.0.0
 */
public interface UserCommFunMapper {

	/**
	 * 保存用户常用功能
	 * 
	 * @param uumUserCommfun 用户常用功能对象
	 * @return
	 */
	int save(UserCommfun userCommfun);

	/**
	 * 修改用户常用功能
	 * 
	 * @param newUserCommfun 新的用户常用功能对象
	 * @param oldUserCommfun 旧的用户常用功能对象
	 * @return
	 */
	int update(@Param("newUserCommfun") UserCommfun newUserCommfun, @Param("oldUserCommfun") UserCommfun oldUserCommfun);
	
	/**
	 * 删除用户常用功能
	 * 
	 * @param userCommfun
	 */
	void delete(UserCommfun userCommfun);
	
	/**
	 * 获取用户常用功能
	 * 
	 * @param UumUserCommfun 用户常用功能对象
	 * @return 返回对应实体对象或null
	 * @throws
	 */
	UserCommfun get(UserCommfun userCommfun);
	
	/**
	 * 判断用户常用功能是否存在
	 * 
	 * @param userCommfun 实体对象
	 * @return true 如果找到对应的实体, 否则为false
	 * @throws
	 */
	boolean exists(UserCommfun userCommfun);
	
	/**
	 * 返回符合 {@code Pageable} object 限制的页面{@link Page} .
	 * 
	 * @param pageable
	 * @return a page of entities
	 */
	Page<UserCommfun> findByPage(Pageable pageable);
	
	/**
	 * 统计用户常用功能数量
	 * 
	 * @return the number of entities
	 */
	long count();
	
	/**
	 * 根据ID删除用户所有常用功能
	 * 
	 * @param id userId or moduleId 用户ID或模块ID
	 * @author rutine
	 * @time Oct 9, 2012 10:01:35 PM
	 */
	void deleteByUserId(long userId);
	
	/**
	 * 根据用户ID查询用户常用功能
	 * 
	 * @param userId
	 * @return
	 * @author rutine
	 * @time Oct 9, 2012 10:02:53 PM
	 */
	List<UserCommfun> findByUserId(long userId);
}

package com.mycuckoo.persistence.iface.uum;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.domain.uum.UumUserCommfun;

/**
 * 功能说明: 用户常用功能持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:49:28 AM
 * @version 2.0.0
 */
public interface UumUserCommFunRepository {

	/**
	 * 统计用户常用功能数量
	 * 
	 * @return the number of entities
	 */
	long count();

	/**
	 * 删除用户常用功能
	 * 
	 * @param uumUserCommfun
	 */
	void delete(UumUserCommfun uumUserCommfun);

	/**
	 * 删除所有用户常用功能
	 * 
	 * 此方法慎用.
	 */
	void deleteAll();

	/**
	 * 根据ID删除用户所有常用功能
	 * 
	 * @param id userId or moduleId 用户ID或模块ID
	 * @author rutine
	 * @time Oct 9, 2012 10:01:35 PM
	 */
	void deleteUserCommFun(long userId);

	/**
	 * 判断用户常用功能是否存在
	 * 
	 * @param uumUserCommfun 实体对象
	 * @return true 如果找到对应的实体, 否则为false
	 * @throws
	 */
	boolean exists(UumUserCommfun uumUserCommfun);

	/**
	 * 返回所有用户常用功能
	 * 
	 * @return
	 */
	Iterable<UumUserCommfun> findAll();

	/**
	 * 返回符合 {@code Pageable} object 限制的页面{@link Page} .
	 * 
	 * @param pageable
	 * @return a page of entities
	 */
	Page<UumUserCommfun> findAll(Pageable pageable);

	/**
	 * 根据用户ID查询用户常用功能
	 * 
	 * @param userId
	 * @return
	 * @author rutine
	 * @time Oct 9, 2012 10:02:53 PM
	 */
	List<UumUserCommfun> findUserCommFunByUserId(long userId);

	/**
	 * 获取用户常用功能
	 * 
	 * @param UumUserCommfun 用户常用功能对象
	 * @return 返回对应实体对象或null
	 * @throws
	 */
	UumUserCommfun get(UumUserCommfun uumUserCommfun);

	/**
	 * 保存用户常用功能
	 * 
	 * @param entities
	 * @return 被保存的用户常用功能
	 */
	Iterable<UumUserCommfun> save(Iterable<UumUserCommfun> entities);

	/**
	 * 保存用户常用功能
	 * 
	 * @param uumUserCommfun 用户常用功能对象
	 * @return
	 */
	UumUserCommfun save(UumUserCommfun uumUserCommfun);

	/**
	 * 修改用户常用功能
	 * 
	 * @param newUserCommfun 新的用户常用功能对象
	 * @param oldUserCommfun 旧的用户常用功能对象
	 * @return
	 */
	int update(UumUserCommfun newUserCommfun, UumUserCommfun oldUserCommfun);
}

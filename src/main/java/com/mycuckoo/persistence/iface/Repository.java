package com.mycuckoo.persistence.iface;

import java.io.Serializable;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;

/**
 * 功能说明: 持久层顶级接口
 *
 * @author rutine
 * @time Sep 23, 2014 8:05:27 PM
 * @version 2.0.0
 */
public interface Repository<T, ID extends Serializable> {

	/**
	 * 保存所给的实体对象. 返回受保存操作所影响的实体以便进行更深的操作
	 * 
	 * @param entity
	 * @return 被保存实体对象
	 */
	<S extends T> S save(S entity);

	/**
	 * 保存所给的所有实体
	 * 
	 * @param entities
	 * @return 被保存的所有实体对象
	 */
	<S extends T> Iterable<S> save(Iterable<S> entities);

	/**
	 * 更新所给的实体对象. 返回受保存操作所影响的实体以便进行更深的操作
	 * 
	 * @param entity
	 * @return 被更新实体对象
	 */
	<S extends T> int update(S entity);

	/**
	 * 更新所给的所有实体
	 * 
	 * @param entities
	 * @return 被更新的所有实体对象
	 */
	<S extends T> int update(Iterable<S> entities);

	/**
	 * 通过id查询实体对象
	 * 
	 * @param id
	 *            不能为null
	 * @return 返回所给id的对应实体对象或null
	 * @throws
	 */
	T get(ID id);

	/**
	 * 判断是否存在所给id的实体对象
	 * 
	 * @param id 不能为null
	 * @return true 如果id对应的实体, 否则为false
	 * @throws
	 */
	boolean exists(ID id);

	/**
	 * 返回所有实体对象
	 * 
	 * @return
	 */
	Iterable<T> findAll();

	/**
	 * 返回符合 {@code Pageable} object 限制的页面{@link Page} .
	 * 
	 * @param pageable
	 * @return a page of entities
	 */
	Page<T> findAll(Pageable pageable);

	/**
	 * 返回所有实体对象数量
	 * 
	 * @return the number of entities
	 */
	long count();

	/**
	 * 删除所给id对应的实体对象
	 * 
	 * @param id 不能null
	 * @throws IllegalArgumentException 如果 {@code id} 是 {@literal null}
	 */
	void delete(ID id);

	/**
	 * 删除实体对象
	 * 
	 * @param entity
	 * @throws IllegalArgumentException 如果 {@code id} 是 {@literal null}
	 */
	void delete(T entity);

	/**
	 * 删除所有给定的实体对象
	 * 
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is (@literal null}.
	 */
	void delete(Iterable<? extends T> entities);

	/**
	 * 删除所有的实体对象. 此方法慎用.
	 */
	void deleteAll();
}

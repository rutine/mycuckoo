package com.mycuckoo.core.repository;

import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.Pageable;

import java.io.Serializable;
import java.util.Map;

/**
 * 功能说明: 持久层顶级接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 23, 2014 8:05:27 PM
 */
public interface Repository<T, ID extends Serializable> {

    /**
     * 保存所给的实体对象. 返回受保存操作所影响的实体以便进行更深的操作
     *
     * @param entity
     * @return 被保存实体对象
     */
    <S extends T> int save(S entity);

    /**
     * 更新所给的实体对象. 返回受保存操作所影响的实体以便进行更深的操作
     *
     * @param entity
     * @return 被更新实体对象
     */
    <S extends T> int update(S entity);

    /**
     * 删除所给id对应的实体对象
     *
     * @param id 不能null
     * @throws IllegalArgumentException 如果 {@code id} 是 {@literal null}
     */
    void delete(ID id);

    /**
     * 通过id查询实体对象
     *
     * @param id 不能为null
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
     * 返回符合 {@code Pageable} object 限制的页面{@link Page} .
     *
     * @param params   过滤参数
     * @param pageable
     * @return a page of entities
     */
    Page<T> findByPage(Map<String, Object> params, Pageable pageable);
}

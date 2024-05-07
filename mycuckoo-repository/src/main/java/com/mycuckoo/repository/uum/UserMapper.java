package com.mycuckoo.repository.uum;

import com.mycuckoo.domain.uum.User;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.Repository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 用户持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:51:46 AM
 */
public interface UserMapper extends Repository<User, Long> {

    /**
     * <p>通过用户代码和用户名称进行模糊查询用户记录</p>
     *
     * @param code 用户代码 like '%keyword%'
     * @param name 用户名称 like '%keyword%'
     * @return 用户对象列表
     */
    List<User> findByCodeAndName(@Param("code") String code, @Param("name") String name);

    /**
     * <p>根据条件分页查询用户</p>
     *
     * @param orgRoleId 树节点, 如: 1-0
     * @param orgIds    机构ids
     * @param code  用户代码 like '%keyword%'
     * @param name  用户名称 like '%keyword%'
     * @param page      分页
     * @return
     */
    Page<User> findByPage2(
            @Param("orgRoleId") Long orgRoleId,
            @Param("orgIds") Long[] orgIds,
            @Param("code") String code,
            @Param("name") String name, Pageable page);

    /**
     * <p>判断用户号是否存在</p>
     *
     * @param code 用户代码
     * @return true 用户号重复
     */
    boolean existsByUserCode(String code);

    /**
     * <p>根据用户号获取用户信息</p>
     *
     * @param code     用户号
     * @return 用户
     */
    User getByUserCode(@Param("code") String code);

    /**
     * <p>根据拼音代码查询用户信息, 返回值只有用户id和用户名称</p>
     * <p>
     * <pre>
     * 	[
     *        {
     * 			userId, name
     *        },
     * 		...
     * 	]
     * </pre>
     *
     * @param pinyin
     * @param userId
     * @return
     */
    List findByPinyin(@Param("pinyin") String pinyin, @Param("userId") long userId);

    /**
     * <p>根据用户IDs查询用户信息, 返回值只有用户id和用户名称</p>
     * <p>
     * <pre>
     * 	[
     *        {
     * 			userId, name
     *        },
     * 		...
     * 	]
     * </pre>
     *
     * @param userIds
     * @return
     */
    List<User> findByUserIds(Long[] userIds);
}

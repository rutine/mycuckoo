package com.mycuckoo.repository.uum;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.core.repository.annotation.PreAuth;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 功能说明: 用户持久层接口
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 24, 2014 10:51:46 AM
 */
@PreAuth(table = "uum_user")
public interface UserMapper extends Repository<User, Long> {

    /**
     * <p>通过用户名称进行模糊查询用户记录</p>
     *
     * @param name 用户名称 like '%keyword%'
     * @return 用户对象列表
     */
    List<User> findByName(@Param("name") String name);

    /**
     * <p>根据拼音代码查询用户信息, 返回值只有用户id和用户名称</p>
     * <pre>
     * 	[{userId, name}, ... ]
     * </pre>
     *
     * @param pinyin
     * @param userId
     * @return
     */
    List findByPinyin(@Param("pinyin") String pinyin, @Param("userId") long userId);

    /**
     * <p>根据用户IDs查询用户信息, 返回值只有用户id和用户名称</p>
     * <pre>
     * 	[{userId, name}, ... ]
     * </pre>
     *
     * @param userIds
     * @return
     */
    List<User> findByUserIds(Long[] userIds);

    /**
     * <p>根据账号id查询用户所属组织, 返回值</p>
     * <pre>
     * 	[{userId, orgId, accountId, roleId, deptId, code, name, avidate, status, orgName}, ... ]
     * </pre>
     *
     * @param accountId
     * @return
     */
    List<UserExtend> findByAccountId(Long accountId);

    /**
     * <p>登录成功, 根据账号id和用户id获取用户信息</p>
     *
     * @param accountId
     * @param userId
     * @return
     */
    UserExtend getByAccountIdAndUserId(@Param("accountId") Long accountId, @Param("userId") Long userId);
}

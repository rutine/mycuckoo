package com.mycuckoo.repository.uum;

import com.mycuckoo.core.repository.Repository;
import com.mycuckoo.domain.uum.Account;
import org.apache.ibatis.annotations.Param;

/**
 * 功能说明: 账号持久层接口
 *
 * @author rutine
 * @version 4.1.0
 * @time May 18, 2024 8:52:46 PM
 */
public interface AccountMapper extends Repository<Account, Long> {

    /**
     * <p>通过手机号或邮箱查询记录</p>
     *
     * @param account
     * @param phone
     * @param email
     */
   Account getBy(@Param("account") String account, @Param("phone") String phone, @Param("email") String email);
}

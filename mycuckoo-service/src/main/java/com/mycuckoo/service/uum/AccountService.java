package com.mycuckoo.service.uum;

import com.mycuckoo.domain.uum.Account;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.uum.AccountMapper;
import com.mycuckoo.utils.CommonUtils;
import com.mycuckoo.utils.PwdCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 功能说明: 账号业务类
 *
 * @author rutine
 * @version 4.1.0
 * @time Sep 25, 2014 11:37:54 AM
 */
@Service
@Transactional(readOnly = true)
public class AccountService {
    static Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private HelpService helpService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long getAccountIdByPhoneOrEmail(String phone, String email, String password, String ip) {
        if (CommonUtils.isNullOrEmpty(phone) || CommonUtils.isNullOrEmpty(phone)) return null;

        Account account = accountMapper.getByPhoneOrEmail(phone, email);
        if (account == null) {
            throw new ApplicationException("账号不存在!");
        }
        if (account.getErrorCount() >= 3) {
            throw new ApplicationException("密码多次错误, 禁止登录!");
        }
        if (!account.getPassword().equals(password)) {
            helpService.updateErrorLogin(account, ip);
            throw new ApplicationException("用户密码不正确!");
        }

        Account updateEntity = new Account(account.getAccountId());
        updateEntity.setErrorCount(0);
        updateEntity.setIp(ip);
        updateEntity.setLoginDate(new Date());
        accountMapper.update(updateEntity);

        return account.getAccountId();
    }

    @Service
    @Transactional(readOnly = true)
    public static class HelpService {
        @Autowired
        private AccountMapper accountMapper;


        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public void updateErrorLogin(Account account, String ip) {
            Account updateEntity = new Account(account.getAccountId());
            updateEntity.setErrorCount(account.getErrorCount() + 1);
            updateEntity.setIp(ip);
            updateEntity.setLoginDate(new Date());
            accountMapper.update(updateEntity);
        }
    }
}

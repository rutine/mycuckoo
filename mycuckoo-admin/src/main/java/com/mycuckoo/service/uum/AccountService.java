package com.mycuckoo.service.uum;

import com.google.common.collect.Lists;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageImpl;
import com.mycuckoo.core.util.StrUtils;
import com.mycuckoo.core.util.PwdCrypt;
import com.mycuckoo.core.util.SystemConfigXmlParse;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.domain.uum.Account;
import com.mycuckoo.repository.uum.AccountMapper;
import com.mycuckoo.web.vo.res.AccountInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public Account getBy(String account, String password, String ip) {
        if (StrUtils.isEmpty(account)) return null;
        String[] arr = getActAndPhoneAndEmail(account);
        String code = arr[0];
        String phone = arr[1];
        String email = arr[2];

        Account entity = accountMapper.getBy(code, phone, email);
        if (entity == null) {
            throw new MyCuckooException("账号不存在!");
        }
        if (entity.getErrorCount() >= 3) {
            throw new MyCuckooException("密码多次错误, 禁止登录!");
        }
        if (!entity.getPassword().equals(password)) {
            helpService.updateErrorLogin(entity, ip);
            throw new MyCuckooException("用户密码不正确!");
        }

        Account updateEntity = new Account(entity.getAccountId());
        updateEntity.setErrorCount(0);
        updateEntity.setIp(ip);
        updateEntity.setLoginTime(LocalDateTime.now());
        accountMapper.update(updateEntity);

        return entity;
    }

    public Page<AccountInfo> findForSetAdmin(Querier querier) {
        Page<Account> page2 = accountMapper.findByPage(querier.getQ(), querier);
        List<AccountInfo> result = Lists.newArrayList();
        List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
        int count = 0;
        for (Account entity : page2.getContent()) {
            String code = entity.getAccount();
            if (StringUtils.isEmpty(code)) {
                code = entity.getPhone();
            }
            if (StringUtils.isEmpty(code)) {
                code = entity.getEmail();
            }
            if (!systemAdminCode.contains(entity.getAccount())
                    && !systemAdminCode.contains(entity.getPhone())
                    && !systemAdminCode.contains(entity.getEmail())) {
                count++;

                AccountInfo info = new AccountInfo();
                info.setAccountId(entity.getAccountId());
                info.setAccount(code);
                info.setLoginTime(entity.getLoginTime());
                result.add(info);
            }
        }

        return new PageImpl<>(result, querier, page2.getTotalElements() - count);
    }

    public Page<AccountInfo> findAdmins() {
        List<String> systemAdminCode = SystemConfigXmlParse.getInstance().getSystemConfigBean().getSystemMgr();
        List<AccountInfo> result = new ArrayList<>();
        for (String code : systemAdminCode) {
            if (StrUtils.isEmpty(code)) continue;
            String[] arr = getActAndPhoneAndEmail(code);
            String act = arr[0];
            String phone = arr[1];
            String email = arr[2];
            try {
                Account entity = accountMapper.getBy(act, phone, email);
                AccountInfo info = new AccountInfo();
                info.setAccountId(entity.getAccountId());
                info.setAccount(code);
                info.setLoginTime(entity.getLoginTime());
                result.add(info);
            } catch (Exception e) {
                logger.warn("编码'{}'存在重复!", code);
            }
        }

        return new PageImpl<>(result);
    }

    @Transactional
    public void updatePassword(String password, String newPassword) {
        Account old = accountMapper.get(SessionContextHolder.getAccountId());
        Assert.state(password.equals(old.getPassword()), "密码错误");

        Account update = new Account();
        update.setAccountId(SessionContextHolder.getAccountId());
        update.setPassword(StrUtils.encrypt(newPassword));
        update.setUpdateTime(LocalDateTime.now());
        accountMapper.update(update);
    }


    @Transactional
    public long save(String account, String phone, String email, String password) {
        Account old = accountMapper.getBy(account, phone, email);
        Assert.isNull(old, "账号已存在!");

        Account entity = new Account();
        entity.setAccount(account);
        entity.setPhone(phone);
        entity.setEmail(email);
        entity.setPassword(PwdCrypt.getInstance().encrypt(password));
        entity.setIp(SessionContextHolder.getIP());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setCreateTime(LocalDateTime.now());
        accountMapper.save(entity);

        return entity.getAccountId();
    }


    private String[] getActAndPhoneAndEmail(String account) {
        if (account == null || account.trim().equals("")) {
            return null;
        }

        String code = null;
        String phone = null;
        String email = null;
        if (account.indexOf("@") != -1) {
            email = account;
        } else if (account.length() > 8 && account.length() < 15 && NumberUtils.isDigits(account)) {
            phone = account;
        } else {
            code = account;
        }

        return new String[] {code, phone, email};
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
            updateEntity.setLoginTime(LocalDateTime.now());
            accountMapper.update(updateEntity);
        }
    }
}

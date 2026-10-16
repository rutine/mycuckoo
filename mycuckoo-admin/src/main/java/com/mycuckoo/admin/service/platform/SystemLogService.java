package com.mycuckoo.admin.service.platform;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.constant.enums.ModuleName;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.admin.entity.platform.SysLog;
import com.mycuckoo.admin.repository.platform.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * 功能说明: 系统日志业务类
 *
 * @author rutine
 * @version 5.0.0
 * @time 2026/9/12 11:00
 */
@Service
@Transactional(readOnly = true)
public class SystemLogService {

    @Autowired
    private SysLogMapper sysLogMapper;


    @Transactional
    public void deleteLog(int keepdays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -keepdays);

        sysLogMapper.deleteLogger(calendar.getTime());
    }

    public Page<SysLog> findByPage(Querier querier) {
        return sysLogMapper.findByPage(querier.getQ(), querier);
    }

    public String getContentById(long logId) {
        return sysLogMapper.getContentById(logId);
    }

    @Transactional
    public void save(ModuleName module, String bizId, String title, String content) {
        SysLog sysLog = new SysLog();
        sysLog.setTenantId(SessionContextHolder.getTenantId());
        sysLog.setTitle(title);
        sysLog.setContent(content);
        sysLog.setBizType(module.code);
        sysLog.setBizId(bizId);
        sysLog.setIp(SessionContextHolder.getIP());
        sysLog.setUserName(SessionContextHolder.getUserName());
        sysLog.setUserRole(SessionContextHolder.getRoleName());
        sysLog.setCreator(SessionContextHolder.getUserId().toString());
        sysLog.setCreateTime(LocalDateTime.now());

        sysLogMapper.save(sysLog);
    }

}
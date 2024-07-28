package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.SystemConfigBean;
import com.mycuckoo.core.exception.MyCuckooException;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.util.SystemConfigXmlParse;
import com.mycuckoo.core.util.web.SessionContextHolder;
import com.mycuckoo.domain.platform.SysOptLog;
import com.mycuckoo.repository.platform.SysOptLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;

import static com.mycuckoo.core.util.StrUtils.isEmpty;

/**
 * 功能说明: TODO(这里用一句话描述这个类的作用)
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:50:20 AM
 */
@Service
@Transactional(readOnly = true)
public class SystemOptLogService {
    private static Logger logger = LoggerFactory.getLogger(SystemOptLogService.class);

    @Autowired
    private SysOptLogMapper sysOptLogMapper;


    @Transactional
    public void deleteLog(int keepdays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -keepdays);

        sysOptLogMapper.deleteLogger(calendar.getTime());
    }

    public Page<SysOptLog> findByPage(Querier querier) {
        return sysOptLogMapper.findByPage(querier.getQ(), querier);
    }

    public String getOptContentById(long optId) {
        return sysOptLogMapper.getOptContentById(optId);
    }

    @Transactional
    public void save(ModuleName module, OptName operate, String busiId, String title, String content, LogLevel level) {
        SystemConfigXmlParse.getInstance();
        SystemConfigBean systemConfigBean = SystemConfigXmlParse.getInstance().getSystemConfigBean();
        String sysConfigLevel = systemConfigBean.getLoggerLevel();
        String[] levelArray = { level.code() + "", sysConfigLevel };
        for (String myLevel : levelArray) {//检查日志级别是否合法
            if (isEmpty(myLevel) || "0123".indexOf(myLevel) < 0 || myLevel.length() > 1) {
                throw new MyCuckooException("日志级别错误,值为: " + myLevel);
            }
        }
        int iLevel = level.code();
        int iSysConfigLevel = Integer.parseInt(sysConfigLevel);
        if (iSysConfigLevel == 0 || iLevel < iSysConfigLevel) {
            return;
        }

        SysOptLog sysOptLog = new SysOptLog();
        sysOptLog.setOrgId(SessionContextHolder.getOrganId());
        sysOptLog.setModName(module.title());
        sysOptLog.setOptName(operate.title());
        sysOptLog.setContent(content);
        sysOptLog.setBusiType(module.code());
        sysOptLog.setBusiId(busiId);
        sysOptLog.setIp(SessionContextHolder.getIP());
        sysOptLog.setUserName(SessionContextHolder.getUserName());
        sysOptLog.setUserRole(SessionContextHolder.getRoleName());
        sysOptLog.setCreator(SessionContextHolder.getUserId().toString());
        sysOptLog.setCreateTime(LocalDateTime.now());

        sysOptLogMapper.save(sysOptLog);
    }

}

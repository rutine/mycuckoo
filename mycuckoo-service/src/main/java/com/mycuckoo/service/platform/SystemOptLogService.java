package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.domain.platform.SysOptLog;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.Pageable;
import com.mycuckoo.repository.platform.SysOptLogMapper;
import com.mycuckoo.utils.SessionUtil;
import com.mycuckoo.utils.SystemConfigXmlParse;
import com.mycuckoo.vo.SystemConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static com.mycuckoo.utils.CommonUtils.isNullOrEmpty;

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

    public Page<SysOptLog> findByPage(Map<String, Object> params, Pageable page) {
        return sysOptLogMapper.findByPage(params, page);
    }

    public String getOptContentById(long optId) {
        return sysOptLogMapper.getOptContentById(optId);
    }

    @Transactional
    public void save(ModuleName module, OptName operate, String busiId, String title, String content, LogLevel level) {
        SystemConfigXmlParse.getInstance();
        SystemConfigBean systemConfigBean = SystemConfigXmlParse.getInstance().getSystemConfigBean();
        String sysConfigLevel = systemConfigBean.getLoggerLevel();
        String[] levelArray = { level.value().toString(), sysConfigLevel };
        for (String myLevel : levelArray) {//检查日志级别是否合法
            if (isNullOrEmpty(myLevel) || "0123".indexOf(myLevel) < 0 || myLevel.length() > 1) {
                throw new ApplicationException("日志级别错误,值为: " + myLevel);
            }
        }
        int iLevel = level.value();
        int iSysConfigLevel = Integer.parseInt(sysConfigLevel);
        if (iSysConfigLevel == 0 || iLevel < iSysConfigLevel) {
            return;
        }

        SysOptLog sysOptLog = new SysOptLog();
        sysOptLog.setModName(module.value());
        sysOptLog.setName(operate.value());
        sysOptLog.setContent(content);
//        sysOptLog.setBusiType(module.name());
        sysOptLog.setBusiId(busiId);
        sysOptLog.setHost(SessionUtil.getHostName()); //得到计算机名称
        sysOptLog.setIp(SessionUtil.getIP());
        sysOptLog.setUserName(SessionUtil.getUserName());
        sysOptLog.setUserRole(SessionUtil.getRoleName());
        sysOptLog.setUserOrgan(SessionUtil.getOrganName());
        sysOptLog.setCreator(SessionUtil.getUserCode());
        sysOptLog.setCreateDate(new Date());

        sysOptLogMapper.save(sysOptLog);
    }

}

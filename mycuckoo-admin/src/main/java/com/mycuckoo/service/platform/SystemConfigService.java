package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.exception.SystemException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.util.SystemConfigLoader;
import com.mycuckoo.core.xml.SystemConfigXml;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mycuckoo.core.operator.LogOperator.COMMA;

/**
 * 功能说明: 系统配置文件维护业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:52:31 AM
 */
@Service
public class SystemConfigService {

    public SystemConfigXml getSystemConfigInfo() {
        return SystemConfigLoader.getInstance().getConfig();
    }

    public void setSystemConfigInfo(SystemConfigXml systemConfig, String userAddDelFlag) throws SystemException {
        saveSystemConfig(systemConfig, userAddDelFlag);
        SystemConfigLoader.getInstance().load();
    }

    void saveSystemConfig(SystemConfigXml newConfig,
                          String userAddDelFlag) throws SystemException {
        String systemName = newConfig.getSystemName();
        String logLevel = newConfig.getLogLevel();
        String logRetentionDays = newConfig.getLogRetentionDays();
        String defaultRowPrivilegeLevel = newConfig.getDefaultRowPrivilegeLevel();
        List<String> adminUsers = newConfig.getAdminUsers();

        SystemConfigXml oldConfig = SystemConfigLoader.getInstance().getConfig();
        StringBuilder optContent = new StringBuilder();
        if (systemName != null) { // 系统名称
            oldConfig.setSystemName(systemName);
            optContent.append("设置系统名称: " + systemName + COMMA);
        } else if (adminUsers != null && userAddDelFlag != null) {
            applyAdminUsersChange(oldConfig, adminUsers, userAddDelFlag, optContent);
        } else if (logLevel != null) {
            oldConfig.setLogLevel(logLevel);
            optContent.append("设置日志级别:" + logLevel + COMMA);
        } else if (defaultRowPrivilegeLevel != null) {
            oldConfig.setDefaultRowPrivilegeLevel(defaultRowPrivilegeLevel);
            optContent.append("设置权限级别:" + defaultRowPrivilegeLevel + COMMA);
        } else if (logRetentionDays != null) {
            oldConfig.setLogRetentionDays(logRetentionDays);
            optContent.append("设置日志保留天数:" + logRetentionDays + COMMA);
        }
        SystemConfigLoader.getInstance().write(oldConfig);

        LogOperator.begin()
                .module(ModuleName.SYS_CONFIG_MGR)
                .operate(OptName.SAVE)
                .id("")
                .title(null)
                .content(optContent.toString())
                .level(LogLevel.THIRD)
                .emit();
    }

    void applyAdminUsersChange(SystemConfigXml persistedConfig,
                               List<String> adminUsers,
                               String userAddDelFlag,
                               StringBuilder optContent) {
        if ("add".equals(userAddDelFlag)) {
            persistedConfig.addAdminUsers(adminUsers);
            for (String userCode : adminUsers) {
                optContent.append("增加管理员:" + userCode + COMMA);
            }
            return;
        }

        if ("delete".equals(userAddDelFlag)) {
            persistedConfig.removeAdminUsers(adminUsers);
            for (String userCode : adminUsers) {
                optContent.append("删除管理员:" + userCode + COMMA);
            }
        }
    }
}

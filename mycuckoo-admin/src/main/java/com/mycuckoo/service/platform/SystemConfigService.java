package com.mycuckoo.service.platform;

import com.mycuckoo.constant.enums.LogLevel;
import com.mycuckoo.constant.enums.ModuleName;
import com.mycuckoo.constant.enums.OptName;
import com.mycuckoo.core.exception.SystemException;
import com.mycuckoo.core.operator.LogOperator;
import com.mycuckoo.core.util.SystemConfigXmlParse;
import com.mycuckoo.core.util.XmlOptUtils;
import com.mycuckoo.core.SystemConfigBean;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mycuckoo.core.operator.LogOperator.COMMA;
import static com.mycuckoo.core.util.FileUtils.getClusterResourcePath;

/**
 * 功能说明: 系统配置文件维护业务类
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 25, 2014 10:52:31 AM
 */
@Service
public class SystemConfigService {

    public SystemConfigBean getSystemConfigInfo() {
        return SystemConfigXmlParse.getInstance().getSystemConfigBean();
    }

    public void setSystemConfigInfo(SystemConfigBean systemConfigBean, String userAddDelFlag) throws SystemException {

        String fileName = getClusterResourcePath(SystemConfigXmlParse.getInstance().getSysConfigFileXml());
        String loggerLevel = systemConfigBean.getLoggerLevel();
        String logRecordKeepDays = systemConfigBean.getLogRecordKeepDays();
        List<String> systemMgr = systemConfigBean.getSystemMgr();
        String systemName = systemConfigBean.getSystemName();
        String rowPrivilegeLevel = systemConfigBean.getRowPrivilegeLevel();

        Document doc = XmlOptUtils.readXML(fileName);
        StringBuilder optContent = new StringBuilder();
        if (systemName != null) { // 系统名称
            Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/systemName");
            el.setText(systemName);
            optContent.append("设置系统名称: " + systemName + COMMA);
        } else if (systemMgr != null) {
            if ("add".equals(userAddDelFlag)) { // 增加
                Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/systemMgr");
                for (String userCode : systemMgr) {
                    Element userCodeEl = el.addElement("userCode");
                    userCodeEl.setText(userCode);
                    optContent.append("增加管理员:" + userCode + COMMA);
                }
            } else if ("delete".equals(userAddDelFlag)) { // 删除
                Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/systemMgr");
                List<Element> elList = doc.selectNodes("//systemConfig/systemMgr/userCode");
                for (String userCode : systemMgr) {
                    for (Element userCodeEl : elList) {
                        if (userCodeEl.getText().equals(userCode)) {
                            el.remove(userCodeEl);
                        }
                    }
                    optContent.append("删除管理员:" + userCode + COMMA);
                }
            }
        } else if (loggerLevel != null) {
            Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/loggerLevel");
            el.setText(loggerLevel);
            optContent.append("设置日志级别:" + loggerLevel + COMMA);
        } else if (rowPrivilegeLevel != null) {
            Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/rowPrivilegeLevel");
            el.setText(rowPrivilegeLevel);
            optContent.append("设置权限级别:" + rowPrivilegeLevel + COMMA);
        } else if (logRecordKeepDays != null) {
            Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/logRecordKeepDays");
            el.setText(logRecordKeepDays);
            optContent.append("设置日志保留天数:" + logRecordKeepDays + COMMA);
        }
        XmlOptUtils.writeXML(doc, fileName);
        SystemConfigXmlParse.getInstance().loadSystemConfigDoc();

        LogOperator.begin()
                .module(ModuleName.SYS_CONFIG_MGR)
                .operate(OptName.SAVE)
                .id("")
                .title(null)
                .content(optContent.toString())
                .level(LogLevel.THIRD)
                .emit();
    }

}

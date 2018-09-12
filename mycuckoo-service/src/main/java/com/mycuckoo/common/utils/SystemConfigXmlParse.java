package com.mycuckoo.common.utils;

import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.vo.SystemConfigBean;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.mycuckoo.common.utils.CommonUtils.getClusterResourcePath;

/**
 * 功能说明: 系统配置文件操作类
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:31:33 PM
 */
public class SystemConfigXmlParse {
    protected static Logger logger = LoggerFactory.getLogger(SystemConfigXmlParse.class);

    private final static String SYS_CONFIG_FILE_XML = "SystemConfig.xml";
    private final static String TOMCAT = "tomcat";
    private final static String WEBSPHERE = "websphere";
    private final static String WEBLOGIC = "weblogic";

    private static SystemConfigXmlParse xmlSystemConifg = new SystemConfigXmlParse();

    private long fileLastModifyTime = 0;
    private String sysConfigFileXml = SYS_CONFIG_FILE_XML;
    private SystemConfigBean systemConfigBean = null;

    private SystemConfigXmlParse() {

    }

    public static SystemConfigXmlParse getInstance() {
        if (xmlSystemConifg.systemConfigBean == null) {
            synchronized (SystemConfigXmlParse.class) {
                if (xmlSystemConifg.systemConfigBean == null) {
                    xmlSystemConifg.loadSystemConfigDoc();
                }
            }
        }

        return xmlSystemConifg;
    }

    public void loadSystemConfigDoc() {
        try {
            String fileName = getClusterResourcePath(SYS_CONFIG_FILE_XML);
            File systemConfigFile = new File(fileName);
            if (!systemConfigFile.exists()) {
                try {
                    FileCopyUtils.copy(new ClassPathResource(SYS_CONFIG_FILE_XML).getFile(), systemConfigFile);
                } catch (IOException e) {
                    logger.info("加载class系统配置文件错误", e);
                }
            }

            if (!systemConfigFile.exists()) {
                throw new ApplicationException("对不起，文件" + fileName + "找不到.");
            }
            fileName = systemConfigFile.getAbsolutePath();
            logger.info("系统配置文件 ---> {}", fileName);

            long sysConfigMod = systemConfigFile.lastModified();
            if (sysConfigMod > fileLastModifyTime) {
                Document doc = XmlOptUtils.readXML(fileName);

                String loggerLevel = XmlOptUtils.selectSingleText(doc, "/systemConfig/loggerLevel");
                String logRecordKeepDays = XmlOptUtils.selectSingleText(doc, "/systemConfig/logRecordKeepDays");
                List<String> systemMgr = XmlOptUtils.selectNodesText(doc, "/systemConfig/systemMgr/userCode");
                String systemName = XmlOptUtils.selectSingleText(doc, "/systemConfig/systemName");
                String cluster = XmlOptUtils.selectSingleText(doc, "/systemConfig/cluster");
                String rowPrivilegeLevel = XmlOptUtils.selectSingleText(doc, "/systemConfig/rowPrivilegeLevel");

                String jmxTomcatDefault = XmlOptUtils.selectSingleAttribute(doc, "/systemConfig/JMX/tomcat", "default");
                String jmxWeblogicDefault = XmlOptUtils.selectSingleAttribute(doc, "/systemConfig/JMX/weblogic", "default");
                String jmxWebsphereDefault = XmlOptUtils.selectSingleAttribute(doc, "/systemConfig/JMX/websphere", "default");

                String tomcatJmxURL = XmlOptUtils.selectSingleText(doc, "/systemConfig/JMX/tomcat/jmxURL");
                String tomcatObjectName = XmlOptUtils.selectSingleText(doc, "/systemConfig/JMX/tomcat/objectName");

                systemConfigBean = new SystemConfigBean();
                String[] defaultArr = {jmxTomcatDefault, jmxWeblogicDefault, jmxWebsphereDefault};
                for (int i = 0, len = defaultArr.length; i < len; i++) {
                    String defaultDesc = defaultArr[i];
                    if ("true".equalsIgnoreCase(defaultDesc)) {
                        switch (i) {
                            case 0:
                                systemConfigBean.setJmxDefault(TOMCAT);
                                break;
                            case 1:
                                systemConfigBean.setJmxDefault(WEBLOGIC);
                                break;
                            case 2:
                                systemConfigBean.setJmxDefault(WEBSPHERE);
                                break;
                        }
                        break;
                    }
                }

                systemConfigBean.setLoggerLevel(loggerLevel);
                systemConfigBean.setLogRecordKeepDays(logRecordKeepDays);
                systemConfigBean.setSystemMgr(systemMgr);
                systemConfigBean.setSystemName(systemName);
                systemConfigBean.setCluster(cluster);
                systemConfigBean.setRowPrivilegeLevel(rowPrivilegeLevel);
                systemConfigBean.setTomcatJmxURL(tomcatJmxURL);
                systemConfigBean.setTomcatObjectName(tomcatObjectName);

                fileLastModifyTime = sysConfigMod;

                logger.info("SystemConfig.xml reload success !");
            }
        } catch (SystemException e) {
            logger.info("加载系统配置文件错误", e);
        }
    }

    public SystemConfigBean getSystemConfigBean() {
        return systemConfigBean;
    }

    public String getSysConfigFileXml() {
        return sysConfigFileXml;
    }


    public static void main(String[] args) {
        try {
            SystemConfigXmlParse xMLSystemConfig = SystemConfigXmlParse.getInstance();
            SystemConfigBean bean = xMLSystemConfig.getSystemConfigBean();
            System.out.println(bean.getSystemName());
            System.out.println(bean.getLoggerLevel());
            System.out.println(bean.getLogRecordKeepDays());
            System.out.println(bean.getRowPrivilegeLevel());
            System.out.println(bean.getSystemMgr());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

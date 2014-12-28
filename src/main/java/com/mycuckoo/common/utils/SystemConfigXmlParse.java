package com.mycuckoo.common.utils;

import static com.mycuckoo.common.constant.Common.SYS_CONFIGFILE_XML;
import static com.mycuckoo.common.constant.Common.TOMCAT;
import static com.mycuckoo.common.constant.Common.WEBLOGIC;
import static com.mycuckoo.common.constant.Common.WEBSPHERE;
import static com.mycuckoo.common.utils.CommonUtils.getClusterResourcePath;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycuckoo.domain.vo.SystemConfigBean;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 系统配置文件操作类
 *
 * @author rutine
 * @time Sep 22, 2014 9:31:33 PM
 * @version 2.0.0
 */
public class SystemConfigXmlParse {

	protected static Logger logger = LoggerFactory.getLogger(SystemConfigXmlParse.class);
	
	private static SystemConfigXmlParse xmlSystemConifg = new SystemConfigXmlParse();
	private static long fileLastModifyTime = 0;
	private static SystemConfigBean systemConfigBean = null;

	private SystemConfigXmlParse() {
		
	}

	public static SystemConfigXmlParse getInstance() {
		loadSystemConfigDoc();
		return xmlSystemConifg;
	}
	
	public static void loadSystemConfigDoc() {
		try {
			String fileName = getClusterResourcePath(SYS_CONFIGFILE_XML);
			File systemConfigFile = new File(fileName);
			
			if (!systemConfigFile.exists()) {
				throw new ApplicationException("对不起，文件" + fileName + "找不到.");
			}
			logger.info("系统配置文件 ---> {}", fileName);
			
			long sysConfigMod = systemConfigFile.lastModified();
			if(sysConfigMod > fileLastModifyTime) {
				Document doc = XmlOptUtils.loadXMLFile(fileName);
				systemConfigBean = new SystemConfigBean();
				
				String loggerLevel 		= XmlOptUtils.getSelectSingleText(doc, "/systemConfig/loggerLevel");
				String logRecordKeepDays = XmlOptUtils.getSelectSingleText(doc, "/systemConfig/logRecordKeepDays");
				List<String> systemMgr	= XmlOptUtils.selectNodesText(doc, 	"/systemConfig/systemMgr/userCode");
				String systemName		= XmlOptUtils.getSelectSingleText(doc, "/systemConfig/systemName");
				String cluster			= XmlOptUtils.getSelectSingleText(doc, "/systemConfig/cluster");
				String rowPrivilegeLevel	= XmlOptUtils.getSelectSingleText(doc, "/systemConfig/rowPrivilegeLevel");
				
				String jmxTomcatDefault	= XmlOptUtils.getSelectSingleAttribute(doc, "/systemConfig/JMX/tomcat", "default");
				String jmxWeblogicDefault	= XmlOptUtils.getSelectSingleAttribute(doc, "/systemConfig/JMX/weblogic", "default");
				String jmxWebsphereDefault = XmlOptUtils.getSelectSingleAttribute(doc, "/systemConfig/JMX/websphere", "default");
				
				String[] defaultArr = {jmxTomcatDefault, jmxWeblogicDefault, jmxWebsphereDefault}; 
				for(int i = 0, len = defaultArr.length; i < len; i++) {
					String defaultDesc = defaultArr[i];
					if("true".equalsIgnoreCase(defaultDesc)) {
						switch(i) {
							case 0 : 
								systemConfigBean.setJmxDefault(TOMCAT);
								break;
							case 1 :
								systemConfigBean.setJmxDefault(WEBLOGIC);
								break;
							case 2 :
								systemConfigBean.setJmxDefault(WEBSPHERE);
								break;
						}
						break;
					}
				}
				String tomcatJmxURL 		= XmlOptUtils.getSelectSingleText(doc, "/systemConfig/JMX/tomcat/jmxURL");
				String tomcatObjectName 	= XmlOptUtils.getSelectSingleText(doc, "/systemConfig/JMX/tomcat/objectName");
				
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
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public static SystemConfigBean getSystemConfigBean() {
		return systemConfigBean;
	}

	public static void setSystemConfigBean(SystemConfigBean systemConfigBean) {
		SystemConfigXmlParse.systemConfigBean = systemConfigBean;
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

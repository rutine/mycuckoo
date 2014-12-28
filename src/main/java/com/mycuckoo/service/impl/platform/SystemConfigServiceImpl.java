package com.mycuckoo.service.impl.platform;

import static com.mycuckoo.common.constant.Common.LOG_LEVEL_THIRD;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.Common.SYS_CONFIGFILE_XML;
import static com.mycuckoo.common.constant.ServiceVariable.ADD;
import static com.mycuckoo.common.constant.ServiceVariable.DELETE;
import static com.mycuckoo.common.constant.ServiceVariable.SYS_CONFIG_MGR;
import static com.mycuckoo.common.utils.CommonUtils.getClusterResourcePath;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycuckoo.common.utils.SystemConfigXmlParse;
import com.mycuckoo.common.utils.XmlOptUtils;
import com.mycuckoo.domain.vo.SystemConfigBean;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.platform.SystemConfigService;

/**
 * 功能说明: 系统配置文件维护业务类
 *
 * @author rutine
 * @time Sep 25, 2014 10:52:31 AM
 * @version 2.0.0
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {
	
	private SystemOptLogService sysOptLogService;

	@Override
	public SystemConfigBean getSystemConfigInfo() throws ApplicationException {
		SystemConfigXmlParse.getInstance();
		
		return SystemConfigXmlParse.getSystemConfigBean();
	}

	@Override
	public void setSystemConfigInfo(SystemConfigBean systemConfigBean, String userAddDelFlag, 
			HttpServletRequest request) throws ApplicationException {
		
		String fileName = getClusterResourcePath(SYS_CONFIGFILE_XML);
		String loggerLevel = systemConfigBean.getLoggerLevel();
		String logRecordKeepDays = systemConfigBean.getLogRecordKeepDays();
		List<String> systemMgr = systemConfigBean.getSystemMgr();
		String systemName = systemConfigBean.getSystemName();
		String rowPrivilegeLevel = systemConfigBean.getRowPrivilegeLevel();
		
		Document doc = XmlOptUtils.loadXMLFile(fileName);
		StringBuilder optContent = new StringBuilder();
		
		if(systemName != null) { // 系统名称
			Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/systemName");
			el.setText(systemName);
			optContent.append("设置系统名称: " + systemName + SPLIT);
		} else if(systemMgr != null) {
			if(ADD.equals(userAddDelFlag)) { // 增加
				Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/systemMgr");
				for(String userCode : systemMgr) {
					Element userCodeEl = el.addElement("userCode");
					userCodeEl.setText(userCode);
					optContent.append("增加管理员:" + userCode + SPLIT);
				}
			} else if(DELETE.equals(userAddDelFlag)) { // 删除
				Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/systemMgr");
				List<Element> elList = doc.selectNodes("//systemConfig/systemMgr/userCode");
				for(String userCode : systemMgr) {
					for(Element userCodeEl : elList) {
						if(userCodeEl.getText().equals(userCode)) {
							el.remove(userCodeEl);
						}
					}
					optContent.append("删除管理员:" + userCode + SPLIT);
				}
			} 
		} else if (loggerLevel != null) {
			Element el = XmlOptUtils.selectSingleNode(doc,  "/systemConfig/loggerLevel");
			el.setText(loggerLevel);
			optContent.append("设置日志级别:" + loggerLevel + SPLIT);
		} else if (rowPrivilegeLevel != null) {
			Element el = XmlOptUtils.selectSingleNode(doc,  "/systemConfig/rowPrivilegeLevel");
			el.setText(rowPrivilegeLevel);
			optContent.append("设置权限级别:" + rowPrivilegeLevel + SPLIT);
		} else if (logRecordKeepDays != null) {
			Element el = XmlOptUtils.selectSingleNode(doc, "/systemConfig/logRecordKeepDays");
			el.setText(logRecordKeepDays);
			optContent.append("设置日志保留天数:" + logRecordKeepDays + SPLIT);
		}
		XmlOptUtils.doc2XMLFile(doc, fileName);
		
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, SYS_CONFIG_MGR, SAVE_OPT, optContent.toString(), "", request);
	}

   
	
	
	
	// --------------------------依赖注入-------------------------------
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	
}

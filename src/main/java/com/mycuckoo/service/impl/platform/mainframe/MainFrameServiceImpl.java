package com.mycuckoo.service.impl.platform.mainframe;

import static com.mycuckoo.common.constant.Common.LOG_LEVEL_THIRD;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.HEADER;
import static com.mycuckoo.common.constant.ServiceVariable.MAINFRAME_FILE_NAME;
import static com.mycuckoo.common.constant.ServiceVariable.MAIN_PAGE;
import static com.mycuckoo.common.constant.ServiceVariable.MAIN_PAGE_SET;
import static com.mycuckoo.common.constant.ServiceVariable.NAVIGATE;
import static com.mycuckoo.common.constant.ServiceVariable.OUTLOOK_BAR;
import static com.mycuckoo.common.constant.ServiceVariable.STATE_BAR;
import static com.mycuckoo.common.utils.CommonUtils.getResourcePath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.utils.XmlOptUtils;
import com.mycuckoo.domain.vo.MainFrame;
import com.mycuckoo.domain.vo.MainFrameFun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.platform.mainframe.MainFrameFunService;
import com.mycuckoo.service.iface.platform.mainframe.MainFrameService;

/**
 * 功能说明: 主框架业务类
 *
 * @author rutine
 * @time Sep 25, 2014 11:16:16 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class MainFrameServiceImpl implements MainFrameService {
	
	private SystemOptLogService sysOptLogService;
	private MainFrameFunService mainFrameFunService;

	@Override
	public Map<String, List<MainFrameFun>> findAllMainFrameFun() throws ApplicationException {
		return mainFrameFunService.findAllMainFrameFun();
	}

	@Override
	public Map<String, String> findMainFrame() throws ApplicationException {
		boolean flag = true;
		if(flag) {
			throw new ApplicationException("暂不支持...");
		}
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + MAINFRAME_FILE_NAME);
		Element header = (Element) document.selectSingleNode("/module/funArea/area[@name=\"header\"]");
		Element navigate = (Element) document.selectSingleNode("/module/funArea/area[@name=\"navigate\"]");
		Element outlookBar = (Element) document.selectSingleNode("/module/funArea/area[@name=\"outlookBar\"]");
		Element mainPage = (Element) document.selectSingleNode("/module/funArea/area[@name=\"mainPage\"]");
		Element stateBar = (Element) document.selectSingleNode("/module/funArea/area[@name=\"stateBar\"]");
	
		Map<String, String> mainFrameFunMap = new HashMap<String, String>();
		mainFrameFunMap.put(HEADER, header.getText());
		mainFrameFunMap.put(NAVIGATE, navigate.getText());
		mainFrameFunMap.put(OUTLOOK_BAR, outlookBar.getText());
		mainFrameFunMap.put(MAIN_PAGE, mainPage.getText());
		mainFrameFunMap.put(STATE_BAR, stateBar.getText());
		
		return mainFrameFunMap;
	}

	@Override
	public void saveMainFrame(MainFrame mainFrame, HttpServletRequest request) throws ApplicationException {
		boolean flag = true;
		if(flag) {
			throw new ApplicationException("暂不支持...");
		}
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + MAINFRAME_FILE_NAME);
		Element header = (Element) document.selectSingleNode("/module/funArea/area[@name=\"header\"]");
		Element navigate = (Element) document.selectSingleNode("/module/funArea/area[@name=\"navigate\"]");
		Element outlookBar = (Element) document.selectSingleNode("/module/funArea/area[@name=\"outlookBar\"]");
		Element mainPage = (Element) document.selectSingleNode("/module/funArea/area[@name=\"mainPage\"]");
		Element stateBar = (Element) document.selectSingleNode("/module/funArea/area[@name=\"stateBar\"]");
		
		header.setText(mainFrame.getHeader());
		outlookBar.setText(mainFrame.getOutlookBar());
		mainPage.setText(mainFrame.getMainPage());
		stateBar.setText(mainFrame.getStateBar());
		navigate.setText(mainFrame.getNavigate());
		
		XmlOptUtils.doc2XMLFile(document, getResourcePath() + MAINFRAME_FILE_NAME);
		
		StringBuffer sb = new StringBuffer();
		sb.append(HEADER + ":" + mainFrame.getHeader() + SPLIT)
			.append(NAVIGATE + ":" + mainFrame.getNavigate() + SPLIT)
			.append(MAIN_PAGE + ":" + mainFrame.getMainPage() + SPLIT)
			.append(STATE_BAR + ":" + mainFrame.getStateBar() + SPLIT)
			.append(OUTLOOK_BAR + ":" + mainFrame.getOutlookBar() + SPLIT);
	
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, MAIN_PAGE_SET, SAVE_OPT, sb.toString(), "-1", request);
	}

	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}
	@Autowired
	public void setMainFrameFunService(MainFrameFunService mainFrameFunService) {
		this.mainFrameFunService = mainFrameFunService;
	}
}

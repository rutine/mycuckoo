package com.mycuckoo.service.impl.platform.mainframe;

import static com.mycuckoo.common.constant.Common.DELETE_OPT;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_FIRST;
import static com.mycuckoo.common.constant.Common.LOG_LEVEL_THIRD;
import static com.mycuckoo.common.constant.Common.SAVE_OPT;
import static com.mycuckoo.common.constant.Common.SPLIT;
import static com.mycuckoo.common.constant.ServiceVariable.HEADER;
import static com.mycuckoo.common.constant.ServiceVariable.MAINFRAMEFUN_FILE_NAME;
import static com.mycuckoo.common.constant.ServiceVariable.MAIN_PAGE;
import static com.mycuckoo.common.constant.ServiceVariable.MAIN_PAGE_FUN;
import static com.mycuckoo.common.constant.ServiceVariable.NAVIGATE;
import static com.mycuckoo.common.constant.ServiceVariable.OUTLOOK_BAR;
import static com.mycuckoo.common.constant.ServiceVariable.STATE_BAR;
import static com.mycuckoo.common.utils.CommonUtils.getResourcePath;
import static com.mycuckoo.common.utils.CommonUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycuckoo.common.utils.XmlOptUtils;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.vo.MainFrameFun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.iface.platform.SystemOptLogService;
import com.mycuckoo.service.iface.platform.mainframe.MainFrameFunService;

/**
 * 功能说明: TODO(这里用一句话描述这个类的作用)
 *
 * @author rutine
 * @time Sep 25, 2014 10:58:38 AM
 * @version 2.0.0
 */
@Service
@Transactional(readOnly=true)
public class MainFrameFunServiceImpl implements MainFrameFunService {
	
	private static Logger logger = LoggerFactory.getLogger(MainFrameFunServiceImpl.class);
	private SystemOptLogService sysOptLogService;

	/**
	 * 计算功能区的最大ID
	 *
	 * @param document
	 * @return
	 * @author rutine
	 * @time Oct 9, 2012 8:05:16 PM
	 */
	public synchronized int getFunAreaMaxId(Document document) {
		List<Element> list = document.selectNodes("//module/funArea/area");
		int id = -1;
		for (Element e : list) {
			if (e.elementText("funId") != null) {
				int funId = Integer.parseInt(e.elementText("funId"));
				if (funId > id) {
					id = funId;
				}
			}
		}
		logger.debug("funarea max id is " + (id + 1));
		
		return id + 1;
	}

	@Transactional(readOnly=false)
	@Override
	public void deleteMainFrameFun(String[] delRecIds, HttpServletRequest request) throws ApplicationException {
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + MAINFRAMEFUN_FILE_NAME);
		Element funArea = (Element)document.selectSingleNode("/module/funArea");
		List<Element> elList = document.selectNodes("//module/funArea/area/funId");
		for(Element element : elList) {
			String text = element.getText();
			for(String id : delRecIds) {
				if(text.equals(id)) funArea.remove(element.getParent());
			}
		}
		
		XmlOptUtils.doc2XMLFile(document, getResourcePath() + MAINFRAMEFUN_FILE_NAME);
		String logContent = "删除功能区的id: " + Arrays.asList(delRecIds) + SPLIT;
		
		logger.debug(logContent);
		
		sysOptLogService.saveLog(LOG_LEVEL_THIRD, MAIN_PAGE_FUN, DELETE_OPT, logContent, "", request);
	}

	@Override
	public Map<String, List<MainFrameFun>> findAllMainFrameFun() throws ApplicationException {
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + MAINFRAMEFUN_FILE_NAME);
		List[] list = {new ArrayList<MainFrameFun>(2), new ArrayList<MainFrameFun>(2), 
				new ArrayList<MainFrameFun>(2), new ArrayList<MainFrameFun>(2), new ArrayList<MainFrameFun>(2)};
		String[] funNameArr = {HEADER, NAVIGATE, OUTLOOK_BAR, MAIN_PAGE, STATE_BAR};
		List<Element> eList = document.selectNodes("//module/funArea/area");
		for (Element targetEl : eList) {
			String funNameTemp = targetEl.elementText("funName");
			String funURI = targetEl.elementText("funURI");
			String funURIDesc = targetEl.elementText("funURIDesc");
			MainFrameFun mainFrameFun = new MainFrameFun();
			mainFrameFun.setFunName(funNameTemp);
			mainFrameFun.setFunURI(funURI);
			mainFrameFun.setFunURIDesc(funURIDesc);
			if (funNameArr[0].equalsIgnoreCase(funNameTemp)) {
				list[0].add(mainFrameFun);
			} else if (funNameArr[1].equalsIgnoreCase(funNameTemp)) {
				list[1].add(mainFrameFun);
			} else if (funNameArr[2].equalsIgnoreCase(funNameTemp)) {
				list[2].add(mainFrameFun);
			} else if (funNameArr[3].equalsIgnoreCase(funNameTemp)) {
				list[3].add(mainFrameFun);
			} else if (funNameArr[4].equalsIgnoreCase(funNameTemp)) {
				list[4].add(mainFrameFun);
			}
		}
		Map<String, List<MainFrameFun>> mainFrameFunMap = new HashMap<String, List<MainFrameFun>>();
		for(int i = 0, len = list.length; i < len; i++){
			mainFrameFunMap.put(funNameArr[i], list[i]);
		}
		
		return mainFrameFunMap;
	}

	@Override
	public Page<MainFrameFun> findMainFrameFunByCond(String funName,  Pageable page) throws ApplicationException {
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + MAINFRAMEFUN_FILE_NAME);
		List<Element> elList = document.selectNodes("//module/funArea/area");
		List<Element> newElList = new ArrayList<Element>();
		if(!isNullOrEmpty(funName)) {
			for(Element conEl : elList) {
				String funNameTemp = conEl.elementText("funName");
				Pattern p = Pattern.compile(funName);
				Matcher m = p.matcher(funNameTemp);
				boolean b = m.find();
				if(b){
					newElList.add(conEl);
				}
			}
		} else {
			newElList = elList;
		}
		
		List<MainFrameFun> frameList = new ArrayList<MainFrameFun>();
		int count = 0;
		for (Element targetEl : newElList) {
			count++;
			if (count >= (page.getOffset() + 1) && count <= (page.getOffset() + page.getPageSize())) {
				if (targetEl != null) {
					String funId = targetEl.elementText("funId");
					String funNameTemp = targetEl.elementText("funName");
					String funURI = targetEl.elementText("funURI");
					String funMemo = targetEl.elementText("funMemo");
					String funURIDesc = targetEl.elementText("funURIDesc");
					MainFrameFun mainFrameFun = new MainFrameFun();
					mainFrameFun.setFunId(funId);
					mainFrameFun.setFunName(funNameTemp);
					mainFrameFun.setFunURI(funURI);
					mainFrameFun.setFunMemo(funMemo);
					mainFrameFun.setFunURIDesc(funURIDesc);
					frameList.add(mainFrameFun);
				}
			}
		}
		
		return new PageImpl<MainFrameFun>(frameList, page, count);
	}

	@Override
	public MainFrameFun findMainFrameFunByFunId(String funId) throws ApplicationException {
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + MAINFRAMEFUN_FILE_NAME);
		List<Element> elList = document.selectNodes("//module/funArea/area/funId");
		Element targetEl = null;
		for(Element el : elList) {
			String text = el.getText();
			if(funId.equals(text)) {
				targetEl = el.getParent();
				break;
			}
		}
		MainFrameFun mainFrameFun = null;
		if(targetEl != null){
			mainFrameFun = new MainFrameFun();
			String funIdTemp = targetEl.elementText("funId");
			String funName = targetEl.elementText("funName");
			String funURIDesc = targetEl.elementText("funURIDesc");
			String funURI = targetEl.elementText("funURI");
			String funMemo = targetEl.elementText("funMemo");
			mainFrameFun.setFunId(funIdTemp);
			mainFrameFun.setFunName(funName);
			mainFrameFun.setFunURI(funURI);
			mainFrameFun.setFunMemo(funMemo);
			mainFrameFun.setFunURIDesc(funURIDesc);
		}
		
		return mainFrameFun;
	}

	@Override
	public MainFrameFun findMainFrameFunByFunName(String funName) throws ApplicationException {
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + MAINFRAMEFUN_FILE_NAME);
		List<Element> elList = document.selectNodes("//module/funArea/area/funName");
		Element targetEl = null;
		for(Element el : elList) {
			String text = el.getText();
			if(funName.equals(text)) {
				targetEl = el.getParent();
				break;
			}
		}
		MainFrameFun mainFrameFun = null;
		if(targetEl != null) {
			mainFrameFun = new MainFrameFun();
			String funId = targetEl.elementText("funId");
			String funNameTemp = targetEl.elementText("funName");
			String funURIDesc = targetEl.elementText("funURIDesc");
			String funURI = targetEl.elementText("funURI");
			String funMemo = targetEl.elementText("funMemo");
			mainFrameFun.setFunId(funId);
			mainFrameFun.setFunName(funNameTemp);
			mainFrameFun.setFunURI(funURI);
			mainFrameFun.setFunMemo(funMemo);
			mainFrameFun.setFunURIDesc(funURIDesc);
		}
		
		return mainFrameFun;
	}

	@Override
	public void modifyMainFrameFun() {
	
	}

	@Override
	public void saveMainFrameFun(MainFrameFun mainFrameFun, HttpServletRequest request) 
			throws ApplicationException {
		
		if (mainFrameFun.getFunId() != null) {
			deleteMainFrameFun(new String[]{mainFrameFun.getFunId()}, request);
		}
		Document document = XmlOptUtils.loadXMLFile(getResourcePath() + MAINFRAMEFUN_FILE_NAME);
	
		Element el = (Element) document.selectSingleNode("/module/funArea");
		Element areaEl = el.addElement("area");
	
		int id = getFunAreaMaxId(document);
		Element idEl = areaEl.addElement("funId");
		idEl.addText(id + "");
	
		Element funNameEl = areaEl.addElement("funName");
		funNameEl.addText(mainFrameFun.getFunName());
	
		Element funURIEl = areaEl.addElement("funURI");
		funURIEl.addText(mainFrameFun.getFunURI());
	
		Element funURIDescEl = areaEl.addElement("funURIDesc");
		funURIDescEl.addText(mainFrameFun.getFunURIDesc());
	
		Element funMemoEl = areaEl.addElement("funMemo");
		funMemoEl.addText(mainFrameFun.getFunMemo());
	
		XmlOptUtils.doc2XMLFile(document, getResourcePath() + MAINFRAMEFUN_FILE_NAME);
		StringBuffer sb = new StringBuffer();
		sb.append("增加的功能区信息 id:" + id + SPLIT)
			.append("funName:" + mainFrameFun.getFunName() + SPLIT)
			.append("funURI:" + mainFrameFun.getFunURI() + SPLIT)
			.append("funURIDesc:" + mainFrameFun.getFunURIDesc() + SPLIT)
			.append("funMemo:" + mainFrameFun.getFunMemo() + SPLIT);
		sysOptLogService.saveLog(LOG_LEVEL_FIRST, MAIN_PAGE_FUN, SAVE_OPT, sb.toString(), id + "", request);
	}
	
	
	
	
	// --------------------------- 依赖注入 -------------------------------
	@Autowired
	public void setSysOptLogService(SystemOptLogService sysOptLogService) {
		this.sysOptLogService = sysOptLogService;
	}

	public static void main(String[] args) {
		System.setProperty("t", "bhtchPltUus");
		System.out.println(System.getProperty("t"));
		System.out.println(new MainFrameFunServiceImpl().getClass().getClassLoader().getResource(""));
	}

}

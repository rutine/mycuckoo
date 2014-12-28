package com.mycuckoo.service.iface.platform.mainframe;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.vo.MainFrame;
import com.mycuckoo.domain.vo.MainFrameFun;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 主框架业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:08:13 AM
 * @version 2.0.0
 */
public interface MainFrameService {

	/**
	 * 获得定义的所有功能区列表
	 * 
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 9, 2012 8:22:07 PM
	 */
	Map<String, List<MainFrameFun>> findAllMainFrameFun() throws ApplicationException;

	/**
	 * 根据条件查询所有主框架功能区
	 * 
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 9, 2012 8:21:58 PM
	 */
	Map<String, String> findMainFrame() throws ApplicationException;

	/**
	 * 保存主页模版设置
	 * 
	 * @param mainFrame
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 9, 2012 8:21:48 PM
	 */
	void saveMainFrame(MainFrame mainFrame, HttpServletRequest request)
			throws ApplicationException;
}

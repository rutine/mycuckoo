package com.mycuckoo.service.iface.platform.mainframe;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.vo.MainFrameFun;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 功能区业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:07:07 AM
 * @version 2.0.0
 */
public interface MainFrameFunService {

	/**
	 * 刪除功能區
	 * 
	 * @param delRecIds 功能区名称
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 7, 2012 9:30:03 AM
	 */
	void deleteMainFrameFun(String[] delRecIds, HttpServletRequest request) 
			throws ApplicationException;

	/**
	 * 获得定义的所有功能区列表
	 * 
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 7, 2012 9:30:22 AM
	 */
	Map<String, List<MainFrameFun>> findAllMainFrameFun() throws ApplicationException;

	/**
	 * 根据条件查询所有主框架功能区
	 * 
	 * @param funName
	 * @param page
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 7, 2012 9:31:20 AM
	 */
	Page<MainFrameFun> findMainFrameFunByCond(String funName, Pageable page) 
			throws ApplicationException;

	/**
	 * 根据id查找功能区
	 * 
	 * @param funId
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 7, 2012 9:31:48 AM
	 */
	MainFrameFun findMainFrameFunByFunId(String funId) throws ApplicationException;

	/**
	 * 根据name查找功能区
	 * 
	 * @param funName
	 * @return
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 7, 2012 9:32:36 AM
	 */
	MainFrameFun findMainFrameFunByFunName(String funName) throws ApplicationException;

	/**
	 * 修改功能區
	 * 
	 * @author rutine
	 * @time Oct 7, 2012 9:32:59 AM
	 */
	void modifyMainFrameFun();

	/**
	 * 增加功能區,同时包含修改功能区功能
	 * 
	 * @param mainFrameFun 功能区对象
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 7, 2012 9:33:13 AM
	 */
	void saveMainFrameFun(MainFrameFun mainFrameFun, HttpServletRequest request) 
			throws ApplicationException;
}

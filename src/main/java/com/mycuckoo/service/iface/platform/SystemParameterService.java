package com.mycuckoo.service.iface.platform;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSysParameter;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 系统参数业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:21:33 AM
 * @version 2.0.0
 */
public interface SystemParameterService {

	/**
	 * <p>停用/启用系统参数</p>
	 * 
	 * @param paraId 系统参数ID
	 * @param disEnableFlag 停用/启用标志
	 * @return boolean true为停用/启用成功，false不能停用
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 7:52:18 PM
	 */
	boolean disEnableSystemParameter(long paraId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据系统参数名称及键值查找系统参数记录</p>
	 * 
	 * @param paraName
	 * @param paraKeyName
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 15, 2012 7:53:28 PM
	 */
	Page<SysplSysParameter> findSystemParametersByCon(String paraName, String paraKeyName, Pageable page);

	/**
	 * <p>根据参数<code>ID</code>获取系统参数记录</p>
	 * 
	 * @param paraId
	 * @return
	 * @author rutine
	 * @time Oct 15, 2012 7:52:50 PM
	 */
	SysplSysParameter getSystemParameterById(long paraId);

	/**
	 * <p>根据参数名称查找系统参数记录</p>
	 * 
	 * @param paraKeyName
	 * @return
	 * @author rutine
	 * @time Oct 15, 2012 7:55:17 PM
	 */
	SysplSysParameter getSystemParameterByParaName(String paraName);

	/**
	 * <p>修改系统参数</p>
	 * 
	 * @param systemParameter 系统参数对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 7:55:34 PM
	 */
	void updateSystemParameter(SysplSysParameter systemParameter,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存系统参数</p>
	 * 
	 * @param systemParameter 系统参数对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 7:55:50 PM
	 */
	void saveSystemParameter(SysplSysParameter systemParameter,
			HttpServletRequest request) throws ApplicationException;
}

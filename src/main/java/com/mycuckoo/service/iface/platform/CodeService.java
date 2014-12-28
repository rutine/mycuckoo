package com.mycuckoo.service.iface.platform;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplCode;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 编码管理业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:11:14 AM
 * @version 2.0.0
 */
public interface CodeService {

	/**
	 * <p>停用/启用编码</p>
	 * 
	 * @param codeId 编码ID
	 * @param disEnableFlag 停用/启用标志
	 * @return boolean true为停用启用成功，false不能停用
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 4:36:59 PM
	 */
	boolean disEnableCode(long codeId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>根据条件查找编码记录</p>
	 * 
	 * @param codeEngName 英文编码名称
	 * @param codeName 中文编码名称
	 * @param moduleName 模块名称
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 14, 2012 4:38:00 PM
	 */
	Page<SysplCode> findCodesByCon(String codeEngName, String codeName,
			String moduleName, Pageable page);

	/**
	 * <p>判断编码英文名称是否存在</p>
	 * 
	 * @param codeEngName 英文编码名称
	 * @return
	 * @author rutine
	 * @time Oct 14, 2012 4:38:45 PM
	 */
	boolean existCodeByCodeEngName(String codeEngName);

	/**
	 * <p>根据编码<code>ID</code>获取编码记录</p>
	 * 
	 * @param codeId 编码ID
	 * @return sysplCode 编码对象
	 * @author rutine
	 * @time Oct 14, 2012 4:40:15 PM
	 */
	SysplCode getCodeByCodeId(Long codeId);

	/**
	 * <p>修改编码记录</p>
	 * 
	 * @param code 编码对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 4:40:44 PM
	 */
	void updateCode(SysplCode code, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * 根据英文编码名称获取编码
	 * 
	 * @param codeEngName 英文编码名称
	 * @param request
	 * @return
	 * @author rutine
	 * @time Oct 14, 2012 4:42:42 PM
	 * @deprecated 待解决方法
	 */
	String getCodeNameByCodeEngName(String codeEngName,
			HttpServletRequest request);

	/**
	 * <p>保存编码记录</p>
	 * 
	 * @param code 编码对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 4:41:54 PM
	 */
	void saveCode(SysplCode code, HttpServletRequest request)
			throws ApplicationException;
}

package com.mycuckoo.service.iface.platform;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDicBigType;
import com.mycuckoo.domain.platform.SysplDicSmallType;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 字典大小类业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:12:04 AM
 * @version 2.0.0
 */
public interface DictionaryService {

	/**
	 * <p>停用/启用字典大类</p>
	 * 
	 * @param bigTypeId 字典大类ID
	 * @param disEnableFlag 停用/启用标志
	 * @return boolean true为停用启用成功，false不能停用
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 3:42:02 PM
	 */
	boolean disEnableDicBigType(long bigTypeId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>判断大类代码是否存在</p>
	 * 
	 * @param bigTypeCode 大类代码
	 * @return
	 * @author rutine
	 * @time Oct 14, 2012 3:45:36 PM
	 */
	boolean existDicBigTypeByBigTypeCode(String bigTypeCode);

	/**
	 * <p>根据大类代码查询所有小类</p>
	 * 
	 * @param bigTypeCode 大类代码
	 * @return
	 * @author rutine
	 * @time Oct 14, 2012 3:46:58 PM
	 */
	List<SysplDicSmallType> findDicSmallTypesByBigTypeCode(String bigTypeCode);

	/**
	 * <p>根据字典大类ID获取字典大类</p>
	 * 
	 * @param bigTypeId 字典大类ID
	 * @return
	 * @author rutine
	 * @time Oct 14, 2012 3:47:38 PM
	 */
	SysplDicBigType getDicBigTypeByBigTypeId(long bigTypeId);

	/**
	 * <p>根据大类名称及代码分页查询大类</p>
	 * 
	 * @param bigTypeName 大类名称
	 * @param bigTypeCode 大类代码
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 14, 2012 3:48:52 PM
	 */
	Page<SysplDicBigType> findDicBigTypesByCon(String bigTypeName, String bigTypeCode, Pageable page);

	/**
	 * <p>修改字典大类</p>
	 * 
	 * @param dicBigType 字典大类对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 3:50:56 PM
	 */
	void updateDicBigType(SysplDicBigType dicBigType,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存字典大类</p>
	 * 
	 * @param dicBigType 字典大类对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 3:41:29 PM
	 */
	void saveDicBigType(SysplDicBigType dicBigType,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>保存字典小类</p>
	 * 
	 * @param dicSmallTypes 保存字典小类
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Jun 11, 2013 5:25:52 PM
	 */
	void saveDicSmallTypes(List<SysplDicSmallType> dicSmallTypes,
			HttpServletRequest request) throws ApplicationException;
}

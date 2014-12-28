package com.mycuckoo.service.iface.platform;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 操作按钮业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:16:19 AM
 * @version 2.0.0
 */
public interface OperateService {

	/**
	 * <p>停用/启用操作按钮</p>
	 * 
	 * @param operateId 操作按钮ID
	 * @param disEnableFlag 停用/启用标志
	 * @return boolean true 为停用/启用成功
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 10:51:34 AM
	 */
	boolean disEnableOperate(long operateId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>返回所有操作按钮记录</p>
	 * 
	 * @return List<SysplOperate> 所有操作按钮对象
	 * @author rutine
	 * @time Oct 14, 2012 10:55:05 AM
	 */
	List<SysplOperate> findAllOperates();

	/**
	 * <p>根据条件查询操作按钮</p>
	 * 
	 * @param modOptName 操作按钮名称
	 * @param page
	 * @return Map 1.list 2.count
	 * @author rutine
	 * @time Oct 14, 2012 10:54:39 AM
	 */
	Page<SysplOperate> findOperatesByCon(String modOptName, Pageable page);

	/**
	 * <p>判断操作按钮名称是否存在</p>
	 * 
	 * @param operateName 操作按钮名称
	 * @return true 存在 false 不存在
	 * @author rutine
	 * @time Oct 14, 2012 10:53:45 AM
	 */
	boolean existsOperateByName(String operateName);

	/**
	 * <p>根据操作按钮<code>ID</code>查询模块操作</p>
	 * 
	 * @param operateId 操作按钮<code>ID</code>
	 * @return SysplOperate 操作按钮对象
	 * @author rutine
	 * @time Oct 14, 2012 10:52:45 AM
	 */
	SysplOperate findOperatesByOperateId(Long operateId);

	/**
	 * <p>修改操作按钮</p>
	 * 
	 * @param operate 操作按钮对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 10:53:22 AM
	 */
	void updateOperate(SysplOperate operate, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>保存操作按钮</p>
	 * 
	 * @param operate 操作按钮对象
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 14, 2012 10:50:56 AM
	 */
	void saveOperate(SysplOperate operate, HttpServletRequest request)
			throws ApplicationException;
}

package com.mycuckoo.persistence.iface.platform;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSysParameter;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 系统参数持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:42:09 AM
 * @version 2.0.0
 */
public interface SysplSysParameterRepository extends Repository<SysplSysParameter, Long> {

	/**
	 * 根据系统参数名称及键值查询系统参数
	 * 
	 * @param paraName
	 * @param paraKeyName
	 * @param page
	 * @return 分页对象
	 */
	Page<SysplSysParameter> findSystemParametersByCon(String paraName,
			String paraKeyName, Pageable page);

	/**
	 * 根据参数名称获取系统参数
	 * 
	 * @param paraName
	 * @return 系统参数对象
	 */
	SysplSysParameter getSystemParameterByParaName(String paraName);
}

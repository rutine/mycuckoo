package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 系统参数持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:42:09 AM
 * @version 3.0.0
 */
public interface SysParameterMapper extends Repository<SysParameter, Long> {

	/**
	 * 根据参数名称获取系统参数
	 * 
	 * @param paraName
	 * @return 系统参数对象
	 */
	SysParameter getByParaName(String paraName);
}

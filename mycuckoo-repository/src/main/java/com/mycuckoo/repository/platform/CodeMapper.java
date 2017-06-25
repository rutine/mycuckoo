package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.Code;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 编码持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:29:48 AM
 * @version 3.0.0
 */
public interface CodeMapper extends Repository<Code, Long> {

	/**
	 * 根据编码英文名称统计编码是否存在
	 * 
	 * @param codeEngName 编码英文名称
	 * @return
	 */
	int countByCodeEngName(String codeEngName);
	
	/**
	 * 根据编码英文名称获取编码
	 * 
	 * @param codeEngName 编码英文名称
	 * @return
	 */
	Code getByCodeEngName(String codeEngName);
}

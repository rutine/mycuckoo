package com.mycuckoo.persistence.iface.platform;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplCode;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 编码持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:29:48 AM
 * @version 2.0.0
 */
public interface SysplCodeRepository extends Repository<SysplCode, Long> {

	/**
	 * 根据编码英文名称统计编码是否存在
	 * 
	 * @param codeEngName 编码英文名称
	 * @return
	 */
	int countCodesByCodeEngName(String codeEngName);

	/**
	 * 根据条件分页查询编码
	 * 
	 * @param codeEngName 英文编码名称
	 * @param codeName 中文编码名称
	 * @param moduleName 模块名称
	 * @param page
	 * @return 分页对象
	 */
	Page<SysplCode> findCodesByCon(String codeEngName, String codeName, String moduleName, Pageable page);

	/**
	 * 根据编码英文名称获取编码
	 * 
	 * @param codeEngName 编码英文名称
	 * @return
	 */
	SysplCode getCodeByCodeEngName(String codeEngName);
}

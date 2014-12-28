package com.mycuckoo.persistence.iface.platform;

import java.util.List;

import com.mycuckoo.domain.platform.SysplDicSmallType;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 字典小类型持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:33:43 AM
 * @version 2.0.0
 */
public interface SysplDicSmallTypeRepository extends Repository<SysplDicSmallType, Long> {

	/**
	 * 根据大类ID删除小类
	 * 
	 * @param bigTypeId 大类代码
	 */
	void deleteDicSmallTypesByBigTypeId(long bigTypeId);

	/**
	 * 根据大类ID查询小类
	 * 
	 * @param bigTypeId
	 * @return 列表
	 */
	List<SysplDicSmallType> findDicSmallTypesByBigTypeId(long bigTypeId);

	/**
	 * 根据大类代码查询小类
	 * 
	 * @param bigTypeCode 大类代码
	 * @return 列表
	 */
	List<SysplDicSmallType> findDicSmallTypesByBigTypeCode(String bigTypeCode);

}

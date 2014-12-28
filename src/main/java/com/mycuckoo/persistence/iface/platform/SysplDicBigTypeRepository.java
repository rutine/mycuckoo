package com.mycuckoo.persistence.iface.platform;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDicBigType;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 字典大类型持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:32:28 AM
 * @version 2.0.0
 */
public interface SysplDicBigTypeRepository extends Repository<SysplDicBigType, Long> {

	/**
	 * 根据字典大类编码统计字典大类数量
	 * 
	 * @param bigTypeCode
	 * @return 数量
	 */
	int countDicBigTypesByBigTypeCode(String bigTypeCode);

	/**
	 * 根据字典大类名称及代码分页查询字典大类
	 * 
	 * @param bigTypeName 大类名称
	 * @param bigTypeCode 大类代码
	 * @param page
	 * @return 分页对象
	 */
	Page<SysplDicBigType> findDicBigTypesByCon(String bigTypeName,
			String bigTypeCode, Pageable page);

	/**
	 * 根据字典大类ID修改字典大类状态
	 * 
	 * @param bigTypeId 典大类ID
	 * @param status 新的大类状态
	 */
	void updateDicBigTypeStatus(long bigTypeId, String status);
}

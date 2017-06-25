package com.mycuckoo.repository.platform;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.platform.DicBigType;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 字典大类型持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:32:28 AM
 * @version 3.0.0
 */
public interface DicBigTypeMapper extends Repository<DicBigType, Long> {

	/**
	 * 根据字典大类编码统计字典大类数量
	 * 
	 * @param bigTypeCode
	 * @return 数量
	 */
	int countByBigTypeCode(String bigTypeCode);

	/**
	 * 根据字典大类ID修改字典大类状态
	 * 
	 * @param bigTypeId 典大类ID
	 * @param status 新的大类状态
	 */
	void updateStatus(@Param("bigTypeId") long bigTypeId, @Param("status")  String status);
}

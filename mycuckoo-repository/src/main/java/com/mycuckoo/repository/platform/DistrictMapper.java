package com.mycuckoo.repository.platform;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.platform.District;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 地区持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:35:12 AM
 * @version 3.0.0
 */
public interface DistrictMapper extends Repository<District, Long> {

	/**
	 * 根据地区ID统计下级地区数
	 * 
	 * @param upDistrictId 父地区ID
	 * @return 下级数量
	 */
	int countByUpDistrictId(long upDistrictId);

	/**
	 * 根据地区名称判断地区是否存在
	 * 
	 * @param districtName 地区名称
	 * @return 地区数量
	 */
	int countByDistrictName(String districtName);

	/**
	 * 
	 * 根据地区ID查询下级地区
	 * 
	 * @param upDistrictId 父级ID
	 * @return 下级地区
	 */
	List<District> findByUpDistrictId(Long upDistrictId);

	/**
	 * 根据地区ID和过滤条件查询下级地区
	 * 
	 * @param upDistrictId 父地区ID
	 * @param filterDistrictId 过滤id,当修改时将本ID过滤掉
	 * @return
	 */
	List<District> findByUpDistrictIdAndFilterIds(@Param("upDistrictId") long upDistrictId, @Param("filterDistrictIds") long[] filterDistrictIds);
}

package com.mycuckoo.persistence.iface.platform;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 地区持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:35:12 AM
 * @version 2.0.0
 */
public interface SysplDistrictRepository extends Repository<SysplDistrict, Long> {

	/**
	 * 根据地区ID统计下级地区数
	 * 
	 * @param upDistrictId 父地区ID
	 * @return 下级数量
	 */
	int countDistrictsByUpDistrictId(long upDistrictId);

	/**
	 * 根据地区名称判断地区是否存在
	 * 
	 * @param districtName 地区名称
	 * @return 地区数量
	 */
	int countDistrictsByDistrictName(String districtName);

	/**
	 * 
	 * 根据地区ID查询下级地区
	 * 
	 * @param upDistrictId 父级ID
	 * @return 下级地区
	 */
	List<SysplDistrict> findDistrictsByUpDistrictId(Long upDistrictId);

	/**
	 * 根据地区ID和过滤条件查询下级地区
	 * 
	 * @param upDistrictId 父地区ID
	 * @param filterDistrictId 过滤id,当修改时将本ID过滤掉
	 * @return
	 */
	List<SysplDistrict> findDistrictsByUpDistrictsAFilterIds(long upDistrictId, long filterDistrictId);

	/**
	 * 根据条件分页查询地区
	 * 
	 * @param ids 下级IDs
	 * @param districtName 地区名称
	 * @param districtLevel 地区级别
	 * @param page
	 * @return 分页对象
	 */
	Page<SysplDistrict> findDistrictsByCon(Long[] ids, String districtName, String districtLevel, Pageable page);
}

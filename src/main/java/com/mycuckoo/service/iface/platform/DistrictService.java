package com.mycuckoo.service.iface.platform;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.domain.vo.TreeVo;
import com.mycuckoo.exception.ApplicationException;

/**
 * 功能说明: 地区业务接口
 * 
 * @author rutine
 * @time Sep 24, 2014 11:15:28 AM
 * @version 2.0.0
 */
public interface DistrictService {

	/**
	 * <p>停用/启用地区记录</p>
	 * 
	 * @param districtId 地区ID
	 * @param disEnableFlag 停用/启用标志
	 * @param request
	 * @return boolean true为停用启用成功，false不能停用
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 10:01:52 PM
	 */
	boolean disEnableDistrict(long districtId, String disEnableFlag,
			HttpServletRequest request) throws ApplicationException;

	/**
	 * <p>判断地区名称是否存在</p>
	 * 
	 * @param districtName 地区名称
	 * @return boolean true 地区名存在 false 不存在
	 * @author rutine
	 * @time Oct 15, 2012 10:05:42 PM
	 */
	boolean existDistrictByName(String districtName);

	/**
	 * <p>返回所有地区记录</p>
	 * 
	 * @return List<sysplDistrict> 所有地区记录
	 * @author rutine
	 * @time Oct 15, 2012 10:03:10 PM
	 */
	List<SysplDistrict> findAllDistricts();

	/**
	 * <p>分页查询地区记录</p>
	 * 
	 * @param treeId 树结点ID
	 * @param districtName 地区名称
	 * @param districtLevel 地区级别
	 * @param page
	 * @return
	 * @author rutine
	 * @time Oct 15, 2012 10:03:58 PM
	 */
	Page<SysplDistrict> findDistrictsByCon(long treeId, String districtName,
			String districtLevel, Pageable page);

	/**
	 * <p>根据地区<code>ID</code>获取地区记录
	 * 
	 * @param districtId 地区ID
	 * @return
	 * @author rutine
	 * @time Oct 15, 2012 10:06:51 PM
	 */
	SysplDistrict getDistrictByDistrictId(Long districtId);

	/**
	 * <p>根据地区ID和过滤条件查询下级地区</p>
	 * 
	 * @param districtId 地区id
	 * @param filterdistrictId 过滤地区id,当修改地区时将本地区ID过滤掉
	 * @return List<TreeVo> 下一级地区
	 * @author rutine
	 * @time Oct 15, 2012 10:06:17 PM
	 */
	List<TreeVo> findNextLevelChildNodes(long districtId, long filterdistrictId);

	/**
	 * <p>修改地区</p>
	 * 
	 * @param district 地区对象
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 10:07:17 PM
	 */
	void updateDistrict(SysplDistrict district, HttpServletRequest request)
			throws ApplicationException;

	/**
	 * <p>保存地区</p>
	 * 
	 * @param district 地区对象
	 * @param request
	 * @throws ApplicationException
	 * @author rutine
	 * @time Oct 15, 2012 10:07:45 PM
	 */
	void saveDistrict(SysplDistrict district, HttpServletRequest request)
			throws ApplicationException;
}

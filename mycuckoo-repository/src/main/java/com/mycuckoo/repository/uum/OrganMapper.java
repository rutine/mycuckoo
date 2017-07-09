package com.mycuckoo.repository.uum;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.repository.Repository;

/**
 * 功能说明: 机构持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:43:42 AM
 * @version 3.0.0
 */
public interface OrganMapper extends Repository<Organ, Long> {

	/**
	 * 
	 * 查询所有直接下级机构记录
	 * 
	 * @param upOrgId 父级机构ID
	 * @return 所有关联的子机构
	 */
	List<Organ> findByUpOrgId(Long upOrgId);

	/**
	 * 统计所有下级机构数量
	 * 
	 * @param upOrgId 父级机构ID
	 * @return
	 */
	int countByUpOrgId(Long upOrgId);

	/**
	 * 根据机构简称统计机构数量
	 * 
	 * @param simpleName 机构简称
	 * @return
	 */
	int countByOrgSimpleName(String simpleName);

	/**
	 * 根据机构ID和过滤ID查询直接下级机构记录
	 * 
	 * @param orgId 父级机构ID
	 * @param filterOrgId 过滤掉此机构
	 * @return
	 */
	List<Organ> findByUpOrgIdAFilter(@Param("orgId") Long orgId, @Param("filterOrgId") Long filterOrgId);

	/**
	 * 根据机构IDs查询机构记录
	 * 
	 * <pre>
	 *  数据结构:
	 *  [
	 *  	{orgId, orgSimpleName}, ...
	 *  ]
	 * </pre>
	 * 
	 * @param orgIds 机构IDs
	 * @return
	 */
	List<Organ> findByOrgIds(Long[] orgIds);

}

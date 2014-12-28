package com.mycuckoo.persistence.iface.uum;

import java.util.List;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 机构持久层接口
 * 
 * @author rutine
 * @time Sep 24, 2014 10:43:42 AM
 * @version 2.0.0
 */
public interface UumOrganRepository extends Repository<UumOrgan, Long> {

	/**
	 * 
	 * 查询所有直接下级机构记录
	 * 
	 * @param upOrgId 父级机构ID
	 * @return 所有关联的子机构
	 */
	List<UumOrgan> findOrgansByUpOrgId(Long upOrgId);

	/**
	 * 返回所有机构记录
	 * 
	 * @return
	 */
	List<UumOrgan> findAllOrgans();

	/**
	 * 统计所有下级机构数量
	 * 
	 * @param upOrgId 父级机构ID
	 * @return
	 */
	int countOrgansByUpOrgId(Long upOrgId);

	/**
	 * 根据机构简称统计机构数量
	 * 
	 * @param simpleName 机构简称
	 * @return
	 */
	int countOrgansByOrgSimpleName(String simpleName);

	/**
	 * 根据机构ID和过滤ID查询直接下级机构记录
	 * 
	 * @param orgId 父级机构ID
	 * @param filterOrgId 过滤掉此机构
	 * @return
	 */
	List<UumOrgan> findOrgansByUpOrgIdAFilter(Long orgId, Long filterOrgId);

	/**
	 * 根据条件分页查询机构记录
	 * 
	 * @param orgIds 机构ID数组
	 * @param orgCode 机构代码
	 * @param orgName 机构名称
	 * @param page 分页
	 * @return
	 */
	Page<UumOrgan> findOrgansByCon(Long[] orgIds, String orgCode,
			String orgName, Pageable page);

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
	List findOrgansByOrgIds(Long[] orgIds);

}

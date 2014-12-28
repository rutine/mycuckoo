package com.mycuckoo.persistence.impl.uum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.persistence.iface.uum.UumOrganRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 机构持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:03:30 PM
 * @version 2.0.0
 */
@Repository
public class UumOrganRepositoryImpl extends AbstractRepository<UumOrgan, Long>
		implements UumOrganRepository {

	@Override
	public List<UumOrgan> findOrgansByUpOrgId(Long orgId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findOrgansByUpOrgId", orgId);
	}

	@Override
	public List<UumOrgan> findAllOrgans() {
		return (List<UumOrgan>) this.findAll();
	}

	@Override
	public int countOrgansByUpOrgId(Long upOrgId) {
		Map<String, Long> map = new HashMap<String, Long>(1);
		map.put("upOrgId", upOrgId);

		return getSqlSession().selectOne(obtainMapperNamespace() + ".countOrgansByCon", map);
	}

	@Override
	public int countOrgansByOrgSimpleName(String simpleName) {
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("simpleName", simpleName);

		return getSqlSession().selectOne(obtainMapperNamespace() + ".countOrgansByCon", map);
	}

	@Override
	public List<UumOrgan> findOrgansByUpOrgIdAFilter(Long orgId, Long filterOrgId) {
		
		Map<String, Long> map = new HashMap<String, Long>(2);
		map.put("orgId", orgId);
		map.put("filterOrgId", filterOrgId);

		return getSqlSession().selectList(obtainMapperNamespace() + ".findOrgansByUpOrgIdAFilter", map);
	}

	@Override
	public Page<UumOrgan> findOrgansByCon(Long[] orgIds, String orgCode, String orgName, Pageable page) {
		
		if (StringUtils.isNotBlank(orgCode)) {
			orgCode = "%" + orgCode + "%";
		}
		if (StringUtils.isNotBlank(orgName)) {
			orgName = "%" + orgName + "%";
		}
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("orgIds", ArrayUtils.isNotEmpty(orgIds) ? orgIds : null);
		map.put("orgCode", orgCode);
		map.put("orgName", orgName);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<UumOrgan> list = getSqlSession().selectList(obtainMapperNamespace() + ".findOrgansByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countOrgansByCon", map);

		return new PageImpl<UumOrgan>(list, page, count);
	}

	@Override
	public List findOrgansByOrgIds(Long[] orgIds) {
		if (ArrayUtils.isEmpty(orgIds)) {
			throw new SystemException("orgIds is null or empty in method of class UumOrganRepositoryImpl: " + "findOrgansByOrgIds.");
		}
		
		return getSqlSession().selectList(obtainMapperNamespace() + ".findOrgansByOrgIds", orgIds);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumOrgan entity) {
		return entity.getOrgId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.UumOrganMapper";
	}

}

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
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.persistence.iface.uum.UumOrgRoleRefRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 机构角色关系持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:06:04 PM
 * @version 2.0.0
 */
@Repository
public class UumOrgRoleRefRepositoryImpl extends AbstractRepository<UumOrgRoleRef, Long> 
		implements UumOrgRoleRefRepository {

	@Override
	public int countOrgRoleRefsByOrgId(Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("orgId", orgId);

		return getSqlSession().selectOne(obtainMapperNamespace() + ".countRolesByCon", map);
	}

	@Override
	public void deleteOrgRoleRefsByRoleId(Long roleId) {
		int count = getSqlSession().delete(obtainMapperNamespace() + ".deleteOrgRoleRefsByRoleId", roleId);
		
		logger.debug("successing to delete OrgRoleRef with total: " + count);
	}

	@Override
	public List<UumOrgRoleRef> findOrgansByRoleId(Long roleId) {
		return this.getSqlSession().selectList(obtainMapperNamespace() + ".findOrgansByRoleId", roleId);
	}

	@Override
	public List<Long> findOrgRoleIdsByRoleId(Long roleId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findOrgRoleIdsByRoleId", roleId);
	}

	@Override
	public List<UumOrgRoleRef> findOrgRoleRefsByOrgRoleIds(Long[] orgRoleRefIds) {
		if (ArrayUtils.isEmpty(orgRoleRefIds)) {
			throw new SystemException("orgRoleRefs is null  or empty in mehtod of class UumOrgRoleRefRepositoryImpl: " + "findOrgRoleRefsByOrgRoleIds");
		}

		return getSqlSession().selectList(obtainMapperNamespace() + ".findOrgRoleRefsByOrgRoleIds", orgRoleRefIds);
	}

	@Override
	public Page<UumOrgRoleRef> findRolesByCon(Long orgId, String roleName, Pageable page) {
		
		if (StringUtils.isNotBlank(roleName)) {
			roleName = "%" + roleName + "%";
		} else {
			roleName = null;
		}
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("orgId", orgId);
		map.put("roleName", roleName);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<UumOrgRoleRef> list = getSqlSession().selectList(obtainMapperNamespace() + ".findRolesByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countRolesByCon", map);

		return new PageImpl<UumOrgRoleRef>(list, page, count);
	}

	@Override
	public List<UumOrgRoleRef> findRolesByOrgId(Long orgId) {
		return this.getSqlSession().selectList(obtainMapperNamespace() + ".findRolesByOrgId", orgId);
	}

	@Override
	public UumOrgRoleRef getOrgRoleRefByOrgRoleId(Long orgId, Long roleId) {
		Map<String, Long> map = new HashMap<String, Long>(2);
		map.put("orgId", orgId);
		map.put("roleId", roleId);

		UumOrgRoleRef orgRoleRef = getSqlSession().selectOne(obtainMapperNamespace() + ".getOrgRoleRefByOrgRoleId", map);

		return orgRoleRef == null ? new UumOrgRoleRef() : orgRoleRef;
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumOrgRoleRef entity) {
		return entity.getOrgRoleId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.UumOrgRoleRefMapper";
	}

}

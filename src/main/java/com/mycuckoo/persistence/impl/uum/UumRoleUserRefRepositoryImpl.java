package com.mycuckoo.persistence.impl.uum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.persistence.iface.uum.UumRoleUserRefRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 角色用户关系持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:13:25 PM
 * @version 2.0.0
 */
@Repository
public class UumRoleUserRefRepositoryImpl extends AbstractRepository<UumRoleUserRef, Long> 
		implements UumRoleUserRefRepository {

	@Override
	public List<UumRoleUserRef> findRoleUserRefsByUserId(Long userId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findRoleUserRefsByUserId", userId);
	}

	@Override
	public void deleteRoleUserRefByUserId(Long userId) {
		int count = getSqlSession().delete(obtainMapperNamespace() + ".deleteRoleUserRefByUserId", userId);

		logger.debug("successing to delete RoleUserRefs with total: " + count);
	}

	@Override
	public int countRoleUserRefsByOrgRoleId(Long orgRoleId) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countRoleUserRefsByOrgRoleId", orgRoleId);
	}

	@Override
	public int countRoleUserRefsByRoleId(Long roleId) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countRoleUserRefsByRoleId", roleId);
	}

	@Override
	public UumRoleUserRef getRoleUserRefByUserIdAOrgRoleId(Long userId, Long orgRoleId) {
		Map<String, Long> map = new HashMap<String, Long>(2);
		map.put("userId", userId);
		map.put("orgRoleId", orgRoleId);

		return getSqlSession().selectOne(obtainMapperNamespace() + ".findRoleUserRefByUserIdAOrgRoleId", map);
	}

	@Override
	public List<UumRoleUserRef> findRoleUserRefsByOrgRoleId(Long orgId, Long roleId) {
		Map<String, Long> map = new HashMap<String, Long>(2);
		map.put("orgId", orgId);
		map.put("roleId", roleId);
		
		return getSqlSession().selectList(obtainMapperNamespace() + ".findRoleUserRefsByOrgRoleId", map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumRoleUserRef entity) {
		return entity.getOrgRoleUserId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.UumRoleUserRefMapper";
	}

}

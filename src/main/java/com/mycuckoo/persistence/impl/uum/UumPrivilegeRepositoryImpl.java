package com.mycuckoo.persistence.impl.uum;

import static com.mycuckoo.common.constant.Common.OWNER_TYPE_USR;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_SCOPE_ORG;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_OPT;
import static com.mycuckoo.common.constant.Common.PRIVILEGE_TYPE_ROW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.uum.UumPrivilege;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.persistence.iface.uum.UumPrivilegeRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 权限持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:09:06 PM
 * @version 2.0.0
 */
@Repository
public class UumPrivilegeRepositoryImpl extends AbstractRepository<UumPrivilege, Long> 
		implements UumPrivilegeRepository {

	@Override
	public List<UumPrivilege> findPrivilegesByOwnIdAPriType(Long[] ownerIds,
			String[] ownerTypes, String[] privilegeTypes) {
		
		if (ArrayUtils.isEmpty(ownerIds)) {
			throw new SystemException("ownerIds is null or empty in mehtod of class "
					+ "UumPrivilegeRepositoryImpl: " + "findPrivilegesByOwnIdAPriType.");
		}
		
		if (ArrayUtils.isEmpty(ownerTypes)) {
			throw new SystemException("ownerTypes is null or empty in method of class "
					+ "UumPrivilegeRepositoryImpl: " + "findPrivilegesByOwnIdAPriType.");
		}
		
		if (ArrayUtils.isEmpty(privilegeTypes)) {
			throw new SystemException("privilegeTypes is null or empty in in method of class "
					+ "UumPrivilegeRepositoryImpl: " + "findPrivilegesByOwnIdAPriType.");
		}
		

		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("ownerIds", ownerIds);
		map.put("ownerTypes", ownerTypes);
		map.put("privilegeTypes", privilegeTypes);

		return getSqlSession().selectList(obtainMapperNamespace() + ".findPrivilegesByOwnIdAPriType", map);
	}

	@Override
	public int countSpecialPrivilegesByUserId(Long userId) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("userId", userId);
		map.put("ownerType", OWNER_TYPE_USR);

		return getSqlSession().selectOne(obtainMapperNamespace() + ".countSpecialPrivilegesByUserId", map);
	}

	@Override
	public void deletePrivilegeByOwnerIdAType(Long ownerId, String ownerType) {
		deletePrivilegeByOwnerIdAPriType(ownerId, ownerType, null);
	}

	@Override
	public void deletePrivilegeByOwnerIdAPriType(Long ownerId,
			String ownerType, String privilegeType) {
		
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("ownerId", ownerId);
		map.put("ownerType", ownerType);
		map.put("privilegeType", privilegeType);

		getSqlSession().delete(obtainMapperNamespace() + ".deleteByCon", map);
	}

	@Override
	public void deleteRowPrivilegeByOrgId(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("resourceId", orgId);
		map.put("privilegeType", PRIVILEGE_TYPE_ROW);
		map.put("privilegeScope", PRIVILEGE_SCOPE_ORG);

		getSqlSession().delete(obtainMapperNamespace() + ".deleteByCon", map);
	}

	@Override
	public void deletePrivilegeByModOptId(String[] modOptRefIds) {
		if (ArrayUtils.isEmpty(modOptRefIds)) {
			throw new SystemException("modOptRefIds is null or empty in method of class "
					+ "UumPrivilegeRepositoryImpl:" + " deletePrivilegeByModOptId.");
		}
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("modOptRefIds", modOptRefIds);
		map.put("privilegeType", PRIVILEGE_TYPE_OPT);

		getSqlSession().delete(obtainMapperNamespace() + ".deletePrivilegeByModOptId", map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumPrivilege entity) {
		return entity.getOwnerId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.UumPrivilegeMapper";
	}

}

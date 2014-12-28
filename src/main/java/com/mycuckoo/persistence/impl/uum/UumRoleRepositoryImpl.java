package com.mycuckoo.persistence.impl.uum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.persistence.iface.uum.UumRoleRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 角色持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:12:32 PM
 * @version 2.0.0
 */
@Repository
public class UumRoleRepositoryImpl extends AbstractRepository<UumRole, Long>
		implements UumRoleRepository {

	@Override
	public List<UumRole> findAllRoles() {
		return (List<UumRole>) this.findAll();
	}

	@Override
	public Page<UumRole> findRolesByCon(String roleName, Pageable page) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("roleName", StringUtils.isNotBlank(roleName) ? "%" + roleName + "%" : null);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<UumRole> list = getSqlSession().selectList(obtainMapperNamespace() + ".findRolesByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countRolesByCon", map);

		return new PageImpl<UumRole>(list, page, count);
	}

	@Override
	public int countRolesByRoleName(String roleName) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countRolesByRoleName", roleName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumRole entity) {
		return entity.getRoleId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.UumRoleMapper";
	}

}

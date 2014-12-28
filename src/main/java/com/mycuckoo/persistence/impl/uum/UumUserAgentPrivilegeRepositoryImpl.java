package com.mycuckoo.persistence.impl.uum;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.uum.UumUserAgentPrivilege;
import com.mycuckoo.persistence.iface.uum.UumUserAgentPrivilegeRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 用户代理权限持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:15:19 PM
 * @version 2.0.0
 */
@Repository
public class UumUserAgentPrivilegeRepositoryImpl extends AbstractRepository<UumUserAgentPrivilege, Long> 
		implements UumUserAgentPrivilegeRepository {

	@Override
	public List findPrivilegeIdsByAgentId(Long agentId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findPrivilegeIdsByAgentId", agentId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumUserAgentPrivilege entity) {
		return entity.getUserAgentPriId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.UumUserAgentPrivilegeMapper";
	}

}

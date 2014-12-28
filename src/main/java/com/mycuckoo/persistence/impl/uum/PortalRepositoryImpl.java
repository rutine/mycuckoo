package com.mycuckoo.persistence.impl.uum;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.uum.Portal;
import com.mycuckoo.persistence.iface.uum.PortalRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 门户持久层接口实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:03:00 PM
 * @version 2.0.0
 */
@Repository
public class PortalRepositoryImpl extends AbstractRepository<Portal, Long> 
		implements PortalRepository {

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(Portal entity) {
		return entity.getPortalId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.PortalMapper";
	}

}

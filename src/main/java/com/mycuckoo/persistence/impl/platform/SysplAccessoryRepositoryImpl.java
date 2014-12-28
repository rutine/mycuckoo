package com.mycuckoo.persistence.impl.platform;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.platform.SysplAccessory;
import com.mycuckoo.persistence.iface.platform.SysplAccessoryRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 附件持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:19:18 PM
 * @version 2.0.0
 */
@Repository
public class SysplAccessoryRepositoryImpl extends AbstractRepository<SysplAccessory, Long> 
		implements SysplAccessoryRepository {

	@Override
	public void deleteAccessorysByIds(Long[] accessoryIds) {
		getSqlSession().delete(obtainMapperNamespace() + ".deleteSome", Arrays.asList(accessoryIds));
	}

	@Override
	public List<SysplAccessory> findAccessorysByAfficheId(Long afficheId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findAccessorysByAfficheId", afficheId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplAccessory entity) {
		return entity.getAccessoryId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplAccessoryMapper";
	}

}

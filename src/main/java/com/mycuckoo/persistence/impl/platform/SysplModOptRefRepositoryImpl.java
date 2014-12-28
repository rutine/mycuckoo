package com.mycuckoo.persistence.impl.platform;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.persistence.iface.platform.SysplModOptRefRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 模块操作关系持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:45:35 PM
 * @version 2.0.0
 */
@Repository
public class SysplModOptRefRepositoryImpl extends AbstractRepository<SysplModOptRef, Long> 
		implements SysplModOptRefRepository {

	@Override
	public void deleteModOptRefByModuleId(long moduleId) {
		int count = getSqlSession().delete(obtainMapperNamespace() + ".deleteModOptRefByModuleId", moduleId);

		logger.debug("success deleting module: " + count);
	}

	@Override
	public void deleteModOptRefByOperateId(long operateId) {
		int count = getSqlSession().delete(obtainMapperNamespace() 
				+ ".deleteModOptRefByOperateId", operateId);

		logger.debug("success deleting module: " + count);
	}

	@Override
	public List<SysplModOptRef> findModOptRefsByModuleId(Long moduleId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findModOptRefsByModuleId", moduleId);
	}

	@Override
	public List<SysplModOptRef> findModOptRefsByOperateId(Long operateId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findModOptRefsByOperateId", operateId);
	}

	@Override
	public List<SysplModOptRef> findModOptRefsByIds(Long[] modOptRefIds) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findModOptRefsByIds", modOptRefIds);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object
	 * )
	 */
	@Override
	protected Long obtainID(SysplModOptRef entity) {
		return entity.getModOptId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplModOptRefMapper";
	}

}

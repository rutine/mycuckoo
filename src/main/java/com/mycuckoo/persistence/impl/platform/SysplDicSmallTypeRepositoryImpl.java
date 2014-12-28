package com.mycuckoo.persistence.impl.platform;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.platform.SysplDicSmallType;
import com.mycuckoo.persistence.iface.platform.SysplDicSmallTypeRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 字典小类型持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:36:52 PM
 * @version 2.0.0
 */
@Repository
public class SysplDicSmallTypeRepositoryImpl extends AbstractRepository<SysplDicSmallType, Long> 
		implements SysplDicSmallTypeRepository {

	@Override
	public void deleteDicSmallTypesByBigTypeId(long bigTypeId) {
		int count = getSqlSession().delete(obtainMapperNamespace()  + ".deleteDicSmallTypesByBigTypeId", bigTypeId);

		logger.debug("successing delete SysplDicSmallType in method of class SysplDicSmallTypeRepositoryImpl: "
				+ "deleteDicSmallTypesByBigTypeId with '" + count + "' record by bigTypeId = " + bigTypeId);
	}

	@Override
	public List<SysplDicSmallType> findDicSmallTypesByBigTypeId(long bigTypeId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findDicSmallTypesByBigTypeId", bigTypeId);
	}

	@Override
	public List<SysplDicSmallType> findDicSmallTypesByBigTypeCode(String bigTypeCode) {
		
		return getSqlSession().selectList(obtainMapperNamespace() + ".findDicSmallTypesByBigTypeCode", bigTypeCode);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplDicSmallType entity) {
		return entity.getSmallTypeId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplDicSmallTypeMapper";
	}

}

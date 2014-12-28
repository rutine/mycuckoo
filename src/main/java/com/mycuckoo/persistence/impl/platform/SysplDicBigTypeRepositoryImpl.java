package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDicBigType;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.persistence.iface.platform.SysplDicBigTypeRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 字典大类型持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:32:16 PM
 * @version 2.0.0
 */
@Repository
public class SysplDicBigTypeRepositoryImpl extends AbstractRepository<SysplDicBigType, Long> 
		implements SysplDicBigTypeRepository {

	@Override
	public int countDicBigTypesByBigTypeCode(String bigTypeCode) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countDicBigTypesByBigTypeCode", bigTypeCode);
	}

	@Override
	public Page<SysplDicBigType> findDicBigTypesByCon(String bigTypeName,
			String bigTypeCode, Pageable page) {
		
		if (page == null)
			throw new SystemException("page is null in method of class SysplDicBigTypeRepositoryImpl: findDicBigTypesByCon.");

		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("bigTypeName", isNotBlank(bigTypeName) ? "%" + bigTypeName + "%" : null);
		map.put("bigTypeCode", isNotBlank(bigTypeCode) ? "%" + bigTypeCode + "%" : null);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<SysplDicBigType> list = getSqlSession()
				.selectList(obtainMapperNamespace() + ".findDicBigTypesByCon", map);
		int count = getSqlSession()
				.selectOne(obtainMapperNamespace() + ".countDicBigTypesByCon", map);

		return new PageImpl<SysplDicBigType>(list, page, count);
	}

	@Override
	public void updateDicBigTypeStatus(long bigTypeId, String status) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("bigTypeId", bigTypeId);
		map.put("status", status);

		getSqlSession().update(obtainMapperNamespace() + ".updateDicBigTypeStatus", map);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplDicBigType entity) {
		return entity.getBigTypeId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplDicBigTypeMapper";
	}

}

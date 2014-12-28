package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplSysParameter;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.persistence.iface.platform.SysplSysParameterRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 系统参数持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:59:43 PM
 * @version 2.0.0
 */
@Repository
public class SysplSysParameterRepositoryImpl 
		extends AbstractRepository<SysplSysParameter, Long> 
		implements SysplSysParameterRepository {

	@Override
	public Page<SysplSysParameter> findSystemParametersByCon(String paraName,
			String paraKeyName, Pageable page) {
		
		if (page == null) {
			throw new SystemException("page is null in SysplSysParameterRepositoryImpl's method:"
					+ " findSystemParaByConditions.");
		}

		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("paraName", isNotBlank(paraName) ? "%" + paraName + "%" : null);
		map.put("paraKeyName", isNotBlank(paraKeyName) ? "%" + paraKeyName + "%" : null);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<SysplSysParameter> list = getSqlSession()
				.selectList(obtainMapperNamespace() + ".findSystemParametersByCon", map);
		int count = getSqlSession()
				.selectOne(obtainMapperNamespace() + ".countSystemParametersByCon", map);

		return new PageImpl<SysplSysParameter>(list, page, count);
	}

	@Override
	public SysplSysParameter getSystemParameterByParaName(String paraName) {
		List<SysplSysParameter> list = getSqlSession()
				.selectList(obtainMapperNamespace() + ".getSystemParameterByParaName", paraName);

		if (list == null || list.isEmpty()) {
			return null;
		}
			
		return list.get(0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplSysParameter entity) {
		return entity.getParaId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplSysParameterMapper";
	}

}

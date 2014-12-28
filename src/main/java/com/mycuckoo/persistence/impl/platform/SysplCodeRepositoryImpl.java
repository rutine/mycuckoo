package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplCode;
import com.mycuckoo.persistence.iface.platform.SysplCodeRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 编码持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:29:30 PM
 * @version 2.0.0
 */
@Repository
public class SysplCodeRepositoryImpl extends AbstractRepository<SysplCode, Long> 
		implements SysplCodeRepository {

	@Override
	public int countCodesByCodeEngName(String codeEngName) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countCodesByCodeEngName", codeEngName);
	}

	@Override
	public Page<SysplCode> findCodesByCon(String codeEngName,
			String codeName, String moduleName, Pageable page) {
		
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("codeEngName", isNotBlank(codeEngName) ? "%" + codeEngName + "%" : null);
		map.put("codeName", isNotBlank(codeName) ? "%" + codeName + "%" : null);
		map.put("moduleName", isNotBlank(moduleName) ? "%" + moduleName + "%" : null);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<SysplCode> list = getSqlSession().selectList(obtainMapperNamespace() + ".findCodesByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countCodesByCon", map);

		return new PageImpl<SysplCode>(list, page, count);
	}

	@Override
	public SysplCode getCodeByCodeEngName(String codeEngName) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".getCodeByCodeEngName", codeEngName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplCode entity) {
		return entity.getCodeId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplCodeMapper";
	}

}

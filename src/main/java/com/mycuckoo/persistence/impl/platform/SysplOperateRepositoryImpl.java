package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.persistence.iface.platform.SysplOperateRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 模块操作持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:53:01 PM
 * @version 2.0.0
 */
@Repository
public class SysplOperateRepositoryImpl extends AbstractRepository<SysplOperate, Long> 
		implements SysplOperateRepository {

	@Override
	public int countOperatesByName(String operateName) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countOperatesByName", operateName);
	}

	@Override
	public List<SysplOperate> findAllOperates() {
		return (List<SysplOperate>) this.findAll();
	}

	@Override
	public Page<SysplOperate> findOperatesByCon(String operateName, Pageable page) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("operateName", isNotBlank(operateName) ? "%" + operateName + "%" : null);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<SysplOperate> list = getSqlSession().selectList(obtainMapperNamespace() + ".findOperatesByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countOperatesByCon", map);

		return new PageImpl<SysplOperate>(list, page, count);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplOperate entity) {
		return entity.getOperateId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplOperateMapper";
	}

}

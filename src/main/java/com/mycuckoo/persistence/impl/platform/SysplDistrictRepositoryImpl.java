package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.persistence.iface.platform.SysplDistrictRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 地区持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:40:20 PM
 * @version 2.0.0
 */
@Repository
public class SysplDistrictRepositoryImpl extends AbstractRepository<SysplDistrict, Long> 
		implements SysplDistrictRepository {

	@Override
	public int countDistrictsByUpDistrictId(long upDistrictId) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countDistrictsByUpDistrictId", upDistrictId);
	}

	@Override
	public int countDistrictsByDistrictName(String districtName) {
		return getSqlSession().selectOne(obtainMapperNamespace() + ".countDistrictsByDistrictName", districtName);
	}

	@Override
	public List<SysplDistrict> findDistrictsByUpDistrictId(Long upDistrictId) {
		return getSqlSession().selectList(obtainMapperNamespace() + ".findDistrictsByUpDistrictId", upDistrictId);
	}

	@Override
	public List<SysplDistrict> findDistrictsByUpDistrictsAFilterIds(long upDistrictId, long filterDistrictId) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("upDistrictId", upDistrictId);
		List<Long> list = new ArrayList<Long>(2);
		list.add(0l);
		
		if (filterDistrictId != 0) {
			list.add(filterDistrictId);
		}
		map.put("list", list);

		return getSqlSession().selectList(obtainMapperNamespace() + ".findDistrictsByUpDistrictsAFilterIds", map);
	}

	@Override
	public Page<SysplDistrict> findDistrictsByCon(Long[] ids,
			String districtName, String districtLevel, Pageable page) {
		
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("array", ArrayUtils.isNotEmpty(ids) ? ids : null);
		map.put("districtName", isNotBlank(districtName) ? "%" + districtName + "%" : null);
		map.put("districtLevel", isNotBlank(districtLevel) ? districtLevel : null);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<SysplDistrict> list = getSqlSession().selectList(obtainMapperNamespace() + ".findDistrictsByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countDistrictsByCon", map);

		return new PageImpl<SysplDistrict>(list, page, count);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object
	 * )
	 */
	@Override
	protected Long obtainID(SysplDistrict entity) {
		return entity.getDistrictId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplDistrictMapper";
	}

}

package com.mycuckoo.persistence.impl.platform;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.platform.SysplAffiche;
import com.mycuckoo.persistence.iface.platform.SysplAfficheRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 公告持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 8:25:35 PM
 * @version 2.0.0
 */
@Repository
public class SysplAfficheRepositoryImpl extends AbstractRepository<SysplAffiche, Long> 
		implements SysplAfficheRepository {

	@Override
	public void deleteAffichesByIds(Long[] afficheIds) {
		getSqlSession().delete(obtainMapperNamespace() + ".deleteSome", Arrays.asList(afficheIds));
	}

	@Override
	public Page<SysplAffiche> findAffichesByCon(String afficheTitle, 
			Short affichePulish, Pageable page) {
		
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("afficheTitle", isNotBlank(afficheTitle) ? "%" + afficheTitle + "%" : null);
		map.put("affichePulish", affichePulish);
		map.put("start", page.getPageNumber());
		map.put("limit", page.getPageSize());

		List<SysplAffiche> list = getSqlSession().selectList(obtainMapperNamespace() + ".findAffichesByCon", map);
		int count = getSqlSession().selectOne(obtainMapperNamespace() + ".countAffichesByCon", map);

		return new PageImpl<SysplAffiche>(list, page, count);
	}

	@Override
	public List<SysplAffiche> findAffichesBeforeValidate() {
		return getSqlSession().selectList(obtainMapperNamespace() 
				+ ".findAffichesBeforeValidate", Calendar.getInstance().getTime());
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(SysplAffiche entity) {
		return entity.getAfficheId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.platform.SysplAfficheMapper";
	}

}

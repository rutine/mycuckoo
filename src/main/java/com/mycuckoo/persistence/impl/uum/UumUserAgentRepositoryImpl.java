package com.mycuckoo.persistence.impl.uum;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.persistence.iface.uum.UumUserAgentRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 用户代理持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:17:39 PM
 * @version 2.0.0
 */
@Repository
public class UumUserAgentRepositoryImpl extends AbstractRepository<UumUserAgent, Long> 
		implements UumUserAgentRepository {

	@Override
	public Page<UumUserAgent> findUserAgentsByCon(Long userId, Pageable page) {
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put("userId", userId);
		map.put("start", page.getOffset());
		map.put("limit", page.getPageSize());

		List<UumUserAgent> list = getSqlSession().selectList(obtainMapperNamespace() + ".findUserAgentsByCon", map);
		long count = getSqlSession().selectOne(obtainMapperNamespace() + ".countUserAgentsByCon", map);

		return new PageImpl<UumUserAgent>(list, page, count);
	}

	@Override
	public int countUserAgentsByUserIdAAgentUserId(long userId, long agentUserId) {
		Map<String, Long> map = new HashMap<String, Long>(2);
		map.put("userId", userId);
		map.put("agentUserId", agentUserId);

		return getSqlSession().selectOne(obtainMapperNamespace() + ".countUserAgentsByUserIdAAgentUserId", map);
	}

	@Override
	public List<UumUserAgent> findUserAgentsByAgentUserId(long agentUserId) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("agentUserId", agentUserId);
		map.put("now", Calendar.getInstance().getTime());

		return getSqlSession().selectList(obtainMapperNamespace() + ".findUserAgentsByAgentUserId", map);
	}

	@Override
	public List<UumUserAgent> findUserAgentsByUserId(long userId) {
		Map<String, Long> map = new HashMap<String, Long>(1);
		map.put("userId", userId);

		return getSqlSession().selectList(obtainMapperNamespace() + ".findUserAgentsByCon", map);
	}

	@Override
	public List<UumUserAgent> findUserAgentsByOwnerId(Long[] ownerIds) {
		if (ArrayUtils.isEmpty(ownerIds)) {
			throw new SystemException("ownerIds is null or empty in method of class "
					+ "UumUserAgentRepositoryImpl: " + "findUserAgentsByOwnerId.");
		}
		
		return getSqlSession().selectList(obtainMapperNamespace() + ".findUserAgentsByOwnerId", ownerIds);
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumUserAgent entity) {
		return entity.getAgentId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.UumUserAgentMapper";
	}

}

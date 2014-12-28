package com.mycuckoo.persistence.impl.uum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.persistence.iface.uum.UumUserCommFunRepository;

/**
 * 功能说明: 用户常用功能持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:20:38 PM
 * @version 2.0.0
 */
@Repository
public class UumUserCommFunRepositoryImpl extends SqlSessionDaoSupport
		implements UumUserCommFunRepository {

	private static String mapperNameSpace = "com.mycuckoo.persistence.uum.UumUserCommfunMapper";

	@Override
	public long count() {
		return getSqlSession().selectOne(mapperNameSpace + ".count");
	}

	@Override
	public void delete(UumUserCommfun uumUserCommfun) {
		getSqlSession().delete(mapperNameSpace + ".delete", uumUserCommfun);
	}

	@Override
	public void deleteAll() {
		getSqlSession().delete(mapperNameSpace + ".deleteAll");
	}

	@Override
	public void deleteUserCommFun(long userId) {
		getSqlSession().delete(mapperNameSpace + ".deleteUserCommFun", userId);
	}

	@Override
	public boolean exists(UumUserCommfun uumUserCommfun) {
		return getSqlSession().selectOne(mapperNameSpace + ".exists", uumUserCommfun);
	}

	@Override
	public Iterable<UumUserCommfun> findAll() {
		return getSqlSession().selectList(mapperNameSpace + ".findAll");
	}

	@Override
	public Page<UumUserCommfun> findAll(Pageable pageable) {
		RowBounds rowBounds = new RowBounds(pageable.getOffset(), pageable.getPageSize());
		List<UumUserCommfun> list = getSqlSession().selectList(mapperNameSpace + ".findAll", null, rowBounds);
		Page<UumUserCommfun> page = new PageImpl<UumUserCommfun>(list, pageable, count());

		return page;
	}

	@Override
	public List<UumUserCommfun> findUserCommFunByUserId(long userId) {
		return getSqlSession().selectList(mapperNameSpace + ".findUserCommFunByUserId", userId);
	}

	@Override
	public UumUserCommfun get(UumUserCommfun uumUserCommfun) {

		return getSqlSession().selectOne(mapperNameSpace + ".get", uumUserCommfun);
	}

	@Override
	public Iterable<UumUserCommfun> save(Iterable<UumUserCommfun> entities) {
		int count = 0;
		for (UumUserCommfun useCommfunId : entities) {
			save(useCommfunId);
			count++;
		}

		logger.debug("success save " + count + " entities in UumUserCommfunRepositoryImpl's save method.");

		return entities;
	}

	@Override
	public UumUserCommfun save(UumUserCommfun UumUserCommfun) {
		int count = getSqlSession().insert(mapperNameSpace + ".save", UumUserCommfun);

		logger.debug("success save " + count + " entities in method of class UumUserCommfunRepositoryImpl: save");

		return UumUserCommfun;
	}

	@Override
	public int update(UumUserCommfun newUserCommfun, UumUserCommfun oldUserCommfun) {
		
		Map<String, Long> map = new HashMap<String, Long>(4);
		map.put("newUserId", newUserCommfun.getUserId());
		map.put("newModuleId", newUserCommfun.getModuleId());
		map.put("oldUserId", oldUserCommfun.getUserId());
		map.put("oldModuleId", oldUserCommfun.getModuleId());

		return getSqlSession().update(mapperNameSpace + ".update", map);
	}
}

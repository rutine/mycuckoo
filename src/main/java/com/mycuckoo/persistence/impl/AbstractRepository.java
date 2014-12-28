package com.mycuckoo.persistence.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.exception.SystemException;
import com.mycuckoo.persistence.iface.Repository;

/**
 * 功能说明: 对持久层顶级接口的一般实现
 *
 * @author rutine
 * @time Sep 23, 2014 8:08:33 PM
 * @version 2.0.0
 */
public abstract class AbstractRepository<T, ID extends Serializable> extends
		SqlSessionDaoSupport implements Repository<T, ID> {

	/**
	 * Logger available to subclasses.
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final String mapperNameSpace;
	
	private final String saveStatement;
	private final String updateStatement;
	private final String getStatement;
	private final String existsStatement;
	private final String findAllStatement;
	private final String countStatement;
	private final String deleteStatement;
	private final String deleteAllStatement;
	
	public AbstractRepository() {
		if(obtainMapperNamespace() == null) {
			Type type = getType(getClass());
			if(type instanceof ParameterizedType) {
				Type[] types = ((ParameterizedType) type).getActualTypeArguments();
				if(types == null || types.length == 0) {
					throw new SystemException(getClass() + "必须具体指明泛型参数值或重写obtainMapperNamespace()方法");
				}
				
				String className = ((Class<?>) types[0]).getName();
				mapperNameSpace = className.replaceFirst("\\.domain\\.", "\\.persistence\\.") + "Mapper";
				
				logger.info("Mapper文件命名空间是: {}", mapperNameSpace);
			} else {
				mapperNameSpace = null;
				
				throw new SystemException(getClass() + "必须具体指明泛型参数值或重写obtainMapperNamespace()方法");
			}
		} else {
			mapperNameSpace = obtainMapperNamespace();
		}
		
		saveStatement		= mapperNameSpace + ".save";
		updateStatement		= mapperNameSpace + ".update";
		getStatement		= mapperNameSpace + ".get";
		existsStatement		= mapperNameSpace + ".exists";
		findAllStatement	= mapperNameSpace + ".findAll";
		countStatement		= mapperNameSpace + ".count";
		deleteStatement		= mapperNameSpace + ".delete";
		deleteAllStatement	= mapperNameSpace + ".deleteAll";
	}
	
	private Type getType(Class<?> clazz) {
		if(clazz == null) return null;
		
		Type type = clazz.getGenericSuperclass();
		if(type instanceof ParameterizedType) {
			return type;
		} else {
			clazz = clazz.getSuperclass();
			return getType(clazz);
		}
	}
	
	@Override
	public <S extends T> S save(S entity) {
		getSqlSession().insert(saveStatement, entity);
		
		return entity;
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		Iterator<S> it = entities.iterator();
		while(it.hasNext()) {
			S entity = (S)it.next();
			save(entity);
		}
		
		return entities;
	}
	
	@Override
	public <S extends T> int update(S entity) {
		return getSqlSession().update(updateStatement, entity);
	}

	@Override
	public <S extends T> int update(Iterable<S> entities) {
		int affectedRow = 0;
		Iterator<S> it = entities.iterator();
		while(it.hasNext()) {
			S entity = (S)it.next();
			affectedRow += update(entity);
		}
		
		return affectedRow;
	}

	@Override
	public T get(ID id) {
		return getSqlSession().selectOne(getStatement, id);
	}

	@Override
	public boolean exists(ID id) {
		return getSqlSession().selectOne(existsStatement, id);
	}

	@Override
	public Iterable<T> findAll() {
		return getSqlSession().selectList(findAllStatement);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		RowBounds rowBounds = new RowBounds(pageable.getOffset(), pageable.getPageSize());
		List<T> list = getSqlSession().selectList(findAllStatement, null, rowBounds);
		Page<T> page = new PageImpl<T>(list, pageable, count());
		
		return page;
	}

	@Override
	public long count() {
		return getSqlSession().selectOne(countStatement);
	}

	@Override
	public void delete(ID id) {
		getSqlSession().delete(deleteStatement, id);
	}

	@Override
	public void delete(T entity) {
		ID id = obtainID(entity);
		delete(id);
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		Iterator<? extends T> it = entities.iterator();
		while(it.hasNext()) {
			T entity = (T)it.next();
			delete(entity);
		}
	}

	@Override
	public void deleteAll() {
		getSqlSession().delete(deleteAllStatement);
	}
	
	/**
	* 设置 my-batis Mapper 映射文件的命名空间
	*
	* @return
	*/
	protected String obtainMapperNamespace() {
		return null;
	}
	
	/**
	* 获取实体对象唯一标识值
	*
	* @param 实体对象
	* @return
	*/
	protected abstract ID obtainID(T entity);
	
}

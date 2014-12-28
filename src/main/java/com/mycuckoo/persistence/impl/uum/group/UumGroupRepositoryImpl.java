package com.mycuckoo.persistence.impl.uum.group;

import static com.mycuckoo.common.constant.Common.GENERAL_GROUP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.persistence.iface.uum.group.UumGroupRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 分组持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:28:54 PM
 * @version 2.0.0
 */
@Repository
public class UumGroupRepositoryImpl extends AbstractRepository<UumGroup, Long>
		implements UumGroupRepository {
	
	@Override
	public int countGroupsByGroupName(String groupName) {
		Map<String, String> params = new HashMap<String, String>(2);
		params.put("groupName", groupName);
		params.put("groupType", GENERAL_GROUP);
		
		return getSqlSession().selectOne(mapperNameSpace + ".countGroupsByGroupName", params);
	}
	
	@Override
	public void deleteGroupMembersByGroupId(Long groupId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("groupId", groupId);
		params.put("groupType", GENERAL_GROUP);
		int count = getSqlSession().delete(mapperNameSpace + ".deleteGroupMembersByGroupId", params);

		logger.debug("successing to delete GroupMember with total: " + count);
	}
	
	@Override
	public Page<UumGroup> findGroupsByCon(String groupName, Pageable page) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("groupType", GENERAL_GROUP);
		if (StringUtils.isNotBlank(groupName)) {
			params.put("groupName", "%" + groupName + "%");
		}
		params.put("start", page.getOffset());
		params.put("limit", page.getPageSize());

		List<UumGroup> list = getSqlSession().selectList(mapperNameSpace + ".findGroupsByCon", params);
		int count = getSqlSession().selectOne(mapperNameSpace + ".countGroupsByCon", params);

		return new PageImpl<UumGroup>(list, page, count);
	}
	
	@Override
	public void updateGroupStatus(Long groupId, String status) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("groupId", groupId);
		params.put("status", status);

		getSqlSession().update(mapperNameSpace + ".updateGroupStatus", params);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumGroup entity) {
		return entity.getGroupId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.group.UumGroupMapper";
	}

}

package com.mycuckoo.persistence.impl.uum.group;

import static com.mycuckoo.common.constant.Common.ROLE_GROUP;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageImpl;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumGroupMember;
import com.mycuckoo.persistence.iface.uum.group.UumRoleGroupRepository;

/**
 * 功能说明: 角色组管理持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:29:24 PM
 * @version 2.0.0
 */
@Repository
public class UumRoleGroupRepositoryImpl extends UumGroupRepositoryImpl
		implements UumRoleGroupRepository {

	@Override
	public int countRoleGroupsByGroupName(String groupName) {
		Map<String, String> params = new HashMap<String, String>(2);
		params.put("groupName", groupName);
		params.put("groupType", ROLE_GROUP);

		return getSqlSession().selectOne(mapperNameSpace + ".countGroupsByGroupName", params);
	}

	@Override
	public void deleteRoleGroupMembersByGroupId(Long groupId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("groupId", groupId);
		params.put("groupType", ROLE_GROUP);
		int count = getSqlSession().delete(mapperNameSpace + ".deleteGroupMembersByGroupId", params);

		logger.debug("successing to delete GroupMember with total: " + count);
	}

	@Override
	public List<UumGroupMember> findRoleGroupMembersByGroupId(Long groupId) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("groupId", groupId);
		params.put("groupType", ROLE_GROUP);
		List<UumGroupMember> list = getSqlSession().selectList(mapperNameSpace + ".findGroupMembersByGroupId", params);
		Collections.sort(list, new UumGroupMember().getUumGroupMemberCompInst());

		return list;
	}

	@Override
	public Page<UumGroup> findRoleGroupsByCon(String groupName, Pageable page) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("groupType", ROLE_GROUP);
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
	public void updateRoleGroupStatus(Long groupId, String status) {
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("groupId", groupId);
		map.put("status", status);

		getSqlSession().update(mapperNameSpace + ".updateGroupStatus", map);
	}

}

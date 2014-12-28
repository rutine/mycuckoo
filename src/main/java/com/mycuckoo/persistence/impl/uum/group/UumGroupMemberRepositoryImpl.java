package com.mycuckoo.persistence.impl.uum.group;

import org.springframework.stereotype.Repository;

import com.mycuckoo.domain.uum.UumGroupMember;
import com.mycuckoo.persistence.iface.uum.group.UumGroupMemberRepository;
import com.mycuckoo.persistence.impl.AbstractRepository;

/**
 * 功能说明: 组员持久层实现类
 * 
 * @author rutine
 * @time Sep 24, 2014 9:28:12 PM
 * @version 2.0.0
 */
@Repository
public class UumGroupMemberRepositoryImpl extends AbstractRepository<UumGroupMember, Long> 
		implements UumGroupMemberRepository {

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainID(java.lang.Object)
	 */
	@Override
	protected Long obtainID(UumGroupMember entity) {
		return entity.getGroupMemberId();
	}

	/*
	 * (non-Javadoc)
	 * @see com.mycuckoo.persistence.impl.AbstractRepository#obtainMapperNamespace()
	 */
	@Override
	protected String obtainMapperNamespace() {
		return "com.mycuckoo.persistence.uum.group.UumGroupMemberMapper";
	}

}

package com.mycuckoo.persistence.uum.group;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumGroupMember;
import com.mycuckoo.persistence.iface.uum.group.UumRoleGroupRepository;

/**
 * 功能说明: UumRoleGroupRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作. 默认在每个测试函数后进行回滚.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:38:05 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumRoleGroupRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumRoleGroupRepository uumRoleGroupRepository;

	// @Test
	public void countGroupByGroupName() {
		logger.debug(uumRoleGroupRepository.countRoleGroupsByGroupName("a"));
	}

	// @Test
	public void deleteGroupMemberByGroupId() {
		this.uumRoleGroupRepository.deleteRoleGroupMembersByGroupId(3l);
	}

	// @Test
	public void findGroupMemberByGroupId() {
		List<UumGroupMember> list = this.uumRoleGroupRepository.findRoleGroupMembersByGroupId(1L);
		for (UumGroupMember member : list)
			logger.debug(member.getGroupMemberType());
	}

	// @Test
	@SuppressWarnings("unchecked")
	public void findRoleGroupsByConditions() {
		Page<UumGroup> page = this.uumRoleGroupRepository.findRoleGroupsByCon("组", new PageRequest(0, 10));

		logger.debug(page.getTotalElements());
	}

	@Test
	public void modifyGroupStatus() {
		uumRoleGroupRepository.updateRoleGroupStatus(1L, "disable");
	}

}

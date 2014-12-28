package com.mycuckoo.persistence.uum.group;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.persistence.iface.uum.group.UumUserGroupRepository;

/**
 * 功能说明: UumUserGroupRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作. 默认在每个测试函数后进行回滚.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:39:02 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumUserGroupRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumUserGroupRepository uumUserGroupRepository;

	// @Test
	public void countGroupByGroupName() {
		logger.debug(uumUserGroupRepository.countUserGroupsByGroupName("a"));
	}

	// @Test
	public void deleteGroupMemberByGroupId() {
		this.uumUserGroupRepository.deleteUserGroupMembersByGroupId(3l);
	}

	// @Test
	@SuppressWarnings("unchecked")
	public void findUserGroupsByConditions() {
		Page<UumGroup> page = this.uumUserGroupRepository.findUserGroupsByCon("组", new PageRequest(0, 10));

		logger.debug(page.getTotalElements());
	}

	@Test
	public void modifyGroupStatus() {
		uumUserGroupRepository.updateUserGroupStatus(1L, "disable");
	}

}

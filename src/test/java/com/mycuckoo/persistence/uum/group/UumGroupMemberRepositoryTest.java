package com.mycuckoo.persistence.uum.group;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.uum.UumGroup;
import com.mycuckoo.domain.uum.UumGroupMember;
import com.mycuckoo.persistence.iface.uum.group.UumGroupMemberRepository;

/**
 * 功能说明: UumGroupMemberRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作. 默认在每个测试函数后进行回滚.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:37:10 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumGroupMemberRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumGroupMemberRepository uumGroupMemberRepository;

	@Test
	public void saveUumGroupMember() {
		UumGroupMember uumGroupMember = new UumGroupMember();
		uumGroupMember.setGroupMemberType("角色");
		uumGroupMember.setUumGroup(new UumGroup(1L, "enable"));

		uumGroupMemberRepository.save(uumGroupMember);

		Assert.assertEquals(new Long(1), uumGroupMember.getGroupMemberId(), 20L);
	}

	// @Test
	public void updateUumGroupMember() {
		UumGroupMember uumGroupMember = new UumGroupMember();
		uumGroupMember.setGroupMemberType("角色");
		uumGroupMember.setUumGroup(new UumGroup(1L, "enable"));
		uumGroupMember.setGroupMemberId(1L);

		int row = uumGroupMemberRepository.update(uumGroupMember);

		Assert.assertEquals(1, row);
	}

	// @Test
	public void updateUumGroupMembers() {
		UumGroupMember uumGroupMember = new UumGroupMember();
		uumGroupMember.setGroupMemberType("角色");
		uumGroupMember.setUumGroup(new UumGroup(1L, "enable"));
		uumGroupMember.setGroupMemberId(1L);

		List<UumGroupMember> list = Lists.newArrayList();
		list.add(uumGroupMember);
		uumGroupMember = new UumGroupMember();
		uumGroupMember.setGroupMemberType("角色");
		uumGroupMember.setUumGroup(new UumGroup(1L, "enable"));
		uumGroupMember.setGroupMemberId(1L);
		list.add(uumGroupMember);

		int row = uumGroupMemberRepository.update(list);

		Assert.assertEquals(1, row);
	}

	// @Test
	public void deleteUumGroupMember() {
		uumGroupMemberRepository.delete(3L);
	}

	// @Test
	public void existsUumGroupMember() {
		boolean exists = uumGroupMemberRepository.exists(34L);

		Assert.assertEquals(Boolean.TRUE, exists);
	}

	// @Test
	public void countUumGroupMember() {
		long count = uumGroupMemberRepository.count();

		logger.debug(count);
	}

	// @Test
	public void getUumGroupMember() {
		UumGroupMember uumGroupMember = uumGroupMemberRepository.get(2L);

		Assert.assertNotNull(uumGroupMember);
		Assert.assertEquals("技术", uumGroupMember.getGroupMemberType());
	}

	// @Test
	public void findAllUumGroupMember() {

		List<UumGroupMember> list = (List<UumGroupMember>) uumGroupMemberRepository.findAll();

		logger.debug(list.get(0).getGroupMemberType());
	}
}

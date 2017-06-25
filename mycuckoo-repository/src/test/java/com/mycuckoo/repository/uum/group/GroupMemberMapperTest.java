package com.mycuckoo.repository.uum.group;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.uum.Group;
import com.mycuckoo.domain.uum.GroupMember;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class GroupMemberMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(GroupMemberMapperTest.class);

	@Autowired
	private GroupMemberMapper mapper;
	

	@Test
	public void testSave() {
		GroupMember groupMember = new GroupMember();
		groupMember.setGroupMemberType("角色");
		groupMember.setUumGroup(new Group(1L, "enable"));

		mapper.save(groupMember);

		Assert.assertEquals(new Long(1), groupMember.getGroupMemberId(), 20L);
	}

	@Test
	public void testUpdate() {
		GroupMember groupMember = new GroupMember();
		groupMember.setGroupMemberType("角色");
		groupMember.setUumGroup(new Group(1L, "enable"));
		groupMember.setGroupMemberId(1L);

		int row = mapper.update(groupMember);

		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		GroupMember uumGroupMember = mapper.get(2L);

		Assert.assertNotNull(uumGroupMember);
		Assert.assertEquals("技术", uumGroupMember.getGroupMemberType());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(34L);

		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Page<GroupMember> page = mapper.findByPage(null, new PageRequest(0, 5));

		for(GroupMember entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();

		logger.info("------> count: {}", count);
	}

}

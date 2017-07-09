package com.mycuckoo.repository.uum.group;

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Maps;
import com.mycuckoo.common.constant.GroupTypeEnum;
import com.mycuckoo.domain.uum.Group;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class GroupMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(GroupMapperTest.class);
	
	@Autowired
	private GroupMapper mapper;
	

	@Test
	public void testCountByGroupName() {
		long count = mapper.countByGroupName("a", GroupTypeEnum.ROLE_GROUP.value());
		
		logger.info("------> countByGroupName: {}", count);
	}

	@Test
	public void testUpdateStatus() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteGroupMembersByGroupId() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		Group group = new Group();
		group.setGroupName("分组1");
		group.setGroupType("用户");
		group.setStatus("enable");

		mapper.save(group);

		Assert.assertEquals(new Long(1), group.getGroupId(), 20L);
	}

	@Test
	public void testUpdate() {
		Group group = new Group();
		group.setGroupName("分组1");
		group.setGroupType("用户");
		group.setStatus("enable");
		group.setGroupId(3L);

		int row = mapper.update(group);

		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		Group group = mapper.get(2L);

		Assert.assertNotNull(group);
		Assert.assertEquals("技术", group.getGroupName());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(34L);

		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("groupName", "%组%");
		params.put("groupType", GroupTypeEnum.ROLE_GROUP.value());
		Page<Group> page = mapper.findByPage(null, new PageRequest(0, 5));
		
		for(Group entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();

		logger.info("------> count: {}", count);
	}

}

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
import com.mycuckoo.persistence.iface.uum.group.UumGroupRepository;

/**
 * 功能说明: UumGroupRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作. 默认在每个测试函数后进行回滚.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:37:41 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumGroupRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumGroupRepository uumGroupRepository;

	// @Test
	public void saveUumGroup() {
		UumGroup uumGroup = new UumGroup();
		uumGroup.setGroupName("分组1");
		uumGroup.setGroupType("用户");
		uumGroup.setStatus("enable");

		uumGroupRepository.save(uumGroup);

		Assert.assertEquals(new Long(1), uumGroup.getGroupId(), 20L);
	}

	// @Test
	public void updateUumGroup() {
		UumGroup uumGroup = new UumGroup();
		uumGroup.setGroupName("分组1");
		uumGroup.setGroupType("用户");
		uumGroup.setStatus("enable");
		uumGroup.setGroupId(3L);

		int row = uumGroupRepository.update(uumGroup);

		Assert.assertEquals(1, row);
	}

	// @Test
	public void updateUumGroups() {
		UumGroup uumGroup = new UumGroup();
		uumGroup.setGroupName("分组1");
		uumGroup.setGroupType("用户");
		uumGroup.setStatus("enable");
		uumGroup.setGroupId(3L);

		List<UumGroup> list = Lists.newArrayList();
		list.add(uumGroup);
		uumGroup = new UumGroup();
		uumGroup.setGroupName("分组4");
		uumGroup.setGroupType("用户");
		uumGroup.setStatus("enable");
		uumGroup.setGroupId(6L);
		list.add(uumGroup);

		int row = uumGroupRepository.update(list);

		Assert.assertEquals(1, row);
	}

	// @Test
	public void deleteUumGroup() {
		uumGroupRepository.delete(3L);
	}

	// @Test
	public void existsUumGroup() {
		boolean exists = uumGroupRepository.exists(34L);

		Assert.assertEquals(Boolean.TRUE, exists);
	}

	// @Test
	public void countUumGroup() {
		long count = uumGroupRepository.count();

		logger.debug(count);
	}

	// @Test
	public void getUumGroup() {
		UumGroup uumGroup = uumGroupRepository.get(2L);

		Assert.assertNotNull(uumGroup);
		Assert.assertEquals("技术", uumGroup.getGroupName());
	}

	@Test
	public void findAllUumGroup() {

		List<UumGroup> list = (List<UumGroup>) uumGroupRepository.findAll();

		logger.debug(list.get(0).getGroupName());
	}
}

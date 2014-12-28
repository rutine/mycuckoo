package com.mycuckoo.persistence.uum;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumUserAgent;
import com.mycuckoo.persistence.iface.uum.UumUserAgentRepository;

/**
 * 功能说明: UumUserAgentRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作. 默认在每个测试函数后进行回滚.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:33:31 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumUserAgentRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumUserAgentRepository uumUserAgentRepository;

	// @Test
	public void saveUumUserAgent() {
		UumUserAgent uumUserAgent = new UumUserAgent();
		uumUserAgent.setAgentUserId(8L);
		uumUserAgent.setBeginDate(Calendar.getInstance().getTime());
		uumUserAgent.setUserId(5L);
		uumUserAgent.setOrganId(8L);
		uumUserAgent.setReason("测试");

		uumUserAgentRepository.save(uumUserAgent);

		Assert.assertEquals(new Long(1), uumUserAgent.getAgentId(), 20L);
	}

	// @Test
	public void updateUumUserAgent() {
		UumUserAgent uumUserAgent = new UumUserAgent();
		uumUserAgent.setAgentUserId(8L);
		uumUserAgent.setBeginDate(Calendar.getInstance().getTime());
		uumUserAgent.setUserId(5L);
		uumUserAgent.setOrganId(8L);
		uumUserAgent.setReason("测试");
		uumUserAgent.setAgentId(4L);

		int row = uumUserAgentRepository.update(uumUserAgent);

		Assert.assertEquals(1, row);
	}

	// @Test
	public void updateUumUserAgents() {
		UumUserAgent uumUserAgent = new UumUserAgent();
		uumUserAgent.setAgentUserId(8L);
		uumUserAgent.setBeginDate(Calendar.getInstance().getTime());
		uumUserAgent.setUserId(5L);
		uumUserAgent.setOrganId(8L);
		uumUserAgent.setReason("测试");
		uumUserAgent.setAgentId(4L);

		List<UumUserAgent> list = Lists.newArrayList();

		uumUserAgent = new UumUserAgent();
		uumUserAgent.setAgentUserId(8L);
		uumUserAgent.setBeginDate(Calendar.getInstance().getTime());
		uumUserAgent.setUserId(5L);
		uumUserAgent.setOrganId(8L);
		uumUserAgent.setReason("测试");
		uumUserAgent.setAgentId(34L);
		list.add(uumUserAgent);

		int row = uumUserAgentRepository.update(list);

		Assert.assertEquals(1, row);
	}

	// @Test
	public void deleteUumUserAgent() {
		uumUserAgentRepository.delete(3L);
	}

	// @Test
	public void existsUumUserAgent() {
		boolean exists = uumUserAgentRepository.exists(34L);

		Assert.assertEquals(Boolean.TRUE, exists);
	}

	// @Test
	public void countUumUserAgent() {
		long count = uumUserAgentRepository.count();

		logger.debug(count);
	}

	// @Test
	public void getUumUserAgent() {
		UumUserAgent uumUserAgent = uumUserAgentRepository.get(2L);

		Assert.assertNotNull(uumUserAgent);
		Assert.assertEquals("技术", uumUserAgent.getReason());
	}

	// @Test
	public void findAllUumUserAgent() {

		List<UumUserAgent> list = (List<UumUserAgent>) uumUserAgentRepository.findAll();

		logger.debug(list.get(0).getAgentUserId());
	}

	// @Test
	public void findUumUserAgentsByUserId() {
		Page<UumUserAgent> page = uumUserAgentRepository.findUserAgentsByCon(0L, new PageRequest(0, 10));

		logger.debug(page.getContent());
	}

	// @Test
	public void countByUserIdAAgentUserId() {
		logger.debug(uumUserAgentRepository.countUserAgentsByUserIdAAgentUserId(0L, 2L));
	}

	// @Test
	public void findUserAgentsByAgentUserId() {
		logger.debug(uumUserAgentRepository.findUserAgentsByAgentUserId(2L));
	}

	// @Test
	public void findUserAgentsByUserId() {
		logger.debug(uumUserAgentRepository.findUserAgentsByUserId(0L));
	}

	@Test
	public void findUserAgentsByOwnerId() {
		logger.debug(uumUserAgentRepository.findUserAgentsByOwnerId(new Long[] { 0L, 1L, 2L, 3L, 7L }));
	}
}

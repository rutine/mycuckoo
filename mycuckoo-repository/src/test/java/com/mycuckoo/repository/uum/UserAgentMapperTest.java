package com.mycuckoo.repository.uum;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.uum.UserAgent;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class UserAgentMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(UserAgentMapperTest.class);
	
	@Autowired
	private UserAgentMapper mapper;
	

	@Test
	public void testCountByUserIdAndAgentUserId() {
		long count = mapper.countByUserIdAndAgentUserId(0L, 2L);

		logger.info("------> countByUserIdAndAgentUserId: {}", count);
	}

	@Test
	public void testFindByAgentUserId() {
		List<UserAgent> list = mapper.findByAgentUserId(2L, Calendar.getInstance().getTime());
		
		for(UserAgent entity : list) {
			logger.info("------> findByAgentUserId: {}", entity);
		}
	}

	@Test
	public void testFindByOwnerId() {
		List<UserAgent> list = mapper.findByOwnerId(new Long[] { 0L, 1L, 2L, 3L, 7L });
		
		for(UserAgent entity : list) {
			logger.info("------> findByOwnerId: {}", entity);
		}
	}

	@Test
	public void testSave() {
		UserAgent userAgent = new UserAgent();
		userAgent.setAgentUserId(8L);
		userAgent.setBeginDate(Calendar.getInstance().getTime());
		userAgent.setUserId(5L);
		userAgent.setOrganId(8L);
		userAgent.setReason("测试");

		mapper.save(userAgent);

		Assert.assertEquals(new Long(1), userAgent.getAgentId(), 20L);
	}

	@Test
	public void testUpdate() {
		UserAgent userAgent = new UserAgent();
		userAgent.setAgentUserId(8L);
		userAgent.setBeginDate(Calendar.getInstance().getTime());
		userAgent.setUserId(5L);
		userAgent.setOrganId(8L);
		userAgent.setReason("测试");
		userAgent.setAgentId(4L);

		int row = mapper.update(userAgent);

		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		UserAgent userAgent = mapper.get(2L);

		Assert.assertNotNull(userAgent);
		Assert.assertEquals("技术", userAgent.getReason());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(34L);

		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Page<UserAgent> page = mapper.findByPage(Collections.singletonMap("userId", 0L), new PageRequest(0, 10));

		for(UserAgent entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();

		logger.info("------> count: {}", count);
	}

}

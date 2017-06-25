package com.mycuckoo.repository.uum;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.uum.UserAgentPrivilege;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class UserAgentPrivilegeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static Logger logger = LoggerFactory.getLogger(UserAgentPrivilegeMapperTest.class);
	
	@Autowired
	private UserAgentPrivilegeMapper mapper;
	

	@Test
	public void testFindPrivilegeIdsByAgentId() {
		List<String> list = mapper.findPrivilegeIdsByAgentId(34L);
		
		logger.info("------> findPrivilegeIdsByAgentId: {}", list);
	}

	@Test
	public void testSave() {
		UserAgentPrivilege userAgentPrivilege = new UserAgentPrivilege();
		userAgentPrivilege.setPrivilegeType("rol");
		userAgentPrivilege.setPrivilegeId("3");
		
		mapper.save(userAgentPrivilege);
		
		Assert.assertEquals(new Long(1), userAgentPrivilege.getUserAgentPriId(), 20L);
	}

	@Test
	public void testUpdate() {
		UserAgentPrivilege userAgentPrivilege = new UserAgentPrivilege();
		userAgentPrivilege.setPrivilegeType("rol");
		userAgentPrivilege.setPrivilegeId("3");
		userAgentPrivilege.setUserAgentPriId(8L);
		
		int row = mapper.update(userAgentPrivilege);
		
		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		UserAgentPrivilege userAgentPrivilege = mapper.get(2L);
	
		Assert.assertNotNull(userAgentPrivilege);
		Assert.assertEquals("技术", userAgentPrivilege.getPrivilegeType());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Page<UserAgentPrivilege> page = mapper.findByPage(null, new PageRequest(0, 5));
		
		for(UserAgentPrivilege entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity.getUserAgent().getAgentId());
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();
		
		logger.info("------> count: {}", count);
	}

}

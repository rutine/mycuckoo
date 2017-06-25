package com.mycuckoo.repository.uum;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.uum.Portal;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class PortalMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(RoleMapperTest.class);
	
	@Autowired
	private PortalMapper portalMapper;

	@Test
	public void testSave() {
		Portal portal = new Portal();
		portal.setPortalUserid(1L);
		portal.setPortlet1("门户1");
		portal.setPortlet2("门户2");
		portal.setPortlet3("门户3");
		portal.setPortlet4("门户4");
		
		portalMapper.save(portal);
		
		Assert.assertEquals(new Long(1), portal.getPortalId(), 20L);
	}

	@Test
	public void testUpdate() {Portal portal = new Portal();
		portal.setPortalUserid(1L);
		portal.setPortlet1("门户1");
		portal.setPortlet2("门户2");
		portal.setPortlet3("门户3");
		portal.setPortlet4("门户4");
		portal.setPortalId(5L);
		
		int row = portalMapper.update(portal);
		
		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		portalMapper.delete(3L);
	}

	@Test
	public void testGet() {
		Portal portal = portalMapper.get(2L);
		
		Assert.assertNotNull(portal);
		Assert.assertEquals("技术", portal.getPortlet2());
	}

	@Test
	public void testExists() {
		boolean exists = portalMapper.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Page<Portal> page = portalMapper.findByPage(null, new PageRequest(0, 5));
		
		logger.info("------> page: {}", page);
	}

	@Test
	public void testCount() {
		long count = portalMapper.count();
		
		logger.info("------> count: {}", count);
	}

}

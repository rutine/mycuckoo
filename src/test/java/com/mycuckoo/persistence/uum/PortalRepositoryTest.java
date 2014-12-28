package com.mycuckoo.persistence.uum;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.uum.Portal;
import com.mycuckoo.persistence.iface.uum.PortalRepository;

/**
 * 功能说明: PortalRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:26:05 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class PortalRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private PortalRepository portalRepository;
	
	@Test
	public void savePortal() {
		Portal portal = new Portal();
		portal.setPortalUserid(1L);
		portal.setPortlet1("门户1");
		portal.setPortlet2("门户2");
		portal.setPortlet3("门户3");
		portal.setPortlet4("门户4");
		
		portalRepository.save(portal);
		
		Assert.assertEquals(new Long(1), portal.getPortalId(), 20L);
	}
	
//	@Test
	public void updatePortal() {
		Portal portal = new Portal();
		portal.setPortalUserid(1L);
		portal.setPortlet1("门户1");
		portal.setPortlet2("门户2");
		portal.setPortlet3("门户3");
		portal.setPortlet4("门户4");
		portal.setPortalId(5L);
		
		int row = portalRepository.update(portal);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updatePortals() {
		Portal portal = new Portal();
		portal.setPortalUserid(1L);
		portal.setPortlet1("门户1");
		portal.setPortlet2("门户2");
		portal.setPortlet3("门户3");
		portal.setPortlet4("门户4");
		portal.setPortalId(5L);
		
		List<Portal> list = Lists.newArrayList();
		list.add(portal);
		portal = new Portal();
		portal.setPortalUserid(1L);
		portal.setPortlet1("门户1");
		portal.setPortlet2("门户2");
		portal.setPortlet3("门户3");
		portal.setPortlet4("门户4");
		portal.setPortalId(3L);
		list.add(portal);
		
		int row = portalRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deletePortal() {
		portalRepository.delete(3L);
	}
	
//	@Test
	public void existsPortal() {
		boolean exists = portalRepository.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countPortal() {
		long count = portalRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getPortal() {
		Portal portal = portalRepository.get(2L);
		
		Assert.assertNotNull(portal);
		Assert.assertEquals("技术", portal.getPortlet2());
	}
	
//	@Test
	public void findAllPortal() {
		
		List<Portal> list = (List<Portal>)portalRepository.findAll();
		
		logger.debug(list.size());
	}
}

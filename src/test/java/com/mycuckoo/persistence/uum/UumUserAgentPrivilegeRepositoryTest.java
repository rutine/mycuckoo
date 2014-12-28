package com.mycuckoo.persistence.uum;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.uum.UumUserAgentPrivilege;
import com.mycuckoo.persistence.iface.uum.UumUserAgentPrivilegeRepository;

/**
 * 功能说明: UumUserAgentPrivilegeRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:32:29 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumUserAgentPrivilegeRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private UumUserAgentPrivilegeRepository uumUserAgentPrivilegeRepository;
	
//	@Test
	public void saveUumUserAgentPrivilege() {
		UumUserAgentPrivilege uumUserAgentPrivilege = new UumUserAgentPrivilege();
		uumUserAgentPrivilege.setPrivilegeType("rol");
		uumUserAgentPrivilege.setPrivilegeId("3");
		
		uumUserAgentPrivilegeRepository.save(uumUserAgentPrivilege);
		
		Assert.assertEquals(new Long(1), uumUserAgentPrivilege.getUserAgentPriId(), 20L);
	}
	
//	@Test
	public void updateUumUserAgentPrivilege() {
		UumUserAgentPrivilege uumUserAgentPrivilege = new UumUserAgentPrivilege();
		uumUserAgentPrivilege.setPrivilegeType("rol");
		uumUserAgentPrivilege.setPrivilegeId("3");
		uumUserAgentPrivilege.setUserAgentPriId(8L);
		
		int row = uumUserAgentPrivilegeRepository.update(uumUserAgentPrivilege);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateUumUserAgentPrivileges() {
		UumUserAgentPrivilege uumUserAgentPrivilege = new UumUserAgentPrivilege();
		uumUserAgentPrivilege.setPrivilegeType("rol");
		uumUserAgentPrivilege.setPrivilegeId("3");
		uumUserAgentPrivilege.setUserAgentPriId(8L);
		
		List<UumUserAgentPrivilege> list = Lists.newArrayList();
		list.add(uumUserAgentPrivilege);
		uumUserAgentPrivilege = new UumUserAgentPrivilege();
		uumUserAgentPrivilege.setPrivilegeType("rol");
		uumUserAgentPrivilege.setPrivilegeId("3");
		uumUserAgentPrivilege.setUserAgentPriId(12L);
		list.add(uumUserAgentPrivilege);
		
		int row = uumUserAgentPrivilegeRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteUumUserAgentPrivilege() {
		uumUserAgentPrivilegeRepository.delete(3L);
	}
	
//	@Test
	public void existsUumUserAgentPrivilege() {
		boolean exists = uumUserAgentPrivilegeRepository.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countUumUserAgentPrivilege() {
		long count = uumUserAgentPrivilegeRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getUumUserAgentPrivilege() {
		UumUserAgentPrivilege uumUserAgentPrivilege = uumUserAgentPrivilegeRepository.get(2L);
		
		Assert.assertNotNull(uumUserAgentPrivilege);
		Assert.assertEquals("技术", uumUserAgentPrivilege.getPrivilegeType());
	}
	
//	@Test
	public void findAllUumUserAgentPrivilege() {
	
		List<UumUserAgentPrivilege> list = (List<UumUserAgentPrivilege>)uumUserAgentPrivilegeRepository.findAll();
		
		for(UumUserAgentPrivilege userAgentPrivilege : list) {
			logger.debug(userAgentPrivilege.getUumUserAgent().getAgentId());
		}
	}
	
	@Test
	public void findPrivilegeIdsByAgentId() {
		logger.debug(uumUserAgentPrivilegeRepository.findPrivilegeIdsByAgentId(34L));
	}
}

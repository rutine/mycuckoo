package com.mycuckoo.persistence.uum;

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
import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumOrgan;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.persistence.iface.uum.UumOrgRoleRefRepository;

/**
 * 功能说明: UumOrgRoleRefRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:27:21 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumOrgRoleRefRepositoryTest extends
	AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private UumOrgRoleRefRepository uumOrgRoleRefRepository;
	
//	@Test
	public void saveOrgRoleRef() {
		UumOrgRoleRef orgRoleRef = new UumOrgRoleRef();
		orgRoleRef.setUumOrgan(new UumOrgan(3L, "enable"));
		orgRoleRef.setUumRole(new UumRole(3L, "enable"));
		
		uumOrgRoleRefRepository.save(orgRoleRef);
	
	Assert.assertEquals(new Long(5), orgRoleRef.getOrgRoleId(), 20L);
	}
	
//	@Test
	public void updateOrgRoleRef() {
		UumOrgRoleRef orgRoleRef = new UumOrgRoleRef();
		orgRoleRef.setUumOrgan(new UumOrgan(3L, "enable"));
		orgRoleRef.setUumRole(new UumRole(3L, "enable"));
		orgRoleRef.setOrgRoleId(55L);
		
		int row = uumOrgRoleRefRepository.update(orgRoleRef);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateOrgRoleRefs() {
		UumOrgRoleRef orgRoleRef = new UumOrgRoleRef();
		orgRoleRef.setUumOrgan(new UumOrgan(3L, "enable"));
		orgRoleRef.setUumRole(new UumRole(3L, "enable"));
		orgRoleRef.setOrgRoleId(3L);
		
		List<UumOrgRoleRef> list = Lists.newArrayList();
		list.add(orgRoleRef);
		orgRoleRef = new UumOrgRoleRef();
		orgRoleRef.setUumOrgan(new UumOrgan(3L, "enable"));
		orgRoleRef.setUumRole(new UumRole(5L, "enable"));
		orgRoleRef.setOrgRoleId(4L);
		list.add(orgRoleRef);
		
		int row = uumOrgRoleRefRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteOrgRoleRef() {
		uumOrgRoleRefRepository.delete(3L);
	}
	
//	@Test
	public void existsOrgRoleRef() {
		boolean exists = uumOrgRoleRefRepository.exists(55L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countOrgRoleRef() {
		long count = uumOrgRoleRefRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getOrgRoleRef() {
		UumOrgRoleRef orgRoleRef = uumOrgRoleRefRepository.get(45L);
		
		Assert.assertNotNull(orgRoleRef);
		logger.debug(orgRoleRef.getUumOrgan());
		logger.debug(orgRoleRef.getUumRole());
	}
	
//	@Test
	public void findAllOrgRoleRef() {
	
		List<UumOrgRoleRef> list = (List<UumOrgRoleRef>)uumOrgRoleRefRepository.findAll();
		
		for(UumOrgRoleRef orgRoleRef : list) {
			logger.debug(orgRoleRef.getOrgRoleId());
		}
	}
	
//	@Test
	public void findOrgRoleRefByOrgID() {
		List<UumOrgRoleRef> list = (List<UumOrgRoleRef>)uumOrgRoleRefRepository.findRolesByOrgId(1L);
		
		Assert.assertTrue(list.size() > 0);
		
		logger.debug(list.get(0).getUumRole());
	}
	
//	@Test
	public void findOrgRoleRefByRoleID() {
		List<UumOrgRoleRef> list = (List<UumOrgRoleRef>)uumOrgRoleRefRepository.findOrgansByRoleId(1L);
		
		Assert.assertTrue(list.size() > 0);
		
		for(UumOrgRoleRef orgRoleRef : list) {
			logger.debug(orgRoleRef.getOrgRoleId());
		}
	}
	
//	@Test
	public void findOrgRoleRefByOrgRoleId() {
		UumOrgRoleRef orgRoleRef = this.uumOrgRoleRefRepository.getOrgRoleRefByOrgRoleId(2l, 13l);
		
		logger.debug(orgRoleRef.getUumRole());
	}
	
	@Test
	public void findOrgRoleRefsByOrgId() {
		Page<UumOrgRoleRef> page = this.uumOrgRoleRefRepository.findRolesByCon(1l, null, new PageRequest(0, 20));
		
		for(UumOrgRoleRef orgRoleRef : page.getContent()) {
			logger.debug(orgRoleRef.getUumRole());
		}
		logger.debug(page.getTotalElements());
	}
	
//	@Test
	public void deleteOrgRoleRefsByRoleId() {
		this.uumOrgRoleRefRepository.deleteOrgRoleRefsByRoleId(3l);
	}
	
//	@Test
	public void findOrgRoleRefsByOrgRoleIds() {
		List<UumOrgRoleRef> list = this.uumOrgRoleRefRepository
				.findOrgRoleRefsByOrgRoleIds(new Long[] {1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l});
		
		for(UumOrgRoleRef orgRoleRef : list) {
			logger.debug(orgRoleRef.getUumRole());
		}
	}
	
//	@Test
	public void findOrgRoleIdsByRoleId() {
		List<Long> list = this.uumOrgRoleRefRepository.findOrgRoleIdsByRoleId(1l);
		
		logger.debug(list);
	}
}

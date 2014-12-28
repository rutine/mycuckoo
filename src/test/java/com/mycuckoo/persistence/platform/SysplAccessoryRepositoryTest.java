package com.mycuckoo.persistence.platform;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.platform.SysplAccessory;
import com.mycuckoo.persistence.iface.platform.SysplAccessoryRepository;

/**
 * 功能说明: SysplAccessoryRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:05:52 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplAccessoryRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private SysplAccessoryRepository sysplAccessoryRepository;
	
//	@Test
	public void saveSysplAccessory() {
		SysplAccessory accessory = new SysplAccessory();
		accessory.setAccessoryName("测试附件");
		accessory.setInfoId(4L);
		
		sysplAccessoryRepository.save(accessory);
		
		Assert.assertEquals(new Long(1), accessory.getAccessoryId(), 20L);
	}
	
//	@Test
	public void updateSysplAccessory() {
		SysplAccessory accessory = new SysplAccessory();
		accessory.setAccessoryName("测试附件");
		accessory.setInfoId(4L);
		accessory.setAccessoryId(3L);
		
		int row = sysplAccessoryRepository.update(accessory);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplAccessorys() {
		SysplAccessory accessory = new SysplAccessory();
		accessory.setAccessoryName("测试附件");
		accessory.setInfoId(4L);
		accessory.setAccessoryId(3L);
		
		List<SysplAccessory> list = Lists.newArrayList();
		list.add(accessory);
		accessory = new SysplAccessory();
		accessory.setAccessoryName("测试附件");
		accessory.setInfoId(4L);
		accessory.setAccessoryId(7L);
		list.add(accessory);
		
		int row = sysplAccessoryRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplAccessory() {
		sysplAccessoryRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplAccessory() {
		boolean exists = sysplAccessoryRepository.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplAccessory() {
		long count = sysplAccessoryRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplAccessory() {
		SysplAccessory accessory = sysplAccessoryRepository.get(2L);
		
		Assert.assertNotNull(accessory);
		Assert.assertEquals("技术", accessory.getAccessoryName());
	}
	
//	@Test
	public void findAllSysplAccessory() {
		
		List<SysplAccessory> list = (List<SysplAccessory>)sysplAccessoryRepository.findAll();
		
		logger.debug(list.size());
	}
	
//	@Test
	public void deleteAccessoryByIds() {
		this.sysplAccessoryRepository.deleteAccessorysByIds(new Long[]{0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L});
	}
	
	@Test
	public void findAccessorysByAfficheId() {
		List<SysplAccessory> list = (List<SysplAccessory>)sysplAccessoryRepository.findAccessorysByAfficheId(3L);
		
		for(SysplAccessory accessory : list) {
			logger.debug(accessory.getAccessoryName());
		}
	}
}

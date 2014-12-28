package com.mycuckoo.persistence.platform;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.platform.SysplDicSmallType;
import com.mycuckoo.persistence.iface.platform.SysplDicSmallTypeRepository;

/**
 * 功能说明: SysplDicSmallTypeRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:13:11 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplDicSmallTypeRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SysplDicSmallTypeRepository sysplDicSmallTypeRepository;
	
//	@Test
	public void saveSysplDicSmallType() {
		SysplDicSmallType dicSmallType = new SysplDicSmallType();
		dicSmallType.setSmallTypeCode("小类型code");
		dicSmallType.setSmallTypeName("字典小类型");
		
		sysplDicSmallTypeRepository.save(dicSmallType);
		
		Assert.assertEquals(new Long(1), dicSmallType.getSmallTypeId(), 20L);
	}
	
//	@Test
	public void updateSysplDicSmallType() {
		SysplDicSmallType dicSmallType = new SysplDicSmallType();
		dicSmallType.setSmallTypeCode("小类型code");
		dicSmallType.setSmallTypeName("字典小类型");
		dicSmallType.setSmallTypeId(8L);
		
		int row = sysplDicSmallTypeRepository.update(dicSmallType);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplDicSmallTypes() {
		SysplDicSmallType dicSmallType = new SysplDicSmallType();
		dicSmallType.setSmallTypeCode("小类型code");
		dicSmallType.setSmallTypeName("字典小类型");
		dicSmallType.setSmallTypeId(8L);
		
		List<SysplDicSmallType> list = Lists.newArrayList();
		list.add(dicSmallType);
		dicSmallType = new SysplDicSmallType();
		dicSmallType.setSmallTypeCode("小类型code");
		dicSmallType.setSmallTypeName("字典小类型");
		dicSmallType.setSmallTypeId(45L);
		list.add(dicSmallType);
		
		int row = sysplDicSmallTypeRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplDicSmallType() {
		sysplDicSmallTypeRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplDicSmallType() {
		boolean exists = sysplDicSmallTypeRepository.exists(45L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplDicSmallType() {
		long count = sysplDicSmallTypeRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplDicSmallType() {
		SysplDicSmallType dicSmallType = sysplDicSmallTypeRepository.get(45L);
		
		Assert.assertNotNull(dicSmallType);
		Assert.assertEquals("技术", dicSmallType.getSmallTypeName());
	}
	
//	@Test
	public void findAllSysplDicSmallType() {
	
		List<SysplDicSmallType> list = (List<SysplDicSmallType>)sysplDicSmallTypeRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getSmallTypeId());
	}
	
//	@Test
	public void deleteDicSmallTypeByBigTypeId() {
		sysplDicSmallTypeRepository.deleteDicSmallTypesByBigTypeId(25l);
	}
	
//  @Test
	public void findAllByBigTypeId() {
		List<SysplDicSmallType> list = (List<SysplDicSmallType>)sysplDicSmallTypeRepository.findDicSmallTypesByBigTypeId(25l);
		
		for(SysplDicSmallType smallType : list) {
			logger.debug(smallType.getSmallTypeName());
		}
	}
	
	@Test
	public void findDicSmallTypeByBigTypeCode() {
		List<SysplDicSmallType> list = (List<SysplDicSmallType>)sysplDicSmallTypeRepository.findDicSmallTypesByBigTypeCode("modPageType");
	
		for(SysplDicSmallType smallType : list) {
			logger.debug(smallType.getSmallTypeName());
		}
	}
	
}

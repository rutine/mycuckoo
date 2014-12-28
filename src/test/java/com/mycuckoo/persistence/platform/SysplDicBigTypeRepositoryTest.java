package com.mycuckoo.persistence.platform;

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
import com.mycuckoo.domain.platform.SysplDicBigType;
import com.mycuckoo.persistence.iface.platform.SysplDicBigTypeRepository;

/**
 * 功能说明: SysplDicBigTypeRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:11:55 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplDicBigTypeRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SysplDicBigTypeRepository sysplDicBigTypeRepository;
	
//	@Test
	public void saveSysplDicBigType() {
		SysplDicBigType dicBigType = new SysplDicBigType();
		dicBigType.setBigTypeCode("bigTypeCode");
		dicBigType.setBigTypeName("大字典类型");
		dicBigType.setCreateDate(Calendar.getInstance().getTime());
		dicBigType.setCreator("rutine");
		dicBigType.setStatus("enable");
		
		sysplDicBigTypeRepository.save(dicBigType);
		
		Assert.assertEquals(new Long(1), dicBigType.getBigTypeId(), 20L);
	}
	
//	@Test
	public void updateSysplDicBigType() {
		SysplDicBigType dicBigType = new SysplDicBigType();
		dicBigType.setBigTypeCode("bigTypeCode");
		dicBigType.setBigTypeName("大字典类型");
		dicBigType.setCreateDate(Calendar.getInstance().getTime());
		dicBigType.setCreator("rutine");
		dicBigType.setStatus("enable");
		dicBigType.setBigTypeId(5L);
		
		int row = sysplDicBigTypeRepository.update(dicBigType);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplDicBigTypes() {
		SysplDicBigType dicBigType = new SysplDicBigType();
		dicBigType.setBigTypeCode("bigTypeCode");
		dicBigType.setBigTypeName("大字典类型");
		dicBigType.setCreateDate(Calendar.getInstance().getTime());
		dicBigType.setCreator("rutine");
		dicBigType.setStatus("enable");
		dicBigType.setBigTypeId(5L);
		
		List<SysplDicBigType> list = Lists.newArrayList();
		list.add(dicBigType);
		dicBigType = new SysplDicBigType();
		dicBigType.setBigTypeCode("bigTypeCode");
		dicBigType.setBigTypeName("大字典类型");
		dicBigType.setCreateDate(Calendar.getInstance().getTime());
		dicBigType.setCreator("rutine");
		dicBigType.setStatus("enable");
		dicBigType.setBigTypeId(15L);
		list.add(dicBigType);
		
		int row = sysplDicBigTypeRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplDicBigType() {
		sysplDicBigTypeRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplDicBigType() {
		boolean exists = sysplDicBigTypeRepository.exists(25L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplDicBigType() {
		long count = sysplDicBigTypeRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplDicBigType() {
		SysplDicBigType dicBigType = sysplDicBigTypeRepository.get(25L);
		
		Assert.assertNotNull(dicBigType);
		Assert.assertEquals("技术", dicBigType.getBigTypeCode());
	}
	
//	@Test
	public void findAllSysplDicBigType() {
		
		List<SysplDicBigType> list = (List<SysplDicBigType>)sysplDicBigTypeRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getBigTypeId());
	}
	
//	@Test
	public void countDicBigTypeByBigTypeCode() {
		logger.debug(sysplDicBigTypeRepository.countDicBigTypesByBigTypeCode("modPageType"));
	}
	
//	@Test
	public void findDicBigTypeByConditions() {
		Page<SysplDicBigType> page = sysplDicBigTypeRepository.findDicBigTypesByCon("", "mod", new PageRequest(0, 10));
		
		for(SysplDicBigType bigType : page.getContent()) {
			logger.debug(bigType.getBigTypeName());
		}
	}
	
	@Test
	public void modifyDicBigTypeStatus() {
		sysplDicBigTypeRepository.updateDicBigTypeStatus(1l, "enable");
	}
}

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
import com.mycuckoo.domain.platform.SysplDistrict;
import com.mycuckoo.persistence.iface.platform.SysplDistrictRepository;

/**
 * 功能说明: SysplDistrictRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:15:07 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplDistrictRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SysplDistrictRepository sysplDistrictRepository;
	
//	@Test
	public void saveSysplDistrict() {
		SysplDistrict district = new SysplDistrict();
		district.setCreateDate(Calendar.getInstance().getTime());
		district.setCreator("rutine");
		district.setDistrictCode("020");
		district.setDistrictLevel("1");
		district.setDistrictName("广州");
		district.setDistrictPostal("7333");
		district.setDistrictTelcode("020");
		district.setMemo("测试");
		district.setStatus("enable");
		
		sysplDistrictRepository.save(district);
		
		Assert.assertEquals(new Long(1), district.getDistrictId(), 20L);
	}
	
//	@Test
	public void updateSysplDistrict() {
		SysplDistrict district = new SysplDistrict();
		district.setCreateDate(Calendar.getInstance().getTime());
		district.setCreator("rutine");
		district.setDistrictCode("020");
		district.setDistrictLevel("1");
		district.setDistrictName("广州");
		district.setDistrictPostal("7333");
		district.setDistrictTelcode("020");
		district.setMemo("测试");
		district.setStatus("enable");
		district.setDistrictId(2001L);
		
		int row = sysplDistrictRepository.update(district);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplDistricts() {
		SysplDistrict district = new SysplDistrict();
		district.setCreateDate(Calendar.getInstance().getTime());
		district.setCreator("rutine");
		district.setDistrictCode("020");
		district.setDistrictLevel("1");
		district.setDistrictName("广州");
		district.setDistrictPostal("7333");
		district.setDistrictTelcode("020");
		district.setMemo("测试");
		district.setStatus("enable");
		district.setDistrictId(8L);
		
		List<SysplDistrict> list = Lists.newArrayList();
		list.add(district);
		district = new SysplDistrict();
		district.setCreateDate(Calendar.getInstance().getTime());
		district.setCreator("rutine");
		district.setDistrictCode("020");
		district.setDistrictLevel("1");
		district.setDistrictName("广州");
		district.setDistrictPostal("7333");
		district.setDistrictTelcode("020");
		district.setMemo("测试");
		district.setStatus("enable");
		district.setDistrictId(2003L);
		list.add(district);
		
		int row = sysplDistrictRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplDistrict() {
		sysplDistrictRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplDistrict() {
		boolean exists = sysplDistrictRepository.exists(2003L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplDistrict() {
		long count = sysplDistrictRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplDistrict() {
		SysplDistrict district = sysplDistrictRepository.get(2005L);
		
		Assert.assertNotNull(district);
		Assert.assertEquals("技术", district.getDistrictName());
	}
	
//	@Test
	public void findAllSysplDistrict() {
	
		List<SysplDistrict> list = (List<SysplDistrict>)sysplDistrictRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(1).getDistrictId());
	}
	
//	@Test
	public void countChildDistrictsById() {
		logger.debug(this.sysplDistrictRepository.countDistrictsByUpDistrictId(8l));
	}
	
//	@Test
	public void countDistrictsByDistrictName() {
		logger.debug(sysplDistrictRepository.countDistrictsByDistrictName("广州"));
	}
	
//	@Test
	public void findAllDisttrictByParentID() {
		List<SysplDistrict> list = (List<SysplDistrict>)sysplDistrictRepository.findDistrictsByUpDistrictId(0L);
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getDistrictName());
	}
	
//	@Test
	public void findChildDistricts() {
		List<SysplDistrict> list = (List<SysplDistrict>)sysplDistrictRepository.findDistrictsByUpDistrictsAFilterIds(7l, 9);
		
		logger.debug(list.size());
	}
	
	@Test
	public void findDistrictsByConditions() {
		Page<SysplDistrict> page = this.sysplDistrictRepository.findDistrictsByCon(new Long[]{0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l}, null, null, new PageRequest(0, 10));
		
		logger.debug(page.getContent().get(0).getDistrictName());
	}
	
}

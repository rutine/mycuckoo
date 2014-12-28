package com.mycuckoo.persistence.platform;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.persistence.iface.platform.SysplModOptRefRepository;

/**
 * 功能说明: SysplModOptRefRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:16:14 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplModOptRefRepositoryTest extends
	AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SysplModOptRefRepository sysplModOptRefRepository;
	
//	@Test
	public void saveSysplModOptRef() {
		SysplModOptRef modOptRef = new SysplModOptRef();
		modOptRef.setSysplModuleMemu(new SysplModuleMemu(8L));
		modOptRef.setSysplOperate(new SysplOperate(2L, "enable"));
		
		sysplModOptRefRepository.save(modOptRef);
		
		Assert.assertEquals(new Long(1), modOptRef.getModOptId(), 20L);
	}
	
//	@Test
	public void updateSysplModOptRef() {
		SysplModOptRef modOptRef = new SysplModOptRef();
		modOptRef.setSysplModuleMemu(new SysplModuleMemu(8L));
		modOptRef.setSysplOperate(new SysplOperate(2L, "enable"));
		modOptRef.setModOptId(3L);
		
		int row = sysplModOptRefRepository.update(modOptRef);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplModOptRefs() {
		SysplModOptRef modOptRef = new SysplModOptRef();
		modOptRef.setSysplModuleMemu(new SysplModuleMemu(8L));
		modOptRef.setSysplOperate(new SysplOperate(2L, "enable"));
		modOptRef.setModOptId(3L);
		
		List<SysplModOptRef> list = Lists.newArrayList();
		list.add(modOptRef);
		modOptRef = new SysplModOptRef();
		modOptRef.setSysplModuleMemu(new SysplModuleMemu(8L));
		modOptRef.setSysplOperate(new SysplOperate(2L, "enable"));
		modOptRef.setModOptId(2L);
		list.add(modOptRef);
		
		int row = sysplModOptRefRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplModOptRef() {
		sysplModOptRefRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplModOptRef() {
		boolean exists = sysplModOptRefRepository.exists(3L);
	
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplModOptRef() {
		long count = sysplModOptRefRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplModOptRef() {
		SysplModOptRef modOptRef = sysplModOptRefRepository.get(25L);
		
		Assert.assertNotNull(modOptRef);
		Assert.assertEquals("技术", modOptRef.getSysplModuleMemu().getModImgCls());
	}
	
//	@Test
	public void findAllSysplModOptRef() {
	
		List<SysplModOptRef> list = (List<SysplModOptRef>)sysplModOptRefRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getModOptId());
	}
	
//	@Test
	public void findAllByOperateID() {
		List<SysplModOptRef> list = (List<SysplModOptRef>)sysplModOptRefRepository.findModOptRefsByOperateId(10L);
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getSysplOperate().getOperateId());
	}
	
//	@Test
	public void findAllByModuleID() {
		List<SysplModOptRef> list = (List<SysplModOptRef>)sysplModOptRefRepository.findModOptRefsByModuleId(10L);
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getSysplModuleMemu().getModuleId());
	}
	
	@Test
	public void findSysplModOptRefByIds() {
		List<SysplModOptRef> list = (List<SysplModOptRef>)sysplModOptRefRepository.findModOptRefsByIds(new Long[]{0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l});
		
		logger.debug(list.size());
	}
}

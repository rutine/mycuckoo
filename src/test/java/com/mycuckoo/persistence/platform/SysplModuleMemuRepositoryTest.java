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
import com.mycuckoo.domain.platform.SysplModOptRef;
import com.mycuckoo.domain.platform.SysplModuleMemu;
import com.mycuckoo.persistence.iface.platform.SysplModuleMemuRepository;

/**
 * 功能说明: SysplModuleMemuRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:17:17 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplModuleMemuRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SysplModuleMemuRepository sysplModuleMemuRepository;
	
//	@Test
	public void saveSysplModuleMemu() {
		SysplModuleMemu moduleMemu = new SysplModuleMemu();
		moduleMemu.setBelongToSys("用户");
		moduleMemu.setCreateDate(Calendar.getInstance().getTime());
		moduleMemu.setCreator("rutine");
		moduleMemu.setIsLeaf(true);
		moduleMemu.setMemo("测试");
		moduleMemu.setModEnId("en10001");
		moduleMemu.setModImgCls("no-resource");
		moduleMemu.setModLevel("3");
		moduleMemu.setModName("测试模块");
		moduleMemu.setModOrder(6);
		moduleMemu.setModPageType("jsp");
		moduleMemu.setStatus("enable");
	
		sysplModuleMemuRepository.save(moduleMemu);
		
		Assert.assertEquals(new Long(1), moduleMemu.getModuleId(), 20L);
	}
	
//	@Test
	public void updateSysplModuleMemu() {
		SysplModuleMemu moduleMemu = new SysplModuleMemu();
		moduleMemu.setBelongToSys("用户");
		moduleMemu.setCreateDate(Calendar.getInstance().getTime());
		moduleMemu.setCreator("rutine");
		moduleMemu.setIsLeaf(true);
		moduleMemu.setMemo("测试");
		moduleMemu.setModEnId("en10001");
		moduleMemu.setModImgCls("no-resource");
		moduleMemu.setModLevel("3");
		moduleMemu.setModName("测试模块");
		moduleMemu.setModOrder(6);
		moduleMemu.setModPageType("jsp");
		moduleMemu.setStatus("enable");
		moduleMemu.setModuleId(25L);
		
		int row = sysplModuleMemuRepository.update(moduleMemu);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplModuleMemus() {
		SysplModuleMemu moduleMemu = new SysplModuleMemu();
		moduleMemu.setBelongToSys("用户");
		moduleMemu.setCreateDate(Calendar.getInstance().getTime());
		moduleMemu.setCreator("rutine");
		moduleMemu.setIsLeaf(true);
		moduleMemu.setMemo("测试");
		moduleMemu.setModEnId("en10001");
		moduleMemu.setModImgCls("no-resource");
		moduleMemu.setModLevel("3");
		moduleMemu.setModName("测试模块");
		moduleMemu.setModOrder(6);
		moduleMemu.setModPageType("jsp");
		moduleMemu.setStatus("enable");
		moduleMemu.setModuleId(5L);
		
		List<SysplModuleMemu> list = Lists.newArrayList();
		list.add(moduleMemu);
		moduleMemu = new SysplModuleMemu();
		moduleMemu.setBelongToSys("用户");
		moduleMemu.setCreateDate(Calendar.getInstance().getTime());
		moduleMemu.setCreator("rutine");
		moduleMemu.setIsLeaf(true);
		moduleMemu.setMemo("测试");
		moduleMemu.setModEnId("en10001");
		moduleMemu.setModImgCls("no-resource");
		moduleMemu.setModLevel("3");
		moduleMemu.setModName("测试模块");
		moduleMemu.setModOrder(6);
		moduleMemu.setModPageType("jsp");
		moduleMemu.setStatus("enable");
		moduleMemu.setModuleId(15L);
		list.add(moduleMemu);
		
		int row = sysplModuleMemuRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplModuleMemu() {
		sysplModuleMemuRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplModuleMemu() {
		boolean exists = sysplModuleMemuRepository.exists(3L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplModuleMemu() {
		long count = sysplModuleMemuRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplModuleMemu() {
		SysplModuleMemu moduleMemu = sysplModuleMemuRepository.get(25L);
		
		Assert.assertNotNull(moduleMemu);
		Assert.assertEquals("技术",moduleMemu.getModName());
	}
	
//	@Test
	public void findAllSysplModuleMemu() {
	
		List<SysplModuleMemu> list = (List<SysplModuleMemu>)sysplModuleMemuRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getModName());
	}
	
//	@Test
	public void countChildModulesById() {
		logger.debug(sysplModuleMemuRepository.countModulesByUpModId(2l));
	}
	
//	@Test
	public void countModulesByModuleName() {
		logger.debug(sysplModuleMemuRepository.countModulesByModuleName("系统配置管理"));
	}
	
//	@Test
	public void deleteModOptRefByModId() {
		sysplModuleMemuRepository.deleteModOptRefByModuleId(3);
	}
	
//	@Test
	public void deleteModOptRefByOperateId() {
		sysplModuleMemuRepository.deleteModOptRefByOperateId(3);
	}
	
	@Test
	public void findAllModOptRef() {
		List<SysplModOptRef> list = sysplModuleMemuRepository.findAllModOptRefs();
		logger.debug(list.size());
		logger.debug(list.get(0).getSysplModuleMemu().getSysplModuleMemu().getModName());
	}
	
//	@Test
	public void findAssignedModOptByModId() {
		List<SysplModOptRef> list = sysplModuleMemuRepository.findAssignedModOptRefsByModuleId(14);
		
		logger.debug(list.get(0).getSysplOperate().getOperateId());
	}
	
//	@Test
	public void findChildModules() {
		List<SysplModuleMemu> list = sysplModuleMemuRepository.findModulesByUpModId(6, 3);

		logger.debug(list.get(0).getModName());
	}
	
//	@Test
	public void findModulesByConditions() {
		Page<SysplModuleMemu> page = sysplModuleMemuRepository.findModulesByCon(new Long[]{0l, 11l, 12l, 13l, 14l, 15l, 16l, 17l, 18l, 19l, 20l}, "管理", "Group", new PageRequest(0, 10));
		
		for(SysplModuleMemu mod : page.getContent()) {
			logger.debug(mod.getModEnId());
		}
	}
}

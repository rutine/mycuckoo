package com.mycuckoo.repository.platform;

import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.ModuleMemu;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class ModuleMemuMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(ModuleMemuMapperTest.class);

	@Autowired
	private ModuleMemuMapper mapper;
	
	
	@Test
	public void testFindAll() {
		Page<ModuleMemu> page = mapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE));
		
		for(ModuleMemu entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCountByUpModuleId() {
		long count = mapper.countByUpModuleId(2L);
		
		logger.info("------> countByUpModuleId: {}", count);
	}

	@Test
	public void testCountByModuleName() {
		long count = mapper.countByModuleName("系统配置管理");
		
		logger.info("------> countByModuleName: {}", count);
	}

	@Test
	public void testFindByUpModuleIdAndFilterModuleIds() {
		List<ModuleMemu> list = mapper.findByUpModuleIdAndFilterModuleIds(6, new long[]{0L, 3L});

		for(ModuleMemu entity : list) {
			logger.info("------> findByUpModuleIdAndFilterModuleIds: {}", entity);
		}
	}

	@Test
	public void testUpdateRowPrivilege() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		ModuleMemu moduleMemu = new ModuleMemu();
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
	
		mapper.save(moduleMemu);
		
		Assert.assertEquals(new Long(1), moduleMemu.getModuleId(), 20L);
	}

	@Test
	public void testUpdate() {
		ModuleMemu moduleMemu = new ModuleMemu();
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
		
		int row = mapper.update(moduleMemu);
		
		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		ModuleMemu moduleMemu = mapper.get(25L);
		
		Assert.assertNotNull(moduleMemu);
		Assert.assertEquals("技术",moduleMemu.getModName());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(3L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("array", new Long[]{0l, 11l, 12l, 13l, 14l, 15l, 16l, 17l, 18l, 19l, 20l});
		params.put("modName", "%管理%");
		params.put("modEnId", "%Group%");
		Page<ModuleMemu> page = mapper.findByPage(params, new PageRequest(0, 10));
		
		for(ModuleMemu entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();
		
		logger.info("------> count: {}", count);
	}

}

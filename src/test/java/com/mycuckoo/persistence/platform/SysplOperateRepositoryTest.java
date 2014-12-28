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
import com.mycuckoo.domain.platform.SysplOperate;
import com.mycuckoo.persistence.iface.platform.SysplOperateRepository;

/**
 * 功能说明: SysplOperateRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:18:55 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplOperateRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SysplOperateRepository sysplOperateRepository;
	
//	@Test
	public void saveSysplOperate() {
		SysplOperate operate = new SysplOperate();
		operate.setCreateDate(Calendar.getInstance().getTime());
		operate.setCreator("rutine");
		operate.setMemo("测试");
		operate.setOperateName("增加");
		operate.setOptFunLink("no-resource");
		operate.setOptImgLink("no-img");
		operate.setOptOrder(3);
		operate.setStatus("enabel");
		
		sysplOperateRepository.save(operate);
		
		Assert.assertEquals(new Long(1), operate.getOperateId(), 20L);
	}
	
//	@Test
	public void updateSysplOperate() {
		SysplOperate operate = new SysplOperate();
		operate.setCreateDate(Calendar.getInstance().getTime());
		operate.setCreator("rutine");
		operate.setMemo("测试");
		operate.setOperateName("增加");
		operate.setOptFunLink("no-resource");
		operate.setOptImgLink("no-img");
		operate.setOptOrder(3);
		operate.setStatus("enabel");
		operate.setOperateId(5L);
		
		int row = sysplOperateRepository.update(operate);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplOperates() {
		SysplOperate operate = new SysplOperate();
		operate.setCreateDate(Calendar.getInstance().getTime());
		operate.setCreator("rutine");
		operate.setMemo("测试");
		operate.setOperateName("增加");
		operate.setOptFunLink("no-resource");
		operate.setOptImgLink("no-img");
		operate.setOptOrder(3);
		operate.setStatus("enabel");
		operate.setOperateId(5L);
		
		List<SysplOperate> list = Lists.newArrayList();
		list.add(operate);
		operate = new SysplOperate();
		operate.setCreateDate(Calendar.getInstance().getTime());
		operate.setCreator("rutine");
		operate.setMemo("测试");
		operate.setOperateName("增加");
		operate.setOptFunLink("no-resource");
		operate.setOptImgLink("no-img");
		operate.setOptOrder(3);
		operate.setStatus("enabel");
		operate.setOperateId(15L);
		list.add(operate);
		
		int row = sysplOperateRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplOperate() {
		sysplOperateRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplOperate() {
		boolean exists = sysplOperateRepository.exists(3L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplOperate() {
		long count = sysplOperateRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplOperate() {
		SysplOperate operate = sysplOperateRepository.get(2L);
		
		Assert.assertNotNull(operate);
		Assert.assertEquals("技术", operate.getOperateName());
	}
	
//	@Test
	public void findAllSysplOperate() {
	
		List<SysplOperate> list = (List<SysplOperate>)sysplOperateRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getOptImgLink());
	}
	
//	@Test
	public void countOperatesByName() {
		logger.debug(sysplOperateRepository.countOperatesByName("增加"));
	}
	
	@Test
	public void findOperatesByConditions() {
		Page<SysplOperate> page = sysplOperateRepository.findOperatesByCon("删", new PageRequest(0, 10));
		
		for(SysplOperate opt : page.getContent()) {
			logger.debug(opt.getStatus());
		}
	}
}

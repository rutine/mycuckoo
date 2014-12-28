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
import com.mycuckoo.domain.platform.SysplCode;
import com.mycuckoo.persistence.iface.platform.SysplCodeRepository;

/**
 * 功能说明: SysplCodeRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:10:10 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplCodeRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private SysplCodeRepository sysplCodeRepository;
	
//	@Test
	public void saveSysplCode() {
		SysplCode code = new SysplCode();
		code.setCodeEffect("编码效果");
		code.setCodeEngName("编码英文名");
		code.setCodeName("编码名称");
		code.setCreateDate(Calendar.getInstance().getTime());
		code.setCreator("rutine");
		code.setDelimite(",");
		code.setMemo("供测试");
		code.setModuleName("属于模块");
		code.setPart1("st");
		code.setPart1Con("-");
		code.setPart2("tine");
		code.setPart2Con("-");
		code.setPart3("ru");
		code.setPart3Con("->");
		code.setPartNum(3);
		code.setStatus("enable");
		
		sysplCodeRepository.save(code);
		
		Assert.assertEquals(new Long(1), code.getCodeId(), 20L);
	}
	
//	@Test
	public void updateSysplCode() {
		SysplCode code = new SysplCode();
		code.setCodeEffect("编码效果");
		code.setCodeEngName("编码英文名");
		code.setCodeName("编码名称");
		code.setCreateDate(Calendar.getInstance().getTime());
		code.setCreator("rutine");
		code.setDelimite(",");
		code.setMemo("供测试");
		code.setModuleName("属于模块");
		code.setPart1("st");
		code.setPart1Con("-");
		code.setPart2("tine");
		code.setPart2Con("-");
		code.setPart3("ru");
		code.setPart3Con("->");
		code.setPartNum(3);
		code.setStatus("enable");
		code.setCodeId(25L);
		
		int row = sysplCodeRepository.update(code);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplCodes() {
		SysplCode code = new SysplCode();
		code.setCodeEffect("编码效果");
		code.setCodeEngName("编码英文名");
		code.setCodeName("编码名称");
		code.setCreateDate(Calendar.getInstance().getTime());
		code.setCreator("rutine");
		code.setDelimite(",");
		code.setMemo("供测试");
		code.setModuleName("属于模块");
		code.setPart1("st");
		code.setPart1Con("-");
		code.setPart2("tine");
		code.setPart2Con("-");
		code.setPart3("ru");
		code.setPart3Con("->");
		code.setPartNum(3);
		code.setStatus("enable");
		code.setCodeId(10L);
		
		List<SysplCode> list = Lists.newArrayList();
		list.add(code);
		code = new SysplCode();
		code.setCodeEffect("编码效果");
		code.setCodeEngName("编码英文名");
		code.setCodeName("编码名称");
		code.setCreateDate(Calendar.getInstance().getTime());
		code.setCreator("rutine");
		code.setDelimite(",");
		code.setMemo("供测试");
		code.setModuleName("属于模块");
		code.setPart1("st");
		code.setPart1Con("-");
		code.setPart2("tine");
		code.setPart2Con("-");
		code.setPart3("ru");
		code.setPart3Con("->");
		code.setPartNum(3);
		code.setStatus("enable");
		code.setCodeId(25L);
		list.add(code);
		
		int row = sysplCodeRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplCode() {
		sysplCodeRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplCode() {
		boolean exists = sysplCodeRepository.exists(25L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplCode() {
		long count = sysplCodeRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplCode() {
		SysplCode code = sysplCodeRepository.get(25L);
		
		Assert.assertNotNull(code);
		Assert.assertEquals("技术", code.getCodeEngName());
	}
	
//	@Test
	public void findAllSysplCode() {
	
		List<SysplCode> list = (List<SysplCode>)sysplCodeRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getCodeId());
	}
	
//	@Test
	public void countCodesByCodeEngName() {
		logger.debug(sysplCodeRepository.countCodesByCodeEngName("RKD"));
	}
	
//	@Test
	public void findCodesByConditions() {
	
		Page<SysplCode> page = this.sysplCodeRepository.findCodesByCon("RKD", null, null, new PageRequest(0, 10));
	
		logger.debug(page.getNumberOfElements());
	}
	
	@Test
	public void getCodeByCodeEngName() {
		SysplCode code = this.sysplCodeRepository.getCodeByCodeEngName("RKD");
		
		logger.debug(code.getModuleName());
	}
	
}

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
import com.mycuckoo.domain.platform.SysplSysParameter;
import com.mycuckoo.persistence.iface.platform.SysplSysParameterRepository;

/**
 * 功能说明: SysplSysParameterRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:24:03 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplSysParameterRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private SysplSysParameterRepository sysplSysParameterRepository;
	
//	@Test
	public void saveSysplSysParameter() {
		SysplSysParameter sysParameter = new SysplSysParameter();
		sysParameter.setCreateDate(Calendar.getInstance().getTime());
		sysParameter.setCreator("rutine");
		sysParameter.setMemo("测试");
		sysParameter.setParaKeyName("no-key-name");
		sysParameter.setParaName("编号");
		sysParameter.setParaType("用户");
		sysParameter.setParaValue("yyyymmdd");
		sysParameter.setStatus("enable");
		
		sysplSysParameterRepository.save(sysParameter);
		
		Assert.assertEquals(new Long(1), sysParameter.getParaId(), 20L);
	}
	
//	@Test
	public void updateSysplSysParameter() {
		SysplSysParameter sysParameter = new SysplSysParameter();
		sysParameter.setCreateDate(Calendar.getInstance().getTime());
		sysParameter.setCreator("rutine");
		sysParameter.setMemo("测试");
		sysParameter.setParaKeyName("no-key-name");
		sysParameter.setParaName("编号");
		sysParameter.setParaType("用户");
		sysParameter.setParaValue("yyyymmdd");
		sysParameter.setStatus("enable");
		sysParameter.setParaId(2L);
		
		int row = sysplSysParameterRepository.update(sysParameter);
	
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplSysParameters() {
		SysplSysParameter sysParameter = new SysplSysParameter();
		sysParameter.setCreateDate(Calendar.getInstance().getTime());
		sysParameter.setCreator("rutine");
		sysParameter.setMemo("测试");
		sysParameter.setParaKeyName("no-key-name");
		sysParameter.setParaName("编号");
		sysParameter.setParaType("用户");
		sysParameter.setParaValue("yyyymmdd");
		sysParameter.setStatus("enable");
		sysParameter.setParaId(2L);
		
		List<SysplSysParameter> list = Lists.newArrayList();
		list.add(sysParameter);
		sysParameter = new SysplSysParameter();
		sysParameter.setCreateDate(Calendar.getInstance().getTime());
		sysParameter.setCreator("rutine");
		sysParameter.setMemo("测试");
		sysParameter.setParaKeyName("no-key-name");
		sysParameter.setParaName("编号");
		sysParameter.setParaType("用户");
		sysParameter.setParaValue("yyyymmdd");
		sysParameter.setStatus("enable");
		sysParameter.setParaId(7L);
		list.add(sysParameter);
		
		int row = sysplSysParameterRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplSysParameter() {
		sysplSysParameterRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplSysParameter() {
		boolean exists = sysplSysParameterRepository.exists(1L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplSysParameter() {
		long count = sysplSysParameterRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplSysParameter() {
		SysplSysParameter sysParameter = sysplSysParameterRepository.get(1L);
		
		Assert.assertNotNull(sysParameter);
		Assert.assertEquals("技术", sysParameter.getParaKeyName());
	}
	
//	@Test
	public void findAllSysplSysParameter() {
	
		List<SysplSysParameter> list = (List<SysplSysParameter>)sysplSysParameterRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getParaName());
	}
	
//	@Test
	public void findSystemParaByConditions() {
		Page<SysplSysParameter> page = sysplSysParameterRepository.
				findSystemParametersByCon("", "user", new PageRequest(0, 10));
		
		for(SysplSysParameter para : page.getContent()) {
			logger.debug(para.getParaName());
		}
	}
	
	@Test
	public void findSystemParaByParaName() {
		SysplSysParameter para  = sysplSysParameterRepository.getSystemParameterByParaName("用户有效期");
	
		Assert.assertNotNull(para);
		
		logger.debug(para.getStatus());
	}
}

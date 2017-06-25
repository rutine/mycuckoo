package com.mycuckoo.repository.platform;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.platform.SysParameter;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class SysParameterMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(SysParameterMapperTest.class);

	@Autowired
	private SysParameterMapper mapper;
	

	@Test
	public void testGetByParaName() {
		SysParameter para  = mapper.getByParaName("用户有效期");
	
		Assert.assertNotNull(para);
		
		logger.info("------> getByParaName: {}", para);
	}

	@Test
	public void testSave() {
		SysParameter sysParameter = new SysParameter();
		sysParameter.setCreateDate(Calendar.getInstance().getTime());
		sysParameter.setCreator("rutine");
		sysParameter.setMemo("测试");
		sysParameter.setParaKeyName("no-key-name");
		sysParameter.setParaName("编号");
		sysParameter.setParaType("用户");
		sysParameter.setParaValue("yyyymmdd");
		sysParameter.setStatus("enable");
		
		mapper.save(sysParameter);
		
		Assert.assertEquals(new Long(1), sysParameter.getParaId(), 20L);
	}

	@Test
	public void testUpdate() {
		SysParameter sysParameter = new SysParameter();
		sysParameter.setCreateDate(Calendar.getInstance().getTime());
		sysParameter.setCreator("rutine");
		sysParameter.setMemo("测试");
		sysParameter.setParaKeyName("no-key-name");
		sysParameter.setParaName("编号");
		sysParameter.setParaType("用户");
		sysParameter.setParaValue("yyyymmdd");
		sysParameter.setStatus("enable");
		sysParameter.setParaId(2L);
		
		int row = mapper.update(sysParameter);
	
		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		SysParameter sysParameter = mapper.get(1L);
		
		Assert.assertNotNull(sysParameter);
		Assert.assertEquals("技术", sysParameter.getParaKeyName());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(1L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("paraName", null); //like
		params.put("paraKeyName", "%user%");
		Page<SysParameter> page = mapper.findByPage(params, new PageRequest(0, 10));
		
		for(SysParameter entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();
		
		logger.info("------> count: {}", count);
	}

}

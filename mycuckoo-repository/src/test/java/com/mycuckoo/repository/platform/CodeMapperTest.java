package com.mycuckoo.repository.platform;

import java.util.Calendar;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.Code;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class CodeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(CodeMapperTest.class);
	
	@Autowired
	private CodeMapper mapper;
	

	@Test
	public void testCountByCodeEngName() {
		long count = mapper.countByCodeEngName("RKD");
		
		logger.info("------> countByCodeEngName: {}", count);
	}

	@Test
	public void testGetByCodeEngName() {
		Code code = this.mapper.getByCodeEngName("RKD");
		
		logger.info("------> getByCodeEngName: {}", code);
	}

	@Test
	public void testSave() {
		Code code = new Code();
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
		
		mapper.save(code);
		
		Assert.assertEquals(new Long(1), code.getCodeId(), 20L);
	}

	@Test
	public void testUpdate() {
		Code code = new Code();
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
		
		int row = mapper.update(code);
		
		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		Code code = mapper.get(25L);
		
		Assert.assertNotNull(code);
		Assert.assertEquals("技术", code.getCodeEngName());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(25L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("codeEngName", "%RKD%");
		params.put("codeName", null);
		params.put("moduleName", null);
		Page<Code> page = this.mapper.findByPage(params, new PageRequest(0, 10));
	
		for(Code entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();
		
		logger.info("------> count: {}", count);
	}

}

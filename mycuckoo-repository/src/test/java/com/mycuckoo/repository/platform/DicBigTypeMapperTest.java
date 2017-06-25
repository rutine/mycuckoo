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
import com.mycuckoo.domain.platform.DicBigType;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class DicBigTypeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(DicBigTypeMapperTest.class);

	@Autowired
	private DicBigTypeMapper mapper;
	

	@Test
	public void testCountByBigTypeCode() {
		long count = mapper.countByBigTypeCode("modPageType");
		
		logger.info("------> countByBigTypeCode: {}", count);		
	}

	@Test
	public void testUpdateStatus() {
		mapper.updateStatus(1l, "enable");
	}

	@Test
	public void testSave() {
		DicBigType dicBigType = new DicBigType();
		dicBigType.setBigTypeCode("bigTypeCode");
		dicBigType.setBigTypeName("大字典类型");
		dicBigType.setCreateDate(Calendar.getInstance().getTime());
		dicBigType.setCreator("rutine");
		dicBigType.setStatus("enable");
		
		mapper.save(dicBigType);
		
		Assert.assertEquals(new Long(1), dicBigType.getBigTypeId(), 20L);
	}

	@Test
	public void testUpdate() {
		DicBigType dicBigType = new DicBigType();
		dicBigType.setBigTypeCode("bigTypeCode");
		dicBigType.setBigTypeName("大字典类型");
		dicBigType.setCreateDate(Calendar.getInstance().getTime());
		dicBigType.setCreator("rutine");
		dicBigType.setStatus("enable");
		dicBigType.setBigTypeId(5L);
		
		int row = mapper.update(dicBigType);
		
		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		DicBigType dicBigType = mapper.get(25L);
		
		Assert.assertNotNull(dicBigType);
		Assert.assertEquals("技术", dicBigType.getBigTypeCode());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(25L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("bigTypeName", null);
		params.put("bigTypeCode", "%mod%");
		Page<DicBigType> page = mapper.findByPage(params, new PageRequest(0, 10));
		
		for(DicBigType entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();
		
		logger.info("------> count: {}", count);
	}

}

package com.mycuckoo.repository.uum;

import java.util.Calendar;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class OrganMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(OrganMapperTest.class);
	
	@Autowired
	private OrganMapper organMapper;

	@Test
	public void testFindByUpOrgId() {
		List<Organ> list = organMapper.findByUpOrgId(0L);
		
		logger.info("------> findByUpOrgId: {}", list);
	}

	@Test
	public void testCountByUpOrgId() {
		int count = organMapper.countByUpOrgId(12L);
		
		logger.info("------> countByUpOrgId: {}", count);
	}

	@Test
	public void testCountByOrgSimpleName() {
		int count = organMapper.countByOrgSimpleName("销售");
		
		logger.info("------> countByOrgSimpleName: {}", count);
	}

	@Test
	public void testFindByUpOrgIdAFilter() {
		List<Organ> list = (List<Organ>) organMapper.findByUpOrgIdAFilter(1L, 3L);
		
		logger.info("------> findByUpOrgIdAFilter: {}", list);
	}

	@Test
	public void testFindByPage2() {
		Page<Organ> page = organMapper.findByPage2(new Long[]{0L, 1L, 2L, 3L, 4L, 8L, 9L, 10L}, "2", "董事", new PageRequest(0, 5));
		
		logger.info("------> findByPage2: {}", page);
	}

	@Test
	public void testFindByOrgIds() {
		List<Organ> list = organMapper.findByOrgIds(Arrays.array(0L, 1L, 2L, 3L, 4L, 8L, 9L, 10L));
		
		logger.info("------> findByOrgIds: {}", list);
	}

	@Test
	public void testSave() {
		Organ organ = new Organ();
		organ.setCreateDate(Calendar.getInstance().getTime());
		organ.setMemo("测试");
		organ.setOrgCode("10010");
		organ.setOrgSimpleName("技术部");
		organ.setStatus("disable");
		
		organMapper.save(organ);
		
		Assert.assertEquals(new Long(5), organ.getOrgId(), 20L);
	}

	@Test
	public void testUpdate() {
		Organ organ = new Organ();
		organ.setCreateDate(Calendar.getInstance().getTime());
		organ.setMemo("测试");
		organ.setOrgCode("10010");
		organ.setOrgSimpleName("技术部");
		organ.setStatus("disable");
		organ.setOrgId(2L);
		
		int row = organMapper.update(organ);
		
		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		organMapper.delete(3L);
	}

	@Test
	public void testGet() {
		Organ organ = organMapper.get(2L);
		
		Assert.assertNotNull(organ);
		Assert.assertEquals("技术", organ.getOrgSimpleName());
	}

	@Test
	public void testExists() {
		boolean exists = organMapper.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Page<Organ> page = organMapper.findByPage(null, new PageRequest(0, 5));
		
		logger.info("------> findByPage: {}", page);
	}

	@Test
	public void testCount() {
		long count = organMapper.count();
		
		logger.info("------> count: {}", count);
	}

}

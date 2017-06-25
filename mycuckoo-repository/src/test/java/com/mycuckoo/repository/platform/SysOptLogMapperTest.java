package com.mycuckoo.repository.platform;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.platform.SysOptLog;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class SysOptLogMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(SysOptLogMapperTest.class);

	@Autowired
	private SysOptLogMapper mapper;
	

	@Test
	public void testDeleteLogger() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -4);
		
		mapper.deleteLogger(calendar.getTime());
	}

	@Test
	public void testGetOptContentById() {
		String optContent = mapper.getOptContentById(5L);
		
		logger.info("------> getOptContentById: {}", optContent);
	}

	@Test
	public void testSave() {
		SysOptLog sysOptLog = new SysOptLog();
		sysOptLog.setEndTime(new Date(Calendar.getInstance().getTimeInMillis() + 60 * 60 * 1000));
		sysOptLog.setOptBusinessId("4");
		sysOptLog.setOptContent("测试");
		sysOptLog.setOptModName("系统");
		sysOptLog.setOptPcIp("localhost");
		sysOptLog.setOptPcName("rutine");
		sysOptLog.setOptTime(Calendar.getInstance().getTime());
		sysOptLog.setOptUserName("rutine");
		sysOptLog.setOptUserOgan("no-organ");
		sysOptLog.setOptUserRole("admin");
		sysOptLog.setStartTime(Calendar.getInstance().getTime());
		
		mapper.save(sysOptLog);
		
		Assert.assertEquals(new Long(1), sysOptLog.getOptId(), 20L);
	}

	@Test
	public void testUpdate() {
		SysOptLog sysOptLog = new SysOptLog();
		sysOptLog.setEndTime(new Date(Calendar.getInstance().getTimeInMillis() + 60 * 60 * 1000));
		sysOptLog.setOptBusinessId("4");
		sysOptLog.setOptContent("测试");
		sysOptLog.setOptModName("系统");
		sysOptLog.setOptPcIp("localhost");
		sysOptLog.setOptPcName("rutine");
		sysOptLog.setOptTime(Calendar.getInstance().getTime());
		sysOptLog.setOptUserName("rutine");
		sysOptLog.setOptUserOgan("no-organ");
		sysOptLog.setOptUserRole("admin");
		sysOptLog.setStartTime(Calendar.getInstance().getTime());
		sysOptLog.setOptId(4L);
		
		int row = mapper.update(sysOptLog);
		
		Assert.assertEquals(1, row);
	}

	@Test
	public void testDelete() {
		mapper.delete(3L);
	}

	@Test
	public void testGet() {
		SysOptLog sysOptLog = mapper.get(5L);
		
		Assert.assertNotNull(sysOptLog);
		Assert.assertEquals("技术", sysOptLog.getOptModName());
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(3L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}

	@Test
	public void testFindByPage() {
		Map<String, Object> params = new HashMap<String, Object>(10);
		params.put("optModName", "%登录%");
		params.put("optName", "%登录%");
		params.put("optUserName", "%平%");
		params.put("optUserRole", "%经理%");
		params.put("optPcIp", null); // like
		params.put("optBusinessId", null);
		params.put("startTime", null);
		params.put("endTime", null);
	
		Page<SysOptLog> page = mapper.findByPage(params, new PageRequest(0, 10));
	
		for(SysOptLog entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();
		
		logger.info("------> count: {}", count);
	}

}

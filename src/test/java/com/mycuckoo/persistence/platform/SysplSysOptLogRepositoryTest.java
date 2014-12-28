package com.mycuckoo.persistence.platform;

import java.util.Calendar;
import java.util.Date;
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
import com.mycuckoo.domain.platform.SysplSysOptLog;
import com.mycuckoo.persistence.iface.platform.SysplSysOptLogRepository;

/**
 * 功能说明: SysplSysOptLogRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:22:25 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplSysOptLogRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SysplSysOptLogRepository sysplSysOptLogRepository;
	
//	@Test
	public void saveSysplSysOptLog() {
		SysplSysOptLog sysOptLog = new SysplSysOptLog();
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
		
		sysplSysOptLogRepository.save(sysOptLog);
		
		Assert.assertEquals(new Long(1), sysOptLog.getOptId(), 20L);
	}
	
//	@Test
	public void updateSysplSysOptLog() {
		SysplSysOptLog sysOptLog = new SysplSysOptLog();
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
		
		int row = sysplSysOptLogRepository.update(sysOptLog);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplSysOptLogs() {
		SysplSysOptLog sysOptLog = new SysplSysOptLog();
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
		
		List<SysplSysOptLog> list = Lists.newArrayList();
		list.add(sysOptLog);
		sysOptLog = new SysplSysOptLog();
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
		sysOptLog.setOptId(6L);
		list.add(sysOptLog);
		
		int row = sysplSysOptLogRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplSysOptLog() {
		sysplSysOptLogRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplSysOptLog() {
		boolean exists = sysplSysOptLogRepository.exists(3L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplSysOptLog() {
		long count = sysplSysOptLogRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplSysOptLog() {
		SysplSysOptLog sysOptLog = sysplSysOptLogRepository.get(5L);
		
		Assert.assertNotNull(sysOptLog);
		Assert.assertEquals("技术", sysOptLog.getOptModName());
	}
	
//	@Test
	public void findAllSysplSysOptLog() {
	
		List<SysplSysOptLog> list = (List<SysplSysOptLog>)sysplSysOptLogRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getOptPcName());
	}
	
//	@Test
	public void deleteLogger() {
		this.sysplSysOptLogRepository.deleteLogger(4);
	}
	
//	@Test
	public void findLoggerByConditions() {
		SysplSysOptLog optLog = new SysplSysOptLog();
		optLog.setOptModName("登录");
		optLog.setOptName("登录");
		optLog.setOptUserName("平");
		optLog.setOptUserRole("经理");
	
	
		Page<SysplSysOptLog> page = sysplSysOptLogRepository.findOptLogsByCon(optLog, new PageRequest(0, 10));
	
		for(SysplSysOptLog log : page.getContent()) {
			logger.debug(log.getOptModName() + ", " + log.getOptName() + ", " + log.getOptUserName() 
					+ ", " + log.getOptUserRole() + ", " + log.getOptId() + ", " + log.getOptBusinessId() 
					+ ", " + log.getOptTime());
		}
	}
	
	@Test
	public void findLoggerContentById() {
		logger.debug(sysplSysOptLogRepository.getOptLogContentById(5l));
	}
}

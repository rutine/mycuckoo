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
import com.mycuckoo.domain.platform.SysplSchedulerJob;
import com.mycuckoo.persistence.iface.platform.SysplSchedulerJobRepository;

/**
 * 功能说明: SysplSchedulerJobRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:21:11 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SysplSchedulerJobRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private SysplSchedulerJobRepository sysplSchedulerJobRepository;
	
//	@Test
	public void saveSysplSchedulerJob() {
		SysplSchedulerJob schedulerJob = new SysplSchedulerJob();
		schedulerJob.setCreateDate(Calendar.getInstance().getTime());
		schedulerJob.setCreator("rutine");
		schedulerJob.setEndTime(new Date(Calendar.getInstance().getTimeInMillis() + 24 * 60 *60 * 1000L));
		schedulerJob.setJobClassDescript("打酱油");
		schedulerJob.setJobName("闲职");
		schedulerJob.setMemo("测试");
		schedulerJob.setRepeatTime(10);
		schedulerJob.setSplitTime(60 *60 * 1000L);
		schedulerJob.setStartTime(Calendar.getInstance().getTime());
		schedulerJob.setStatus("enable");
		
		
		sysplSchedulerJobRepository.save(schedulerJob);
		
		Assert.assertEquals(new Long(1), schedulerJob.getJobId(), 20L);
	}
	
//	@Test
	public void updateSysplSchedulerJob() {
		SysplSchedulerJob schedulerJob = new SysplSchedulerJob();
		schedulerJob.setCreateDate(Calendar.getInstance().getTime());
		schedulerJob.setCreator("rutine");
		schedulerJob.setEndTime(new Date(Calendar.getInstance().getTimeInMillis() + 24 * 60 *60 * 1000L));
		schedulerJob.setJobClassDescript("打酱油");
		schedulerJob.setJobName("闲职");
		schedulerJob.setMemo("测试");
		schedulerJob.setRepeatTime(10);
		schedulerJob.setSplitTime(60 *60 * 1000L);
		schedulerJob.setStartTime(Calendar.getInstance().getTime());
		schedulerJob.setStatus("enable");
		schedulerJob.setJobId(20L);
		
		int row = sysplSchedulerJobRepository.update(schedulerJob);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateSysplSchedulerJobs() {
		SysplSchedulerJob schedulerJob = new SysplSchedulerJob();
		schedulerJob.setCreateDate(Calendar.getInstance().getTime());
		schedulerJob.setCreator("rutine");
		schedulerJob.setEndTime(new Date(Calendar.getInstance().getTimeInMillis() + 24 * 60 *60 * 1000L));
		schedulerJob.setJobClassDescript("打酱油");
		schedulerJob.setJobName("闲职");
		schedulerJob.setMemo("测试");
		schedulerJob.setRepeatTime(10);
		schedulerJob.setSplitTime(60 *60 * 1000L);
		schedulerJob.setStartTime(Calendar.getInstance().getTime());
		schedulerJob.setStatus("enable");
		schedulerJob.setJobId(21L);
			
		List<SysplSchedulerJob> list = Lists.newArrayList();
		list.add(schedulerJob);
		schedulerJob = new SysplSchedulerJob();
		schedulerJob.setCreateDate(Calendar.getInstance().getTime());
		schedulerJob.setCreator("rutine");
		schedulerJob.setEndTime(new Date(Calendar.getInstance().getTimeInMillis() + 24 * 60 *60 * 1000L));
		schedulerJob.setJobClassDescript("打酱油");
		schedulerJob.setJobName("闲职");
		schedulerJob.setMemo("测试");
		schedulerJob.setRepeatTime(10);
		schedulerJob.setSplitTime(60 *60 * 1000L);
		schedulerJob.setStartTime(Calendar.getInstance().getTime());
		schedulerJob.setStatus("enable");
		schedulerJob.setJobId(19L);
		list.add(schedulerJob);
		
		int row = sysplSchedulerJobRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteSysplSchedulerJob() {
		sysplSchedulerJobRepository.delete(3L);
	}
	
//	@Test
	public void existsSysplSchedulerJob() {
		boolean exists = sysplSchedulerJobRepository.exists(3L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countSysplSchedulerJob() {
		long count = sysplSchedulerJobRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getSysplSchedulerJob() {
		SysplSchedulerJob schedulerJob = sysplSchedulerJobRepository.get(1L);
		
		Assert.assertNotNull(schedulerJob);
		Assert.assertEquals("技术", schedulerJob.getJobName());
	}
	
//	@Test
	public void findAllSysplSchedulerJob() {
		
		List<SysplSchedulerJob> list = (List<SysplSchedulerJob>)sysplSchedulerJobRepository.findAll();
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getJobClassDescript());
	}
	
//	@Test
	public void countSchedulerJobByJobName() {
		logger.debug(sysplSchedulerJobRepository.countSchedulerJobsByJobName("logJob"));
	}
	
//	@Test
	public void findSchedulerJobByConditions() {
		Page<SysplSchedulerJob> page = sysplSchedulerJobRepository.findSchedulerJobsByCon("log", "", new PageRequest(0, 10));
		
		for(SysplSchedulerJob job : page.getContent()) {
			logger.debug(job.getTriggerType());
		}
	}
	
//	@Test
	public void modifyAllJobStatus() {
		sysplSchedulerJobRepository.updateSchedulerJobsStatus(new Long[]{0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l}, "enable");
	}
	
	@Test
	public void modifyJobStatus() {
		sysplSchedulerJobRepository.updateScheduleJobStatus(1l, "enable");
	}
}

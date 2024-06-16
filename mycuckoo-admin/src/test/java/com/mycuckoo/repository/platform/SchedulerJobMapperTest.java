package com.mycuckoo.repository.platform;

import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import com.mycuckoo.domain.platform.SchedulerJob;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class SchedulerJobMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(SchedulerJobMapperTest.class);

    @Autowired
    private SchedulerJobMapper mapper;


    @Test
    public void testCountByJobName() {
        long count = mapper.countByJobName("logJob");

        logger.info("------> countByJobName: {}", count);
    }

    @Test
    public void testUpdateStatuses() {
        mapper.updateStatuses(new Long[]{0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l}, "enable");
    }

    @Test
    public void testUpdateStatus() {
        mapper.updateStatus(1l, "enable");
    }

    @Test
    public void testSave() {
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setCreateTime(LocalDateTime.now());
        schedulerJob.setCreator("rutine");
        schedulerJob.setUpdateTime(LocalDateTime.now());
        schedulerJob.setUpdator("rutine");
        schedulerJob.setEndTime(new Date(Calendar.getInstance().getTimeInMillis() + 24 * 60 * 60 * 1000L));
        schedulerJob.setJobClass("打酱油");
        schedulerJob.setJobName("闲职");
        schedulerJob.setMemo("测试");
        schedulerJob.setRepeatCount(10);
        schedulerJob.setIntervalTime(60 * 60 * 1000L);
        schedulerJob.setStartTime(Calendar.getInstance().getTime());
        schedulerJob.setStatus("enable");


        mapper.save(schedulerJob);

        Assert.assertEquals(new Long(1), schedulerJob.getJobId(), 20L);
    }

    @Test
    public void testUpdate() {
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setCreateTime(LocalDateTime.now());
        schedulerJob.setCreator("rutine");
        schedulerJob.setUpdateTime(LocalDateTime.now());
        schedulerJob.setUpdator("rutine");
        schedulerJob.setEndTime(new Date(Calendar.getInstance().getTimeInMillis() + 24 * 60 * 60 * 1000L));
        schedulerJob.setJobClass("打酱油");
        schedulerJob.setJobName("闲职");
        schedulerJob.setMemo("测试");
        schedulerJob.setRepeatCount(10);
        schedulerJob.setIntervalTime(60 * 60 * 1000L);
        schedulerJob.setStartTime(new Date());
        schedulerJob.setStatus("enable");
        schedulerJob.setJobId(20L);

        int row = mapper.update(schedulerJob);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        SchedulerJob schedulerJob = mapper.get(1L);

        Assert.assertNotNull(schedulerJob);
        Assert.assertEquals("技术", schedulerJob.getJobName());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(3L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("jobName", "%log%");
        params.put("triggerType", null); //like
        Page<SchedulerJob> page = mapper.findByPage(params, new PageRequest(0, 10));

        for (SchedulerJob entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }
}

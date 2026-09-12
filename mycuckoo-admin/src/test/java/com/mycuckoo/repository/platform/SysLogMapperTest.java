package com.mycuckoo.repository.platform;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SysLog;
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
public class SysLogMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(SysLogMapperTest.class);

    @Autowired
    private SysLogMapper mapper;


    @Test
    public void testDeleteLogger() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -4);

        mapper.deleteLogger(calendar.getTime());
    }

    @Test
    public void testGetContentById() {
        String logContent = mapper.getContentById(5L);

        logger.info("------> getContentById: {}", logContent);
    }

    @Test
    public void testSave() {
        SysLog sysLog = new SysLog();
        sysLog.setTitle("系统-测试");
        sysLog.setBusiType(null);
        sysLog.setBusiId("4");
        sysLog.setContent("测试");
        sysLog.setIp("127.0.0.1");
        sysLog.setUserName("rutine");
        sysLog.setUserRole("admin");
        sysLog.setStartTime(Calendar.getInstance().getTime());
        sysLog.setEndTime(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        sysLog.setCreator("1");
        sysLog.setCreateTime(LocalDateTime.now());

        mapper.save(sysLog);

        Assert.assertEquals(new Long(1), sysLog.getLogId(), 20L);
    }

    @Test
    public void testUpdate() {
        SysLog sysLog = new SysLog();
        sysLog.setTitle("系统-测试");
        sysLog.setBusiType(null);
        sysLog.setBusiId("4");
        sysLog.setContent("测试");
        sysLog.setIp("127.0.0.1");
        sysLog.setUserName("rutine");
        sysLog.setUserRole("admin");
        sysLog.setStartTime(Calendar.getInstance().getTime());
        sysLog.setEndTime(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        sysLog.setCreator("1");
        sysLog.setCreateTime(LocalDateTime.now());
        sysLog.setLogId(4L);

        int row = mapper.update(sysLog);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        SysLog sysLog = mapper.get(5L);

        Assert.assertNotNull(sysLog);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = new HashMap<String, Object>(10);
        params.put("title", "%登录%");
        params.put("userName", "%平%");
        params.put("userRole", "%经理%");
        params.put("ip", null); // like
        params.put("busiId", null);
        params.put("startTime", null);
        params.put("endTime", null);

        Page<SysLog> page = mapper.findByPage(params, new Querier(1, 10));

        for (SysLog entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}
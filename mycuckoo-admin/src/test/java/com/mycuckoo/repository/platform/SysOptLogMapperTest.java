package com.mycuckoo.repository.platform;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SysOptLog;
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
        sysOptLog.setModName("系统");
        sysOptLog.setBusiType(null);
        sysOptLog.setBusiId("4");
        sysOptLog.setContent("测试");
        sysOptLog.setIp("127.0.0.1");
        sysOptLog.setUserName("rutine");
        sysOptLog.setUserRole("admin");
        sysOptLog.setStartTime(Calendar.getInstance().getTime());
        sysOptLog.setEndTime(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        sysOptLog.setCreator("1");
        sysOptLog.setCreateTime(LocalDateTime.now());

        mapper.save(sysOptLog);

        Assert.assertEquals(new Long(1), sysOptLog.getOptId(), 20L);
    }

    @Test
    public void testUpdate() {
        SysOptLog sysOptLog = new SysOptLog();
        sysOptLog.setModName("系统");
        sysOptLog.setBusiType(null);
        sysOptLog.setBusiId("4");
        sysOptLog.setContent("测试");
        sysOptLog.setIp("127.0.0.1");
        sysOptLog.setUserName("rutine");
        sysOptLog.setUserRole("admin");
        sysOptLog.setStartTime(Calendar.getInstance().getTime());
        sysOptLog.setEndTime(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        sysOptLog.setCreator("1");
        sysOptLog.setCreateTime(LocalDateTime.now());
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
        Assert.assertEquals("技术", sysOptLog.getModName());
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = new HashMap<String, Object>(10);
        params.put("modName", "%登录%");
        params.put("name", "%登录%");
        params.put("userName", "%平%");
        params.put("userRole", "%经理%");
        params.put("ip", null); // like
        params.put("busiId", null);
        params.put("startTime", null);
        params.put("endTime", null);

        Page<SysOptLog> page = mapper.findByPage(params, new Querier(1, 10));

        for (SysOptLog entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}

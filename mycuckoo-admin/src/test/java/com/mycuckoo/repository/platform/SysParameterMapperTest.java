package com.mycuckoo.repository.platform;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.SysParameter;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class SysParameterMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(SysParameterMapperTest.class);

    @Autowired
    private SysParameterMapper mapper;


    @Test
    public void testGetByKey() {
        SysParameter para = mapper.getByKey("用户有效期");

        Assert.assertNotNull(para);

        logger.info("------> getByKey: {}", para);
    }

    @Test
    public void testSave() {
        SysParameter sysParameter = new SysParameter();
        sysParameter.setCreateTime(LocalDateTime.now());
        sysParameter.setCreator("1");
        sysParameter.setUpdateTime(LocalDateTime.now());
        sysParameter.setUpdator("1");
        sysParameter.setMemo("测试");
        sysParameter.setName("编号");
        sysParameter.setKey("no-key-name");
        sysParameter.setValue("yyyymmdd");
        sysParameter.setType("用户");
        sysParameter.setStatus("enable");

        mapper.save(sysParameter);

        Assert.assertEquals(new Long(1), sysParameter.getParaId(), 20L);
    }

    @Test
    public void testUpdate() {
        SysParameter sysParameter = new SysParameter();
        sysParameter.setUpdateTime(LocalDateTime.now());
        sysParameter.setUpdator("1");
        sysParameter.setMemo("测试");
        sysParameter.setName("编号");
        sysParameter.setKey("no-key-name");
        sysParameter.setValue("yyyymmdd");
        sysParameter.setType("用户");
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
        Assert.assertEquals("技术", sysParameter.getName());
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("name", null); //like
        params.put("key", "%user%");
        Page<SysParameter> page = mapper.findByPage(params, new Querier(1, 10));

        for (SysParameter entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}

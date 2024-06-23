package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import com.mycuckoo.domain.platform.Operate;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;
import java.util.Map;

@SpringBootTest
public class OperateMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(OperateMapperTest.class);

    @Autowired
    private OperateMapper mapper;


    @Test
    public void testCountByName() {
        long count = mapper.countByName("增加");

        logger.info("------> count: {}", count);
    }

    @Test
    public void testSave() {
        Operate operate = new Operate();
        operate.setCreateTime(LocalDateTime.now());
        operate.setCreator("1");
        operate.setMemo("测试");
        operate.setName("增加");
        operate.setCode("no-resource");
        operate.setIconCls("no-img");
        operate.setOrder(3);
        operate.setStatus("enabel");

        mapper.save(operate);

        Assert.assertEquals(new Long(1), operate.getOperateId(), 20L);
    }

    @Test
    public void testUpdate() {
        Operate operate = new Operate();
        operate.setCreateTime(LocalDateTime.now());
        operate.setCreator("1");
        operate.setMemo("测试");
        operate.setName("增加");
        operate.setCode("no-resource");
        operate.setIconCls("no-img");
        operate.setOrder(3);
        operate.setStatus("enabel");
        operate.setOperateId(5L);

        int row = mapper.update(operate);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        Operate operate = mapper.get(2L);

        Assert.assertNotNull(operate);
        Assert.assertEquals("技术", operate.getName());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(3L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("optName", "%删%");
        Page<Operate> page = mapper.findByPage(params, new PageRequest(0, 10));

        for (Operate entity : page.getContent()) {
            logger.info("------> count: {}", entity);
        }
    }

}

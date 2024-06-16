package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import com.mycuckoo.domain.platform.Resource;
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
public class ResourceMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(ResourceMapperTest.class);

    @Autowired
    private ResourceMapper mapper;


    @Test
    public void testSave() {
        Resource entity = new Resource();
        entity.setCreateTime(LocalDateTime.now());
        entity.setCreator("rutine");
        entity.setMemo("测试");
        entity.setName("增加");
        entity.setModuleId(7L);
        entity.setOperateId(1L);
        entity.setOrder(3);
        entity.setStatus("enable");

        mapper.save(entity);

        Assert.assertEquals(new Long(1), entity.getOperateId(), 20L);
    }

    @Test
    public void testUpdate() {
        Resource entity = new Resource();
        entity.setCreateTime(LocalDateTime.now());
        entity.setCreator("rutine");
        entity.setMemo("测试");
        entity.setName("增加");
        entity.setModuleId(7L);
        entity.setOperateId(2L);
        entity.setOrder(2);
        entity.setStatus("enable");
        entity.setOperateId(1L);

        int row = mapper.update(entity);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        Resource entity = mapper.get(2L);

        Assert.assertNotNull(entity);
        Assert.assertEquals("技术", entity.getName());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(3L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("name", "%增加%");
        Page<Resource> page = mapper.findByPage(params, new PageRequest(0, 10));

        for (Resource entity : page.getContent()) {
            logger.info("------> count: {}", entity);
        }
    }
}

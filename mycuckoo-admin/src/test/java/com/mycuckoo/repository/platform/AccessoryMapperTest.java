package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.Accessory;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

@SpringBootTest
public class AccessoryMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(AccessoryMapperTest.class);

    @Autowired
    private AccessoryMapper mapper;

    @Test
    public void testFindByAfficheId() {
        List<Accessory> list = mapper.findByAfficheId(3L);

        for (Accessory entity : list) {
            logger.info("------> findByAfficheId: {}", entity);
        }
    }

    @Test
    public void testSave() {
        Accessory accessory = new Accessory();
        accessory.setAccessoryName("测试附件");
        accessory.setInfoId(4L);

        mapper.save(accessory);

        Assert.assertEquals(new Long(1), accessory.getAccessoryId(), 20L);
    }

    @Test
    public void testUpdate() {
        Accessory accessory = new Accessory();
        accessory.setAccessoryName("测试附件");
        accessory.setInfoId(4L);
        accessory.setAccessoryId(3L);

        int row = mapper.update(accessory);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        Accessory accessory = mapper.get(2L);

        Assert.assertNotNull(accessory);
        Assert.assertEquals("技术", accessory.getAccessoryName());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(34L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<Accessory> page = mapper.findByPage(null, new PageRequest(0, 5));

        for (Accessory entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}

package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.DicSmallType;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

@SpringBootTest
public class DicSmallTypeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DicSmallTypeMapperTest.class);

    @Autowired
    private DicSmallTypeMapper mapper;


    @Test
    public void testDeleteByBigTypeId() {
        mapper.deleteByBigTypeId(25l);
    }

    @Test
    public void testFindByBigTypeId() {
        List<DicSmallType> list = mapper.findByBigTypeId(25l);

        for (DicSmallType entity : list) {
            logger.info("------> findByBigTypeId: {}", entity);
        }
    }

    @Test
    public void testFindByBigTypeCode() {
        List<DicSmallType> list = mapper.findByBigTypeCode("modPageType");

        for (DicSmallType entity : list) {
            logger.info("------> findByBigTypeCode: {}", entity);
        }
    }

    @Test
    public void testSave() {
        DicSmallType dicSmallType = new DicSmallType();
        dicSmallType.setBigTypeId(21L);
        dicSmallType.setSmallTypeCode("小类型code");
        dicSmallType.setSmallTypeName("字典小类型");

        mapper.save(dicSmallType);

        Assert.assertEquals(new Long(1), dicSmallType.getSmallTypeId(), 20L);
    }

    @Test
    public void testUpdate() {
        DicSmallType dicSmallType = new DicSmallType();
        dicSmallType.setSmallTypeCode("小类型code");
        dicSmallType.setSmallTypeName("字典小类型");
        dicSmallType.setSmallTypeId(8L);

        int row = mapper.update(dicSmallType);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        DicSmallType dicSmallType = mapper.get(45L);

        Assert.assertNotNull(dicSmallType);
        Assert.assertEquals("技术", dicSmallType.getSmallTypeName());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(45L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<DicSmallType> page = mapper.findByPage(null, new PageRequest(0, 10));

        for (DicSmallType entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

    @Test
    public void testCount() {
        long count = mapper.count();

        logger.info("------> count: {}", count);
    }

}

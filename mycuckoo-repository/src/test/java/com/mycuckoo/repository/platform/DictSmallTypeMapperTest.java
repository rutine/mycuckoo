package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.DictSmallType;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class DictSmallTypeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DictSmallTypeMapperTest.class);

    @Autowired
    private DictSmallTypeMapper mapper;


    @Test
    public void testDeleteByBigTypeId() {
        mapper.deleteByBigTypeId(25l);
    }

    @Test
    public void testFindByBigTypeId() {
        List<DictSmallType> list = mapper.findByBigTypeId(25l);

        for (DictSmallType entity : list) {
            logger.info("------> findByBigTypeId: {}", entity);
        }
    }

    @Test
    public void testFindByBigTypeCode() {
        List<DictSmallType> list = mapper.findByBigTypeCode("modPageType");

        for (DictSmallType entity : list) {
            logger.info("------> findByBigTypeCode: {}", entity);
        }
    }

    @Test
    public void testSave() {
        DictSmallType dictSmallType = new DictSmallType();
        dictSmallType.setBigTypeId(21L);
        dictSmallType.setCode("小类型code");
        dictSmallType.setName("字典小类型");
        dictSmallType.setCreator("rutine");
        dictSmallType.setCreateDate(new Date());

        mapper.save(dictSmallType);

        Assert.assertEquals(new Long(1), dictSmallType.getSmallTypeId(), 20L);
    }

    @Test
    public void testUpdate() {
        DictSmallType dictSmallType = new DictSmallType();
        dictSmallType.setCode("小类型code");
        dictSmallType.setName("字典小类型");
        dictSmallType.setCreator("rutine");
        dictSmallType.setCreateDate(new Date());
        dictSmallType.setSmallTypeId(8L);

        int row = mapper.update(dictSmallType);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        DictSmallType dictSmallType = mapper.get(45L);

        Assert.assertNotNull(dictSmallType);
        Assert.assertEquals("技术", dictSmallType.getName());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(45L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<DictSmallType> page = mapper.findByPage(null, new PageRequest(0, 10));

        for (DictSmallType entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

    @Test
    public void testCount() {
        long count = mapper.count();

        logger.info("------> count: {}", count);
    }

}

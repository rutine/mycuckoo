package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.DictBigType;
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
public class DictBigTypeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DictBigTypeMapperTest.class);

    @Autowired
    private DictBigTypeMapper mapper;


    @Test
    public void testCountByBigTypeCode() {
        long count = mapper.countByCode("modPageType");

        logger.info("------> countByCode: {}", count);
    }

    @Test
    public void testUpdateStatus() {
        mapper.updateStatus(1l, "enable");
    }

    @Test
    public void testSave() {
        DictBigType dictBigType = new DictBigType();
        dictBigType.setCode("bigTypeCode");
        dictBigType.setName("大字典类型");
        dictBigType.setUpdateTime(LocalDateTime.now());
        dictBigType.setUpdator("1");
        dictBigType.setCreateTime(LocalDateTime.now());
        dictBigType.setCreator("1");
        dictBigType.setStatus("enable");

        mapper.save(dictBigType);

        Assert.assertEquals(new Long(1), dictBigType.getBigTypeId(), 20L);
    }

    @Test
    public void testUpdate() {
        DictBigType dictBigType = new DictBigType();
        dictBigType.setCode("bigTypeCode");
        dictBigType.setName("大字典类型");
        dictBigType.setUpdateTime(LocalDateTime.now());
        dictBigType.setUpdator("1");
        dictBigType.setCreateTime(LocalDateTime.now());
        dictBigType.setCreator("1");
        dictBigType.setStatus("enable");
        dictBigType.setBigTypeId(5L);

        int row = mapper.update(dictBigType);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        DictBigType dictBigType = mapper.get(25L);

        Assert.assertNotNull(dictBigType);
        Assert.assertEquals("技术", dictBigType.getCode());
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("name", null);
        params.put("code", "%mod%");
        Page<DictBigType> page = mapper.findByPage(params, new Querier(1, 10));

        for (DictBigType entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}

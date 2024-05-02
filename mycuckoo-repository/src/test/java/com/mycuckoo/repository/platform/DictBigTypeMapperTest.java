package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.DictBigType;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.Calendar;
import java.util.Map;

@SpringBootTest
public class DictBigTypeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DictBigTypeMapperTest.class);

    @Autowired
    private DictBigTypeMapper mapper;


    @Test
    public void testCountByBigTypeCode() {
        long count = mapper.countByBigTypeCode("modPageType");

        logger.info("------> countByBigTypeCode: {}", count);
    }

    @Test
    public void testUpdateStatus() {
        mapper.updateStatus(1l, "enable");
    }

    @Test
    public void testSave() {
        DictBigType dictBigType = new DictBigType();
        dictBigType.setBigTypeCode("bigTypeCode");
        dictBigType.setBigTypeName("大字典类型");
        dictBigType.setCreateDate(Calendar.getInstance().getTime());
        dictBigType.setCreator("rutine");
        dictBigType.setStatus("enable");

        mapper.save(dictBigType);

        Assert.assertEquals(new Long(1), dictBigType.getBigTypeId(), 20L);
    }

    @Test
    public void testUpdate() {
        DictBigType dictBigType = new DictBigType();
        dictBigType.setBigTypeCode("bigTypeCode");
        dictBigType.setBigTypeName("大字典类型");
        dictBigType.setCreateDate(Calendar.getInstance().getTime());
        dictBigType.setCreator("rutine");
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
        Assert.assertEquals("技术", dictBigType.getBigTypeCode());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(25L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("bigTypeName", null);
        params.put("bigTypeCode", "%mod%");
        Page<DictBigType> page = mapper.findByPage(params, new PageRequest(0, 10));

        for (DictBigType entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

    @Test
    public void testCount() {
        long count = mapper.count();

        logger.info("------> count: {}", count);
    }

}

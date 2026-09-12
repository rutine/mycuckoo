package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.District;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class DistrictMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DistrictMapperTest.class);

    @Autowired
    private DistrictMapper mapper;


    @Test
    public void testCountByParentCode() {
        long count = mapper.countByParentCode("110000");

        logger.info("------> countByParentCode: {}", count);
    }

    @Test
    public void testCountByName() {
        long count = mapper.countByName("广州");

        logger.info("------> countByName: {}", count);
    }

    @Test
    public void testGetByCode() {
        District entity = mapper.getByCode("110000");

        Assert.assertTrue(entity != null);

        logger.info("------> getByCode: {}", entity);

    }

    @Test
    public void testFindByParentIdAndIgnoreIds() {
        List<District> list = mapper.findByParentCodeAndIgnoreIds("110000", new long[]{0L, 9L});

        for (District entity : list) {
            logger.info("------> findByParentCodeAndIgnoreIds: {}", entity);
        }
    }

    @Test
    public void testSave() {
        District district = new District();
        district.setCreateTime(LocalDateTime.now());
        district.setCreator("1");
        district.setUpdateTime(LocalDateTime.now());
        district.setUpdator("1");
        district.setType("province");
        district.setCode("020");
        district.setName("广州");

        mapper.save(district);

        Assert.assertEquals(1L, district.getId(), 20L);
    }

    @Test
    public void testUpdate() {
        District district = new District();
        district.setCreateTime(LocalDateTime.now());
        district.setCreator("1");
        district.setUpdateTime(LocalDateTime.now());
        district.setUpdator("1");
        district.setCode("020");
        district.setType("province");
        district.setName("广州");

        int row = mapper.update(district);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        District district = mapper.get(2005L);

        Assert.assertNotNull(district);
        Assert.assertEquals("技术", district.getName());
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("array", new Long[]{0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l});
        params.put("districtName", null); //like
        params.put("districtLevel", null);
        Page<District> page = this.mapper.findByPage(params, new Querier(1, 10));

        for (District entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}

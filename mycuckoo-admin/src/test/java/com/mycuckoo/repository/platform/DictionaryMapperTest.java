package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.Dictionary;
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
public class DictionaryMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DictionaryMapperTest.class);

    @Autowired
    private DictionaryMapper mapper;


    @Test
    public void testCountByCode() {
        long count = mapper.countByCode("modPageType");

        logger.info("------> countByCode: {}", count);
    }

    @Test
    public void testUpdateStatus() {
        mapper.updateStatus(1l, "enable");
    }

    @Test
    public void testSave() {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode("dictCode");
        dictionary.setName("字典");
        dictionary.setUpdateTime(LocalDateTime.now());
        dictionary.setUpdator("1");
        dictionary.setCreateTime(LocalDateTime.now());
        dictionary.setCreator("1");
        dictionary.setStatus("enable");

        mapper.save(dictionary);

        Assert.assertEquals(new Long(1), dictionary.getDictId(), 20L);
    }

    @Test
    public void testUpdate() {
        Dictionary dictionary = new Dictionary();
        dictionary.setCode("dictCode");
        dictionary.setName("字典");
        dictionary.setUpdateTime(LocalDateTime.now());
        dictionary.setUpdator("1");
        dictionary.setCreateTime(LocalDateTime.now());
        dictionary.setCreator("1");
        dictionary.setStatus("enable");
        dictionary.setDictId(5L);

        int row = mapper.update(dictionary);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        Dictionary dictionary = mapper.get(25L);

        Assert.assertNotNull(dictionary);
        Assert.assertEquals("技术", dictionary.getCode());
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("name", null);
        params.put("code", "%mod%");
        Page<Dictionary> page = mapper.findByPage(params, new Querier(1, 10));

        for (Dictionary entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}
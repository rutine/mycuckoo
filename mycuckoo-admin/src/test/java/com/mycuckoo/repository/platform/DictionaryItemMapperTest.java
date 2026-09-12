package com.mycuckoo.repository.platform;

import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.platform.DictionaryItem;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class DictionaryItemMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(DictionaryItemMapperTest.class);

    @Autowired
    private DictionaryItemMapper mapper;


    @Test
    public void testDeleteByDictId() {
        mapper.deleteByDictId(25l);
    }

    @Test
    public void testFindByDictId() {
        List<DictionaryItem> list = mapper.findByDictId(25l);

        for (DictionaryItem entity : list) {
            logger.info("------> findByDictId: {}", entity);
        }
    }

    @Test
    public void testFindByDictCode() {
        List<DictionaryItem> list = mapper.findByDictCode("modPageType");

        for (DictionaryItem entity : list) {
            logger.info("------> findByDictCode: {}", entity);
        }
    }

    @Test
    public void testSave() {
        DictionaryItem item = new DictionaryItem();
        item.setDictId(21L);
        item.setCode("itemCode");
        item.setName("字典项");
        item.setCreator("1");
        item.setCreateTime(LocalDateTime.now());

        mapper.save(item);

        Assert.assertEquals(new Long(1), item.getItemId(), 20L);
    }

    @Test
    public void testUpdate() {
        DictionaryItem item = new DictionaryItem();
        item.setCode("itemCode");
        item.setName("字典项");
        item.setCreator("1");
        item.setCreateTime(LocalDateTime.now());
        item.setItemId(8L);

        int row = mapper.update(item);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        DictionaryItem item = mapper.get(45L);

        Assert.assertNotNull(item);
        Assert.assertEquals("技术", item.getName());
    }

    @Test
    public void testFindByPage() {
        Page<DictionaryItem> page = mapper.findByPage(null, new Querier(1, 10));

        for (DictionaryItem entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}
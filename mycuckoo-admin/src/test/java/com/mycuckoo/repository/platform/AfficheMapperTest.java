package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.Affiche;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class AfficheMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(AfficheMapperTest.class);

    @Autowired
    private AfficheMapper mapper;


    @Test
    public void testFindBeforeValidate() {
        List<Affiche> list = this.mapper.findBeforeValidate(Calendar.getInstance().getTime());

        for (Affiche entity : list) {
            logger.info("------> findBeforeValidate: {}", entity);
        }
    }

    @Test
    public void testSave() {
        Affiche affiche = new Affiche();
        affiche.setTitle("公测");
        affiche.setContent("公告测试");
        affiche.setInvalidate(Calendar.getInstance().getTime());
        affiche.setPublish(false);

        mapper.save(affiche);

        Assert.assertEquals(new Long(1), affiche.getAfficheId(), 20L);
    }

    @Test
    public void testUpdate() {
        Affiche affiche = new Affiche();
        affiche.setTitle("公测");
        affiche.setContent("公告测试");
        affiche.setInvalidate(Calendar.getInstance().getTime());
        affiche.setPublish(true);
        affiche.setAfficheId(9L);

        int row = mapper.update(affiche);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        for (Long id : new Long[]{0l, 1l, 2l, 3l, 4l, 5l}) {
            mapper.delete(id);
        }
    }

    @Test
    public void testGet() {
        Affiche affiche = mapper.get(6L);

        Assert.assertNotNull(affiche);
        Assert.assertEquals("技术", affiche.getContent());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(34L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("title", "%工%");
        params.put("publish", null);
        Page<Affiche> page = this.mapper.findByPage(params, new PageRequest(0, 10));

        for (Affiche entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

    @Test
    public void testCount() {
        long count = mapper.count();

        logger.info("------> count: {}", count);
    }

}

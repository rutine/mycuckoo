package com.mycuckoo.repository.platform;

import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.domain.platform.Operate;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.junit.Assert.fail;

@SpringBootTest
public class ModOptRefMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(ModOptRefMapperTest.class);

    @Autowired
    private ModOptRefMapper mapper;


    @Test
    public void testDeleteByModuleId() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteByOperateId() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindByModuleId() {
        List<ModOptRef> list = mapper.findByModuleId(10L);

        Assert.assertTrue(list.size() > 0);

        for (ModOptRef entity : list) {
            logger.info("------> findByModuleId: {}", entity);
        }
    }

    @Test
    public void testFindByOperateId() {
        List<ModOptRef> list = mapper.findByOperateId(10L);

        Assert.assertTrue(list.size() > 0);

        for (ModOptRef entity : list) {
            logger.info("------> findByOperateId: {}", entity);
        }
    }

    @Test
    public void testSave() {
        ModOptRef modOptRef = new ModOptRef();
        modOptRef.setModuleId(8L);
        modOptRef.setOperate(new Operate(2L, "enable"));

        mapper.save(modOptRef);

        Assert.assertEquals(new Long(1), modOptRef.getModOptId(), 20L);
    }

    @Test
    public void testUpdate() {
        ModOptRef modOptRef = new ModOptRef();
        modOptRef.setModuleId(8L);
        modOptRef.setOperate(new Operate(2L, "enable"));
        modOptRef.setModOptId(3L);

        int row = mapper.update(modOptRef);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        ModOptRef modOptRef = mapper.get(25L);

        Assert.assertNotNull(modOptRef);
        Assert.assertEquals("技术", modOptRef.getOperate().getIconCls());
    }

    @Test
    public void testFindByPage() {
        Page<ModOptRef> page = mapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE));

        Assert.assertTrue(page.getContent().size() > 0);

        for (ModOptRef entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}

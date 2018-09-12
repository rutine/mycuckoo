package com.mycuckoo.repository.platform;

import com.mycuckoo.domain.platform.ModOptRef;
import com.mycuckoo.domain.platform.ModuleMenu;
import com.mycuckoo.domain.platform.Operate;
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
    public void testFindByIds() {
        List<ModOptRef> list = mapper.findByIds(new Long[]{0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l});

        for (ModOptRef entity : list) {
            logger.info("------> findByOperateId: {}", entity);
        }
    }

    @Test
    public void testSave() {
        ModOptRef modOptRef = new ModOptRef();
        modOptRef.setModuleMemu(new ModuleMenu(8L));
        modOptRef.setOperate(new Operate(2L, "enable"));

        mapper.save(modOptRef);

        Assert.assertEquals(new Long(1), modOptRef.getModOptId(), 20L);
    }

    @Test
    public void testUpdate() {
        ModOptRef modOptRef = new ModOptRef();
        modOptRef.setModuleMemu(new ModuleMenu(8L));
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
        Assert.assertEquals("技术", modOptRef.getModuleMemu().getModImgCls());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(3L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<ModOptRef> page = mapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE));

        Assert.assertTrue(page.getContent().size() > 0);

        for (ModOptRef entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

    @Test
    public void testCount() {
        long count = mapper.count();

        logger.info("------> count: {}", count);
    }

}

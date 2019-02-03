package com.mycuckoo.repository.platform;

import com.google.common.collect.Maps;
import com.mycuckoo.domain.platform.ModuleMenu;
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
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

@SpringBootTest
public class ModuleMenuMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(ModuleMenuMapperTest.class);

    @Autowired
    private ModuleMenuMapper mapper;


    @Test
    public void testFindAll() {
        Page<ModuleMenu> page = mapper.findByPage(null, new PageRequest(0, Integer.MAX_VALUE));

        for (ModuleMenu entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

    @Test
    public void testCountByParentId() {
        long count = mapper.countByParentId(2L);

        logger.info("------> countByParentId: {}", count);
    }

    @Test
    public void testCountByModEnName() {
        long count = mapper.countByModEnName("systemConfigMgr");

        logger.info("------> countByModEnName: {}", count);
    }

    @Test
    public void testFindByParentIdAndIgnoreModuleIds() {
        List<ModuleMenu> list = mapper.findByParentIdAndIgnoreModuleIds(6, new long[]{0L, 3L});

        for (ModuleMenu entity : list) {
            logger.info("------> findByParentIdAndIgnoreModuleIds: {}", entity);
        }
    }

    @Test
    public void testUpdateRowPrivilege() {
        fail("Not yet implemented");
    }

    @Test
    public void testSave() {
        ModuleMenu moduleMenu = new ModuleMenu();
        moduleMenu.setBelongToSys("用户");
        moduleMenu.setCreateDate(Calendar.getInstance().getTime());
        moduleMenu.setCreator("rutine");
        moduleMenu.setMemo("测试");
        moduleMenu.setModEnName("en10001");
        moduleMenu.setModIconCls("no-resource");
        moduleMenu.setModLevel("3");
        moduleMenu.setModName("测试模块");
        moduleMenu.setModOrder(6);
        moduleMenu.setModPageType("jsp");
        moduleMenu.setStatus("enable");

        mapper.save(moduleMenu);

        Assert.assertEquals(new Long(1), moduleMenu.getModuleId(), 20L);
    }

    @Test
    public void testUpdate() {
        ModuleMenu moduleMenu = new ModuleMenu();
        moduleMenu.setBelongToSys("用户");
        moduleMenu.setCreateDate(Calendar.getInstance().getTime());
        moduleMenu.setCreator("rutine");
        moduleMenu.setMemo("测试");
        moduleMenu.setModEnName("en10001");
        moduleMenu.setModIconCls("no-resource");
        moduleMenu.setModLevel("3");
        moduleMenu.setModName("测试模块");
        moduleMenu.setModOrder(6);
        moduleMenu.setModPageType("jsp");
        moduleMenu.setStatus("enable");
        moduleMenu.setModuleId(25L);

        int row = mapper.update(moduleMenu);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        ModuleMenu moduleMenu = mapper.get(25L);

        Assert.assertNotNull(moduleMenu);
        Assert.assertEquals("技术", moduleMenu.getModName());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(3L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("modName", "%管理%");
        params.put("modEnId", "%Group%");
        Page<ModuleMenu> page = mapper.findByPage(params, new PageRequest(0, 10));

        for (ModuleMenu entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

    @Test
    public void testCount() {
        long count = mapper.count();

        logger.info("------> count: {}", count);
    }

}

package com.mycuckoo.repository.uum;

import com.google.common.collect.Maps;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;
import org.assertj.core.util.Arrays;
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
public class OrganMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(OrganMapperTest.class);

    @Autowired
    private OrganMapper organMapper;

    @Test
    public void testFindByParentId() {
        List<Organ> list = organMapper.findByParentId(0L);

        logger.info("------> findByParentId: {}", list);
    }

    @Test
    public void testCountByParentId() {
        int count = organMapper.countByParentId(12L);

        logger.info("------> countByParentId: {}", count);
    }

    @Test
    public void testCountByOrgSimpleName() {
        int count = organMapper.countByOrgSimpleName("销售");

        logger.info("------> countByOrgSimpleName: {}", count);
    }

    @Test
    public void testFindByParentIdAndFilterOutOrgId() {
        List<Organ> list = organMapper.findByParentIdAndFilterOutOrgId(1L, 3L);

        logger.info("------> findByParentIdAFilter: {}", list);
    }

    @Test
    public void testFindByPage2() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("filterOrgIds", new Long[]{0L, 1L, 2L, 3L, 4L, 8L, 9L, 10L});
        params.put("orgCode", "%2%");
        params.put("orgName", "%董事%");
        Page<Organ> page = organMapper.findByPage(params, new PageRequest(0, 5));

        logger.info("------> findByPage2: {}", page);
    }

    @Test
    public void testFindByOrgIds() {
        List<Organ> list = organMapper.findByOrgIds(Arrays.array(0L, 1L, 2L, 3L, 4L, 8L, 9L, 10L));

        logger.info("------> findByOrgIds: {}", list);
    }

    @Test
    public void testSave() {
        Organ organ = new Organ();
        organ.setCreateDate(Calendar.getInstance().getTime());
        organ.setMemo("测试");
        organ.setOrgCode("10010");
        organ.setOrgSimpleName("技术部");
        organ.setStatus("disable");

        organMapper.save(organ);

        Assert.assertEquals(new Long(5), organ.getOrgId(), 20L);
    }

    @Test
    public void testUpdate() {
        Organ organ = new Organ();
        organ.setCreateDate(Calendar.getInstance().getTime());
        organ.setMemo("测试");
        organ.setOrgCode("10010");
        organ.setOrgSimpleName("技术部");
        organ.setStatus("disable");
        organ.setOrgId(2L);

        int row = organMapper.update(organ);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        organMapper.delete(3L);
    }

    @Test
    public void testGet() {
        Organ organ = organMapper.get(2L);

        Assert.assertNotNull(organ);
        Assert.assertEquals("技术", organ.getOrgSimpleName());
    }

    @Test
    public void testExists() {
        boolean exists = organMapper.exists(34L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<Organ> page = organMapper.findByPage(null, new PageRequest(0, 5));

        logger.info("------> findByPage: {}", page);
    }

    @Test
    public void testCount() {
        long count = organMapper.count();

        logger.info("------> count: {}", count);
    }

}

package com.mycuckoo.repository.uum;

import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.Organ;
import com.mycuckoo.domain.uum.Role;
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

import java.util.List;

@SpringBootTest
public class OrgRoleRefMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(OrgRoleRefMapperTest.class);

    @Autowired
    private OrgRoleRefMapper mapper;

    @Test
    public void testFindRolesByOrgId() {
        List<OrgRoleRef> list = mapper.findRolesByOrgId(1L);

        Assert.assertTrue(list.size() > 0);

        for (OrgRoleRef entity : list) {
            logger.info("------> findRolesByOrgId: {}", entity);
        }
    }

    @Test
    public void testFindUnselectedRolesByOrgId() {
        Page<OrgRoleRef> page = mapper.findUnselectedRolesByOrgId(2L, new PageRequest(0, 5));

        Assert.assertTrue(page.getTotalElements() > 0);

        for (OrgRoleRef entity : page) {
            logger.info("------> findUnselectedRolesByOrgId: {}", entity);
        }
    }

    @Test
    public void testFindOrgansByRoleId() {
        List<OrgRoleRef> list = mapper.findOrgansByRoleId(1L);

        Assert.assertTrue(list.size() > 0);

        for (OrgRoleRef entity : list) {
            logger.info("------> findOrgansByRoleId: {}", entity);
        }
    }

    @Test
    public void testGetByOrgRoleId() {
        OrgRoleRef orgRoleRef = this.mapper.getByOrgRoleId(2l, 13l);

        logger.info("------> getByOrgRoleId: {}", orgRoleRef);
    }

    @Test
    public void testFindRolesByPage() {
        Page<OrgRoleRef> page = mapper.findRolesByPage(1L, null, new PageRequest(0, 5));

        for (OrgRoleRef entity : page.getContent()) {
            logger.info("------> findRolesByPage: {}", entity);
        }
    }

    @Test
    public void testFindOrgRoleIdsByRoleId() {
        List<Long> list = mapper.findOrgRoleIdsByRoleId(1L);

        logger.info("------> findOrgRoleIdsByRoleId: {}", list);
    }

    @Test
    public void testCountByOrgId() {
        int count = mapper.countByOrgId(0L);

        logger.info("------> countByOrgId: {}", count);
    }

    @Test
    public void testDeleteByRoleId() {
        this.mapper.deleteByRoleId(3l);
    }

    @Test
    public void testFindByOrgRoleIds() {
        List<OrgRoleRef> list = this.mapper.findByOrgRoleIds(Arrays.array(1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l));

        for (OrgRoleRef entity : list) {
            logger.info("------> findByOrgRoleIds: {}", entity);
        }
    }

    @Test
    public void testSave() {
        OrgRoleRef orgRoleRef = new OrgRoleRef();
        orgRoleRef.setOrgan(new Organ(3L, "enable"));
        orgRoleRef.setRole(new Role(3L, "enable"));

        mapper.save(orgRoleRef);

        Assert.assertEquals(new Long(5), orgRoleRef.getOrgRoleId(), 20L);
    }

    @Test
    public void testUpdate() {
        OrgRoleRef orgRoleRef = new OrgRoleRef();
        orgRoleRef.setOrgan(new Organ(3L, "enable"));
        orgRoleRef.setRole(new Role(3L, "enable"));
        orgRoleRef.setOrgRoleId(55L);

        int row = mapper.update(orgRoleRef);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
        mapper.delete(3L);
    }

    @Test
    public void testGet() {
        OrgRoleRef orgRoleRef = mapper.get(45L);

        Assert.assertNotNull(orgRoleRef);
        logger.info("------> get: {}", orgRoleRef.getOrgan());
        logger.info("------> get: {}", orgRoleRef.getRole());
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(55L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<OrgRoleRef> page = mapper.findByPage(null, new PageRequest(0, 5));

        for (OrgRoleRef entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

    @Test
    public void testCount() {
        long count = mapper.count();

        logger.info("------> count: {}", count);
    }

}

package com.mycuckoo.repository.uum;

import com.google.common.collect.Maps;
import com.mycuckoo.core.Querier;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.domain.uum.Role;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;

@SpringBootTest
public class RoleMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(RoleMapperTest.class);

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testCountByRoleName() {
        long count = roleMapper.countByRoleName("经理");

        logger.info("------> countByRoleName: {}", count);
    }

    @Test
    public void testSave() {
        Role role = new Role();
        role.setName("admin");
        role.setStatus("enable");
        role.setMemo("测试");
        role.setLevel((short) 3);
        role.setUpdator("1");
        role.setUpdateTime(LocalDateTime.now());
        role.setCreator("1");
        role.setCreateTime(LocalDateTime.now());

        roleMapper.save(role);

        Assert.assertEquals(new Long(5), role.getRoleId(), 20L);

    }

    @Test
    public void testUpdate() {
        Role role = new Role();
        role.setName("admin");
        role.setStatus("enable");
        role.setMemo("测试");
        role.setLevel((short) 3);
        role.setRoleId(3L);
        role.setUpdator("1");
        role.setUpdateTime(LocalDateTime.now());

        int row = roleMapper.update(role);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testGet() {
        Role role = roleMapper.get(4L);

        logger.info("------> get: {}", role);

        Assert.assertNotNull(role);
        Assert.assertEquals("技术", role.getName());
    }

    @Test
    public void testFindByPage() {
        Page<Role> page = roleMapper.findByPage(Maps.newHashMap(), new Querier(1, 5));

        for (Role role : page.getContent()) {
            logger.info("------> findByPage: {}", role);
        }
        logger.info("------> findByPage: {}", page.getTotalElements());
    }

    @Test
    public void testDelete() {
        roleMapper.delete(3L);
    }

}

package com.mycuckoo.repository.uum;

import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import com.mycuckoo.domain.uum.User;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
public class UserMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static Logger logger = LoggerFactory.getLogger(UserMapperTest.class);

    @Autowired
    private UserMapper mapper;


    @Test
    public void testFindByOrgId() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindByName() {
        List<User> list = mapper.findByName("管理");

        for (User entity : list) {
            logger.info("------> findByName: {}", entity);
        }
    }

    @Test
    public void testUpdateByProps() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindByUserName() {
        fail("Not yet implemented");
    }

    @Test
    public void testFindByPinyin() {
        List list = mapper.findByPinyin("w", 3L);

        logger.info("------> findByPinyin: {}", list);
    }

    @Test
    public void testFindByUserIds() {
        List<User> list = mapper.findByUserIds(new Long[]{12l, 13l, 14l, 15l, 16l});

        logger.info("------> findByUserIds: {}", list);
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setUpdator("1");
        user.setUpdateTime(LocalDateTime.now());
        user.setCreator("1");
        user.setCreateTime(LocalDateTime.now());
        user.setMemo("测试");
        user.setName("rutine");
        user.setStatus("enable");

        mapper.save(user);
        logger.info("------> save: {}", user);

        assertEquals(new Long(5L), user.getUserId());
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setUpdator("1");
        user.setUpdateTime(LocalDateTime.now());
        user.setCreator("1");
        user.setCreateTime(LocalDateTime.now());
        user.setMemo("测试");
        user.setName("rutine");
        user.setStatus("enable");
        user.setUserId(0L);

        Integer row = mapper.update(user);
        assertEquals(new Integer(1), row);
    }

    @Test
    public void testDelete() {
        mapper.delete(0L);
    }

    @Test
    public void testGet() {
        User user = mapper.get(0L);

        Assert.assertNotNull(user);
        assertEquals("管理员", user.getName());
        logger.debug(user.toString());

        user = mapper.get(2L);
        Assert.assertNull(user);
    }

    @Test
    public void testFindByPage() {
        Map<String, Object> params = new HashMap<>(2);
        params.put("code", "ad%");
        params.put("name", "管理%");
        Page<User> page = mapper.findByPage(params, new PageRequest(0, 10));

        for (User entity : page.getContent()) {
            logger.info("------> findByPage: {}", entity);
        }
    }

}

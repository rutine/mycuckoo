package com.mycuckoo.repository.uum;

import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.domain.uum.UserOrgRoleRef;
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

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class UserOrgRoleRefMapperTest extends AbstractTransactionalJUnit4SpringContextTests {


    private static Logger logger = LoggerFactory.getLogger(UserOrgRoleRefMapperTest.class);

    @Autowired
    private UserOrgRoleRefMapper mapper;

    @Test
    public void testFindByUserId() {
        List<UserOrgRoleRef> list = mapper.findByUserId(14L);

        Assert.assertTrue(list.size() > 0);
        logger.info("------> findByUserId: {}", list.get(0).getOrgRoleRef().getOrgan());
        logger.info("------> findByUserId: {}", list.get(0).getOrgRoleRef().getRole());
    }

    @Test
    public void testDeleteByUserId() {
        this.mapper.deleteByUserId(45l);
    }

    @Test
    public void testCountByOrgRoleId() {
        int count = this.mapper.countByOrgRoleId(1l);

        logger.info("------> countByOrgRoleId: {}", count);
    }

    @Test
    public void testCountByRoleId() {
        int count = this.mapper.countByRoleId(1l);

        logger.info("------> countByRoleId: {}", count);
    }

    @Test
    public void testFindByUserIdAndOrgRoleId() {
        UserOrgRoleRef roleUserRef = this.mapper.findByUserIdAndOrgRoleId(0l, 1l);

        logger.info("------> findByUserIdAndOrgRoleId: {}", roleUserRef);
    }

    @Test
    public void testFindByOrgRoleId() {
        List<UserOrgRoleRef> roleUserRefs = this.mapper.findByOrgRoleId(11l, 4l);

        for (UserOrgRoleRef entity : roleUserRefs) {
            logger.info("------> findByOrgRoleId: {}", entity);
        }
    }

    @Test
    public void testSave() {
        UserOrgRoleRef roleUserRef = new UserOrgRoleRef();
        roleUserRef.setUser(new User(3L, "enable"));
        roleUserRef.setOrgRoleRef(new OrgRoleRef(3L));
        roleUserRef.setIsDefault("t");

        mapper.save(roleUserRef);
        logger.info("------> save: {}", roleUserRef);

        assertEquals(new Long(5L), roleUserRef.getUserOrgRoleId());
    }

    @Test
    public void testUpdate() {
        UserOrgRoleRef roleUserRef = new UserOrgRoleRef();
        roleUserRef.setUser(new User(3L, "enable"));
        roleUserRef.setOrgRoleRef(new OrgRoleRef(3L));
        roleUserRef.setIsDefault("true");
        roleUserRef.setUserOrgRoleId(3L);

        Integer row = mapper.update(roleUserRef);
        assertEquals(new Integer(1), row);
    }

    @Test
    public void testDelete() {
        mapper.delete(0L);
    }

    @Test
    public void testGet() {
        UserOrgRoleRef roleUserRef = mapper.get(0L);

        Assert.assertNotNull(roleUserRef);
        assertEquals(new Long(1L), roleUserRef.getOrgRoleRef().getOrgRoleId());
        logger.info("------> get: {}", roleUserRef);
    }

    @Test
    public void testExists() {
        boolean exists = mapper.exists(3L);

        assertEquals(Boolean.FALSE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<UserOrgRoleRef> page = mapper.findByPage(null, new PageRequest(0, 5));

        Assert.assertNotNull(page.getContent().get(0).getOrgRoleRef().getOrgRoleId());

        for (UserOrgRoleRef entity : page.getContent()) {
            logger.info("------> findByPage: orgRoleId: {} userId: {}",
                    entity.getOrgRoleRef().getOrgRoleId(), entity.getUser().getUserId());
        }
    }

    @Test
    public void testCount() {
        long count = mapper.count();

        logger.info("------> count: {}", count);
    }

}

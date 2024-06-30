package com.mycuckoo.repository.uum;

import com.mycuckoo.constant.enums.OwnerType;
import com.mycuckoo.constant.enums.PrivilegeScope;
import com.mycuckoo.constant.enums.PrivilegeType;
import com.mycuckoo.domain.uum.Privilege;
import com.mycuckoo.core.repository.Page;
import com.mycuckoo.core.repository.PageRequest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

@SpringBootTest
public class PrivilegeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static Logger logger = LoggerFactory.getLogger(PrivilegeMapperTest.class);

    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Test
    public void testFindByOwnIdAndPrivilegeType() {
        List<Privilege> list = privilegeMapper.findByOwnIdAndPrivilegeType(
                new Long[]{ 0L, 1L, 2L, 8L, 9L, 10L },
                new String[]{ OwnerType.ROLE.value(), OwnerType.USR.value() },
                new String[]{ PrivilegeScope.ALL.value() });

        logger.info("------> findByOwnIdAndPrivilegeType: {}", list);
    }

    @Test
    public void testCountByUserIdAndOwnerType() {
    }

    @Test
    public void testDeleteByOwnerIdAndOwnerType() {
        privilegeMapper.deleteByOwnerIdAndOwnerType(1L, OwnerType.USR.value());
    }

    @Test
    public void testDeleteByOwnerIdAndPrivilegeType() {
    }

    @Test
    public void testDeleteRowPrivilegeByDeptId() {
        privilegeMapper.deleteRowPrivilegeByDeptId("1",
                PrivilegeType.ROW.value(),
                PrivilegeScope.DEPT.value());
    }

    @Test
    public void testDeleteByModOptId() {
    }

    @Test
    public void testSave() {
//		Privilege privilege = new Privilege();
//		privilege.setOwnerType("rol");
//		privilege.setOwnerId(2L);
//		privilege.setPrivilegeScope("rol");
//		privilege.setPrivilegeType("opt");
//		
//		privilegeMapper.save(privilege);
//		
//		Assert.assertEquals(new Long(1), privilege.getPrivilegeId(), 20L);
    }

    @Test
    public void testUpdate() {
        Privilege privilege = new Privilege();
        privilege.setOwnerType("rol");
        privilege.setOwnerId(2L);
        privilege.setPrivilegeScope("rol");
        privilege.setPrivilegeType("opt");
        privilege.setPrivilegeId(3L);

        int row = privilegeMapper.update(privilege);

        Assert.assertEquals(1, row);
    }

    @Test
    public void testDelete() {
//		privilegeMapper.delete(3L);
    }

    @Test
    public void testGet() {
        Privilege privilege = privilegeMapper.get(2L);

        Assert.assertNotNull(privilege);
        Assert.assertEquals("技术", privilege.getOwnerType());
    }

    @Test
    public void testExists() {
        boolean exists = privilegeMapper.exists(34L);

        Assert.assertEquals(Boolean.TRUE, exists);
    }

    @Test
    public void testFindByPage() {
        Page<Privilege> page = privilegeMapper.findByPage(null, new PageRequest(0, 5));

        logger.info("------> findByPage: {}", page);
    }

}

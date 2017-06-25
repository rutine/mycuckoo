package com.mycuckoo.repository.uum;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.common.constant.Common;
import com.mycuckoo.domain.uum.Privilege;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class PrivilegeMapperTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static Logger logger = LoggerFactory.getLogger(PrivilegeMapperTest.class);

	@Autowired
	private PrivilegeMapper privilegeMapper;

	@Test
	public void testFindByOwnIdAndPrivilegeType() {
		List<Privilege> list = (List<Privilege>) privilegeMapper.findByOwnIdAndPrivilegeType(new Long[]{0L, 1L, 2L, 8L, 9L, 10L}, 
					new String[]{Common.OWNER_TYPE_ROL, Common.OWNER_TYPE_USR}, new String[]{Common.PRIVILEGE_SCOPE_ALL});
			
		logger.info("------> findByOwnIdAndPrivilegeType: {}", list);
	}

	@Test
	public void testCountByUserIdAndOwnerType() {}

	@Test
	public void testDeleteByOwnerIdAndOwnerType() {
		privilegeMapper.deleteByOwnerIdAndOwnerType(1L, Common.OWNER_TYPE_USR);
	}

	@Test
	public void testDeleteByOwnerIdAndPrivilegeType() {}

	@Test
	public void testDeleteRowPrivilegeByOrgId() {}

	@Test
	public void testDeleteByModOptId() {}

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

	@Test
	public void testCount() {
		long count = privilegeMapper.count();
		
		logger.info("------> count: {}", count);
	}

}

package com.mycuckoo.persistence.uum;

import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.common.constant.Common;
import com.mycuckoo.domain.uum.UumPrivilege;
import com.mycuckoo.persistence.iface.uum.UumPrivilegeRepository;

/**
 * 功能说明: UumPrivilegeRepositoryTest的集成测试用例, 测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:29:04 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumPrivilegeRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumPrivilegeRepository uumPrivilegeRepository;
	
//	@Test
	public void saveUumPrivilege() {
		UumPrivilege uumPrivilege = new UumPrivilege();
		uumPrivilege.setOwnerType("rol");
		uumPrivilege.setOwnerId(2L);
		uumPrivilege.setPrivilegeScope("rol");
		uumPrivilege.setPrivilegeType("opt");
		
		uumPrivilegeRepository.save(uumPrivilege);
		
		Assert.assertEquals(new Long(1), uumPrivilege.getPrivilegeId(), 20L);
	}
	
//	@Test
	public void updateUumPrivilege() {
		UumPrivilege uumPrivilege = new UumPrivilege();
		uumPrivilege.setOwnerType("rol");
		uumPrivilege.setOwnerId(2L);
		uumPrivilege.setPrivilegeScope("rol");
		uumPrivilege.setPrivilegeType("opt");
		uumPrivilege.setPrivilegeId(3L);
		
		int row = uumPrivilegeRepository.update(uumPrivilege);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateUumPrivileges() {
		UumPrivilege uumPrivilege = new UumPrivilege();
		uumPrivilege.setOwnerType("rol");
		uumPrivilege.setOwnerId(2L);
		uumPrivilege.setPrivilegeScope("rol");
		uumPrivilege.setPrivilegeType("opt");
		uumPrivilege.setPrivilegeId(3L);
		
		List<UumPrivilege> list = Lists.newArrayList();
		list.add(uumPrivilege);
		uumPrivilege = new UumPrivilege();
		uumPrivilege.setOwnerType("rol");
		uumPrivilege.setOwnerId(2L);
		uumPrivilege.setPrivilegeScope("rol");
		uumPrivilege.setPrivilegeType("opt");
		uumPrivilege.setPrivilegeId(6L);
		list.add(uumPrivilege);
		
		int row = uumPrivilegeRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteUumPrivilege() {
		uumPrivilegeRepository.delete(3L);
	}
	
//	@Test
	public void existsUumPrivilege() {
		boolean exists = uumPrivilegeRepository.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countUumPrivilege() {
		long count = uumPrivilegeRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getUumPrivilege() {
		UumPrivilege uumPrivilege = uumPrivilegeRepository.get(2L);
		
		Assert.assertNotNull(uumPrivilege);
		Assert.assertEquals("技术", uumPrivilege.getOwnerType());
	}
	
//	@Test
	public void findAllUumPrivilege() {
	
		List<UumPrivilege> list = (List<UumPrivilege>)uumPrivilegeRepository.findAll();
		
		logger.debug(list.get(0).getPrivilegeScope());
	}
	
//	@Test
	public void findPrivilegesByOwnIdAPriType() {
		List<UumPrivilege> list = (List<UumPrivilege>)uumPrivilegeRepository
			.findPrivilegesByOwnIdAPriType(new Long[]{0L, 1L, 2L, 8L, 9L, 10L}, 
				new String[]{Common.OWNER_TYPE_ROL, Common.OWNER_TYPE_USR}, new String[]{Common.PRIVILEGE_SCOPE_ALL});
		logger.debug(list);
	}
	
	public void countSpecialPrivilegesByUserId() {}
	
//	@Test
	public void deletePrivilegeByOwnerIdAType() {
		uumPrivilegeRepository.deletePrivilegeByOwnerIdAType(1L, Common.OWNER_TYPE_USR);
	}
	
	public void deletePrivilegeByOwnerIdAPriType() {}
	public void deleteRowPriByResourceId() {}
	public void deletePrivilegeByModOptId() {}
}

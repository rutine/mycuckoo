package com.mycuckoo.persistence.uum;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.uum.UumRole;
import com.mycuckoo.persistence.iface.uum.UumRoleRepository;

/**
 * 功能说明: UumRoleRepositoryTest的集成测试用例,测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:30:10 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumRoleRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private UumRoleRepository uumRoleRepository;
	
//	@Test
	public void saveRole() {
		UumRole role = new UumRole();
		role.setRoleName("admin");
		role.setStatus("enable");
		role.setMemo("测试");
		role.setRoleLevel((short)3);
		
		uumRoleRepository.save(role);
		
		Assert.assertEquals(new Long(5), role.getRoleId(), 20L);
	}
	
//	@Test
	public void updateRole() {
		UumRole role = new UumRole();
		role.setRoleName("admin");
		role.setStatus("enable");
		role.setMemo("测试");
		role.setRoleLevel((short)3);
		role.setRoleId(3L);
		
		int row = uumRoleRepository.update(role);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void updateRoles() {
		UumRole role = new UumRole();
		role.setRoleName("admin");
		role.setStatus("enable");
		role.setMemo("测试");
		role.setRoleLevel((short)3);
		role.setRoleId(3L);
		
		List<UumRole> list = Lists.newArrayList();
		list.add(role);
		role.setRoleName("admin");
		role.setStatus("enable");
		role.setMemo("测试");
		role.setRoleLevel((short)3);
		role.setRoleId(4L);
		list.add(role);
		
		int row = uumRoleRepository.update(list);
		
		Assert.assertEquals(1, row);
	}
	
//	@Test
	public void deleteRole() {
		uumRoleRepository.delete(3L);
	}
	
//	@Test
	public void existsRole() {
		boolean exists = uumRoleRepository.exists(34L);
		
		Assert.assertEquals(Boolean.TRUE, exists);
	}
	
//	@Test
	public void countRole() {
		long count = uumRoleRepository.count();
		
		logger.debug(count);
	}
	
//	@Test
	public void getRole() {
		UumRole role = uumRoleRepository.get(4L);
		
		logger.debug(role);
		
		Assert.assertNotNull(role);
		Assert.assertEquals("技术", role.getRoleName());
	}
	
//	@Test
	public void findAllRole() {
	
		List<UumRole> list = (List<UumRole>)uumRoleRepository.findAllRoles();
		
		logger.debug(list);
	}
	
//	@Test
	public void findRolesByConditions() {
		Page<UumRole> page = uumRoleRepository.findRolesByCon("经理", new PageRequest(0, 10));
		
		for(UumRole role : page.getContent()) {
			logger.debug(role);
		}
		logger.debug(page.getTotalElements());
	}
	
	@Test
	public void countRolesByRoleName() {
		logger.debug(uumRoleRepository.countRolesByRoleName("经理"));
	}
}

package com.mycuckoo.repository.uum;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.uum.OrgRoleRef;
import com.mycuckoo.domain.uum.RoleUserRef;
import com.mycuckoo.domain.uum.User;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class RoleUserRefMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static Logger logger = LoggerFactory.getLogger(RoleUserRefMapperTest.class);
	
	@Autowired
	private RoleUserRefMapper mapper;

	@Test
	public void testFindByUserId() {
		List<RoleUserRef> list = mapper.findByUserId(14L);
		
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
		RoleUserRef roleUserRef = this.mapper.findByUserIdAndOrgRoleId(0l, 1l);
		
		logger.info("------> findByUserIdAndOrgRoleId: {}", roleUserRef);
	}

	@Test
	public void testFindByOrgRoleId() {
		List<RoleUserRef> roleUserRefs = this.mapper.findByOrgRoleId(11l, 4l);
		
		for(RoleUserRef entity : roleUserRefs) {
			logger.info("------> findByOrgRoleId: {}", entity);
		}
	}

	@Test
	public void testSave() {
		RoleUserRef roleUserRef = new RoleUserRef();
		roleUserRef.setUser(new User(3L, "enable"));
		roleUserRef.setOrgRoleRef(new OrgRoleRef(3L));
		roleUserRef.setIsDefault("t");
		
		mapper.save(roleUserRef);
		logger.info("------> save: {}", roleUserRef);
		
		assertEquals(new Long(5L), roleUserRef.getOrgRoleUserId());
	}

	@Test
	public void testUpdate() {
		RoleUserRef roleUserRef = new RoleUserRef();
		roleUserRef.setUser(new User(3L, "enable"));
		roleUserRef.setOrgRoleRef(new OrgRoleRef(3L));
		roleUserRef.setIsDefault("true");
		roleUserRef.setOrgRoleUserId(3L);
		
		Integer row = mapper.update(roleUserRef);
		assertEquals(new Integer(1), row);
	}

	@Test
	public void testDelete() {
		mapper.delete(0L);
	}

	@Test
	public void testGet() {
		RoleUserRef roleUserRef = mapper.get(0L);
		
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
		Page<RoleUserRef> page = mapper.findByPage(null, new PageRequest(0, 5));
		
		Assert.assertNotNull(page.getContent().get(0).getOrgRoleRef().getOrgRoleId());
		
		for(RoleUserRef entity : page.getContent()) {
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

package com.mycuckoo.persistence.uum;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.uum.UumOrgRoleRef;
import com.mycuckoo.domain.uum.UumRoleUserRef;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.persistence.iface.uum.UumRoleUserRefRepository;

/**
 * 功能说明: UumUserRepositoryTest的集成测试用例,测试Mapper映射及SQL操作.
 * 默认在每个测试函数后进行回滚.
 *
 * @author rutine
 * @time Sep 23, 2014 9:31:09 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumRoleUserRefRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumRoleUserRefRepository uumRoleUserRefRepository;

//	@Test
	public void saveUumRoleUserRef() {
		UumRoleUserRef roleUserRef = new UumRoleUserRef();
		roleUserRef.setUumUser(new UumUser(3L, "enable"));
		roleUserRef.setUumOrgRoleRef(new UumOrgRoleRef(3L));
		roleUserRef.setIsDefault("t");
		
		uumRoleUserRefRepository.save(roleUserRef);
		logger.debug(roleUserRef);
		
		assertEquals(new Long(5L), roleUserRef.getOrgRoleUserId());
	}
	
//	@Test
	public void updateUumRoleUserRef() {
		UumRoleUserRef roleUserRef = new UumRoleUserRef();
		roleUserRef.setUumUser(new UumUser(3L, "enable"));
		roleUserRef.setUumOrgRoleRef(new UumOrgRoleRef(3L));
		roleUserRef.setIsDefault("true");
		roleUserRef.setOrgRoleUserId(3L);
		
		Integer row = uumRoleUserRefRepository.update(roleUserRef);
		assertEquals(new Integer(1), row);
	
	}
	
//	@Test
	public void deleteUumRoleUserRef() {
		uumRoleUserRefRepository.delete(0L);
	}
	
//	@Test
	public void existUumRoleUserRef() {
		boolean exists = uumRoleUserRefRepository.exists(3L);
		
		assertEquals(Boolean.FALSE, exists);
	}
	
//	@Test
	public void countUumRoleUserRef() {
		long count = uumRoleUserRefRepository.count();
		
		logger.debug(count);
	}
	
//   @Test
	public void getUumRoleUserRef() {
		UumRoleUserRef roleUserRef = uumRoleUserRefRepository.get(0L);
		
		Assert.assertNotNull(roleUserRef);
		assertEquals(new Long(1L), roleUserRef.getUumOrgRoleRef().getOrgRoleId());
		logger.debug(roleUserRef.getUumUser().toString());
	}
	
//	@Test
	public void findAllUumRoleUserRef() {
		List<UumRoleUserRef> list = (List<UumRoleUserRef>)uumRoleUserRefRepository.findAll();
		
		Assert.assertNotNull(list.get(0).getUumOrgRoleRef().getOrgRoleId());
		
		for(UumRoleUserRef roleUserRef : list) {
			logger.debug("userId: " + roleUserRef.getUumOrgRoleRef().getOrgRoleId() + ", userId: " 
					+ roleUserRef.getUumUser().getUserId());
		}
	}
	
//	@Test
	public void findRoleUserRefsByUserId() {
		List<UumRoleUserRef> list = (List<UumRoleUserRef>)uumRoleUserRefRepository.findRoleUserRefsByUserId(14L);
		
		Assert.assertTrue(list.size() > 0);
		logger.debug(list.get(0).getUumOrgRoleRef().getUumOrgan());
		logger.debug(list.get(0).getUumOrgRoleRef().getUumRole());
	}
	
//	@Test
	public void deleteRoleUserRefByUserId() {
		this.uumRoleUserRefRepository.deleteRoleUserRefByUserId(45l);
	}
	
//	@Test
	public void countRoleUserRefsByOrgRoleId() {
		logger.debug(this.uumRoleUserRefRepository.countRoleUserRefsByOrgRoleId(1l));
	}
	
//	@Test
	public void countRoleUserRefsByRoleId() {
		logger.debug(this.uumRoleUserRefRepository.countRoleUserRefsByRoleId(1l));
	}
	
	@Test
	public void findRoleUserRefByUserIdAOrgRoleId() {
		UumRoleUserRef roleUserRef = this.uumRoleUserRefRepository.getRoleUserRefByUserIdAOrgRoleId(0l, 1l);
		
		logger.debug(roleUserRef.getUumUser());
	}
	
//	@Test
	public void findRoleUserRefsByOrgRoleId() {
		List<UumRoleUserRef> roleUserRefs = this.uumRoleUserRefRepository.findRoleUserRefsByOrgRoleId(11l, 4l);
		logger.debug(roleUserRefs);
	}
	
}

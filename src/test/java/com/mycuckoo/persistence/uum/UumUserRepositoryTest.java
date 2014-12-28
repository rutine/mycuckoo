package com.mycuckoo.persistence.uum;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUser;
import com.mycuckoo.persistence.iface.uum.UumUserRepository;

/**
 * 功能说明: UumUserRepositoryTest的集成测试用例,测试Mapper映射及SQL操作. 默认在每个测试函数后进行回滚.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:35:19 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumUserRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumUserRepository uumUserRepository;

	// @Test
	public void saveUumUser() {
		UumUser uumUser = new UumUser();
		uumUser.setCreateDate(Calendar.getInstance().getTime());
		uumUser.setMemo("测试");
		uumUser.setUserCode("10110");
		uumUser.setUserName("rutine");
		uumUser.setStatus("enable");

		uumUserRepository.save(uumUser);
		logger.debug(uumUser);

		assertEquals(new Long(5L), uumUser.getUserId());
	}

	// @Test
	public void updateUumUser() {
		UumUser uumUser = new UumUser();
		uumUser.setCreateDate(Calendar.getInstance().getTime());
		uumUser.setMemo("测试");
		uumUser.setUserCode("10110");
		uumUser.setUserName("rutine");
		uumUser.setStatus("enable");
		uumUser.setUserId(0L);

		Integer row = uumUserRepository.update(uumUser);
		assertEquals(new Integer(1), row);

	}

	// @Test
	public void deleteUumUser() {
		uumUserRepository.delete(0L);
	}

	// @Test
	public void existUumUser() {
		boolean exists = uumUserRepository.exists(3L);

		assertEquals(Boolean.FALSE, exists);
	}

	// @Test
	public void countUumUser() {
		long count = uumUserRepository.count();

		logger.debug(count);
	}

	// @Test
	public void getUumUser() {
		UumUser user = uumUserRepository.get(0L);

		Assert.assertNotNull(user);
		assertEquals("管理员", user.getUserName());
		logger.debug(user.toString());

		user = uumUserRepository.get(2L);
		Assert.assertNull(user);
	}

	// @Test
	public void findAllUumUser() {
		List<UumUser> list = (List<UumUser>) uumUserRepository.findAll();

		logger.debug(list.get(0).getUumOrgan().getOrgId());

		Assert.assertNotNull(list.get(0).getUumOrgan());
	}

	// @Test
	public void findByPage() {
		Pageable pageable = new PageRequest(0, 5);
		Page<UumUser> page = uumUserRepository.findAll(pageable);

		for (UumUser user : page.getContent()) {
			logger.debug(user);
		}
	}

	// @Test
	@SuppressWarnings("rawtypes")
	public void findByProperties() {

		UumUser uumUser = new UumUser();
		uumUser.setCreateDate(Calendar.getInstance().getTime());
		uumUser.setMemo("测试");
		uumUser.setUserCode("10110");
		uumUser.setUserName("%管理%");
		uumUser.setStatus("enable");
		uumUser.setUserId(0L);

		SqlSessionDaoSupport dao = applicationContext.getBean(SqlSessionDaoSupport.class);

		List list = dao.getSqlSession().selectList("com.mycuckoo.persistence.uum.UumUserMapper.findByProperties", uumUser);

		logger.debug(list.size());
	}

	// @Test
	public void findByCodeAndeName() {
		List<UumUser> list = uumUserRepository.findUsersByCodeAndName("ad", "管理");
		logger.debug(list);
	}

	// @Test
	public void findByConditions() {
		Page<UumUser> page = uumUserRepository.findUsersByCon("1_1", 
				new Long[] { 0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l }, "ad", "管理", new PageRequest(0, 10));
		logger.debug(page.getTotalPages());
	}

	// @Test
	public void existsUserByUserCode() {
		boolean exists = uumUserRepository.existsUserByUserCode("admis");
		logger.debug(exists);
	}

	public void findUsersByUserCodeAPwd() { }

	public void modifyUserBelongOrgIdAssignRole() { }

	public void resetPwdByUserId() { }

	public void modifyUserPhotoUrlToEmpty() { }

	public void findUsersByUserName() { }

	// @Test
	@SuppressWarnings("rawtypes")
	public void findUsersByUserNamePy() {
		List list = uumUserRepository.findUsersByUserNamePy("w", 3L);

		logger.debug(list);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void findUsersByUserIds() {
		List list = uumUserRepository.findUsersByUserIds(new Long[] { 12l, 13l, 14l, 15l, 16l });

		logger.debug(list);
	}

}

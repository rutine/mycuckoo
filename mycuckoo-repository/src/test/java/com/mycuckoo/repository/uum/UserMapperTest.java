package com.mycuckoo.repository.uum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.uum.User;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

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
	public void testFindByCodeAndName() {
		List<User> list = mapper.findByCodeAndName("ad", "管理");
		
		for(User entity : list) {
			logger.info("------> findByCodeAndName: {}", entity);
		}
	}

	@Test
	public void testFindByPage2() {
		Page<User> page = mapper.findByPage2(
				null,
				Arrays.array(0l, 1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l, 10l), 
				"ad", "管理", new PageRequest(0, 10));
		
		for(User entity : page.getContent()) {
			logger.info("------> findByPage2: {}", entity);
		}
	}

	@Test
	public void testExistsByUserCode() {
		boolean exists = mapper.existsByUserCode("admis");
		
		logger.info("------> existsByUserCode: {}", exists);
	}

	@Test
	public void testGetByUserCodeAndPwd() {
		fail("Not yet implemented");
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
	public void testFindByUserNamePy() {
		List list = mapper.findByUserNamePy("w", 3L);

		logger.info("------> findByUserNamePy: {}", list);
	}

	@Test
	public void testFindByUserIds() {
		List list = mapper.findByUserIds(new Long[] { 12l, 13l, 14l, 15l, 16l });

		logger.info("------> findByUserIds: {}", list);
	}

	@Test
	public void testSave() {
		User user = new User();
		user.setCreateDate(Calendar.getInstance().getTime());
		user.setMemo("测试");
		user.setUserCode("10110");
		user.setUserName("rutine");
		user.setStatus("enable");

		mapper.save(user);
		logger.info("------> save: {}", user);

		assertEquals(new Long(5L), user.getUserId());
	}

	@Test
	public void testUpdate() {
		User user = new User();
		user.setCreateDate(Calendar.getInstance().getTime());
		user.setMemo("测试");
		user.setUserCode("10110");
		user.setUserName("rutine");
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
		assertEquals("管理员", user.getUserName());
		logger.debug(user.toString());

		user = mapper.get(2L);
		Assert.assertNull(user);
	}

	@Test
	public void testExists() {
		boolean exists = mapper.exists(3L);

		assertEquals(Boolean.FALSE, exists);
	}

	@Test
	public void testFindByPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testCount() {
		long count = mapper.count();

		logger.info("------> count: {}", count);
	}

}

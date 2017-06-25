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

import com.mycuckoo.domain.uum.UserCommfun;
import com.mycuckoo.repository.Page;
import com.mycuckoo.repository.PageRequest;

@SpringBootTest
public class UserCommFunMapperTest extends AbstractTransactionalJUnit4SpringContextTests {
	private static Logger logger = LoggerFactory.getLogger(UserCommFunMapperTest.class);
	
	@Autowired
	private UserCommFunMapper mapper;
	

	@Test
	public void testSave() {
		UserCommfun userCommfunId = new UserCommfun(2l, 3l);

		mapper.save(userCommfunId);
	}

	@Test
	public void testUpdate() {
		UserCommfun newUserCommfunId = new UserCommfun(2l, 3l);
		UserCommfun oldUserCommfunId = new UserCommfun(5l, 3l);

		Integer row = mapper.update(newUserCommfunId, oldUserCommfunId);
		assertEquals(new Integer(1), row);
	}

	@Test
	public void testDelete() {
		UserCommfun userCommfunId = new UserCommfun(2l, 3l);
		mapper.delete(userCommfunId);
	}

	@Test
	public void testGet() {
		UserCommfun userCommfunId = new UserCommfun(2l, 3l);

		Assert.assertNotNull(userCommfunId);

		UserCommfun userCommfun = mapper.get(userCommfunId);
		Assert.assertNull(userCommfun);
	}

	@Test
	public void testExists() {
		UserCommfun userCommfunId = new UserCommfun(2l, 3l);

		boolean exists = mapper.exists(userCommfunId);

		assertEquals(Boolean.FALSE, exists);
	}

	@Test
	public void testFindByPage() {
		Page<UserCommfun> page = mapper.findByPage(new PageRequest(0, 5));

		Assert.assertNotNull(page);

		for (UserCommfun entity : page.getContent()) {
			logger.info("------> findByPage: {}", entity);
		}
	}

	@Test
	public void testCount() {
		long count = mapper.count();

		logger.info("------> count: {}", count);
	}

	@Test
	public void testDeleteByUserId() {
		mapper.deleteByUserId(3l);
	}

	@Test
	public void testFindByUserId() {
		List<UserCommfun> list = mapper.findByUserId(1l);

		assertEquals(3, list.size());
	}

}

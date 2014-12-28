package com.mycuckoo.persistence.uum;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.Pageable;
import com.mycuckoo.domain.uum.UumUserCommfun;
import com.mycuckoo.persistence.iface.uum.UumUserCommFunRepository;

/**
 * 功能说明: UumUserCommfunRepositoryTest的集成测试用例,测试Mapper映射及SQL操作. 默认在每个测试函数后进行回滚.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:34:22 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class UumUserCommFunRepositoryTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UumUserCommFunRepository uumUserCommfunRepository;

	// @Test
	public void saveUumUserCommfun() {
		UumUserCommfun userCommfunId = new UumUserCommfun(2l, 3l);

		uumUserCommfunRepository.save(userCommfunId);
	}

	// @Test
	public void updateUumUserCommfun() {
		UumUserCommfun newUserCommfunId = new UumUserCommfun(2l, 3l);
		UumUserCommfun oldUserCommfunId = new UumUserCommfun(5l, 3l);

		Integer row = uumUserCommfunRepository.update(newUserCommfunId, oldUserCommfunId);
		assertEquals(new Integer(1), row);

	}

	// @Test
	public void getUumUserCommfun() {
		UumUserCommfun userCommfunId = new UumUserCommfun(2l, 3l);

		Assert.assertNotNull(userCommfunId);

		UumUserCommfun userCommfun = uumUserCommfunRepository.get(userCommfunId);
		Assert.assertNull(userCommfun);
	}

	// @Test
	public void existUumUserCommfun() {
		UumUserCommfun userCommfunId = new UumUserCommfun(2l, 3l);

		boolean exists = uumUserCommfunRepository.exists(userCommfunId);

		assertEquals(Boolean.FALSE, exists);
	}

	@Test
	public void findAllUumUserCommfun() {
		List<UumUserCommfun> list = (List<UumUserCommfun>) uumUserCommfunRepository.findAll();

		Assert.assertNotNull(list);

		for (UumUserCommfun userCommfun : list) {
			logger.debug(userCommfun.getUserId() + ", " + userCommfun.getModuleId());
		}
	}

	// @Test
	public void findByPage() {
		Pageable pageable = new PageRequest(0, 5);
		Page<UumUserCommfun> page = uumUserCommfunRepository.findAll(pageable);

		for (UumUserCommfun userCommfun : page.getContent()) {
			logger.debug(userCommfun);
		}
	}

	// @Test
	public void countUumUserCommfun() {
		long count = uumUserCommfunRepository.count();

		logger.debug(count);
	}

	// @Test
	public void deleteUumUserCommfun() {
		UumUserCommfun userCommfunId = new UumUserCommfun(2l, 3l);
		uumUserCommfunRepository.delete(userCommfunId);
	}

	// @Test
	public void deleteUserCommFun() {
		uumUserCommfunRepository.deleteUserCommFun(3l);
	}

	@Test
	public void findUserCommFunByUserId() {
		List<UumUserCommfun> list = uumUserCommfunRepository.findUserCommFunByUserId(1l);

		assertEquals(3, list.size());
	}

}

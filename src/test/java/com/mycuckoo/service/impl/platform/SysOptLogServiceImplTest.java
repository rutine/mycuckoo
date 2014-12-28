package com.mycuckoo.service.impl.platform;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.platform.SysplSysOptLog;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.persistence.iface.platform.SysplSysOptLogRepository;

// 在测试与其它系统有交互的功能块，并且其它系统的稳定性未知的情况下，Mock就得派上用场了，否则测试起来极其吃力，并且效率低下。
/**
 * 功能说明: SysOptLogServiceImpl的测试用例, 测试Service层的业务逻辑.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:39:57 PM
 * @version 2.0.0
 */
public class SysOptLogServiceImplTest {

	private SysOptLogServiceImpl sysOptLogServiceImpl;

	// 指定Mock的对象
	@Mock
	private SysplSysOptLogRepository sysplSysOptLogRepository;

	/**
	 * 功能说明 :
	 * 
	 * @throws java.lang.Exception
	 * @author rutine
	 * @time Oct 7, 2012 3:46:38 PM
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this); // 声明测试用例类

		sysOptLogServiceImpl = new SysOptLogServiceImpl();
		sysOptLogServiceImpl.setSysplSysOptLogRepository(sysplSysOptLogRepository);
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.SysOptLogServiceImpl#deleteLog(int)}
	 * .
	 */
	@Test
	public void testDeleteLog() {
		sysOptLogServiceImpl.deleteLog(30);
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.SysOptLogServiceImpl#findOptLogsByCon(com.mycuckoo.domain.platform.SysplSysOptLog, com.mycuckoo.domain.Pageable)}
	 * .
	 */
	@Test
	public void testFindLogByCond() {
		Page<SysplSysOptLog> page = sysOptLogServiceImpl.findOptLogsByCon(null, null);

		Assert.assertNull(page);
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.SysOptLogServiceImpl#getOptLogContentById(long)}
	 * .
	 */
	@Test
	public void testFindLogContentById() {
		sysOptLogServiceImpl.getOptLogContentById(0);
	}

	@Test
	public void testSaveLog() {
		try {
			sysOptLogServiceImpl.saveLog(null, null, null, null, null, null);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

}

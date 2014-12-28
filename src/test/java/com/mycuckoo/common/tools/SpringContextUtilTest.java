package com.mycuckoo.common.tools;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.mycuckoo.common.utils.SpringContextUtils;

/**
 * 功能说明: SpringContextUtilTest的测试用例. 测试该类注册为spring bean是否成功
 * 
 * @author rutine
 * @time Sep 23, 2014 9:04:53 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class SpringContextUtilTest extends AbstractJUnit4SpringContextTests {

	/**
	 * 功能说明 :
	 * 
	 * @throws java.lang.Exception
	 * @author rutine
	 * @time Oct 31, 2012 10:45:41 PM
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		boolean flag = SpringContextUtils.containsBean("sysOptLogServiceImpl");
		logger.debug(flag);
	}

}

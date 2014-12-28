package com.mycuckoo.aop;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 功能说明: TODO(这里用一句话描述这个类的作用)
 * 
 * @author rutine
 * @time Sep 23, 2014 8:58:45 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class PointCutTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void test() {
		BusinessTest test = this.applicationContext.getBean(BusinessTest.class);
		test.rutine();
//		new BusinessTest().rutine();
	}
}

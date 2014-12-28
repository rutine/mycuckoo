package com.mycuckoo.service.platform.mainframe;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.mycuckoo.domain.Page;
import com.mycuckoo.domain.PageRequest;
import com.mycuckoo.domain.vo.MainFrameFun;
import com.mycuckoo.exception.ApplicationException;
import com.mycuckoo.service.iface.platform.mainframe.MainFrameFunService;

/**
 * 功能说明: MainFrameFunServiceImpl的测试用例, 测试Service层的业务逻辑.
 * 
 * @author rutine
 * @time Sep 23, 2014 9:40:39 PM
 * @version 2.0.0
 */
@ContextConfiguration(locations = { "/applicationContext.xml" })
@ActiveProfiles("production")
public class MainFrameFunServiceImplTest extends
		AbstractJUnit4SpringContextTests {

	@Autowired
	private MainFrameFunService mainFrameFunService;

	// 指定Mock的对象
	@Mock
	private HttpServletRequest request;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this); // 声明测试用例类
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.mainframe.MainFrameFunServiceImpl#deleteMainFrameFun(java.lang.String[])}
	 * .
	 */
	// @Test
	public void testDeleteMainFrameFun() {
		try {
			mainFrameFunService.deleteMainFrameFun(new String[] { "1" }, request);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.mainframe.MainFrameFunServiceImpl#findAllMainFrameFun()}
	 * .
	 */
	// @Test
	public void testFindAllMainFrameFun() {
		try {
			Map<String, List<MainFrameFun>> map = mainFrameFunService.findAllMainFrameFun();
			Assert.assertEquals("h", map.get("header").get(0).getFunURIDesc());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.mainframe.MainFrameFunServiceImpl#findMainFrameFunByConditions(java.lang.String, com.mycuckoo.domain.Pageable)}
	 * .
	 */
	// @Test
	public void testFindMainFrameFunByConditions() {
		try {
			Page<MainFrameFun> page = mainFrameFunService.findMainFrameFunByCond("head", new PageRequest(0, 5));
			Assert.assertEquals(2, page.getContent().size());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.mainframe.MainFrameFunServiceImpl#findMainFrameFunByFunId(java.lang.String)}
	 * .
	 */
	@Test
	public void testFindMainFrameFunByFunId() {
		try {
			MainFrameFun mainFrameFun = mainFrameFunService.findMainFrameFunByFunId("33");
			Assert.assertNotNull(mainFrameFun);
			Assert.assertEquals("", mainFrameFun.getFunURIDesc());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.mainframe.MainFrameFunServiceImpl#findMainFrameFunByFunName(java.lang.String)}
	 * .
	 */
	@Test
	public void testFindMainFrameFunByFunName() {
		try {
			MainFrameFun mainFrameFun = mainFrameFunService.findMainFrameFunByFunName("navigate");
			Assert.assertNotNull(mainFrameFun);
			Assert.assertEquals("", mainFrameFun.getFunURIDesc());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.mainframe.MainFrameFunServiceImpl#modifyMainFrameFun()}
	 * .
	 */
	@Test
	public void testModifyMainFrameFun() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveMainFrameFun() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.mycuckoo.service.impl.platform.mainframe.MainFrameFunServiceImpl#main(java.lang.String[])}
	 * .
	 */
	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

}

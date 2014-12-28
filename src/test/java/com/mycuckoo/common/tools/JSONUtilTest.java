package com.mycuckoo.common.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mycuckoo.common.utils.JsonUtils;
import com.mycuckoo.domain.uum.UumUser;

/**
 * 功能说明: json 工具单元测试类
 *
 * @author rutine
 * @time Sep 23, 2014 9:02:22 PM
 * @version 2.0.0
 */
public class JSONUtilTest {

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#formatBytableCols(java.util.List, java.lang.String[])}.
	 */
	@Test
	public void testFormatBytableColsListOfObjectStringArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#formatBytableCols(java.lang.Object[], java.lang.String[])}.
	 */
	@Test
	public void testFormatBytableColsObjectArrayStringArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFromJsonToDateArray() {
		String str = "[\"2012-10-01\", \"2012-10-02\", \"2012-10-03\", \"2012-10-04\", \"2012-10-05\"]";
		Date[] dateArray = JsonUtils.fromJson(str, Date[].class);
		
		assertEquals("2012-10-01", new SimpleDateFormat("yyyy-MM-dd").format(dateArray[0]));
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFromJsonToDoubleArray() {
		String str = "[1, 2, 3, 4, 5]";
		Double[] doubleArray = JsonUtils.fromJson(str, Double[].class);
	
		assertEquals(new Double(1), doubleArray[0]);
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFromJsonToIntegerArray() {
		String str = "[1, 2, 3, 4, 5]";
		Integer[] integerArray = JsonUtils.fromJson(str, Integer[].class);
	
		assertEquals(new Integer(1), integerArray[0]);
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFromJsonToList() {
		String str = "[{\"userCode\": \"1000\", \"userName\" : \"rutine\"}, {\"userCode\": \"1001\", \"userName\" : \"rutine01\"}, {\"userCode\": \"1002\", \"userName\" : \"rutine02\"}]";
		List<UumUser> list = JsonUtils.fromJson(str, new TypeReference<List<UumUser>>() { });
		
		assertEquals(3, list.size());
		assertEquals("1002", list.get(2).getUserCode());
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFromJsonToList2() {
		String str = "[{\"userCode\": \"1000\", \"userName\" : \"rutine\"}, {\"userCode\": \"1001\", \"userName\" : \"rutine01\"}, {\"userCode\": \"1002\", \"userName\" : \"rutine02\"}]";
		List<UumUser> list = JsonUtils.fromJson(str, new TypeReference<List<UumUser>>() { });
		
		assertEquals(3, list.size());
		assertEquals("1002", list.get(2).getUserCode());
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFromJsonToLongArray() {
		String str = "[1, 2, 3, 4, 5]";
		Long[] longArray = JsonUtils.fromJson(str, Long[].class);
	
		assertEquals(new Long(1), longArray[0]);
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils.fromJson(java.lang.String, java.util.Map)}.
	 */
	@Test
	public void testFromJsonToMap() {
		String str = "{\"userCode\": \"1000\", \"userName\" : \"rutine\"}";
		
		Map map = JsonUtils.fromJson(str, Map.class);
		
		assertEquals(2, map.size());
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFromJsonToObject() {
		String str = "{\"userCode\": \"1000\", \"userName\" : \"rutine\"}";
		UumUser user = (UumUser) JsonUtils.fromJson(str, UumUser.class);
		
		assertEquals("1000", user.getUserCode());
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFromJsonToObjectArray() {
		String str = "[{\"userCode\": \"1000\", \"userName\" : \"rutine\"}, {\"userCode\": \"1001\", \"userName\" : \"rutine01\"}, {\"userCode\": \"1002\", \"userName\" : \"rutine02\"}]";
		Object[] array = JsonUtils.fromJson(str, Object[].class);
		
		assertEquals(3, array.length);
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#fromJson(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFromJsonToStringArray() {
		String str = "[{\"userCode\": \"1000\", \"userName\" : \"rutine\"}, {\"userCode\": \"1001\", \"userName\" : \"rutine1\"}]";
		String[] strArray = JsonUtils.fromJson(str, String[].class);
		
		assertEquals("{userCode=1000, userName=rutine}", strArray[0]);
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#toJson(java.lang.Object)}.
	 */
	@Test
	public void testToJson() {
		UumUser user = new UumUser();
		user.setUserCode("1000");
		user.setUserName("rutinje");
		user.setCreateDate(Calendar.getInstance().getTime());
		
		String jsonStr = JsonUtils.toJson(user);
		
		assertNotNull(jsonStr);
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#toJson(java.util.List, java.lang.Class)}.
	 */
	@Test
	public void testToJsonFromList() {
		List<UumUser> list = new ArrayList<UumUser>(2);
		UumUser user = new UumUser();
		user.setUserCode("1000");
		user.setUserName("rutine");
		list.add(user);
		user = new UumUser();
		user.setUserCode("1001");
		user.setUserName("rutine01");
		list.add(user);
		
		String jsonStr = JsonUtils.toJson(list, UumUser.class);
		assertNotNull(jsonStr);
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#toJson(java.util.List, java.lang.Class, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testToJsonFromList2() {
		List<UumUser> list = new ArrayList<UumUser>(2);
		UumUser user = new UumUser();
		user.setUserCode("1000");
		user.setUserName("rutine");
		list.add(user);
		user = new UumUser();
		user.setUserCode("1001");
		user.setUserName("rutine01");
		list.add(user);
		
		String jsonStr = JsonUtils.toJson(list, UumUser.class, "userCode", "userName");
		assertEquals("[[1000,'rutine'],[1001,'rutine01']] ", jsonStr);
	}

	/**
	 * Test method for {@link com.mycuckoo.common.utils.JsonUtils#toJson(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testToJsonWithDateFormat() {
		UumUser user = new UumUser();
		user.setUserCode("1000");
		user.setUserName("rutinje");
		user.setCreateDate(Calendar.getInstance().getTime());
		
		String jsonStr = JsonUtils.toJson(user, "yyyy-MM-dd");
		
		assertNotNull(jsonStr);
	}

}

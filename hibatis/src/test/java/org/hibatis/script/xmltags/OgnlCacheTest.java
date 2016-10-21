package org.hibatis.script.xmltags;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
* @author pan
* @since Oct 20, 2016 4:23:18 PM
* @desc
*/
public class OgnlCacheTest
{
	@Test
	public void expressiontest()
	{
		Map <String, Object> map = new HashMap <String, Object>();
		map.put("username", "1");
		System.out.println(OgnlCache.getValue("username == 1", map));
	}
}


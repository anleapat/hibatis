package org.hibatis.script.xmltags;

import java.util.Map;

import org.hibatis.builder.BuilderException;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * @author pan
 * @since Oct 20, 2016 4:15:40 PM
 * @desc
 */
public class OgnlCache
{
	private OgnlCache()
	{
	}

	public static boolean getValue(String expression, Map <String, Object> parameter)
	{
		try
		{
			Object expr = Ognl.parseExpression(expression);
			boolean b = (Boolean)Ognl.getValue(expr, parameter);
			return b;
		}
		catch (OgnlException e)
		{
			throw new BuilderException(e.getMessage(), e);
		}
	}
}

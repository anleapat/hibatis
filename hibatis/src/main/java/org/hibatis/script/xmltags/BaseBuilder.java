package org.hibatis.script.xmltags;

import org.hibatis.script.sqls.SqlSource;

/**
* @author pan
* @since Oct 20, 2016 12:55:56 PM
* @desc
*/
public abstract class BaseBuilder
{
	boolean isDynamic;
	boolean isRaw;
	
	public abstract SqlSource parseResource(String resource);
}


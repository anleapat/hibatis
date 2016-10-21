package org.hibatis.script.sqls;

import java.util.List;

import org.hibatis.script.xmltags.SqlNode;

/**
* @author pan
* @since Oct 20, 2016 11:03:36 AM
* @desc
*/
public class StaticSqlSource extends SqlSource
{
	public StaticSqlSource()
	{
	}

	public StaticSqlSource(List <SqlNode> contexts, SqlTypeEnum sqlType)
	{
		this.contexts = contexts;
		this.sqlType = sqlType;
	}
}


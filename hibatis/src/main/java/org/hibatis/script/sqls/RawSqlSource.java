package org.hibatis.script.sqls;

import java.util.List;
import java.util.Map;

import org.hibatis.script.xmltags.SqlNode;

/**
 * @author pan
 * @since Oct 20, 2016 11:03:36 AM
 * @desc
 */
public class RawSqlSource extends SqlSource
{
	private Map <String, Object> parameter;

	public Map <String, Object> getParameter()
	{
		return parameter;
	}

	public void setParameter(Map <String, Object> parameter)
	{
		this.parameter = parameter;
	}

	public RawSqlSource()
	{

	}

	public RawSqlSource(List <SqlNode> contexts, SqlTypeEnum sqlType)
	{
		this.contexts = contexts;
		this.sqlType = sqlType;
	}

	public RawSqlSource(List <SqlNode> contexts, SqlTypeEnum sqlType, Map <String, Object> parameter)
	{
		this(contexts, sqlType);
		this.parameter = parameter;
	}
}

package org.hibatis.script.sqls;

import java.util.List;

import org.hibatis.script.xmltags.SqlNode;

/**
* @author pan
* @since Oct 20, 2016 11:03:07 AM
* @desc
*/
public abstract class SqlSource
{
	
	List<SqlNode> contexts;
	
	SqlTypeEnum sqlType;

	public List <SqlNode> getContexts()
	{
		return contexts;
	}

	public void setContexts(List <SqlNode> contexts)
	{
		this.contexts = contexts;
	}

	public SqlTypeEnum getSqlType()
	{
		return sqlType;
	}

	public void setSqlType(SqlTypeEnum sqlType)
	{
		this.sqlType = sqlType;
	}

}


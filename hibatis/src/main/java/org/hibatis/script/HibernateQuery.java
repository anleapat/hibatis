package org.hibatis.script;

import java.util.Map;

import org.hibatis.script.sqls.SqlTypeEnum;

/**
 * @author pan
 * @since Oct 20, 2016 4:47:06 PM
 * @desc
 */
public class HibernateQuery
{
	private String sql;
	private Map <String, Object> parameter;
	private SqlTypeEnum sqlType;

	public HibernateQuery()
	{
	}

	public HibernateQuery(String sql, Map <String, Object> parameter, SqlTypeEnum sqlType)
	{
		super();
		this.sql = sql;
		this.parameter = parameter;
		this.sqlType = sqlType;
	}

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public Map <String, Object> getParameter()
	{
		return parameter;
	}

	public void setParameter(Map <String, Object> parameter)
	{
		this.parameter = parameter;
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

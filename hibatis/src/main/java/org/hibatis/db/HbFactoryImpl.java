package org.hibatis.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibatis.builder.BuilderException;
import org.hibatis.script.HibernateQuery;
import org.hibatis.script.ParsingHibernateSql;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pan
 * @since Oct 20, 2016 4:39:00 PM
 * @desc
 */
@Transactional
public class HbFactoryImpl implements HbFactory
{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    public HbFactoryImpl()
	{
	}

	public HbFactoryImpl(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
		this.session = sessionFactory.openSession();
	}

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	private Session session;

	public Session getSession()
	{
		if (this.session == null)
		{
			return sessionFactory.getCurrentSession();
		}
		return session;
	}

	public <T> List <T> select(T t)
	{
		Query query = prepareQuery(t);
		List <T> reslist = query.list();
		return reslist;
	}

	public <T> List <T> selectForUpdate(T t)
	{
		Query query = prepareQuery(t);
		query.setLockOptions(LockOptions.UPGRADE);
		List <T> reslist = query.list();
		return reslist;
	}

	public <T> T selectOne(T t)
	{
		List <T> list = select(t);
		T t1 = null;
		if (list != null && list.size() != 0)
		{
			t1 = list.get(0);
		}
		return t1;
	}

	public <T> T selectOneByHql(String selectId, Map <String, Object> parameter)
	{
		Query query = prepareHqlQuery(selectId, parameter);
		T t = null;
		List <T> list = query.list();
		if (list != null && list.size() != 0)
		{
			t = list.get(0);
		}
		return t;
	}

	public <T> List <T> selectByHql(String selectId, Map <String, Object> parameter)
	{
		Query query = prepareHqlQuery(selectId, parameter);
		List <T> list = query.list();
		return list;
	}

	public <T> List <T> selectForUpdateByHql(String selectId, Map <String, Object> parameter)
	{
		Query query = prepareHqlQuery(selectId, parameter);
		query.setLockOptions(LockOptions.UPGRADE);
		List <T> list = query.list();
		return list;
	}

	public <T> List <T> selectByHql(String selectId, Map <String, Object> parameter, int offset, int limit)
	{
		Query query = prepareHqlQuery(selectId, parameter);
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		List <T> list = query.list();
		return list;
	}

	public int recordCountBySql(String selectId, Map <String, Object> parameter)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) AS CNT FROM (\n").append(hquery.getSql()).append("\n) SQL_COUNT_TABLE");
		Query query = getSession().createSQLQuery(sql.toString());
		Map <String, Object> paraMap = hquery.getParameter();
		
		//prepare parameter
		prepareParameter(query, paraMap);
		
		List list = query.list();
		Object cnt = list.get(0);
		int record = Integer.parseInt(cnt.toString());
		return record;
	}

	public int recordCountByHql(String selectId, Map <String, Object> parameter)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);

		// translate hql to sql
		String hql = hquery.getSql();
		QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
		SessionFactoryImplementor factory = (SessionFactoryImplementor) sessionFactory;
		QueryTranslator translator = translatorFactory.createFilterTranslator(hql, hql, hquery.getParameter(), factory);
		translator.compile(hquery.getParameter(), true);
		String tsql = translator.getSQLString();

		// prepare named parameter
		Map <String, Object> paraMap = hquery.getParameter();
		Object[] arry = new Object [paraMap.size()];
		if (null != paraMap && paraMap.size() > 0)
		{
			for (String key : paraMap.keySet())
			{
				int[] arr = translator.getParameterTranslations().getNamedParameterSqlLocations(key);
				arry[arr[0]] = paraMap.get(key);
			}
		}
		Map <String, Object> paramsMap = new HashMap <String, Object>();
		for (int i = 0; i < arry.length; i++)
		{
			tsql = tsql.replaceFirst("\\?", ":param" + i);
			paramsMap.put("param" + i, arry[i]);
		}

		// record count by sql
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(1) AS CNT FROM (\n").append(tsql).append("\n) HQL_COUNT_TABLE");
		Query query = getSession().createSQLQuery(sql.toString());
		
		//prepare parameter
		prepareParameter(query, paramsMap);
		
		List list = query.list();
		Object cnt = list.get(0);
		int record = Integer.parseInt(cnt.toString());
		return record;
	}

	private Query prepareHqlQuery(String selectId, Map <String, Object> parameter)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);
		Query query = getSession().createQuery(hquery.getSql());
		Map <String, Object> paraMap = hquery.getParameter();
		
		//prepare parameter
		prepareParameter(query, paraMap);
		
		return query;
	}

	public List <Map <String, Object>> select(String selectId, Map <String, Object> parameter, int offset, int limit)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);
		Query query = getSession().createSQLQuery(hquery.getSql());
		Map <String, Object> paraMap = hquery.getParameter();
		
		//prepare parameter
		prepareParameter(query, paraMap);
		
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List <Map <String, Object>> list = query.list();
		return list;
	}

	public List <Map <String, Object>> select(String selectId, Map <String, Object> parameter)
	{
		Query query = prepareQuery(selectId, parameter);
		List <Map <String, Object>> list = query.list();
		return list;
	}

	public <T> void insert(T t)
	{
		getSession().persist(t);
	}

	public <T> int insert(String selectId, Map <String, Object> parameter)
	{
		return executeUpdate(selectId, parameter);
	}

	public <T> int delete(T t)
	{
		Field[] fields = t.getClass().getDeclaredFields();
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ").append(t.getClass().getName()).append(" WHERE 1=1\n");
		Map <String, Object> paraMap = new HashMap <String, Object>();
		for (Field field : fields)
		{
			try
			{
				String fieldName = field.getName();
				String md = getMethod(field.getName());
				Method method = null;
				try
				{
					method = t.getClass().getDeclaredMethod(md);
				}
				catch (Exception e)
				{
					// do nothing
					continue;
				}
				Object wval = method.invoke(t, new Object []{});
				// 设置条件值
				if (wval != null)
				{
					sql.append(" AND ").append(fieldName).append("=:").append(fieldName).append("\n");
					paraMap.put(fieldName, wval);
				}
			}
			catch (Exception e)
			{
				throw new BuilderException(e.getMessage(), e);
			}
		}
		Query query = getSession().createQuery(sql.toString());
		// 设置参数
		for (String key : paraMap.keySet())
		{
			query.setParameter(key, paraMap.get(key));
		}
		int record = query.executeUpdate();
		return record;
	}

	public int delete(String selectId, Map <String, Object> parameter)
	{
		return executeUpdate(selectId, parameter);
	}

	public <T> int update(T t1, T t2)
	{
		Field[] fields = t1.getClass().getDeclaredFields();
		StringBuffer usql = new StringBuffer();
		StringBuffer wsql = new StringBuffer();
		usql.append("UPDATE ").append(t1.getClass().getName()).append(" SET\n");
		List <Object> uparams = new ArrayList <Object>();
		List <Object> wparams = new ArrayList <Object>();
		int ufield = 0;
		int wfield = 0;
		for (Field field : fields)
		{
			try
			{
				String fieldName = field.getName();
				// 获取方法与值
				String md = getMethod(fieldName);
				Method umethod = null;
				try
				{
					umethod = t2.getClass().getDeclaredMethod(md);
				}
				catch (Exception e)
				{
					// do nothing
					continue;
				}
				Object uval = umethod.invoke(t2, new Object []{});
				// 设置更新值
				if (uval != null)
				{
					if (ufield == 0)
					{
						uparams.add(uval);
						usql.append(fieldName).append("= ?");
						ufield++;
					}
					else
					{
						uparams.add(uval);
						usql.append(",\n").append(fieldName).append("= ?");
					}
				}

				// 设置条件值
				Method wmethod = t1.getClass().getDeclaredMethod(md);
				Object wval = wmethod.invoke(t1, new Object []{});
				if (uval != null)
				{
					if (wfield == 0)
					{
						wsql.append(" WHERE 1=1\n");
						wfield++;
					}
					uparams.add(wval);
					wsql.append(" AND ").append(fieldName).append("= ?");
				}
			}
			catch (Exception e)
			{
				throw new BuilderException(e.getMessage(), e);
			}
		}
		uparams.addAll(wparams);
		usql.append(wsql);
		Query query = getSession().createQuery(usql.toString());
		// 设置参数
		for (int i = 0; i < uparams.size(); i++)
		{
			query.setParameter(i, uparams.get(i));
		}
		int record = query.executeUpdate();
		return record;
	}

	public int update(String selectId, Map <String, Object> parameter)
	{
		return executeUpdate(selectId, parameter);
	}

	private Query prepareQuery(String selectId, Map <String, Object> parameter)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);
		Query query = getSession().createSQLQuery(hquery.getSql());
		Map <String, Object> paraMap = hquery.getParameter();
		
		//prepare parameter
		prepareParameter(query, paraMap);
		
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query;
	}

	private <T> Query prepareQuery(T t)
	{
		Field[] fields = t.getClass().getDeclaredFields();
		StringBuffer sql = new StringBuffer();
		sql.append("FROM ").append(t.getClass().getName()).append(" WHERE 1=1\n");
		Map <String, Object> paraMap = new HashMap <String, Object>();
		for (Field field : fields)
		{
			try
			{
				String fieldName = field.getName();
				String md = getMethod(field.getName());
				Method method = null;
				try
				{
					method = t.getClass().getDeclaredMethod(md);
				}
				catch (Exception e)
				{
					// do nothing
					continue;
				}
				Object wval = method.invoke(t, new Object []{});
				// 设置条件值
				if (wval != null)
				{
					paraMap.put(fieldName, wval);
					sql.append(" AND ").append(fieldName).append("=:").append(fieldName).append("\n");
				}
			}
			catch (Exception e)
			{
				throw new BuilderException(e.getMessage(), e);
			}
		}
		Query query = getSession().createQuery(sql.toString());
		// 设置参数
		for (String key : paraMap.keySet())
		{
			query.setParameter(key, paraMap.get(key));
		}
		return query;
	}

	private int executeUpdate(String selectId, Map <String, Object> parameter)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);
		Query query = getSession().createSQLQuery(hquery.getSql());
		Map <String, Object> paraMap = hquery.getParameter();
		
		//prepare parameter
		prepareParameter(query, paraMap);
		
		int result = query.executeUpdate();
		return result;
	}

	private void prepareParameter(Query query, Map <String, Object> paraMap)
	{
		if (null != paraMap && paraMap.size() > 0)
		{
			for (String key : paraMap.keySet())
			{
				if (paraMap.get(key) instanceof Object[])
				{
					Object[] arr = (Object[]) paraMap.get(key);
					if (arr != null && arr.length > 0)
					{
						if (arr[0] instanceof Number)
						{
							query.setParameterList(key, (Number[]) arr);
						}
						else if (arr[0] instanceof Date)
						{
							query.setParameterList(key, (Date[]) arr);
						}
						else
						{
							query.setParameterList(key, arr);
						}
					}
				}
				else if (paraMap.get(key) instanceof ArrayList <?>)
				{
					List <?> list = (ArrayList <?>) paraMap.get(key);
					if (list != null && list.size() > 0)
					{
						if (list.get(0) instanceof Number)
						{
							query.setParameterList(key, (ArrayList <Number>) list);
						}
						else if (list.get(0) instanceof Date)
						{
							query.setParameterList(key, (ArrayList <Date>) list);
						}
						else
						{
							query.setParameterList(key, list);
						}
					}
				}
				else
				{
					query.setParameter(key, paraMap.get(key));
				}
			}
		}
	}

	/**
	 * 获得field的get方法
	 * 
	 * @param field
	 * @return
	 */
	private String getMethod(String field)
	{
		String method = "get" + field.substring(0, 1).toUpperCase() + field.substring(1, field.length());
		return method;
	}

}

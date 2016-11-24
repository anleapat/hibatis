package org.hibatis.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibatis.builder.BuilderException;
import org.hibatis.script.HibernateQuery;
import org.hibatis.script.ParsingHibernateSql;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
	
	public HbFactoryImpl()
	{
	}

	public HbFactoryImpl(Session session)
	{
		this.session = session;
	}
	
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory)
	{
	    this.sessionFactory = sessionFactory;
	}
	
	public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    private Session session;
    
	public Session getSession()
    {
	    if(this.session == null)
	    {
	        return sessionFactory.getCurrentSession();
	    }
        return session;
    }

    public <T> List <T> select(T t)
	{
		Method[] methods = t.getClass().getDeclaredMethods();
		Field[] fields = t.getClass().getDeclaredFields();
		StringBuffer sql = new StringBuffer();
		sql.append("FROM ").append(t.getClass().getName()).append(" WHERE 1=1\n");
		Map <String, Object> paraMap = new HashMap <String, Object>();
		for (Method method : methods)
		{
			if (method.getName().startsWith("get"))
			{
				try
				{
					Object wval = method.invoke(t, new Object []{});
					// 设置条件值
					if (wval != null)
					{
						String fld = getFieldName(fields, method);
						paraMap.put(fld, wval);
						sql.append(" AND ").append(fld).append("=:").append(fld).append("\n");
					}
				}
				catch (Exception e)
				{
					throw new BuilderException(e.getMessage(), e);
				}
			}
		}
		Query query = getSession().createQuery(sql.toString());
		// 设置参数
		for (String key : paraMap.keySet())
		{
			query.setParameter(key, paraMap.get(key));
		}
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

	public List <Map <String, Object>> select(String selectId, Map <String, Object> parameter, int offset, int limit)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);
		Query query = getSession().createSQLQuery(hquery.getSql());
		Map <String, Object> paraMap = hquery.getParameter();
		if(null != paraMap)
		{
			Set<String> keys = paraMap.keySet();
			for (String key : keys)
			{
				query.setParameter(key, paraMap.get(key));
			}
		}
		query.setFirstResult(offset);
		query.setMaxResults(limit);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List <Map <String, Object>> list = query.list();
		return list;
	}

	public List <Map <String, Object>> select(String selectId, Map <String, Object> parameter)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);
		Query query = getSession().createSQLQuery(hquery.getSql());
		Map <String, Object> paraMap = hquery.getParameter();
		if(null != paraMap)
		{
			Set<String> keys = paraMap.keySet();
			for (String key : keys)
			{
				query.setParameter(key, paraMap.get(key));
			}
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
		Method[] methods = t.getClass().getDeclaredMethods();
		Field[] fields = t.getClass().getDeclaredFields();
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ").append(t.getClass().getName()).append(" WHERE 1=1\n");
		Map <String, Object> paraMap = new HashMap <String, Object>();
		for (Method method : methods)
		{
			if (method.getName().startsWith("get"))
			{
				try
				{
					Object wval = method.invoke(t, new Object []{});
					// 设置条件值
					if (wval != null)
					{
						String fld = getFieldName(fields, method);
						sql.append(" AND ").append(fld).append("=:").append(fld).append("\n");
						paraMap.put(fld, wval);
					}
				}
				catch (Exception e)
				{
					throw new BuilderException(e.getMessage(), e);
				}
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
	    Method[] methods = t1.getClass().getDeclaredMethods();
        Field[] fields = t1.getClass().getDeclaredFields();
        StringBuffer usql = new StringBuffer();
        StringBuffer wsql = new StringBuffer();
        usql.append("UPDATE ").append(t1.getClass().getName()).append(" SET\n");
        int whereCount = 0;
        List<Object> paraMap = new ArrayList<Object>();
        List<Object> whereClause = new ArrayList<Object>();
        for (Method method : methods)
        {
            if (method.getName().startsWith("get"))
            {
                try
                {

                    Object nval = method.invoke(t2, new Object []{});
                    // 设置更新值
                    if (nval != null)
                    {
                        String fld = getFieldName(fields, method);
                        usql.append(fld).append("=?").append(",\n");
                        paraMap.add(nval);
                    }
                    if (whereCount == 0)
                    {
                        wsql.append(" WHERE 1=1\n");
                        whereCount++;
                    }
                    Object wval = method.invoke(t1, new Object []{});
                    if (wval != null)
                    {
                        String fld = getFieldName(fields, method);
                        wsql.append(" AND ").append(fld).append("=?").append("\n");
                        whereClause.add(wval);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        paraMap.addAll(whereClause);
        StringBuffer sql = new StringBuffer(usql.substring(0, usql.toString().lastIndexOf(","))).append("\n");
        sql.append(wsql);
        Query query = sessionFactory.getCurrentSession().createQuery(sql.toString());
        // 设置参数
        for (int i = 0; i < paraMap.size(); i++)
        {
            query.setParameter(i, paraMap.get(i));
        }
        int record = query.executeUpdate();
        return record;
	}

	public int update(String selectId, Map <String, Object> parameter)
	{
		return executeUpdate(selectId, parameter);
	}

	private int executeUpdate(String selectId, Map <String, Object> parameter)
	{
		HibernateQuery hquery = ParsingHibernateSql.parsing(selectId, parameter);
		Query query = getSession().createSQLQuery(hquery.getSql());
		Map <String, Object> paraMap = hquery.getParameter();
		if(null != paraMap)
		{
			Set<String> keys = paraMap.keySet();
			for (String key : keys)
			{
				query.setParameter(key, paraMap.get(key));
			}
		}
		int result = query.executeUpdate();
		return result;
	}

	private String getFieldName(Field[] fields, Method method)
	{
		String fld = null;
		for (Field field : fields)
		{
			if (method.getName().equalsIgnoreCase("get" + field.getName()))
			{
				fld = field.getName();
				break;
			}
		}
		return fld;
	}
	
}

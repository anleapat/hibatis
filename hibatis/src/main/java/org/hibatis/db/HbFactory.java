package org.hibatis.db;

import java.util.List;
import java.util.Map;

/**
 * @author pan
 * @since Oct 20, 2016 4:35:15 PM
 * @desc
 */
public interface HbFactory
{
	public <T> List <T> select(T t);

	public <T> T selectOne(T t);

	public List <Map <String, Object>> select(String selectId, Map <String, Object> parameter, int offset, int limit);

	public List <Map <String, Object>> select(String selectId, Map <String, Object> parameter);

	public <T> void insert(T t);

	public <T> int insert(String selectId, Map <String, Object> parameter);

	public <T> int delete(T t);

	public int delete(String selectId, Map <String, Object> parameter);

	public <T> int update(T t1, T t2);

	public int update(String selectId, Map <String, Object> parameter);

}

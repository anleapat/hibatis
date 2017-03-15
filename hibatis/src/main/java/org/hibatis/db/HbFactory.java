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
    
    /**
     * 查询满足条件实体列表
     * 
     * @param t
     * @return
     */
    public <T> List <T> select(T t);
    
    /**
     * for update查询满足条件实体列表
     * 
     * @param t
     * @return
     */
    public <T> List <T> selectForUpdate(T t);

    /**
     * 查询满足条件实体列表并返回列表中第一个对象
     * 
     * @param t
     * @return
     */
    public <T> T selectOne(T t);

    /**
     * hql查询实体列表
     * 
     * @return
     */
    public <T> List <T> selectByHql(String selectId, Map <String, Object> parameter);
    
    /**
     * hql for update查询实体列表
     * 
     * @return
     */
    public <T> List <T> selectForUpdateByHql(String selectId, Map <String, Object> parameter);

    /**
     * hql查询实体列表并返回列表中第一个对象
     * 
     * @param t
     * @return
     */
    public <T> T selectOneByHql(String selectId, Map <String, Object> parameter);

    /**
     * hql查询实体列表并限制条数
     * 
     * @return
     */
    public <T> List <T> selectByHql(String selectId, Map <String, Object> parameter, int offset, int limit);

    /**
     * 查询sql返回记录条数
     * 
     * @return
     */
    public int recordCountBySql(String selectId, Map <String, Object> parameter);

    /**
     * 查询hql返回记录条数
     * 
     * @return
     */
    public int recordCountByHql(String selectId, Map <String, Object> parameter);

    /**
     * sql查询返回Map列表
     * 
     * @param selectId
     * @param parameter
     * @return
     */
    public List <Map <String, Object>> select(String selectId, Map <String, Object> parameter);
    
    /**
     * sql for update查询返回Map列表
     * 
     * @param selectId
     * @param parameter
     * @return
     */
    public List <Map <String, Object>> selectForUpdate(String selectId, Map <String, Object> parameter);

    /**
     * sql查询返回Map列表并限制条数
     * 
     * @param selectId
     * @param parameter
     * @return
     */
    public List <Map <String, Object>> select(String selectId, Map <String, Object> parameter, int offset, int limit);

    /**
     * 实体持久化
     * 
     * @param t
     */
    public <T> void insert(T t);

    /**
     * sql持久化
     * 
     * @param t
     */
    public <T> int insert(String selectId, Map <String, Object> parameter);

    /**
     * 删除满足条件的实体
     * 
     * @param t
     */
    public <T> int delete(T t);

    /**
     * sql删除
     * 
     * @param t
     */
    public int delete(String selectId, Map <String, Object> parameter);

    /**
     * 满足t1条件的实体更新为t2实体中对应值
     * 
     * @param t
     */
    public <T> int update(T t1, T t2);

    /**
     * sql更新
     * 
     * @param selectId
     * @param parameter
     * @return
     */
    public int update(String selectId, Map <String, Object> parameter);

}

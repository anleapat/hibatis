package org.hibatis.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibatis.builder.BuilderException;

/**
 * 
 * @author lee
 * @date Oct 20, 2016
 *
 */
public class MapToPo
{
    /**
     * map list convert to bean list
     * 
     * @param list
     * @param c
     * @return
     */
    public static <T> List <T> mapToPo(List <Map <String, Object>> list, Class <T> c)
    {
        if (list != null)
        {
            List <T> glist = new ArrayList <T>();
            for (Map <String, Object> map : list)
            {
                try
                {
                    T t = mapToPo(map, c);
                    glist.add(t);
                }
                catch (Exception e)
                {
                    throw new BuilderException(e.getMessage(), e);
                }

            }
            return glist;
        }
        return null;
    }

    /**
     * map convert to bean
     * 
     * @param list
     * @param c
     * @return
     */
    public static <T> T mapToPo(Map <String, Object> map, Class <T> c)
    {
        if (map != null)
        {
            Set <String> set = map.keySet();
            Method[] methods = c.getDeclaredMethods();
            
            try
            {
                T t = (T) Class.forName(c.getName()).newInstance();
                for (String key : set)
                {
                    // key = key.replaceAll("_", "");
                    for (Method md : methods)
                    {
                        if (("set" + key.replaceAll("_", "")).equalsIgnoreCase(md.getName()))
                        {
                            try
                            {
                                if (map.get(key) != null)
                                {
                                    String getMethod = "g" + md.getName().substring(1, md.getName().length());
                                    Method method = t.getClass().getDeclaredMethod(getMethod);
                                    Class <?> cl = method.getReturnType();
                                    if (Long.class.equals(cl))
                                    {
                                        Long val = Long.parseLong(map.get(key).toString());
                                        md.invoke(t, val);
                                    }
                                    else if (Integer.class.equals(cl))
                                    {
                                        Integer val = Integer.parseInt(map.get(key).toString());
                                        md.invoke(t, val);
                                    }
                                    else
                                    {
                                        md.invoke(t, map.get(key));
                                    }
                                }
                                else
                                {
                                    md.invoke(t, map.get(key));
                                }
                            }
                            catch (Exception e)
                            {
                                throw new BuilderException(e.getMessage(), e);
                            }
                        }
                    }
                }
                return t;
            }
            catch (Exception e)
            {
                throw new BuilderException(e.getMessage(), e);
            }
        }
        return null;
    }
}

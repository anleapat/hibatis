package org.hibatis.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibatis.script.sqls.DynamicSqlSource;
import org.hibatis.script.sqls.RawSqlSource;
import org.hibatis.script.sqls.SqlSource;
import org.hibatis.script.sqls.SqlTypeEnum;
import org.hibatis.script.sqls.StaticSqlSource;
import org.hibatis.script.xmltags.BaseBuilder;
import org.hibatis.script.xmltags.IfSqlNode;
import org.hibatis.script.xmltags.MixedSqlNode;
import org.hibatis.script.xmltags.OgnlCache;
import org.hibatis.script.xmltags.SqlNode;
import org.hibatis.script.xmltags.TextSqlNode;
import org.hibatis.script.xmltags.XMLScriptBuilder;

/**
 * @author pan
 * @since Oct 20, 2016 4:45:33 PM
 * @desc
 */
public class ParsingHibernateSql
{
	private ParsingHibernateSql()
	{
	}

	public static HibernateQuery parsing(String selectId, Map <String, Object> parameter)
	{
		BaseBuilder builder = new XMLScriptBuilder();
		SqlSource sqlSource = builder.parseResource(selectId);
		
		SqlTypeEnum sqlType = sqlSource.getSqlType();
		StringBuilder sb = new StringBuilder();
		List <SqlNode> list = sqlSource.getContexts();
		Map <String, Object> paraMap = new HashMap <String, Object>();
		if(sqlSource instanceof StaticSqlSource)
		{
			for (SqlNode sqlNode : list)
			{
				if (sqlNode instanceof TextSqlNode)
				{
					sb.append(((TextSqlNode) sqlNode).getText());
					parsingParameter(parameter, sb, paraMap);
				}
			}
		}
		else if(sqlSource instanceof RawSqlSource)
		{
			
			for (SqlNode sqlNode : list)
			{
				if (sqlNode instanceof TextSqlNode)
				{
					sb.append(((TextSqlNode) sqlNode).getText());
					parsingParameter(parameter, sb, paraMap);
				}
			}
		}
		else if (sqlSource instanceof DynamicSqlSource)
		{
			paraMap = new HashMap <String, Object>();
			for (SqlNode sqlNode : list)
			{
				if (sqlNode instanceof TextSqlNode)
				{
					sb.append(((TextSqlNode) sqlNode).getText());
					parsingParameter(parameter, sb, paraMap);
				}
				if (sqlNode instanceof IfSqlNode)
				{
					IfSqlNode node = (IfSqlNode) sqlNode;
					MixedSqlNode mnode = (MixedSqlNode) node.getContents();
					boolean b = false;
					if(parameter != null)
					{
					    b = OgnlCache.getValue(node.getTest(), parameter);
	                    if (b)
	                    {
	                        List <SqlNode> tlist = mnode.getContents();
	                        for (SqlNode sn : tlist)
	                        {
	                            sb.append(((TextSqlNode) sn).getText());
	                            parsingParameter(parameter, sb, paraMap);
	                        }
	                    }
					}
				}
			}
		}
		
		return new HibernateQuery(sb.toString(), paraMap, sqlType);
	}

	private static void parsingParameter(Map <String, Object> parameter, StringBuilder sb, Map <String, Object> paraMap)
	{
	    if(parameter != null)
	    {
	        String sql = sb.toString().replace(" ", "");
    		Set<String> keys = parameter.keySet();
    		for (String key : keys)
    		{
    			if(sql.indexOf(":" + key) > 0)
    			{
    				paraMap.put(key, parameter.get(key));
    			}
    		}
	    }
	}
}

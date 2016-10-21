package org.hibatis.script.xmltags;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibatis.builder.BuilderException;
import org.hibatis.builder.xml.XmlSqlsConfigParser;
import org.hibatis.io.Resources;
import org.hibatis.parsing.XNode;
import org.hibatis.parsing.XPathParser;
import org.hibatis.script.sqls.DynamicSqlSource;
import org.hibatis.script.sqls.RawSqlSource;
import org.hibatis.script.sqls.SqlSource;
import org.hibatis.script.sqls.SqlTypeEnum;
import org.hibatis.script.sqls.StaticSqlSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author pan
 * @since Oct 20, 2016 10:10:33 AM
 * @desc
 */
public class XMLScriptBuilder extends BaseBuilder
{

    public SqlSource parseResource(String resource)
    {
        try
        {
            SqlSource sqlSource = null;
            int li = resource.lastIndexOf(".");
            String file = resource.substring(0, li);
            file = file.replaceAll("\\.", "/");
            String selectkey = resource.substring(li + 1, resource.length());
            InputStream inputStream = Resources.getResourceAsStream(file + ".xml");
            XPathParser parser = new XPathParser(inputStream, true, new Properties(), new XmlSqlsConfigParser());
            XNode node = parser.evalNode("/sqls");
            List <XNode> list = node.evalNodes("select|insert|update|delete");
            for (XNode xNode : list)
            {
                String id = xNode.getStringAttribute("id");
                if (id.equals(selectkey))
                {
                    SqlTypeEnum sqlType = SqlTypeEnum.valueOf(xNode.getName().toUpperCase());
                    XMLScriptBuilder builder = new XMLScriptBuilder();
                    sqlSource = builder.parseSqlsource(xNode, sqlType);
                    break;
                }
            }
            if (sqlSource == null)
            {
                throw new BuilderException("No sql id match \"" + selectkey + "\".");
            }
            return sqlSource;
        }
        catch (Exception e)
        {
            throw new BuilderException(e.getMessage(), e);
        }
    }

    public List <SqlNode> parseDynamicTags(XNode xNode)
    {
        List <SqlNode> contents = new ArrayList <SqlNode>();
        NodeList children = xNode.getNode().getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            XNode child = xNode.newXNode(children.item(i));
            if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE || child.getNode().getNodeType() == Node.TEXT_NODE)
            {
                String data = child.getStringBody("");
                if (data.replaceAll(" ", "").indexOf("=:") > 0)
                {
                    this.isRaw = true;
                }
                contents.add(new TextSqlNode(data));
            }
            else if (child.getNode().getNodeType() == Node.ELEMENT_NODE)
            {
                this.isDynamic = true;
                String nodeName = child.getNode().getNodeName();
                NodeHandler handler = nodeHandlers(nodeName);
                handler.handleNode(child, contents);
            }
        }
        return contents;
    }

    public SqlSource parseSqlsource(XNode xNode, SqlTypeEnum sqlType)
    {
        List <SqlNode> contents = parseDynamicTags(xNode);
        SqlSource source = null;
        if (!(this.isDynamic && this.isRaw))
        {
            source = new StaticSqlSource(contents, sqlType);
        }
        if (!isDynamic && this.isRaw)
        {
            source = new RawSqlSource(contents, sqlType);
        }
        if (this.isDynamic)
        {
            source = new DynamicSqlSource(contents, sqlType);
        }
        return source;
    }

    private interface NodeHandler
    {
        void handleNode(XNode nodeToHandle, List <SqlNode> targetContents);
    }

    private class IfHandler implements NodeHandler
    {
        public IfHandler()
        {
            // Prevent Synthetic Access
        }

        public void handleNode(XNode nodeToHandle, List <SqlNode> targetContents)
        {
            List <SqlNode> contents = parseDynamicTags(nodeToHandle);
            MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
            String test = nodeToHandle.getStringAttribute("test");
            IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode, test);
            targetContents.add(ifSqlNode);
        }
    }

    NodeHandler nodeHandlers(String nodeName)
    {
        Map <String, NodeHandler> map = new HashMap <String, NodeHandler>();
        map.put("if", new IfHandler());
        return map.get(nodeName);
    }
}

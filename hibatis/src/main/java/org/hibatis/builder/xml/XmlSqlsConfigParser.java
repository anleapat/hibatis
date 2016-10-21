package org.hibatis.builder.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.hibatis.io.Resources;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XmlSqlsConfigParser implements EntityResolver
{
	private static final Map <String, String> doctypeMap = new HashMap <String, String>();

	static
	{
		doctypeMap.put("-//hibatis.org//DTD sql 1.0//EN".toUpperCase(), "/com/hibatis/hibatis-config_1_0.dtd");
	}

	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
	{
		if (publicId != null)
		{
			publicId = publicId.toUpperCase(Locale.ENGLISH);
		}
		if (systemId != null)
		{
			systemId = systemId.toUpperCase(Locale.ENGLISH);
		}

		InputSource source = null;
		try
		{
			String path = doctypeMap.get(publicId);
			source = getInputSource(path, source);
			if (source == null)
			{
				path = doctypeMap.get(systemId);
				source = getInputSource(path, source);
			}
		}
		catch (Exception e)
		{
			throw new SAXException(e.toString());
		}
		return source;
	}

	private InputSource getInputSource(String path, InputSource source)
	{
		if (path != null)
		{
			InputStream in;
			try
			{
				in = Resources.getResourceAsStream(path);
				source = new InputSource(in);
			}
			catch (IOException e)
			{
				// ignore, null is ok
			}
		}
		return source;
	}
}

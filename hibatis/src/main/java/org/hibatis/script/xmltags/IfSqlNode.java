/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.hibatis.script.xmltags;

/**
 * @author Clinton Begin
 */
public class IfSqlNode implements SqlNode
{
	private String test;
	private SqlNode contents;

	public IfSqlNode(SqlNode contents, String test)
	{
		this.test = test;
		this.contents = contents;
	}

	public String getTest()
	{
		return test;
	}

	public void setTest(String test)
	{
		this.test = test;
	}

	public SqlNode getContents()
	{
		return contents;
	}

	public void setContents(SqlNode contents)
	{
		this.contents = contents;
	}

}

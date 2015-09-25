/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpversioning.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = TrashedResource.TABLE_NAME)
public class TrashedResource {
	
	public TrashedResource() {}
	
	@DatabaseField(columnName = "resid", 
			dataType = DataType.STRING, 
			width = 16, canBeNull = false, id = true)
	private String _resourceId;
	
	@DatabaseField(columnName = "restype", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = false)
	private String _resourceType;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20, canBeNull = false)
	private String _mainGroup;
	
	@DatabaseField(columnName = "resxml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _resourceXml;
	
	public static final String TABLE_NAME = "jpversioning_trashedresources";
	
}
/*
CREATE TABLE jpversioning_trashedresources
(
  resid character varying(16) NOT NULL,
  restype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  maingroup character varying(20) NOT NULL,
  resxml character varying NOT NULL,
  CONSTRAINT jpversioning_trashedresources_pkey PRIMARY KEY (resid)
);
*/
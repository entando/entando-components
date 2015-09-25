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
package org.entando.entando.plugins.jpwebform.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = TypeVersion.TABLE_NAME)
public class TypeVersion {
	
	public TypeVersion() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _code;
	
	@DatabaseField(columnName = "typecode", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = false)
	private String _typeCode;
	
	@DatabaseField(columnName = "version", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _version;
	
	@DatabaseField(columnName = "modelxml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _modelXml;
	
	@DatabaseField(columnName = "stepxml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _stepXml;
	
	public static final String TABLE_NAME = "jpwebform_typeversions";
	
}
/*
 * 
CREATE TABLE jpwebform_typeversions
(
  code integer NOT NULL,
  typecode character varying(30) NOT NULL,
  version integer NOT NULL,
  modelxml character varying NOT NULL,
  stepxml character varying NOT NULL,
  CONSTRAINT jpwebform_typeversions_pkey PRIMARY KEY (code )
)
 * 
 */
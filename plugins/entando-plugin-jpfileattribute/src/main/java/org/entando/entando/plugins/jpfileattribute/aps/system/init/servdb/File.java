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
package org.entando.entando.plugins.jpfileattribute.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = File.TABLE_NAME)
public class File {
	
	public File() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "entityid", 
			dataType = DataType.STRING, 
			width = 20, canBeNull = true)
	private String _entityId;
	
	@DatabaseField(columnName = "entityclass", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _entityClass;
	
	@DatabaseField(columnName = "filename", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _filename;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _contentType;
	/*
	@DatabaseField(columnName = "base64", 
			dataType = DataType.BYTE_ARRAY, 
			canBeNull = false)
	private String _base64;
	*/
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, canBeNull = false)
	private String _username;
	
	@DatabaseField(columnName = "date", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _date;
	
	@DatabaseField(columnName = "approved", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _approved;
	
	public static final String TABLE_NAME = "jpfileattribute_files";
	
}
/*
CREATE TABLE jpfileattribute_files
(
  id integer NOT NULL,
  entityid character varying(20),
  entityclass text NOT NULL,
  filename character varying(100) NOT NULL,
  contenttype character varying(100) NOT NULL,
  -- base64 bytea NOT NULL,
  username character varying(40) NOT NULL,
  date date NOT NULL,
  approved smallint NOT NULL,
  CONSTRAINT jpfileattribute_files_pkey PRIMARY KEY (id )
)
 */
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
package org.entando.entando.plugins.jprss.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Channel.TABLE_NAME)
public class Channel {
	
	public Channel() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "title", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _title;
	
	@DatabaseField(columnName = "description", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "active", 
			dataType = DataType.STRING, 
			width = 5, canBeNull = false)
	private String _active;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = false)
	private String _contentType;
	
	@DatabaseField(columnName = "filters", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _filters;
	
	@DatabaseField(columnName = "feedtype", 
			dataType = DataType.STRING, 
			width = 10, canBeNull = false)
	private String _feedType;
	
	@DatabaseField(columnName = "category", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = true)
	private String _category;
	
	@DatabaseField(columnName = "maxcontentsize", 
			dataType = DataType.INTEGER, 
			canBeNull = true)
	private int _maxContentSize;
	
	public static final String TABLE_NAME = "jprss_channel";
	
}
/*
CREATE TABLE jprss_channel (
    id integer NOT NULL,
    title character varying(100) NOT NULL,
    description character varying(100) NOT NULL,
    active character varying(5) NOT NULL,
    contenttype character varying(30) NOT NULL,
    filters character varying,
    feedtype character varying(10) NOT NULL,
    category character varying(30),
    maxcontentsize integer,
	 CONSTRAINT rsschannel_pkey PRIMARY KEY (id)
);
 */
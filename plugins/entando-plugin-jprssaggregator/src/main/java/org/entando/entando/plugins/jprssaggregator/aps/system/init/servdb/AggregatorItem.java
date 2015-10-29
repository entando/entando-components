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
package org.entando.entando.plugins.jprssaggregator.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = AggregatorItem.TABLE_NAME)
public class AggregatorItem {
	
	public AggregatorItem() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _code;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _contentType;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			width = 255, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "link", 
			dataType = DataType.STRING, 
			width = 255, 
			canBeNull = false)
	private String _link;
	
	@DatabaseField(columnName = "delay", 
			dataType = DataType.DOUBLE, 
			canBeNull = false)
	private double _delay;
	
	@DatabaseField(columnName = "lastupdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _lastupdate;
	
	@DatabaseField(columnName = "categories", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _categories;
	
	public static final String TABLE_NAME = "jprssaggregator_aggregatoritems";
	
}
/*
CREATE TABLE jprssaggregator_aggregatoritems
(
  code integer NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(255) NOT NULL,
  link character varying(255) NOT NULL,
  delay bigint NOT NULL,
  lastupdate timestamp without time zone NOT NULL,
  categories character varying,
  CONSTRAINT jprssaggregator_aggregatoritems_pkey PRIMARY KEY (code)
);
 */
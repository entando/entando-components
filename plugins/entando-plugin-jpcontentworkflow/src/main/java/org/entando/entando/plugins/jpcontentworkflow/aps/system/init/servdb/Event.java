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
package org.entando.entando.plugins.jpcontentworkflow.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Event.TABLE_NAME)
public class Event {
	
	public Event() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "data", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _data;
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, canBeNull = false)
	private String _contentId;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = false)
	private String _contentType;
	
	/*
  id integer NOT NULL,
  data timestamp without time zone,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
	 */
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20, canBeNull = false)
	private String _mainGroup;
	
	@DatabaseField(columnName = "status", 
			dataType = DataType.STRING, 
			width = 12, canBeNull = false)
	private String _status;
	
	@DatabaseField(columnName = "notified", 
			dataType = DataType.SHORT, 
			canBeNull = true)
	private short _notified;
	
	/*
  descr character varying(1000) NOT NULL,
  maingroup character varying(20) NOT NULL,
  status character varying(12) NOT NULL,
  notified smallint,
	 */
	public static final String TABLE_NAME = "jpcontentworkflow_events";
	
}
/*
CREATE TABLE jpcontentworkflow_events
(
  id integer NOT NULL,
  data timestamp without time zone,
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  *
  descr character varying(1000) NOT NULL,
  maingroup character varying(20) NOT NULL,
  status character varying(12) NOT NULL,
  notified smallint,
  CONSTRAINT jpcontentworkflow_events_pkey PRIMARY KEY (id)
);
 */
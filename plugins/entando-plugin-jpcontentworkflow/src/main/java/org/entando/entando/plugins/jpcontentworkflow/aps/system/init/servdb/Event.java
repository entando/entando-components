/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
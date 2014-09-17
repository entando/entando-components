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
package org.entando.entando.plugins.jpactionlogger.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Record.TABLE_NAME)
public class Record {
	
	public Record() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 20, 
			canBeNull = true)
	private String _username;
	
	@DatabaseField(columnName = "actiondate", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _actionDate;
	
	@DatabaseField(columnName = "namespace", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _namespace;
	
	@DatabaseField(columnName = "actionname", 
			dataType = DataType.STRING, 
			width = 250, 
			canBeNull = true)
	private String _actionname;
	
	@DatabaseField(columnName = "params", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _parameters;
	
	public static final String TABLE_NAME = "jpactionlogger_records";
	
}
/*
CREATE TABLE jpactionlogger_records
(
  id integer NOT NULL,
  username character varying(20),
  actiondate timestamp without time zone,
  namespace character varying,
  actionname character varying(255),
  params character varying,
  CONSTRAINT jpactionlogger_records_pkey PRIMARY KEY (id)
)
 */
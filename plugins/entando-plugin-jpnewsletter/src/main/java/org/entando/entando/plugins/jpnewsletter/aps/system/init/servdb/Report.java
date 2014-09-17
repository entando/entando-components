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
package org.entando.entando.plugins.jpnewsletter.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Report.TABLE_NAME)
public class Report {
	
	public Report() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "date", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _date;
	
	@DatabaseField(columnName = "subject", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _subject;
	
	public static final String TABLE_NAME = "jpnewsletter_newsletterreport";
	
}
/*
CREATE TABLE jpnewsletter_newsletterreport (
  id integer NOT NULL,
  date timestamp without time zone NOT NULL,
  subject character varying NOT NULL,
  CONSTRAINT jpnewsletter_newsletterreport_pkey PRIMARY KEY (id)
);
 */
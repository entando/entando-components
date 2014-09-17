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
package org.entando.entando.plugins.jpstats.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Statistics.TABLE_NAME)
public class Statistics {
	
	public Statistics() {}
	
	@DatabaseField(columnName = "ip", 
			dataType = DataType.STRING, 
			width = 19, canBeNull = false)
	private String _ip;
	
	@DatabaseField(columnName = "referer", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _referer;
	
	@DatabaseField(columnName = "session_id", 
			dataType = DataType.STRING, 
			canBeNull = true)
	private String _sessionId;
	
	@DatabaseField(columnName = "role", 
			dataType = DataType.STRING, 
			canBeNull = true)
	private String _role;
	
	@DatabaseField(columnName = "timestamp", 
			dataType = DataType.STRING, 
			canBeNull = true)
	private String _timestamp;
	/*
  ip character varying(19),
  referer character varying,
  session_id character varying(254),
  "role" character varying(254),
  "timestamp" character varying(254),
	 */
	
	@DatabaseField(columnName = "year_value", 
			dataType = DataType.STRING, 
			width = 4, canBeNull = true)
	private String _yearValue;
	
	@DatabaseField(columnName = "month_value", 
			dataType = DataType.STRING, 
			width = 2, canBeNull = true)
	private String _monthValue;
	
	@DatabaseField(columnName = "day_value", 
			dataType = DataType.STRING, 
			width = 2, canBeNull = true)
	private String _dayValue;
	
	@DatabaseField(columnName = "hour_value", 
			dataType = DataType.STRING, 
			width = 8, canBeNull = true)
	private String _hourValue;
	
	/*
  year_value character(4),
  month_value character(2),
  day_value character(2),
  hour_value character(8),
	 */
	
	@DatabaseField(columnName = "pagecode", 
			dataType = DataType.STRING, 
			canBeNull = true)
	private String _pageCode;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 2, canBeNull = true)
	private String _langCode;
	
	@DatabaseField(columnName = "useragent", 
			dataType = DataType.STRING, 
			canBeNull = true)
	private String _useragent;
	
	@DatabaseField(columnName = "browserlang", 
			dataType = DataType.STRING, 
			canBeNull = true)
	private String _browserLang;
	
	@DatabaseField(columnName = "content", 
			dataType = DataType.STRING, 
			width = 16, canBeNull = true)
	private String _content;
	
	/*
  pagecode character varying(254),
  langcode character(2),
  useragent character varying,
  browserlang character varying(254),
  "content" character varying(16)
	 */
	
	public static final String TABLE_NAME = "jpstats_statistics";
	
}
/*
CREATE TABLE jpstats_statistics
(
  ip character varying(19),
  referer character varying,
  session_id character varying(254),
  "role" character varying(254),
  "timestamp" character varying(254),
  * 
  year_value character(4),
  month_value character(2),
  day_value character(2),
  hour_value character(8),
  * 
  pagecode character varying(254),
  langcode character(2),
  useragent character varying,
  browserlang character varying(254),
  "content" character varying(16)
);
 */

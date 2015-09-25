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

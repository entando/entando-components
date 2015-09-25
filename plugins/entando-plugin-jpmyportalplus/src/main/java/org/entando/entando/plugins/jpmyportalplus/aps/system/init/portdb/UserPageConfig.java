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
package org.entando.entando.plugins.jpmyportalplus.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.model.portdb.Page;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = UserPageConfig.TABLE_NAME)
public class UserPageConfig implements ExtendedColumnDefinition {
	
	public UserPageConfig() {}
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _username;
	
	@DatabaseField(foreign=true, columnName = "pagecode", 
			width = 30, 
			canBeNull = false)
	private Page _page;
	
	@DatabaseField(columnName = "framepos", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _framePos;
	
	@DatabaseField(columnName = "widgetcode", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _widgetCode;
	
	@DatabaseField(columnName = "config", 
			dataType = DataType.LONG_STRING)
	private String _config;
	
	@DatabaseField(columnName = "closed", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _closed;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String pageTableName = Page.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			pageTableName = "`" + pageTableName + "`";
		}
		String[] queries = new String[2];
		queries[0] = "ALTER TABLE " + tableName + " "
				+ "ADD CONSTRAINT jpmyportalplus_conf_pkey PRIMARY KEY(username, framepos, pagecode)";
		queries[1] = "ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT jpmyportalplus_conf_page_fkey FOREIGN KEY (pagecode) "
				+ "REFERENCES " + pageTableName + " (code)";
		return queries;
	}
	
	public static final String TABLE_NAME = "jpmyportalplus_userpageconfig";
	
}
/*
CREATE TABLE jpmyportalplus_userpageconfig
(
  username character varying(40) NOT NULL,
  pagecode character varying(30) NOT NULL DEFAULT ''::character varying,
  framepos integer NOT NULL,
  widgetcode character varying(40) NOT NULL,
  config character varying,
  closed integer NOT NULL,
  CONSTRAINT jpmyportalplus_userpageconfig_pkey PRIMARY KEY (username, framepos, pagecode),
  CONSTRAINT jpmyportalplus_userpageconfig_pagecode_fkey FOREIGN KEY (pagecode)
      REFERENCES pages (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
*/
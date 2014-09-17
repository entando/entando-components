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
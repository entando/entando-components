
/*
 *
 *  * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *  *
 *  * This library is free software; you can redistribute it and/or modify it under
 *  * the terms of the GNU Lesser General Public License as published by the Free
 *  * Software Foundation; either version 2.1 of the License, or (at your option)
 *  * any later version.
 *  *
 *  * This library is distributed in the hope that it will be useful, but WITHOUT
 *  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  * details.
 *
 */

package org.entando.entando.plugins.dashboard.aps.system.init.servdb;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = DashboardTable.TABLE_NAME)
public class DashboardTable {
	
	public DashboardTable() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "widgettitle", 
		dataType = DataType.STRING, 
		width=50,  canBeNull=false)
	private String _widgetTitle;
	
	@DatabaseField(columnName = "datasource", 
		dataType = DataType.STRING, 
		width=50,  canBeNull=false)
	private String _datasource;
	
	@DatabaseField(columnName = "context", 
		dataType = DataType.STRING, 
		width=50,  canBeNull=false)
	private String _context;
	
	@DatabaseField(columnName = "download", 
		dataType = DataType.INTEGER, 
		width=1,  canBeNull=false)
	private int _download;
	
	@DatabaseField(columnName = "filter", 
		dataType = DataType.INTEGER, 
		width=1,  canBeNull=false)
	private int _filter;
	
	@DatabaseField(columnName = "allcolumns", 
		dataType = DataType.INTEGER, 
		width=1,  canBeNull=false)
	private int _allColumns;
	
	@DatabaseField(columnName = "columns", 
		dataType = DataType.STRING, 
		width=255,  canBeNull=false)
	private String _columns;
	

public static final String TABLE_NAME = "dashboard_table";
}

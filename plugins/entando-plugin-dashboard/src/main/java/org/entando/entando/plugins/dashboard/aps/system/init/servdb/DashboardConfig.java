
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

@DatabaseTable(tableName = DashboardConfig.TABLE_NAME)
public class DashboardConfig {
	
	public DashboardConfig() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "serverdescription", 
		dataType = DataType.STRING, 
		width=30,  canBeNull=false)
	private String _serverDescription;
	
	@DatabaseField(columnName = "serveruri", 
		dataType = DataType.STRING, 
		width=255,  canBeNull=true)
	private String _serverURI;
	
	@DatabaseField(columnName = "username", 
		dataType = DataType.STRING, 
		width=50,  canBeNull= true)
	private String _username;
	
	@DatabaseField(columnName = "password", 
		dataType = DataType.STRING, 
		width=50,  canBeNull= true)
	private String _password;
	
	@DatabaseField(columnName = "token", 
		dataType = DataType.STRING, 
		width=255,  canBeNull= true)
	private String _token;
	
	@DatabaseField(columnName = "timeconnection", 
		dataType = DataType.INTEGER, 
		 canBeNull= true)
	private int _timeConnection;
	
	@DatabaseField(columnName = "active", 
		dataType = DataType.INTEGER, 
		 canBeNull= true)
	private int _active;

    @DatabaseField(columnName = "type",
            dataType = DataType.STRING,
            width = 100, canBeNull = true)
    private String _type;


    @DatabaseField(columnName = "debug",
		dataType = DataType.INTEGER, 
		 canBeNull= true)
	private int _debug;
    
public static final String TABLE_NAME = "dashboard_config";
}

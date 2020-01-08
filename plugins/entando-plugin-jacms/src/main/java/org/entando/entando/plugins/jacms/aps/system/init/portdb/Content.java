/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jacms.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Content.TABLE_NAME)
public class Content {
	
	public Content() {}
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = false, id = true)
	private String contentId;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String contentType;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			canBeNull = false)
	private String description;
    
	@DatabaseField(columnName = "status", 
			dataType = DataType.STRING, 
			width = 12, 
			canBeNull = false, index = true)
	private String status;
	
	@DatabaseField(columnName = "workxml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String workXml;
	
	@DatabaseField(columnName = "created", 
			dataType = DataType.STRING, 
			width = 20, canBeNull = true)
	private String created;
	
	@DatabaseField(columnName = "lastmodified", 
			dataType = DataType.STRING, 
			width = 20, canBeNull = true, index = true)
	private String lastModified;
	
	@DatabaseField(columnName = "onlinexml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String onlineXml;
	
	@DatabaseField(columnName = "published", 
			dataType = DataType.STRING, 
			width = 20, canBeNull = true, index = true)
	private String published;
    
    @DatabaseField(columnName = "sync",
            dataType = DataType.SHORT)
    private short sync;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20, 
			canBeNull = false, index = true)
	private String mainGroup;
	
	@DatabaseField(columnName = "currentversion", 
			dataType = DataType.STRING, 
			width = 7, 
			canBeNull = false)
	private String currentVersion;
	
	@DatabaseField(columnName = "lasteditor", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = true)
	private String lastEditor;
	
	@DatabaseField(columnName = "firsteditor", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = true)
	private String _firstEditor;

	@DatabaseField(columnName = "restriction",
			dataType = DataType.STRING,
			width = 40,
			canBeNull = true)
	private String _restriction;
	
	public static final String TABLE_NAME = "contents";
	
}

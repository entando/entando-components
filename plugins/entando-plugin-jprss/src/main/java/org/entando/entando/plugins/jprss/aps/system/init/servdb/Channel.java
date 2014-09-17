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
package org.entando.entando.plugins.jprss.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Channel.TABLE_NAME)
public class Channel {
	
	public Channel() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "title", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _title;
	
	@DatabaseField(columnName = "description", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "active", 
			dataType = DataType.STRING, 
			width = 5, canBeNull = false)
	private String _active;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = false)
	private String _contentType;
	
	@DatabaseField(columnName = "filters", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _filters;
	
	@DatabaseField(columnName = "feedtype", 
			dataType = DataType.STRING, 
			width = 10, canBeNull = false)
	private String _feedType;
	
	@DatabaseField(columnName = "category", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = true)
	private String _category;
	
	@DatabaseField(columnName = "maxcontentsize", 
			dataType = DataType.INTEGER, 
			canBeNull = true)
	private int _maxContentSize;
	
	public static final String TABLE_NAME = "jprss_channel";
	
}
/*
CREATE TABLE jprss_channel (
    id integer NOT NULL,
    title character varying(100) NOT NULL,
    description character varying(100) NOT NULL,
    active character varying(5) NOT NULL,
    contenttype character varying(30) NOT NULL,
    filters character varying,
    feedtype character varying(10) NOT NULL,
    category character varying(30),
    maxcontentsize integer,
	 CONSTRAINT rsschannel_pkey PRIMARY KEY (id)
);
 */
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
package org.entando.entando.plugins.jpversioning.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = TrashedResource.TABLE_NAME)
public class TrashedResource {
	
	public TrashedResource() {}
	
	@DatabaseField(columnName = "resid", 
			dataType = DataType.STRING, 
			width = 16, canBeNull = false, id = true)
	private String _resourceId;
	
	@DatabaseField(columnName = "restype", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = false)
	private String _resourceType;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20, canBeNull = false)
	private String _mainGroup;
	
	@DatabaseField(columnName = "resxml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _resourceXml;
	
	public static final String TABLE_NAME = "jpversioning_trashedresources";
	
}
/*
CREATE TABLE jpversioning_trashedresources
(
  resid character varying(16) NOT NULL,
  restype character varying(30) NOT NULL,
  descr character varying(100) NOT NULL,
  maingroup character varying(20) NOT NULL,
  resxml character varying NOT NULL,
  CONSTRAINT jpversioning_trashedresources_pkey PRIMARY KEY (resid)
);
*/
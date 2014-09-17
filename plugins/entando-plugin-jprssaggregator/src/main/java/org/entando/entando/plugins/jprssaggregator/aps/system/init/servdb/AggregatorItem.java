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
package org.entando.entando.plugins.jprssaggregator.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = AggregatorItem.TABLE_NAME)
public class AggregatorItem {
	
	public AggregatorItem() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _code;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _contentType;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			width = 255, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "link", 
			dataType = DataType.STRING, 
			width = 255, 
			canBeNull = false)
	private String _link;
	
	@DatabaseField(columnName = "delay", 
			dataType = DataType.DOUBLE, 
			canBeNull = false)
	private double _delay;
	
	@DatabaseField(columnName = "lastupdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _lastupdate;
	
	@DatabaseField(columnName = "categories", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _categories;
	
	public static final String TABLE_NAME = "jprssaggregator_aggregatoritems";
	
}
/*
CREATE TABLE jprssaggregator_aggregatoritems
(
  code integer NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(255) NOT NULL,
  link character varying(255) NOT NULL,
  delay bigint NOT NULL,
  lastupdate timestamp without time zone NOT NULL,
  categories character varying,
  CONSTRAINT jprssaggregator_aggregatoritems_pkey PRIMARY KEY (code)
);
 */
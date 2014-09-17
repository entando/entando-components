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
package org.entando.entando.plugins.jpnewsletter.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = SubscriberToken.TABLE_NAME)
public class SubscriberToken {
	
	public SubscriberToken() {}
	
	@DatabaseField(columnName = "mailaddress", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false, id = true)
	private String _attributeName;
	
	@DatabaseField(columnName = "token", 
			dataType = DataType.STRING, 
			width = 250, 
			canBeNull = false)
	private String _token;
	
	@DatabaseField(columnName = "regtime", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _regTime;
	
	public static final String TABLE_NAME = "jpnewsletter_subscribertokens";
	
}
/*
CREATE TABLE jpnewsletter_subscribertokens
(
  mailaddress character varying(100) NOT NULL,
  token character varying NOT NULL,
  regtime timestamp without time zone NOT NULL,
  CONSTRAINT jpnewsletter_subscribertokens_pkey PRIMARY KEY (mailaddress)
)
 */
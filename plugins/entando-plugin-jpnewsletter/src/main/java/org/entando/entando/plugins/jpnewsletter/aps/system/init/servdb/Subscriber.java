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
@DatabaseTable(tableName = Subscriber.TABLE_NAME)
public class Subscriber {
	
	public Subscriber() {}
	
	@DatabaseField(columnName = "mailaddress", 
			dataType = DataType.STRING,
			width = 100, 
			canBeNull = false, id = true)
	private String _mailAddress;
	
	@DatabaseField(columnName = "subscription_date", 
			dataType = DataType.DATE)
	private Date _subscriptionDate;
	
	@DatabaseField(columnName = "active", 
			dataType = DataType.SHORT)
	private short _active;
	
	public static final String TABLE_NAME = "jpnewsletter_subscribers";
	
}
/*
CREATE TABLE jpnewsletter_subscribers
(
  mailaddress character varying(100) NOT NULL,
  subscription_date date,
  active smallint,
  CONSTRAINT jpnewsletter_subscribers_pkey PRIMARY KEY (mailaddress)
)
 */
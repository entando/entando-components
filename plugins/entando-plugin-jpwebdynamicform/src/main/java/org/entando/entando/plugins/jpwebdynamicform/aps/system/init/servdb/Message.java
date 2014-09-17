/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpwebdynamicform.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Message.TABLE_NAME)
public class Message {
	
	public Message() {}
	
	@DatabaseField(columnName = "messageid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = false, id = true)
	private String _messageTd;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40)
	private String _username;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 3, 
			canBeNull = false)
	private String _langCode;
	
	@DatabaseField(columnName = "messagetype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _messageType;
	
	@DatabaseField(columnName = "creationdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _creationDate;
	
	@DatabaseField(columnName = "messagexml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _messageXml;
	
	public static final String TABLE_NAME = "jpwebdynamicform_messages";
	
}
/*
CREATE TABLE jpwebdynamicform_messages
(
  messageid character varying(16) NOT NULL,
  username character varying(40),
  langcode character varying(2) NOT NULL,
  messagetype character varying(30) NOT NULL,
  creationdate timestamp without time zone NOT NULL,
  messagexml character varying NOT NULL,
  CONSTRAINT jpwebdynamicform_messages_pkey PRIMARY KEY (messageid)
)
 */
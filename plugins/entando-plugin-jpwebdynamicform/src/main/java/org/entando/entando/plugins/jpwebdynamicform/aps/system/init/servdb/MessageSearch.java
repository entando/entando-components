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

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = MessageSearch.TABLE_NAME)
public class MessageSearch implements ExtendedColumnDefinition {
	
	public MessageSearch() {}
	
	@DatabaseField(foreign = true, columnName = "messageid", 
			width = 16, 
			canBeNull = false, index = true)
	private Message _messageId;
	
	@DatabaseField(columnName = "attrname", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _attributeName;
	
	@DatabaseField(columnName = "textvalue", 
			dataType = DataType.STRING)
	private String _textValue;
	
	@DatabaseField(columnName = "datevalue", 
			dataType = DataType.DATE)
	private Date _dateValue;
	
	@DatabaseField(columnName = "numvalue", 
			dataType = DataType.INTEGER)
	private int _numberValue;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 3)
	private String _langCode;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String messageTableName = Message.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			messageTableName = "`" + messageTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_fkey FOREIGN KEY (messageid) "
				+ "REFERENCES " + messageTableName + " (messageid)"};
	}
	
	public static final String TABLE_NAME = "jpwebdynamicform_search";
	
}
/*
CREATE TABLE jpwebdynamicform_messagesearch
(
  messageid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue date,
  numvalue integer,
  langcode character varying(2),
  CONSTRAINT jpwebdynamicform_messagesearch_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES jpwebdynamicform_messages (messageid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
ALTER TABLE jpwebdynamicform_messagesearch RENAME TO jpwebdynamicform_search;
 */
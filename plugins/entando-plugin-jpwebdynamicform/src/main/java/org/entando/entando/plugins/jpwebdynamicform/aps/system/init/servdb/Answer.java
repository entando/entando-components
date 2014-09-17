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
@DatabaseTable(tableName = Answer.TABLE_NAME)
public class Answer implements ExtendedColumnDefinition {
	
	public Answer() {}
	
	@DatabaseField(columnName = "answerid", 
			width = 16, 
			canBeNull = false, id = true)
	private String _answerId;
	
	@DatabaseField(foreign = true, columnName = "messageid", 
			width = 16, 
			canBeNull = false)
	private Message _messageId;
	
	@DatabaseField(columnName = "operator", 
			dataType = DataType.STRING, 
			width = 20)
	private String _operator;
	
	@DatabaseField(columnName = "senddate", 
			dataType = DataType.DATE,
			canBeNull = false)
	private Date _sendDate;
	
	@DatabaseField(columnName = "text", 
			dataType = DataType.LONG_STRING,
			canBeNull = false)
	private String _text;
	
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
	
	public static final String TABLE_NAME = "jpwebdynamicform_answers";
	
}
/*
CREATE TABLE jpwebdynamicform_messageanswers
(
  answerid character varying(16) NOT NULL,
  messageid character varying(16) NOT NULL,
  "operator" character varying(20),
  senddate timestamp without time zone NOT NULL,
  "text" character varying NOT NULL,
  CONSTRAINT jpwebdynamicform_messageanswers_pkey PRIMARY KEY (answerid),
  CONSTRAINT jpwebdynamicform_messageanswers_messageid_fkey FOREIGN KEY (messageid)
      REFERENCES jpwebdynamicform_messages (messageid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
ALTER TABLE jpwebdynamicform_messageanswers RENAME TO jpwebdynamicform_answers;
 */
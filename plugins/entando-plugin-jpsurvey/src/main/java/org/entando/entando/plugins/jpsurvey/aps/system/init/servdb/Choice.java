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
package org.entando.entando.plugins.jpsurvey.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Choice.TABLE_NAME)
public class Choice implements ExtendedColumnDefinition {
	
	public Choice() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(foreign = true, columnName = "questionid", 
			canBeNull = false)
	private Question _question;
	
	@DatabaseField(columnName = "choice", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _choice;
	
	@DatabaseField(columnName = "pos", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _pos;
	
	@DatabaseField(columnName = "freetext", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _freeText;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String questionTableName = Question.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			questionTableName = "`" + questionTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_questid_fkey FOREIGN KEY (questionid) "
				+ "REFERENCES " + questionTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpsurvey_choices";
	
}
/*
CREATE TABLE jpsurvey_choices
(
  id integer NOT NULL,
  questionid integer NOT NULL,
  choice character varying NOT NULL,
  pos smallint NOT NULL,
  freetext smallint NOT NULL,
  CONSTRAINT jpsurvey_answeres_pkey PRIMARY KEY (id),
  CONSTRAINT jpsurvey_answeres_questionid_fkey FOREIGN KEY (questionid)
      REFERENCES jpsurvey_questions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
); 
 */
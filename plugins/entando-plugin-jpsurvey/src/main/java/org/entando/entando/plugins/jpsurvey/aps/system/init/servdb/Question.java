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
@DatabaseTable(tableName = Question.TABLE_NAME)
public class Question implements ExtendedColumnDefinition {
	
	public Question() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(foreign = true, columnName = "surveyid", 
			canBeNull = false)
	private Survey _survey;
	
	@DatabaseField(columnName = "question", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _question;
	
	@DatabaseField(columnName = "pos", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _pos;
	
	@DatabaseField(columnName = "singlechoice", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _singleChoice;
	
	@DatabaseField(columnName = "minresponsenumber", 
			dataType = DataType.SHORT)
	private short _minresponsenumber;
	
	@DatabaseField(columnName = "maxresponsenumber", 
			dataType = DataType.SHORT)
	private short _maxresponsenumber;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String surveyTableName = Survey.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			surveyTableName = "`" + surveyTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_id_fkey FOREIGN KEY (surveyid) "
				+ "REFERENCES " + surveyTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpsurvey_questions";
	
}
/*
CREATE TABLE jpsurvey_questions
(
  id integer NOT NULL,
  surveyid integer NOT NULL,
  question character varying NOT NULL,
  pos smallint NOT NULL,
  singlechoice smallint NOT NULL,
  minresponsenumber smallint,
  maxresponsenumber smallint,
  CONSTRAINT jpsurvey_questions_pkey PRIMARY KEY (id),
  CONSTRAINT jpsurvey_questions_surveyid_fkey FOREIGN KEY (surveyid)
      REFERENCES jpsurvey (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) ;
 */
/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
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
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
import java.util.Date;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Voter.TABLE_NAME)
public class Voter implements ExtendedColumnDefinition {
	
	public Voter() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "age", 
			dataType = DataType.INTEGER)
	private int _age;
	
	@DatabaseField(columnName = "country", 
			dataType = DataType.STRING, 
			width = 2)
	private String _country;
	
	@DatabaseField(columnName = "sex", 
			dataType = DataType.CHAR)
	private char _sex;
	
	@DatabaseField(columnName = "votedate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _voteDate;
	
	@DatabaseField(foreign = true, columnName = "surveyid", 
			canBeNull = false)
	private Survey _survey;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _username;
	
	@DatabaseField(columnName = "ipaddress", 
			dataType = DataType.STRING, 
			width = 15, 
			canBeNull = false)
	private String _ipaddress;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String surveyTableName = Survey.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			surveyTableName = "`" + surveyTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_sid_fkey FOREIGN KEY (surveyid) "
				+ "REFERENCES " + surveyTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpsurvey_voters";
	
}
/*
CREATE TABLE jpsurvey_voters
(
  id integer NOT NULL,
  age smallint,
  country character varying(2),
  sex char,
  votedate date NOT NULL,
  surveyid integer NOT NULL,
  username character varying(30) NOT NULL,
  ipaddress character varying(15) NOT NULL,
  CONSTRAINT jpsurvey_voters_pkey PRIMARY KEY (id),
  CONSTRAINT surveyid FOREIGN KEY (surveyid)
      REFERENCES jpsurvey (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 */
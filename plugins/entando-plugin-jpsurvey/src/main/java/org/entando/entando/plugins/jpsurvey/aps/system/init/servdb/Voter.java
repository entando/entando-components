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
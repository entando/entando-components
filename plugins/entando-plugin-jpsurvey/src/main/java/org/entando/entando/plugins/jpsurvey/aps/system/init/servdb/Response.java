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
@DatabaseTable(tableName = Response.TABLE_NAME)
public class Response implements ExtendedColumnDefinition {
	
	public Response() {}
	
	@DatabaseField(foreign = true, columnName = "voterid", 
			canBeNull = false)
	private Voter _voter;
	
	@DatabaseField(foreign = true, columnName = "questionid", 
			canBeNull = false)
	private Question _question;
	
	@DatabaseField(foreign = true, columnName = "choiceid", 
			canBeNull = false)
	private Choice _choice;
	
	@DatabaseField(columnName = "freetext", 
			dataType = DataType.STRING, 
			width = 250)
	private String _freeText;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String voterTableName = Voter.TABLE_NAME;
		String questionTableName = Question.TABLE_NAME;
		String choiceTableName = Choice.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			voterTableName = "`" + voterTableName + "`";
			questionTableName = "`" + questionTableName + "`";
			choiceTableName = "`" + choiceTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_chid_fkey FOREIGN KEY (choiceid) "
				+ "REFERENCES " + choiceTableName + " (id)", 
			"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_vtid_fkey FOREIGN KEY (voterid) "
				+ "REFERENCES " + voterTableName + " (id)", 
			"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_qid_fkey FOREIGN KEY (questionid) "
				+ "REFERENCES " + questionTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpsurvey_responses";
	
}
/*
CREATE TABLE jpsurvey_responses
(
  voterid integer NOT NULL,
  questionid integer NOT NULL,
  choiceid integer NOT NULL,
  freetext character varying(30),
  CONSTRAINT choiceid FOREIGN KEY (choiceid)
      REFERENCES jpsurvey_choices (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT jpsurvey_responses_voterid_fkey FOREIGN KEY (voterid)
      REFERENCES jpsurvey_voters (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT questionid FOREIGN KEY (questionid)
      REFERENCES jpsurvey_questions (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (OIDS=TRUE);
 */
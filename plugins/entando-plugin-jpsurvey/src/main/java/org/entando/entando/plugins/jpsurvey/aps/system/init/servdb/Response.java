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
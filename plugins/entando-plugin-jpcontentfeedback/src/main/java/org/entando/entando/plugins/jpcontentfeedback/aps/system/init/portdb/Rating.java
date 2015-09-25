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
package org.entando.entando.plugins.jpcontentfeedback.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Rating.TABLE_NAME)
public class Rating implements ExtendedColumnDefinition {
	
	public Rating() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(foreign = true, columnName = "commentid", 
			canBeNull = true)
	private Comment _comment;
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16)
	private String _contentid;
	
	@DatabaseField(columnName = "voters", 
			dataType = DataType.INTEGER)
	private int _voters;
	
	@DatabaseField(columnName = "sumvote", 
			dataType = DataType.INTEGER)
	private int _sumvote;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String commentTableName = Comment.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			commentTableName = "`" + commentTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT jpcontentfeedback_rat_id_fkey FOREIGN KEY (commentid) "
				+ "REFERENCES " + commentTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpcontentfeedback_rating";
	
}
/*
CREATE TABLE jpcontentfeedback_rating
(
  id integer NOT NULL,
  commentid integer,
  contentid character varying(16),
  voters integer,
  sumvote integer,
  CONSTRAINT jpcontentfeedback_rating_pkey PRIMARY KEY (id),
  CONSTRAINT jpcontentfeedback_rating_refcommentid_fkey FOREIGN KEY (commentid)
      REFERENCES jpcontentfeedback_comments (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 */

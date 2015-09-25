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
package org.entando.entando.plugins.jpcrowdsourcing.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.IDatabaseManager;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Comment.TABLE_NAME)
public class Comment implements ExtendedColumnDefinition {
	
	public Comment() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(foreign = true, columnName = "ideaid", 
			width = 16, 
			canBeNull = false)
	private Idea _idea;
	
	@DatabaseField(columnName = "creationdate", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _creationDate;
	
	@DatabaseField(columnName = "commenttext", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _commenttext;

	@DatabaseField(columnName = "status", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _status;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, canBeNull = false)
	private String _username;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String ideaTableName = Idea.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			ideaTableName = "`" + ideaTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_idi_fkey FOREIGN KEY (ideaid) "
				+ "REFERENCES " + ideaTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpcollaboration_idea_comments";
	
}
/*
CREATE TABLE collaboration_idea_comments
(
  id integer NOT NULL,
  ideaid character varying(16) NOT NULL,
  creationdate timestamp without time zone,
  commenttext character varying NOT NULL,
  status integer NOT NULL,
  username character varying(40) NOT NULL,
  CONSTRAINT collaboration_idea_comments_pkey PRIMARY KEY (id)
)
 */

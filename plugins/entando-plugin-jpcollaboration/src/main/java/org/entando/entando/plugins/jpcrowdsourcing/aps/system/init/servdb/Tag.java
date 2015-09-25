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

import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.IDatabaseManager;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Tag.TABLE_NAME)
public class Tag implements ExtendedColumnDefinition {
	
	public Tag() {}
	
	@DatabaseField(foreign = true, columnName = "ideaid", 
			width = 16, 
			canBeNull = false)
	private Idea _idea;
	
	@DatabaseField(columnName = "catcode", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = false)
	private String _categoryCode;
	
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String ideaTableName = Idea.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			ideaTableName = "`" + ideaTableName + "`";
		}
		String[] queries = new String[2];
		queries[0] = "ALTER TABLE " + TABLE_NAME + " ADD CONSTRAINT "
				+ "collaboration_tags_pkey PRIMARY KEY(ideaid, catcode)";
		queries[1] = "ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT collaboration_tags_idi_fkey FOREIGN KEY (ideaid) "
				+ "REFERENCES " + ideaTableName + " (id)";
		return queries;
	}
	
	public static final String TABLE_NAME = "jpcollaboration_idea_tags";
	
}
/*
CREATE TABLE collaboration_idea_tags
(
  ideaid character varying(16) NOT NULL,
  catcode character varying(30) NOT NULL,
  categorytype character varying(30),
  CONSTRAINT collaboration_idea_tags_pkey PRIMARY KEY (ideaid, catcode)
)
*/
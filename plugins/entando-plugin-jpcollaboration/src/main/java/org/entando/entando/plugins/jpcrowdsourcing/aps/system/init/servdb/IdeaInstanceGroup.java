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
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.entando.entando.aps.system.init.model.servdb.Group;

/**
 * @author scaffolder
 */
@DatabaseTable(tableName = IdeaInstanceGroup.TABLE_NAME)
public class IdeaInstanceGroup implements ExtendedColumnDefinition {

	public IdeaInstanceGroup() {}

	@DatabaseField(columnName = "code", 
			dataType = DataType.STRING, 
			width=20,  canBeNull=false)
	private String _code;

	@DatabaseField(columnName = "groupname", 
			dataType = DataType.STRING, 
			width=20,  canBeNull=false)
	private String _groupname;

	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String groupsTableName = Group.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			groupsTableName = "`" + groupsTableName + "`";
		}
		String[] queries = new String[2];
		queries[0] = "ALTER TABLE " + TABLE_NAME + " "
				+ "ADD CONSTRAINT collaboration_insgr_pkey PRIMARY KEY(code, groupname)";
		queries[1] = "ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT collaboration_insgr_fkey_roles FOREIGN KEY (groupname) "
				+ "REFERENCES " + groupsTableName + " (groupname)";
		return queries;
	}

	public static final String TABLE_NAME = "jpcollaboration_ideainstance_group";
}

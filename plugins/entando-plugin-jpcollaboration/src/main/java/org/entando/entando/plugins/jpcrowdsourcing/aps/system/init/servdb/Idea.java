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

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.IDatabaseManager.DatabaseType;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Idea.TABLE_NAME)
public class Idea implements ExtendedColumnDefinition {
	
	public Idea() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.STRING, 
			width = 16, canBeNull = false, id = true)
	private String _id;
	
	@DatabaseField(columnName = "title", 
			dataType = DataType.STRING, 
			width = 255, canBeNull = false)
	private String _title;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _description;
	
	@DatabaseField(columnName = "pubdate", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _publicationDate;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, canBeNull = false)
	private String _username;
	
	@DatabaseField(columnName = "status", 
			dataType = DataType.INTEGER, 
			canBeNull = true)
	private int _status;
	
	@DatabaseField(columnName = "instancecode", 
			dataType = DataType.STRING, 
			width = 80, canBeNull = false)
	private String _instancecode;
	
	@DatabaseField(columnName = "votepositive", 
			dataType = DataType.INTEGER, 
			canBeNull = true)
	private int _votePositive;
	
	@DatabaseField(columnName = "votenegative", 
			dataType = DataType.INTEGER, 
			canBeNull = true)
	private int _voteNegative;
	
	@Override
	public String[] extensions(DatabaseType type) {
		String tableName = TABLE_NAME;
		String ideaInstanceTableName = IdeaInstance.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			ideaInstanceTableName = "`" + ideaInstanceTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " "
				+ "ADD CONSTRAINT " + TABLE_NAME + "_instance_fkey FOREIGN KEY (instancecode) "
				+ "REFERENCES " + ideaInstanceTableName + " (code)"};
	}
	
	public static final String TABLE_NAME = "jpcollaboration_idea";
	
}


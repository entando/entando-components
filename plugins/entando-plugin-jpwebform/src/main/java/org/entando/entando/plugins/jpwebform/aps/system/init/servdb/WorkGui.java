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
package org.entando.entando.plugins.jpwebform.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = WorkGui.TABLE_NAME)
public class WorkGui implements ExtendedColumnDefinition {
	
	public WorkGui() {}
	
	@DatabaseField(columnName = "typecode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _typeCode;
	
	@DatabaseField(columnName = "stepcode", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _stepCode;
	
	@DatabaseField(columnName = "gui", 
			dataType = DataType.LONG_STRING)
	private String _gui;
	
	@DatabaseField(columnName = "css", 
			dataType = DataType.LONG_STRING)
	private String _css;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_fkey PRIMARY KEY (typecode , stepcode ) "};
	}
	
	public static final String TABLE_NAME = "jpwebform_workgui";
	
}
/*
 * 
CREATE TABLE jpwebform_workgui
(
  typecode character varying(30) NOT NULL,
  stepcode character varying(40) NOT NULL,
  gui character varying,
  css character varying,
  CONSTRAINT jpwebform_workgui_pkey PRIMARY KEY (typecode , stepcode )
)
 * 
 */
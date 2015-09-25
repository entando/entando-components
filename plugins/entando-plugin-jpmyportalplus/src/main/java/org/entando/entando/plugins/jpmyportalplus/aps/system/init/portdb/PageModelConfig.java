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
package org.entando.entando.plugins.jpmyportalplus.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.model.portdb.PageModel;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = PageModelConfig.TABLE_NAME)
public class PageModelConfig implements ExtendedColumnDefinition {
	
	public PageModelConfig() {}
	
	@DatabaseField(foreign = true, columnName = "code", 
			width = 40, 
			canBeNull = false, unique = true)
	private PageModel _pagemodel;
	
	@DatabaseField(columnName = "config", 
			dataType = DataType.LONG_STRING)
	private String _config;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String pageModelTableName = PageModel.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			pageModelTableName = "`" + pageModelTableName + "`";
		}
		String[] queries = new String[1];
		queries[0] = "ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT jpmyportalplus_pagemodel_fkey FOREIGN KEY (code) "
				+ "REFERENCES " + pageModelTableName + " (code)";
		return queries;
	}
	
	public static final String TABLE_NAME = "jpmyportalplus_modelconfig";
	
}
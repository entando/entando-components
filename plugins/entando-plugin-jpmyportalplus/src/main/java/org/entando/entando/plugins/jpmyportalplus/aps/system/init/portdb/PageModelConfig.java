/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
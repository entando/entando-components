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
package org.entando.entando.plugins.jpnewsletter.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Recipient.TABLE_NAME)
public class Recipient implements ExtendedColumnDefinition {
	
	public Recipient() {}
	
	@DatabaseField(foreign = true, columnName = "contentreportid", 
			canBeNull = false)
	private ContentReport _contentReportId;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, canBeNull = false)
	private String _username;
	
	@DatabaseField(columnName = "mailaddress", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _mailAddress;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String contentReportTableName = ContentReport.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			contentReportTableName = "`" + contentReportTableName + "`";
		}
		return new String[]{"ALTER TABLE " + TABLE_NAME + " "
				+ "ADD CONSTRAINT " + TABLE_NAME + "_pkey PRIMARY KEY(contentreportid, username)", 
			"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_fkey FOREIGN KEY (contentreportid) "
				+ "REFERENCES " + contentReportTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpnewsletter_recipient";
	
}
/*
CREATE TABLE jpnewsletter_recipient
(
  contentreportid integer NOT NULL,
  username character varying(40) NOT NULL,
  mailaddress character varying(100) NOT NULL,
  CONSTRAINT jpnewsletterdest_pkey PRIMARY KEY (contentreportid, username),
  CONSTRAINT jpnewsletterdest_recipient_refcontentreport_fkey FOREIGN KEY (contentreportid)
      REFERENCES jpnewsletter_contentreport (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 */
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
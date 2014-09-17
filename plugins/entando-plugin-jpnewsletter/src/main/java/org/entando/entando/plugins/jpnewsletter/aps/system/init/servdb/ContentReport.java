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
@DatabaseTable(tableName = ContentReport.TABLE_NAME)
public class ContentReport implements ExtendedColumnDefinition {
	
	public ContentReport() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(foreign = true, columnName = "newsletterid", 
			canBeNull = false)
	private Report _newsletterId;
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, canBeNull = false)
	private String _contentId;
	
	@DatabaseField(columnName = "textbody", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _textBody;
	
	@DatabaseField(columnName = "htmlbody", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _htmlBody;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String reportTableName = Report.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			reportTableName = "`" + reportTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT jpnewsletter_contrep_fkey FOREIGN KEY (newsletterid) "
				+ "REFERENCES " + reportTableName + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpnewsletter_contentreport";
	
}
/*
CREATE TABLE jpnewsletter_contentreport
(
  id integer NOT NULL,
  newsletterid integer NOT NULL,
  contentid character varying(16) NOT NULL,
  textbody character varying NOT NULL,
  htmlbody character varying,
  CONSTRAINT jpnewsletter_contentreport_pkey PRIMARY KEY (id),
  CONSTRAINT jpnewsletter_contentreport_refnewsletterreport_fkey FOREIGN KEY (newsletterid)
      REFERENCES jpnewsletter_newsletterreport (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 */
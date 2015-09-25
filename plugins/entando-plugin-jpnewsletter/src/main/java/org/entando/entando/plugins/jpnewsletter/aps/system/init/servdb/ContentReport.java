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
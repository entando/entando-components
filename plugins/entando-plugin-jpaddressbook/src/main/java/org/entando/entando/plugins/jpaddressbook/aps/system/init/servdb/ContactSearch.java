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
package org.entando.entando.plugins.jpaddressbook.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ContactSearch.TABLE_NAME)
public class ContactSearch implements ExtendedColumnDefinition {
	
	public ContactSearch() {}
	
	@DatabaseField(foreign = true, columnName = "contactkey", 
			width = 40, canBeNull = false, index = true)
	private Contact _contactKey;
	
	@DatabaseField(columnName = "attrname", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _attributeName;
	
	@DatabaseField(columnName = "textvalue", 
			dataType = DataType.STRING)
	private String _textValue;
	
	@DatabaseField(columnName = "datevalue", 
			dataType = DataType.DATE)
	private Date _dateValue;
	
	@DatabaseField(columnName = "numvalue", 
			dataType = DataType.INTEGER)
	private int _numberValue;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 3)
	private String _langCode;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String contactTableName = Contact.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			contactTableName = "`" + contactTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT jpaddressbook_search_fkey FOREIGN KEY (contactkey) "
				+ "REFERENCES " + contactTableName + " (contactkey)"};
	}
	
	public static final String TABLE_NAME = "jpaddressbook_contactsearch";
	
}
/*
CREATE TABLE jpaddressbook_contactsearch
(
  contactkey character varying(40) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue date,
  numvalue integer,
  langcode character varying(2),
  CONSTRAINT jpaddressbook_contactsearch_contactkey_fkey FOREIGN KEY (contactkey)
      REFERENCES jpaddressbook_contacts (contactkey) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */
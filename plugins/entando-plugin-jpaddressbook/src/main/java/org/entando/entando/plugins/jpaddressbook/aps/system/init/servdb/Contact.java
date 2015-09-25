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

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Contact.TABLE_NAME)
public class Contact {
	
	public Contact() {}
	
	@DatabaseField(columnName = "contactkey", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false, id = true)
	private String _contactKey;
	
	@DatabaseField(columnName = "profiletype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _profileType;
	
	@DatabaseField(columnName = "contactxml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _contactXml;
	
	@DatabaseField(columnName = "contactowner", 
			dataType = DataType.STRING, 
			width = 40, canBeNull = false)
	private String _contactOwner;
	
	@DatabaseField(columnName = "publiccontact", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _publicContact;
	
	public static final String TABLE_NAME = "jpaddressbook_contacts";
	
}
/*
CREATE TABLE jpaddressbook_contacts
(
  contactkey character varying(40) NOT NULL,
  profiletype character varying(30) NOT NULL,
  contactxml character varying NOT NULL,
  contactowner character varying(40) NOT NULL,
  publiccontact smallint NOT NULL,
  CONSTRAINT jpaddressbook_contacts_pkey PRIMARY KEY (contactkey)
)
 */
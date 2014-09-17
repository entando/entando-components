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
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
package com.agiletec.plugins.jpaddressbook.aps.system;

/**
 * Interface for 'jpaddressbook' plugin system constants.
 * @author E.Mezzano
 */
public interface JpaddressbookSystemConstants {

	/**
	 * Address Book Messages manager bean name
	 */
	public static final String ADDRESSBOOK_MANAGER = "jpaddressbookAddressBookManager";
	
	/**
	 * VCard manager bean name
	 */
	public static final String VCARD_MANAGER = "jpaddressbookVCardManager";
	
	/**
	 * Vcard item configuration
	 */
	public static final String VCARD_MAPPING_ITEM = "jpaddressbook_vcardConfig";
	
	public static final String ATTRIBUTE_TYPE_DATE = "Date";
	public static final String ATTRIBUTE_TYPE_ENUMERATOR = "Enumerator";
	public static final String ATTRIBUTE_TYPE_HYPERTEXT = "Hypertext";
	public static final String ATTRIBUTE_TYPE_LONGTEXT = "Longtext";
	public static final String ATTRIBUTE_TYPE_MONOTEXT = "Monotext";
	public static final String ATTRIBUTE_TYPE_NUMBER = "Number";
	public static final String ATTRIBUTE_TYPE_TEXT = "Text";

	public static final String VCARD_FIELD_NAME = "NAME";
	public static final String VCARD_FIELD_SURNAME = "SURNAME";
	public static final String VCARD_FIELD_TITLE = "TITLE";
	public static final String VCARD_FIELD_ROLE = "ROLE";
	public static final String VCARD_FIELD_ORG = "ORG";
	public static final String VCARD_FIELD_BDAY = "BDAY";
	public static final String VCARD_FIELD_ADDR_HOME_COUNTRY = "ADDR_HOME_COUNTRY";
	public static final String VCARD_FIELD_ADDR_HOME_REGION = "ADDR_HOME_REGION";
	public static final String VCARD_FIELD_ADDR_HOME_STREET = "ADDR_HOME_STREET";
	public static final String VCARD_FIELD_ADDR_HOME_CITY = "ADDR_HOME_CITY";
	public static final String VCARD_FIELD_ADDR_HOME_CAP = "ADDR_HOME_CAP";
	public static final String VCARD_FIELD_TEL_CELL = "TEL_CELL";
	public static final String VCARD_FIELD_TEL_HOME = "TEL_HOME";
	public static final String VCARD_FIELD_TEL_WORK = "TEL_WORK";
	public static final String VCARD_FIELD_TEL_MODEM = "TEL_MODEM";
	public static final String VCARD_FIELD_TEL_FAX = "TEL_FAX";
	public static final String VCARD_FIELD_EMAIL_FIRST = "EMAIL_FIRST";
	public static final String VCARD_FIELD_EMAIL_SECOND = "EMAIL_SECOND";
	public static final String VCARD_FIELD_EMAIL_THIRD = "EMAIL_THIRD";
	public static final String VCARD_FIELD_NOTE = "NOTE";
	public static final String VCARD_FIELD_URL = "URL";

	public static final String DEFAULT_DATE = "00/00/0000";
	
	public static final String ATTRIBUTE_DISABLING_CODE_ON_MANAGE_CONTACT = "jpaddressbook:hideOnAddressBook";
	
}
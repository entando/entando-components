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
package com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;

/**
 * @author E.Santoboni
 */
public interface IAddressBookManager {
	
	public void addContact(IContact contact) throws ApsSystemException;
	
	public void deleteContact(String owner, String contactKey) throws ApsSystemException;
	
	public void updateContact(String owner, IContact contact) throws ApsSystemException;
	
	public IContact getContact(String contactKey) throws ApsSystemException;
	
	public List<String> getAllowedContacts(String owner, EntitySearchFilter filters[]) throws ApsSystemException;

	public static final String CONTACT_OWNER_FILTER_KEY = "owner";
	
}

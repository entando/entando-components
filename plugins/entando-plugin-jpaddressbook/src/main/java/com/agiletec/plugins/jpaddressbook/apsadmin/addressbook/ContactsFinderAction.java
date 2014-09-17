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
package com.agiletec.plugins.jpaddressbook.apsadmin.addressbook;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityFinderAction;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class ContactsFinderAction extends AbstractApsEntityFinderAction {
	
	@Override
	public List<String> getSearchResult() {
		List<String> result = null;
		try {
			String owner = this.getCurrentUser().getUsername();
			result = this.getAddressBookManager().getAllowedContacts(owner, this.createFilters());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSearchResult");
			throw new RuntimeException("Error on search Address Book Contacts", t);
		}
		return result;
	}
	
	public IUserProfile getUserProfilePrototype() {
		return (IUserProfile) this.getUserProfileManager().getDefaultProfileType();
	}
	
	@Override
	public IApsEntity getEntityPrototype() {
		return this.getUserProfileManager().getDefaultProfileType();
	}
	
	@Override
	protected IApsEntity getEntity(String entityId) {
		IContact contact = this.getContact(entityId);
		if (null != contact) {
			return contact.getContactInfo();
		}
		return null;
	}
	
	@Override
	protected void deleteEntity(String entityId) throws Throwable {
		this.getAddressBookManager().deleteContact(this.getCurrentUser().getUsername(), entityId);
	}
	
	private EntitySearchFilter[] createFilters() {
		return super.getFilters();
	}
	
	public IContact getContact(String key) {
		try {
			return this.getAddressBookManager().getContact(key);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContact");
			throw new RuntimeException("Error on search Address Book Contact", t);
		}
	}
	
	@Override
	public List<AttributeInterface> getSearcheableAttributes() {
		List<AttributeInterface> searcheableAttributes = new ArrayList<AttributeInterface>();
		IUserProfile defaultProfile = this.getUserProfileManager().getDefaultProfileType();
		defaultProfile.disableAttributes(JpaddressbookSystemConstants.ATTRIBUTE_DISABLING_CODE_ON_MANAGE_CONTACT);
		List<AttributeInterface> contentAttributes = defaultProfile.getAttributeList();
		for (int i=0; i<contentAttributes.size(); i++) {
			AttributeInterface attribute = contentAttributes.get(i);
			if (attribute.isActive() && attribute.isSearcheable()) {
				searcheableAttributes.add(attribute);
			}
		}
		return searcheableAttributes;
	}
	
	protected String checkDeletingEntity() throws Throwable {
		String checkResult = super.checkDeletingEntity();
		if (null != checkResult) return checkResult;
		IContact contact = this.getAddressBookManager().getContact(this.getEntityId());
		if (!this.getCurrentUser().getUsername().equals(contact.getOwner())) {
			this.addFieldError("contactKey", this.getText("*CONTATTO NON PROPRIETARIO*"));//TODO LABEL DA MODIFICARE
			return INPUT;
		}
		return null;
	}
	
	@Override
	protected IEntityManager getEntityManager() {
		return this.getUserProfileManager();
	}
	
	protected IAddressBookManager getAddressBookManager() {
		return _addressBookManager;
	}
	public void setAddressBookManager(IAddressBookManager addressBookManager) {
		this._addressBookManager = addressBookManager;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private IAddressBookManager _addressBookManager;
	private IUserProfileManager _userProfileManager;
	
}
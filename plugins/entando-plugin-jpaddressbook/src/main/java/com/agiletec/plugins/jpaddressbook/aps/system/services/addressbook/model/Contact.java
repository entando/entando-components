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
package com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model;

import java.util.List;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class Contact implements IContact {
	
	public Contact(IUserProfile contactInfo) {
		this.setContactInfo(contactInfo);
	}
	
	@Override
	public String getId() {
		return this._id;
	}
	
	public void setId(String id) {
		this._id = id;
	}
	
	@Override
	public Object getValue(String key) {
		return this.getContactInfo().getValue(key);
	}
	
	@Override
	public Object getAttribute(String key) {
		return this.getContactInfo().getAttribute(key);
	}
	
	@Override
	public List<AttributeInterface> getAttributes() {
		return this.getContactInfo().getAttributeList();
	}
	
	@Override
	public String getOwner() {
		return _owner;
	}
	public void setOwner(String owner) {
		this._owner = owner;
	}
	
	@Override
	public boolean isPublicContact() {
		return _publicContact;
	}
	public void setPublicContact(boolean publicContact) {
		this._publicContact = publicContact;
	}
	
	@Override
	public IUserProfile getContactInfo() {
		return contactInfo;
	}
	private void setContactInfo(IUserProfile contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	private String _id;
	private IUserProfile contactInfo;
	private String _owner;
	private boolean _publicContact;
	
}

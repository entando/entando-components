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

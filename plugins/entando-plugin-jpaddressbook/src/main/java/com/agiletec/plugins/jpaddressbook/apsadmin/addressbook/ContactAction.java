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
package com.agiletec.plugins.jpaddressbook.apsadmin.addressbook;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.Contact;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;

import java.util.List;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class ContactAction extends AbstractApsEntityAction implements IContactAction {
	
	@Override
	public void validate() {
		if (this.getContact() != null) {
			super.validate();
		}
	}
	
	@Override
	public IApsEntity getApsEntity() {
		return this.getContact().getContactInfo();
	}
	
	public IContact getContact() {
		return (IContact) this.getRequest().getSession().getAttribute(SESSION_PARAM_NAME_CURRENT_CONTACT);
	}
	
	@Override
	public String view() {
		return this.edit();
	}
	
	@Override
	public String createNew() {
		try {
			IUserProfile defaultProfile = this.getUserProfileManager().getDefaultProfileType();
			defaultProfile.disableAttributes(JpaddressbookSystemConstants.ATTRIBUTE_DISABLING_CODE_ON_MANAGE_CONTACT);
			Contact contact = new Contact(defaultProfile);
			contact.setOwner(this.getCurrentUser().getUsername());
			this.getRequest().getSession().setAttribute(SESSION_PARAM_NAME_CURRENT_CONTACT, contact);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createNew");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String edit() {
		try {
			IContact contact = this.getAddressBookManager().getContact(this.getEntityId());
			if (null == contact) {
				this.addFieldError("contactKey", this.getText("*CONTATTO NULLO*"));//TODO LABEL DA MODIFICARE
				return INPUT;
			}
			if (!contact.isPublicContact() && !this.getCurrentUser().getUsername().equals(contact.getOwner())) {
				this.addFieldError("contactKey", this.getText("*CONTATTO NON PUBBLICO NON AUTORIZZATO*"));//TODO LABEL DA MODIFICARE
				return INPUT;
			}
			contact.getContactInfo().disableAttributes(JpaddressbookSystemConstants.ATTRIBUTE_DISABLING_CODE_ON_MANAGE_CONTACT);
			this.getRequest().getSession().setAttribute(SESSION_PARAM_NAME_CURRENT_CONTACT, contact);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void checkTypeLabels(IContact contact) {
		if (null == contact) {
			return;
		}
		try {
			List<AttributeInterface> attributes = contact.getAttributes();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				String attributeLabelKey = "jpaddressbook_ATTR" + attribute.getName();
				if (null == this.getI18nManager().getLabelGroup(attributeLabelKey)) {
					String attributeDescription = attribute.getDescription();
					String value = (null != attributeDescription && attributeDescription.trim().length() > 0) ? 
							attributeDescription :
							attribute.getName();
					this.addLabelGroups(attributeLabelKey, value);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "checkTypeLables");
			throw new RuntimeException("Error checking label types", t);
		}
	}
	
	protected void addLabelGroups(String key, String defaultValue) throws ApsSystemException {
		try {
			ApsProperties properties = new ApsProperties();
			Lang defaultLang = super.getLangManager().getDefaultLang();
			properties.put(defaultLang.getCode(), defaultValue);
			this.getI18nManager().addLabelGroup(key, properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addLabelGroups");
			throw new RuntimeException("Error adding label groups - key '" + key + "'", t);
		}
	}
	
	@Override
	public String save() {
		try {
			IContact contact = this.getContact();
			if (contact == null) {
				return FAILURE;
			}
			if (null == contact.getId()) {
				this.getAddressBookManager().addContact(contact);
			} else {
				this.getAddressBookManager().updateContact(this.getCurrentUser().getUsername(), contact);
			}
			this.getRequest().getSession().removeAttribute(SESSION_PARAM_NAME_CURRENT_CONTACT);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
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
	
	private II18nManager _i18nManager;
	private IAddressBookManager _addressBookManager;
	private IUserProfileManager _userProfileManager;
	
}
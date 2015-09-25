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
package com.agiletec.plugins.jpaddressbook.apsadmin.addressbook.attribute.action.list;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.entity.IEntityActionHelper;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;
import com.agiletec.plugins.jpaddressbook.apsadmin.addressbook.IContactAction;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * Action class for the management of operations on the list type attributes in Address Book Contact.
 * @author E.Santoboni
 */
public class ListAttributeAction extends com.agiletec.apsadmin.system.entity.attribute.action.list.ListAttributeAction {
	
	@Override
	protected IApsEntity getCurrentApsEntity() {
		IUserProfile userProfile = this.updateContactOnSession();
		return userProfile;
	}
	
	public IContact getContact() {
		return (IContact) this.getRequest().getSession().getAttribute(IContactAction.SESSION_PARAM_NAME_CURRENT_CONTACT);
	}
	
	protected IUserProfile updateContactOnSession() {
		IUserProfile userProfile = this.getContact().getContactInfo();
		if (null != userProfile) {
			this.getEntityActionHelper().updateEntity(userProfile, this.getRequest());
		}
		return userProfile;
	}
	
	protected IEntityActionHelper getEntityActionHelper() {
		return _entityActionHelper;
	}
	public void setEntityActionHelper(IEntityActionHelper entityActionHelper) {
		this._entityActionHelper = entityActionHelper;
	}
	
	private IEntityActionHelper _entityActionHelper;

}
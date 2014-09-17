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
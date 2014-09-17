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
package com.agiletec.plugins.jpaddressbook.apsadmin.addressbook.helper;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.entity.EntityActionHelper;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;
import com.agiletec.plugins.jpaddressbook.apsadmin.addressbook.IContactAction;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class ContactActionHelper extends EntityActionHelper {
	
	@Override
	public void updateEntity(IApsEntity currentEntity, HttpServletRequest request) {
		IContact contact = (IContact) request.getSession().getAttribute(IContactAction.SESSION_PARAM_NAME_CURRENT_CONTACT);
		IUserProfile profile = contact.getContactInfo();
		super.updateEntity(profile, request);
	}
	
}

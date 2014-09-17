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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.registration;

import com.agiletec.aps.system.SystemConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.BaseAction;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;

public class ProfileTypesAction extends BaseAction {
	
	public List<IApsEntity> getProfileTypes() {
		if (this._profileTypes == null) {
			this._profileTypes = new ArrayList<IApsEntity>();
			Iterator<IApsEntity> prototypesIter = this.getUserProfileManager().getEntityPrototypes().values().iterator();
			while (prototypesIter.hasNext()) {
				IApsEntity profile = prototypesIter.next();
				if (profile.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL) != null) {
					this._profileTypes.add(profile);
				}
			}
		}
		return this._profileTypes;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private List<IApsEntity> _profileTypes;
	
	private IUserProfileManager _userProfileManager;
	
}
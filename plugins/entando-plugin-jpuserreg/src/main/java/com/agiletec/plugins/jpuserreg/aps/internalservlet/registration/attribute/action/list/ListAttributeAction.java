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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.registration.attribute.action.list;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.entity.IEntityActionHelper;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.registration.UserRegistrationAction;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * Action classe for the management of operations on the list type attributes in User Profile.
 * @author E.Santoboni
 */
public class ListAttributeAction extends com.agiletec.apsadmin.system.entity.attribute.action.list.ListAttributeAction {
	
	@Override
	protected IApsEntity getCurrentApsEntity() {
		IUserProfile userProfile = this.updateUserProfileOnSession();
		return userProfile;
	}
	
	public IUserProfile getUserProfile() {
		return (IUserProfile) this.getRequest().getSession().getAttribute(UserRegistrationAction.SESSION_PARAM_NAME_REQ_PROFILE);
	}
	
	protected IUserProfile updateUserProfileOnSession() {
		IUserProfile userProfile = this.getUserProfile();
		if (null != userProfile) {
			this.getEntityActionHelper().updateEntity(userProfile, this.getRequest());
		}
		return userProfile;
	}
	
	public String getProfileTypeCode() {
		return _profileTypeCode;
	}
	public void setProfileTypeCode(String profileTypeCode) {
		this._profileTypeCode = profileTypeCode;
	}
	
	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}
	
	public void setEmailConfirm(String emailConfirm) {
		this._emailConfirm = emailConfirm;
	}
	public String getEmailConfirm() {
		return _emailConfirm;
	}
	
	public void setPrivacyPolicyAgreement(boolean privacyPolicyAgreement) {
		this._privacyPolicyAgreement = privacyPolicyAgreement;
	}
	public boolean isPrivacyPolicyAgreement() {
		return _privacyPolicyAgreement;
	}
	
	protected IEntityActionHelper getEntityActionHelper() {
		return _entityActionHelper;
	}
	public void setEntityActionHelper(IEntityActionHelper entityActionHelper) {
		this._entityActionHelper = entityActionHelper;
	}
	
	private String _profileTypeCode;
	private String _username;
	private String _emailConfirm;
	private boolean _privacyPolicyAgreement = false;
	
	private IEntityActionHelper _entityActionHelper;
	
}
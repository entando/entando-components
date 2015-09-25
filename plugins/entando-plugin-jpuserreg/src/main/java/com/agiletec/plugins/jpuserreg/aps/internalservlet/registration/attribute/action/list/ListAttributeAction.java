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
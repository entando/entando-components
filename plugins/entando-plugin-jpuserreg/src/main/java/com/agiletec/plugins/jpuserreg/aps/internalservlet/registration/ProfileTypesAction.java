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
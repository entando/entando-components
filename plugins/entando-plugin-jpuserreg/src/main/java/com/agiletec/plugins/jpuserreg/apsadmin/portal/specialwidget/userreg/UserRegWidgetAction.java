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
package com.agiletec.plugins.jpuserreg.apsadmin.portal.specialwidget.userreg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jpuserreg.aps.JpUserRegSystemConstants;

public class UserRegWidgetAction extends SimpleWidgetConfigAction {
	
	@Override
	public void validate() {
		super.validate();
		try {
			boolean validType = false;
			String typeCode = this.getTypeCode();
			if (typeCode!=null) {
				IApsEntity entity = this.getUserProfileManager().getProfileType(typeCode);
				if (entity!=null && entity.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL)!=null) {
					validType = true;
				}
			}
			if (!validType) {
				this.addFieldError("typeCode", this.getText("jpuserreg.error.typeCode.required"));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate");
		}
	}
	
	@Override
	public String init() {
		String result = super.init();
		try {
			if (result.equals(SUCCESS)) {
				String paramName = JpUserRegSystemConstants.TYPECODE_SHOWLET_PARAM;
				String typeCode = this.getWidget().getConfig().getProperty(paramName);
				this.setTypeCode(typeCode);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "init");
			return FAILURE;
		}
		return result;
	}
	
	public List<IApsEntity> getProfileTypes() {
		if (this._profileTypes==null) {
			this._profileTypes = new ArrayList<IApsEntity>();
			Iterator<IApsEntity> prototypesIter = this.getUserProfileManager().getEntityPrototypes().values().iterator();
			while (prototypesIter.hasNext()) {
				IApsEntity profile = prototypesIter.next();
				if (profile.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL)!=null) {
					this._profileTypes.add(profile);
				}
			}
		}
		return this._profileTypes;
	}
	
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private String _typeCode;
	private List<IApsEntity> _profileTypes;
	
	private IUserProfileManager _userProfileManager;
	
}
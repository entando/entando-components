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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.recover;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;

/**
 * Struts Action for managing requests for password recover by username
 * @author G.Cocco
 * */
public class UserRecoverFromUsernameAction extends BaseAction implements IUserRecoverFromUsernameAction {
	
	@Override
	public String initRecover() {
		UserDetails userDetails = this.getCurrentUser();
		if (null != userDetails && !userDetails.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
			return "loggedUser";
		}
		return SUCCESS;
	}
	
	/**
	 * Password recover from username
	 * */
	@Override
	public String recoverFromUsername() {
		try {
			this.getUserRegManager().reactivationByUserName(this.getUsername());
		} catch (Throwable t) {
			this.addActionError(this.getText("Errors.userRecover.genericError"));
			ApsSystemUtils.logThrowable(t, this, "recoverFromUsername");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}
	
	public void setUserRegManager(IUserRegManager userRegManager) {
		this._userRegManager = userRegManager;
	}
	protected IUserRegManager getUserRegManager() {
		return _userRegManager;
	}

	private String _username;
	private IUserRegManager _userRegManager;
	
}
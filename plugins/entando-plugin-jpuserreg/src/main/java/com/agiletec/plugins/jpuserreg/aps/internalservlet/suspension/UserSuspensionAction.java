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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.suspension;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.common.UserRegBaseAction;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;
import org.slf4j.Logger;

/**
 * Struts Action to manage account suspension, user disabled.
 * @author G.Cocco
 * */
public class UserSuspensionAction extends UserRegBaseAction implements IUserSuspensionAction {
	
	@Override
	public void validate() {
		try {
			UserDetails user = this.getCurrentUser();
			UserDetails guestUser = this.getUserManager().getGuestUser();
			if ( null == user || user.getUsername().equals(guestUser.getUsername())) {
//				FIX ME
				this.sendHomeRedirect();
				return;
			}
			super.validate();
			if (this.hasFieldErrors()) return;
			UserDetails userDetails = this.getUserManager().getUser(user.getUsername(), this.getPassword());
			if (null == userDetails) {
				this.addFieldError("password", this.getText("jpuserreg.suspension.password.wrong"));
			}
		} catch (Throwable t) {
			throw new RuntimeException("Error validation of request for account suspension", t);
		}
	}
	
	@Override
	public String initSuspension() {
		Logger log = ApsSystemUtils.getLogger();
		try {
			UserDetails user = this.getCurrentUser();
			UserDetails guestUser = this.getUserManager().getGuestUser();
			if ( null == user || user.getUsername().equals(guestUser.getUsername())) {
//				FIX ME
				this.sendHomeRedirect();
				return null;
			}
			log.info(" Request for disabling user " + user.getUsername());
			boolean isAdmin = this.getAuthManager().isAuthOnGroup(user, Group.ADMINS_GROUP_NAME) 
										&& this.getAuthManager().isAuthOnPermission(user, Permission.SUPERUSER);
			
			if (isAdmin || !user.isJapsUser()) {
				log.info(" User " + user.getUsername() + " cannot be disabled because is not a local jAPS user.");
				return USERREG_ERROR;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "suspendAccountInit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String suspend() {
		try {
			UserDetails user = this.getCurrentUser();
			this.getUserRegManager().deactivateUser(user);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "suspendAccount");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected IUserRegManager getUserRegManager() {
		return _userRegManager;
	}
	public void setUserRegManager(IUserRegManager userRegManager) {
		this._userRegManager = userRegManager;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	public void setPassword(String password) {
		this._password = password;
	}
	public String getPassword() {
		return _password;
	}

	public void setAuthManager(IAuthorizationManager authManager) {
		this._authManager = authManager;
	}
	protected IAuthorizationManager getAuthManager() {
		return _authManager;
	}

	private IUserRegManager _userRegManager;
	private IUserManager _userManager;
	private String _password;
	private IAuthorizationManager _authManager;
	
}
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
public class UserSuspensionAction extends UserRegBaseAction {
	
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
			if (isAdmin || !user.isEntandoUser()) {
				log.info(" User " + user.getUsername() + " cannot be disabled because is not a local Entando user.");
				return USERREG_ERROR;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "suspendAccountInit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
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
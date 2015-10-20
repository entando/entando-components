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
public class UserRecoverFromUsernameAction extends BaseAction {
	
	/**
	 * Initialize functionality and redirect if user is already logged
	 * @return The action result
	 */
	public String initRecover() {
		UserDetails userDetails = this.getCurrentUser();
		if (null != userDetails && !userDetails.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
			return "loggedUser";
		}
		return SUCCESS;
	}
	
	/**
	 * Password recover from username.
	 * @return The action result
	 */
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
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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.activation;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.common.UserRegBaseAction;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;

/**
 * Struts Action to manage account activation
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 * */
public class UserActivationAction extends UserRegBaseAction implements IUserActivationAction {

	@Override
	public String initActivation() {
		try {
			Boolean alreadyActivated = 
				(Boolean) this.getRequest().getSession().getAttribute(ACTIVATION_TOKEN_CHECK_PARAM_PREFIX + this.getToken());
			if (null != alreadyActivated && alreadyActivated) {
				this.sendHomeRedirect();
				return null;
			}
			String username = this.extractUsername();
			if (username == null || username.length() == 0) {
				return "activationError";
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "activationAccountInit");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String activate() {
		try {
			String username = this.extractUsername();
			if (username == null || username.length() == 0) {
				return "activationError";
			}
			this.getUserRegManager().activateUser(username, this.getPassword(), this.getToken());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "activationAccount");
			return FAILURE;
		}
		this.getRequest().getSession().setAttribute(ACTIVATION_TOKEN_CHECK_PARAM_PREFIX + this.getToken(), true);
		return SUCCESS;
	}

	
	@Override
	public String initReactivation() {
		try {
			Boolean alreadyActivated = 
				(Boolean) this.getRequest().getSession().getAttribute(REACTIVATION_TOKEN_CHECK_PARAM_PREFIX + this.getToken());
			if (null != alreadyActivated && alreadyActivated) {
				this.sendHomeRedirect();
				return null;
			}
			String username = this.extractUsername();
			if (username == null || username.length() == 0) {
				return "activationError";
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "activationAccountInit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String reactivate() {
		try {
			String username = this.extractUsername();
			if (username == null || username.length() == 0) {
				return "activationError";
			}
			this.getUserRegManager().reactivateUser(username, this.getPassword(), this.getToken());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "reactivationAccount");
			return FAILURE;
		}
		this.getRequest().getSession().setAttribute(REACTIVATION_TOKEN_CHECK_PARAM_PREFIX + this.getToken(), true);
		return SUCCESS;
	}
	
	private String extractUsername() {
		String token = this.getToken();
		String username = null;
		if (null != token && token.length() > 0) {
			username = this.getUserRegManager().getUsernameFromToken(token);
		}
		this.setUsername(username);
		return username;
	}
	
	public void setPassword(String password) {
		this._password = password;
	}
	public String getPassword() {
		return _password;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this._passwordConfirm = passwordConfirm;
	}
	public String getPasswordConfirm() {
		return _passwordConfirm;
	}

	public void setToken(String token) {
		this._token = token;
	}
	public String getToken() {
		return _token;
	}

	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return this._username;
	}

	public void setUserRegManager(IUserRegManager userRegManager) {
		this._userRegManager = userRegManager;
	}
	protected IUserRegManager getUserRegManager() {
		return _userRegManager;
	}

	private String _password;
	private String _passwordConfirm;
	private String _token;
	private String _username;	
	private IUserRegManager _userRegManager;
	
	private final String ACTIVATION_TOKEN_CHECK_PARAM_PREFIX = "activation_sessionParam_tokenCheck_";
	private final String REACTIVATION_TOKEN_CHECK_PARAM_PREFIX = "reactivation_sessionParam_tokenCheck_";
	
}
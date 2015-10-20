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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.activation;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.common.UserRegBaseAction;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;

/**
 * Struts Action to manage account activation
 * @author S.Puddu, E.Mezzano, G.Cocco
 */
public class UserActivationAction extends UserRegBaseAction {

	/**
	 * Initialize the funtionality and redirect user to portal homepage if he has 
	 * activated already, or to an error page if token is consumed or wrong
	 * @return The action result.
	 */
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
	
	/**
	 * Active account with information provided, if token is valid.
	 * Load also default roles and groups defined in the config.
	 * @return The action result.
	 */
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
	
	/**
	 * Initialize the funtionality and redirect user to portal homepage if he has 
	 * reactivated already, or to an error page if token is consumed or wrong
	 * @return The action result.
	 */
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
	
	/**
	 * Reactive account with information provided, if token is valid.
	 * @return The action result.
	 */
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
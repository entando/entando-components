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
package com.agiletec.plugins.jpcasclient.aps.system.services.controller.control;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.common.AuthCommon;
import com.agiletec.plugins.jpcasclient.aps.system.services.user.CasAuthProviderManager;

/**
 * Extension of authentication service for managing CAS protocol
 *
 * @author G.Cocco
 * */
public class CasClientAuthenticatorControlService extends AbstractControlService {

	private static final Logger _logger =  LoggerFactory.getLogger(CasClientAuthenticatorControlService.class);
	
	@Override
    public void afterPropertiesSet() throws Exception {
    	_logger.debug("{} ready", this.getClass().getName());
	}
	
    /**
     * Execution.
     *
     * The service method execute the following operations (int the order indicated):
     *
     * 1) if in session there's the SAML assertion of CAS it is used for extract
     * principal information and load matching user in the session.
     *
     * 2) if in the request there are parameters user and password the are used
     *  to try to load the matching user; if user is not null it is loaded into the session
     *
     * 3) if there is not a user into the session the guest user is loaded into
     * the session.
     *
     * @param reqCtx the request context
     * @param status the status  returned by the preceding service
     * @return the resulting status
     */
	@Override
	public int service(RequestContext reqCtx, int status) {
		String name = null;
		_logger.trace("Invoked {}", this.getClass().getName());
		int retStatus = ControllerManager.INVALID_STATUS;
		if (status == ControllerManager.ERROR) {
			return status;
		}
		try {
			HttpServletRequest req = reqCtx.getRequest();
			//Punto 1
			Assertion assertion = (Assertion) req.getSession().getAttribute(CasClientPluginSystemCostants.JPCASCLIENT_CONST_CAS_ASSERTION);
			_logger.trace(" Assertion {}", assertion);
			if (null != assertion) {
				AttributePrincipal attributePrincipal = assertion.getPrincipal();
				name = attributePrincipal.getName();
				_logger.trace(" Princ {}", attributePrincipal);
				_logger.trace(" Princ - Name {}", attributePrincipal.getName());
			}
			_logger.trace("jpcasclient: request From User with Principal [CAS tiket validation]: " + name + " - info: AuthType " + req.getAuthType() + " " + req.getProtocol() + " " + req.getRemoteAddr() + " " + req.getRemoteHost());
			HttpSession session = req.getSession();
			if (null != name) {
				String username = name;
				if (getAuthCommon().hasRealmDomainInformation(name)) {
					username = getAuthCommon().getUsernameFromPrincipal(name);
				}
				_logger.trace("Request From User with Username: " + username + " - info: AuthType " + req.getAuthType() + " " + req.getProtocol() + " " + req.getRemoteAddr() + " " + req.getRemoteHost());
				if (username != null) {
					_logger.trace("jpcasclient: user is {}", username);
					UserDetails userOnSession = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
					if (userOnSession == null || (userOnSession != null && !username.equals(userOnSession.getUsername()))) {
						UserDetails user = this.getAuthenticationProvider().getUser(username);
						if (user != null) {
							if (!user.isAccountNotExpired()) {
								req.setAttribute("accountExpired", new Boolean(true));
							} else {
								if (userOnSession != null && !userOnSession.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
									((AbstractUser) user).setPassword(userOnSession.getPassword());
								}
								session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
								_logger.trace("jpcasclient: new user: {}", user.getUsername());
							}
						} else {
//                			req.setAttribute("wrongAccountCredential", new Boolean(true));
							/* create user on the fly */
							user = new User();
							((User) user).setUsername(username);
							((User) user).setPassword(CasClientPluginSystemCostants.JPCAS_RUNTIME_USER);
							((User) user).setLastAccess(new Date());
							/* put in the session */
							session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
							_logger.trace("jpcasclient: new user created on the fly: {}", user.getUsername());
						}
					}
				}
			}

			//Punto 2
			String userName = req.getParameter("username");
			String password = req.getParameter("password");
			if (userName != null && password != null) {
				_logger.trace("user {}  - password ******** ", userName);
				UserDetails user = this.getAuthenticationProvider().getUser(userName, password);
				if (user != null) {
					if (!user.isAccountNotExpired()) {
						req.setAttribute("accountExpired", new Boolean(true));
					} else {
						session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
						_logger.trace("New User: {}", user.getUsername());
					}
				} else {
					req.setAttribute("wrongAccountCredential", new Boolean(true));
				}
			}

			//Punto 3
			if (session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) == null) {
				UserDetails guestUser = this.getUserManager().getGuestUser();
				session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, guestUser);
			}
			retStatus = ControllerManager.CONTINUE;
		} catch (Throwable t) {
			_logger.error("Error in processing the request", t);
			retStatus = ControllerManager.ERROR;
		}
		return retStatus;
	}

	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}

	public void setAuthenticationProvider(CasAuthProviderManager authenticationProvider) {
		this._authenticationProvider = authenticationProvider;
	}
	public CasAuthProviderManager getAuthenticationProvider() {
		return _authenticationProvider;
	}

	public void setAuthCommon(AuthCommon kerbAuthCommon) {
		this._authCommon = kerbAuthCommon;
	}
	public AuthCommon getAuthCommon() {
		return _authCommon;
	}

	private AuthCommon _authCommon;
	private CasAuthProviderManager _authenticationProvider;
    private IUserManager _userManager;

}

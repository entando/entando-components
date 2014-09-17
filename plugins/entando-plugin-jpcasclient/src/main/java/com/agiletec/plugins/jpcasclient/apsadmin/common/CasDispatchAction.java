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
package com.agiletec.plugins.jpcasclient.apsadmin.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.controller.control.RequestAuthorizator;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.apsadmin.common.DispatchAction;
import com.agiletec.apsadmin.common.IDispatchAction;
import com.agiletec.plugins.jpcasclient.aps.system.common.AuthCommon;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;
import com.agiletec.plugins.jpcasclient.aps.system.services.user.CasAuthProviderManager;

/**
 * Extends base DispatchAction for managing logout
 * also from CAS sso contest. 
 * 
 * @author G.Cocco
 */
public class CasDispatchAction extends DispatchAction implements IDispatchAction, ServletRequestAware, ServletResponseAware {
	
	@Override
	public String doLogout() {
		ApsSystemUtils.getLogger().info("Exec Logout from jAPS and from CAS.");
		this.getSession().invalidate();
		boolean isActive = this.getCasClientConfigManager().getClientConfig().isActive();
		if (isActive) {
			String baseServerUrl = this.getCasClientConfigManager().getClientConfig().getServerBaseURL();
			if (baseServerUrl.endsWith("/")) {
				baseServerUrl = baseServerUrl.substring(0, baseServerUrl.length()-1);
			}
			String logoutBaseUrl = this.getCasClientConfigManager().getClientConfig().getCasLogoutURL();
			StringBuffer logoutUrl = new StringBuffer(logoutBaseUrl);
			logoutUrl.append("?url=");
			logoutUrl.append(baseServerUrl);
			String contextPath = this.getRequest().getContextPath();
			logoutUrl.append(contextPath);
			if (!contextPath.endsWith("/")) {
				logoutUrl.append("/");
			}
			ApsSystemUtils.getLogger().debug("Logout url " + logoutUrl);
			try {
				this.getServletResponse().sendRedirect(logoutUrl.toString());
			} catch (IOException ioe) {
				ApsSystemUtils.logThrowable(ioe, this, "doLogout", "Error redirecting to CAS logout");
			}
			return null;
		} else {
			return super.doLogout();
		}
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._httpServletResponse = response;
	}
	public HttpServletResponse getServletResponse() {
		return _httpServletResponse;
	}

	public void setServletRequest(HttpServletRequest request) {
		this._request = request;
	}
	protected HttpServletRequest getRequest() {
		return _request;
	}

	protected HttpSession getSession() {
		return this.getRequest().getSession();
	}

	protected IAuthorizationManager getAuthorizatorManager() {
		return _authorizatorManager;
	}
	public void setAuthorizatorManager(IAuthorizationManager authorizatorManager) {
		this._authorizatorManager = authorizatorManager;
	}

	protected CasAuthProviderManager getAuthenticationProvider() {
		return _authenticationProvider;
	}
	public void setAuthenticationProvider(CasAuthProviderManager authenticationProvider) {
		this._authenticationProvider = authenticationProvider;
	}

	public void setRequestAuthorizator(RequestAuthorizator requestAuthorizator) {
		this._requestAuthorizator = requestAuthorizator;
	}
	public RequestAuthorizator getRequestAuthorizator() {
		return _requestAuthorizator;
	}

	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	public IUserManager getUserManager() {
		return _userManager;
	}

	public void setAuthCommon(AuthCommon authCommon) {
		this._authCommon = authCommon;
	}
	public AuthCommon getAuthCommon() {
		return _authCommon;
	}

	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}

	public void setPassword(String password) {
		this._password = password;
	}
	public String getPassword() {
		return _password;
	}

	public ICasClientConfigManager getCasClientConfigManager() {
		return _casClientConfigManager;
	}
	public void setCasClientConfigManager(ICasClientConfigManager casClientConfigManager) {
		this._casClientConfigManager = casClientConfigManager;
	}


	private String _username;
	private String _password;

	private AuthCommon _authCommon;
	private HttpServletRequest _request;
	private IAuthorizationManager _authorizatorManager;
	private CasAuthProviderManager _authenticationProvider;
	private IUserManager _userManager; 
	private RequestAuthorizator _requestAuthorizator;
	private HttpServletResponse _httpServletResponse;
	private ICasClientConfigManager _casClientConfigManager;

}
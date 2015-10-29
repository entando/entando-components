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
package com.agiletec.plugins.jpcasclient.apsadmin.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.controller.control.RequestAuthorizator;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.apsadmin.common.DispatchAction;
import com.agiletec.plugins.jpcasclient.aps.system.common.AuthCommon;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;
import com.agiletec.plugins.jpcasclient.aps.system.services.user.CasAuthProviderManager;

/**
 * Extends base DispatchAction for managing logout
 * also from CAS sso contest.
 * @author G.Cocco
 */
public class CasDispatchAction extends DispatchAction implements ServletRequestAware, ServletResponseAware {
	
	/**
	 * This needs the property followServiceRedirects property to TRUE for the
	 * LogoutController defined in cas-servlet.xml
	 * @return The result code
	 */
	@Override
	public String doLogout() {
		ApsSystemUtils.getLogger().info("Exec Logout from Entando and from CAS.");
		this.getSession().invalidate();
		boolean isActive = this.getCasClientConfigManager().getClientConfig().isActive();
		if (isActive) {
			String baseServerUrl = this.getCasClientConfigManager().getClientConfig().getServerBaseURL();
			if (baseServerUrl.endsWith("/")) {
				baseServerUrl = baseServerUrl.substring(0, baseServerUrl.length()-1);
			}
			String logoutBaseUrl = this.getCasClientConfigManager().getClientConfig().getCasLogoutURL();
			StringBuffer logoutUrl = new StringBuffer(logoutBaseUrl);
			logoutUrl.append("?service=");
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
	
	protected IAuthorizationManager getAuthorizatorManager() {
		return _authorizatorManager;
	}
	public void setAuthorizatorManager(IAuthorizationManager authorizatorManager) {
		this._authorizatorManager = authorizatorManager;
	}
	
	@Override
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
	
	@Override
	public void setUsername(String username) {
		this._username = username;
	}
	@Override
	public String getUsername() {
		return _username;
	}
	
	@Override
	public void setPassword(String password) {
		this._password = password;
	}
	@Override
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
	private IAuthorizationManager _authorizatorManager;
	private CasAuthProviderManager _authenticationProvider;
	private IUserManager _userManager;
	private RequestAuthorizator _requestAuthorizator;
	private HttpServletResponse _httpServletResponse;
	private ICasClientConfigManager _casClientConfigManager;

}
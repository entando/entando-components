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

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.RequestAuthorizator;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcasclient.aps.system.services.auth.CasClientUtils;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

/**
 * Extends RequestAuthorizator for redirection to CAS pages if
 * current user has not authorization for the required page.
 * It switch to default behavior if the CAS plugin is disbled
 * (system param jpcasclient_extended_isactive)
 *
 * @author zuanni - G.Cocco
 * */
public class CasClientRequestAuthorizator extends RequestAuthorizator {

	private static final Logger _logger =  LoggerFactory.getLogger(CasClientRequestAuthorizator.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		CasClientConfig casClientConfig = this.getCasConfigManager().getClientConfig();
		this.setCasClientConfig(casClientConfig);
		super.afterPropertiesSet();
	}
	
	@Override
	public int service(RequestContext reqCtx, int status) {
		_logger.trace("Invoked {}", this.getClass().getName());
		int retStatus = ControllerManager.INVALID_STATUS;
		if (status == ControllerManager.ERROR) {
			return status;
		}
		try {
			boolean isActive = this.getCasClientConfig().isActive();
			if (!isActive) {
				// if cas client is disactivate normal Authorization on request
				return super.service(reqCtx, retStatus);
			} else {
				HttpServletRequest req = reqCtx.getRequest();
				HttpSession session = req.getSession();
				IPage currentPage =
					(IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
				UserDetails currentUser =
					(UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
				boolean authorized = this.getAuthManager().isAuth(currentUser, currentPage);
				if (authorized) {
					retStatus = ControllerManager.CONTINUE;
				} else if (SystemConstants.GUEST_USER_NAME.equals(currentUser.getUsername())) {
					_logger.info("CAS - user not authorized and guest");
					CasClientUtils casClientUtils = new CasClientUtils();
					String loginBaseUrl = this.getCasClientConfig().getCasLoginURL();
					StringBuilder loginUrl = new StringBuilder(loginBaseUrl);
					loginUrl.append("?service=");
					PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
					String serviceUrl = casClientUtils.getURLStringWithoutTicketParam(pageUrl, reqCtx);
					loginUrl.append(serviceUrl);
					_logger.info("CAS - Redirecting to {}", loginUrl.toString());
					reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, loginUrl.toString());
					retStatus = ControllerManager.REDIRECT;
				} else {
					_logger.info("CAS - user authenticated but not authorized");
					Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
	            	String notAuthPageCode = this.getCasClientConfig().getNotAuthPage();
	            	IPage page = this.getPageManager().getPage(notAuthPageCode);
	            	String url = this.getUrlManager().createUrl(page, currentLang, new HashMap<String, String>());
	            	_logger.info("CAS - Redirecting to {}", url);
	            	reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, url);
	            	retStatus = ControllerManager.REDIRECT;
	            }
			}
		} catch (Throwable t) {
			_logger.error("Error in processing the request", t);
			retStatus = ControllerManager.ERROR;
		}
		return retStatus;
	}
	
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	public IPageManager getPageManager() {
		return _pageManager;
	}
	
	public ICasClientConfigManager getCasConfigManager() {
		return _casConfigManager;
	}
	public void setCasConfigManager(ICasClientConfigManager casClientConfigManager) {
		this._casConfigManager = casClientConfigManager;
	}

	public CasClientConfig getCasClientConfig() {
		return _casClientConfig;
	}
	public void setCasClientConfig(CasClientConfig casClientConfig) {
		this._casClientConfig = casClientConfig;
	}

	private IPageManager _pageManager;
	private ICasClientConfigManager _casConfigManager;
	private CasClientConfig _casClientConfig;

}
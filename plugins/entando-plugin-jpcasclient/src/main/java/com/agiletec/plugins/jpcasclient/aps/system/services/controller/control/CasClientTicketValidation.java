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

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.services.auth.CasClientUtils;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

/**
 * Control Service for CAS ticket validation
 * 
 * @author zuanni G.Cocco
 * */
public class CasClientTicketValidation extends AbstractControlService {

	private static final Logger _logger =  LoggerFactory.getLogger(CasClientTicketValidation.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.setCasClientConfig(this.getCasClientConfigManager().getClientConfig());
		String urlCasValidate = this.getCasClientConfig().getCasValidateURL();
		_ticketValidationUtil = new CasClientTicketValidationUtil(urlCasValidate);
		_logger.debug("{} : ready", this.getClass().getName());
	}
	
	/**
	 * the errors of control sub-service must be ignored, 
	 * the required page will be erogated anyway
	 * */
	@Override
	public int service(RequestContext reqCtx, int status) {
		HttpServletRequest request = reqCtx.getRequest();
		int retStatus = ControllerManager.CONTINUE;
		if (status == ControllerManager.ERROR) {
			return status;
		}
		boolean isActive = 
			this.getCasClientConfig().isActive();
		if (!isActive) {
			return retStatus;			
		}
		try {
			String ticket = request.getParameter("ticket");
			if (null != ticket && ticket.length() > 0 ) {
				CasClientUtils casClientUtils = new CasClientUtils();
				PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
				String serviceUrl = casClientUtils.getURLStringWithoutTicketParam(pageUrl, reqCtx);
				_log.info("CAS - service " + serviceUrl + ", ticket " + ticket);
				Assertion assertion;
				assertion = _ticketValidationUtil.validateTicket(serviceUrl, ticket);
				request.getSession().setAttribute(CasClientPluginSystemCostants.JPCASCLIENT_CONST_CAS_ASSERTION, assertion);
				
//				redirect to current page without ticket parameter in the url
// 				to avoid involuntary submits
				reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, serviceUrl.toString());
				retStatus = ControllerManager.REDIRECT;
			}
		} catch (Throwable t) {
			_logger.error("Error in processing the request", t);
		}
		return retStatus;
	}
	
	
	public IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	public void setCasClientConfigManager(ICasClientConfigManager _configManager) {
		this._casClientConfigManager = _configManager;
	}
	public ICasClientConfigManager getCasClientConfigManager() {
		return _casClientConfigManager;
	}

	public CasClientConfig getCasClientConfig() {
		return _casClientConfig;
	}
	public void setCasClientConfig(CasClientConfig casClientConfig) {
		this._casClientConfig = casClientConfig;
	}

	private CasClientTicketValidationUtil _ticketValidationUtil;
	private IURLManager _urlManager;
	private ICasClientConfigManager _casClientConfigManager;
	private CasClientConfig _casClientConfig;
	
}
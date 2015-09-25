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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Struts Base Action for jpuserreg plugin struts action 
 * providing functionality for portal homepage redirect from
 * external framework action
 * 
 * @author G.Cocco
 * */
public class UserRegBaseAction extends BaseAction implements ServletResponseAware {
	
	/**
	 * redirect user to portal homepage
	 * */
	protected void sendHomeRedirect() throws IOException {
		try {
			this.getServletResponse().sendRedirect(this.getUrlForHomePageRedirect());
		} catch (IOException ioe) {
			ApsSystemUtils.logThrowable(ioe, this, "sendHomeRedirect");
			throw ioe;
		}
	}
	
	private String getUrlForHomePageRedirect() {
		StringBuffer temp = new StringBuffer(this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL));
		if (temp.charAt(temp.length()-1) != '/'){
			temp.append('/');
		}
		temp.append("index.jsp");
		return temp.toString();
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._httpServletResponse = response;
	}
	public HttpServletResponse getServletResponse() {
		return _httpServletResponse;
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	
	private HttpServletResponse _httpServletResponse;
	private ConfigInterface _configManager;
	public final static String USERREG_ERROR = "userreg_error";
	
}
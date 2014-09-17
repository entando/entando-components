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
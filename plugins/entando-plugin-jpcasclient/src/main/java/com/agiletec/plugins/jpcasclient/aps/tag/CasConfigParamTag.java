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
package com.agiletec.plugins.jpcasclient.aps.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

public class CasConfigParamTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(CasConfigParamTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ICasClientConfigManager clientConfigManager = 
				(ICasClientConfigManager) ApsWebApplicationUtils.getBean(CasClientPluginSystemCostants.JPCASCLIENT_CONFIG_MANAGER, this.pageContext);
		CasClientConfig casClientConfig = clientConfigManager.getClientConfig();
		String param = null;
		try {
			if (_param.equals("active")) {
				boolean isActive = casClientConfig.isActive();
				param = String.valueOf(isActive);
			} else if (_param.equals("casLoginURL")) {
				param = casClientConfig.getCasLoginURL();
			} else if (_param.equals("casLogoutURL")) {
				param = casClientConfig.getCasLogoutURL();
			} else if (_param.equals("casValidateURL")) {
				param = casClientConfig.getCasValidateURL();
			} else if (_param.equals("serverBaseURL")) {
				param = casClientConfig.getServerBaseURL();
			} else if (_param.equals("notAuthPage")) {
				param = casClientConfig.getNotAuthPage();
			} else if (_param.equals("realm")) {
				param = casClientConfig.getRealm();
			}
			if (null != this.getVar() && this.getVar().length() > 0) {
				this.pageContext.setAttribute(this.getVar(), param);
			} else {
				this.pageContext.getOut().print(param);
			}
		} catch (IOException e) {
			_logger.error("Error closing tag ", e);
			throw new JspException("Error closing tag ", e);
		}
		return SKIP_BODY;
	}


	public String getParam() {
		return _param;
	}
	public void setParam(String param) {
		this._param = param;
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	private String _var;
	private String _param;

}

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

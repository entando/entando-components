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
package org.entando.entando.plugins.jptokenapi.aps.tags;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jptokenapi.aps.system.JpTokenApiSystemConstants;
import org.entando.entando.plugins.jptokenapi.aps.system.token.IApiTokenizerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author E.Santoboni
 */
public class MyTokenTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(MyTokenTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		try {
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (null == currentUser || currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				return super.doEndTag();
			}
			IApiTokenizerManager apiTokenizerManager = 
				(IApiTokenizerManager) ApsWebApplicationUtils.getBean(JpTokenApiSystemConstants.TOKENIZER_MANAGER, this.pageContext);
			this._value = apiTokenizerManager.getToken(currentUser.getUsername());
			this.evalValue();
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Error closing tag ", t);
		}
		this.release();
		return super.doEndTag();
	}
	
	protected void evalValue() throws JspException {
		if (this.getVar() != null) {
			this.pageContext.setAttribute(this.getVar(), this.getValue());
		} else {
			try {
				this.pageContext.getOut().print(this.getValue());
			} catch (IOException e) {
				_logger.error("error in evalValue", e);
				throw new JspException("Error evaling value", e);
			}
		}
	}
	
	@Override
	public void release() {
		this._var = null;
		this._value = null;
	}
	
	public void setVar(String var) {
		this._var = var;
	}
	protected String getVar() {
		return _var;
	}
	
	public String getValue() {
		return _value;
	}
	public void setValue(String value) {
		this._value = value;
	}
	
	private String _var;
	private String _value;
	
}
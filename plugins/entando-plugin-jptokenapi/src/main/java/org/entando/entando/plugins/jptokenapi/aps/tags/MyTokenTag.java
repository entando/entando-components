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
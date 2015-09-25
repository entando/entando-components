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
package org.entando.entando.plugins.jpwebform.aps.tags;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author S.Loru
 */
public class FormTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(FormTag.class);
	
	public FormTag() {
		super();
		this.release();
	}
	
	@Override
	public int doStartTag() throws JspException {
		//ServletRequest request =  this.pageContext.getRequest();
		try {
			IFormManager formManager = (IFormManager) ApsWebApplicationUtils.getBean(JpwebformSystemConstants.FORM_MANAGER, this.pageContext);
			Message message = formManager.getMessage(this.getFormId());
			Object formInfo = null;
			if(null != this.getInfo() && !this.getInfo().isEmpty()){
				if("status".equals(this.getInfo())){
					formInfo = message.isCompleted();
				} else if("sendDate".equals(this.getInfo())){
					formInfo = message.getSendDate();
				} else if("code".equals(this.getInfo())){
					formInfo = message.getTypeCode();
				} else if("description".equals(this.getInfo())){
					formInfo = message.getTypeDescr();
				} else if("repeatable".equals(this.getInfo())){
					Message prototype = (Message) formManager.getEntityPrototype(message.getTypeCode());
					formInfo = prototype.isRepeatable();
				}
			}
			returnValue(formInfo);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error detected while initialising the tag", t);
		}
		return EVAL_PAGE;
	}
	
	private void returnValue(Object formInfo) throws IOException {
		if (null != this.getVar()) {
			this.pageContext.setAttribute(this.getVar(), formInfo);
			if(null != this.getPrintVar() && Boolean.parseBoolean(this.getPrintVar())) {
				this.pageContext.getOut().print(formInfo);
			}
		} else {
			this.pageContext.getOut().print(formInfo);
		} 
	}

	@Override
	public void release() {
		this.setFormId(null);
		this.setVar(null);
		this.setInfo(null);
	}

	/**
	 * Inserts the rendered content in a variable of the page context with the name provided
	 * @return The name of the variable
	 */
	public String getVar() {
		return _var;
	}
	
	/**
	 * Inserts the rendered content in a variable of the page context with the name provided
	 * @param var The name of the variable
	 */
	public void setVar(String var) {
		this._var = var;
	}

	public String getInfo() {
		return _info;
	}
	public void setInfo(String info) {
		this._info = info;
	}

	public String getFormId() {
		return _formId;
	}
	public void setFormId(String formId) {
		this._formId = formId;
	}

	public String getPrintVar() {
		return _printVar;
	}
	public void setPrintVar(String printVar) {
		this._printVar = printVar;
	}
	
	private String _var;
	private String _info;
	private String _formId;
	private String _printVar;
	
}

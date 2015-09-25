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
 *
 * @author S.Loru
 */
public class PrototypeTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(PrototypeTag.class);

	public PrototypeTag() {
		super();
		this.release();
	}
	
	@Override
	public void release() {
		this.setTypecode(null);
		this.setVar(null);
	}
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			IFormManager formManager = (IFormManager) ApsWebApplicationUtils.getBean(JpwebformSystemConstants.FORM_MANAGER, this.pageContext);
			Message entityPrototype = (Message) formManager.getEntityPrototype(this.getTypecode());
			pageContext.setAttribute(this.getVar(), entityPrototype);
			request.setAttribute(this.getVar(), entityPrototype);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error detected while initialising the tag", t);
		}
		return EVAL_PAGE;
	}
	
	public String getVar() {
		return _var;
	}
	
	public void setVar(String var) {
		this._var = var;
	}
	
	public String getTypecode() {
		return _typecode;
	}
	
	public void setTypecode(String typecode) {
		this._typecode = typecode;
	}
	
	private String _var;
	private String _typecode;

}

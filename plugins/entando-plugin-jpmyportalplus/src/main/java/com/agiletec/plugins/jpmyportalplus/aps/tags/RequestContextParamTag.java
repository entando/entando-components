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
package com.agiletec.plugins.jpmyportalplus.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;

/**
 * Restituisce il valore di un parametro del contesto della richiesta.
 * Il Parametro è restituito in funzione della showlet nella quale il tag è inserito.
 * @author E.Santoboni
 */
public class RequestContextParamTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(RequestContextParamTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			Object value = reqCtx.getExtraParam(this.getParam());
			if (null != value) {
				String var = this.getVar();
				if (null == var || "".equals(var)) {
					this.pageContext.getOut().print(value);
				} else {
					this.pageContext.setAttribute(this.getVar(), value);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("error in doStartTag", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this._param = null;
		this._var = null;
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
	
	private String _param;
	private String _var;
	
}
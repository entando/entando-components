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
package com.agiletec.plugins.jpgeoref.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;

/**
 * @author E.Santoboni
 */
public class ReqCtxParamPrinterTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ReqCtxParamPrinterTag.class);
	
	/**
	 * End tag analysis.
	 */
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		try {
			Object value = reqCtx.getExtraParam(this.getVar());
			if (value == null) value = "";
			this.pageContext.getOut().print(value);
		} catch (Throwable t) {
			_logger.error("error in Error in doEndTag", t);
			throw new JspException("Error in doEndTag", t);
		}
		return EVAL_PAGE;
	}

	/**
	 * Sets tag attribute
	 * @param var tag attribute
	 */
	public void setVar(String var) {
		this._var = var;
	}
	/**
	 * Returns tag attribute
	 * @return tag attribute
	 */
	public String getVar() {
		return _var;
	}


	private String _var; // tag attribute

}

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

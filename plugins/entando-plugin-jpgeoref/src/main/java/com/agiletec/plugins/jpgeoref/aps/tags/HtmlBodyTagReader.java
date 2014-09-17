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
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.agiletec.aps.system.RequestContext;

/**
 * @author E.Santoboni
 */
public class HtmlBodyTagReader extends BodyTagSupport {

	/**
	 * End tag analysis.
	 */
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		Object result;
		if (bodyContent == null || bodyContent.getString() == null) {
			result = "";
		} else {
			result = bodyContent.getString().trim();
		}
		reqCtx.addExtraParam(this.getVar(), result);
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
	 * Returns tag attribute.
	 * @return tag attribute.
	 */
	public String getVar() {
		return this._var;
	}

	private String _var; // tag attribute

}

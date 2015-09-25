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
package com.agiletec.plugins.jpcrowdsourcing.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.JpCrowdSourcingSystemConstants;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;

public class IdeaTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaTag.class);

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IIdea idea  = this.loadIdea(reqCtx);
			this.pageContext.setAttribute(this.getVar(), idea);
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Errore tag", t);
		}
		this.release();
		return super.doEndTag();
	}
	
	public IIdea loadIdea(RequestContext reqCtx) {
		IIdea idea = null;
		try {
			IIdeaManager ideaManager = (IIdeaManager) ApsWebApplicationUtils.getBean(JpCrowdSourcingSystemConstants.IDEA_MANAGER, this.pageContext);
			String id = this.getIdeaId();
			if (null == id || id.trim().length() == 0) {
				id = reqCtx.getRequest().getParameter("jpcrsrIdeaid");
			}
			if (null != id && id.trim().length() > 0) {
				idea = ideaManager.getIdea(id);
			}
		} catch (Throwable t) {
			_logger.error("Errore loading idea {}", this.getId(), t);
			throw new RuntimeException("Errore loading idea " + this.getIdeaId());
		}
		return idea;
	}
	
	@Override
	public void release() {
		this.setIdeaId(null);
	}

	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public void setIdeaId(String ideaId) {
		this._ideaId = ideaId;
	}
	public String getIdeaId() {
		return _ideaId;
	}

	private String _var;
	private String _ideaId;
}

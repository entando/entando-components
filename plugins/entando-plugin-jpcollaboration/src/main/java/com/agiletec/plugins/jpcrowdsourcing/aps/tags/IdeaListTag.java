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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.JpCrowdSourcingSystemConstants;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaDAO;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;

public class IdeaListTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaListTag.class);

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			List<String> list = this.loadIdeaList(reqCtx);
			this.pageContext.setAttribute(this.getVar(), list);
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Errore tag", t);
		}
		this.release();
		return super.doEndTag();
	}

	public List<String> loadIdeaList(RequestContext reqCtx) {
		List<String> list = new ArrayList<String>();
		try {
			IIdeaManager ideaManager = (IIdeaManager) ApsWebApplicationUtils.getBean(JpCrowdSourcingSystemConstants.IDEA_MANAGER, this.pageContext);
			Integer sortOrder = this.parseOrder();
			list = ideaManager.searchIdeas(this.getInstance(),IIdea.STATUS_APPROVED, this.getText(), this.getCategory(), sortOrder);
		} catch (Throwable t) {
			_logger.error("Error loading list", t);
			throw new RuntimeException("Error loading list");
		}
		return list;
	}

	@Override
	public void release() {
		this.setInstance(null);
		this.setText(null);
		this.setCategory(null);
		this.setOrder(null);
		this.setVar(null);
	}

	private Integer parseOrder() {
		if (null != this.getOrder() && this.getOrder().equalsIgnoreCase("Vote")) {
			return IIdeaDAO.SORT_MOST_RATED;
		} else {
			return IIdeaDAO.SORT_LATEST;
		}
	}


	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public void setText(String text) {
		this._text = text;
	}
	public String getText() {
		return _text;
	}

	public void setCategory(String category) {
		this._category = category;
	}
	public String getCategory() {
		return _category;
	}

	public void setOrder(String order) {
		this._order = order;
	}
	public String getOrder() {
		return _order;
	}

	public String getInstance() {
		return _instance;
	}
	public void setInstance(String instance) {
		this._instance = instance;
	}

	private String _instance;
	private String _text;
	private String _category;


	private String _order;
	private String _var;
}

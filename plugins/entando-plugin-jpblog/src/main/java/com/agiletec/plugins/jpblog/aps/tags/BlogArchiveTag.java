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
package com.agiletec.plugins.jpblog.aps.tags;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpblog.aps.system.JpblogSystemConstants;
import com.agiletec.plugins.jpblog.aps.system.services.blog.BlogArchiveInfoBean;
import com.agiletec.plugins.jpblog.aps.tags.helper.IBlogArchiveTagHelper;

/**
 * Eroga il conteggio dei post raggruppati per mese e gruppi
 *
 */
public class BlogArchiveTag extends TagSupport implements IBlogArchiveTag {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogArchiveTag.class);

	public BlogArchiveTag() {
		super();
		this.release();
	}

	@Override
	public int doStartTag() throws JspException {
		if (null == this.getTypeCode()) {
			this.setTypeCode(this.extractContentType());
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
			IBlogArchiveTagHelper helper = (IBlogArchiveTagHelper) ApsWebApplicationUtils.getBean(JpblogSystemConstants.BLOG_ARCHIVE_TAG_HELPER, this.pageContext);
			List<BlogArchiveInfoBean> list = helper.getBlogArchiveList(this, reqCtx);
			this.pageContext.setAttribute(this.getVar(), list);
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Error detected while finalising the tag", t);
		}
		this.release();
		return EVAL_PAGE;
	}

	public String extractContentType() {
		String typeCode = this.getTypeCode();
		if (StringUtils.isBlank(typeCode)) {
			RequestContext reqCtx = (RequestContext) pageContext.getRequest().getAttribute(RequestContext.REQCTX);
			Widget currentShowlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			if (null != currentShowlet.getConfig() &&	currentShowlet.getConfig().getProperty("contentType") != null && currentShowlet.getConfig().getProperty("contentType").length() > 0) {
				typeCode = currentShowlet.getConfig().getProperty("contentType");
			}
		}
		return typeCode;
	}

	@Override
	public void release() {
		this._var = null;
		this._typeCode = null;
		this._listEvaluated = false;
	}

	public void setVar(String var) {
		this._var = var;
	}
	
	@Override
	public String getVar() {
		return _var;
	}

	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	@Override
	public String getTypeCode() {
		return _typeCode;
	}

	/**
	 * Checks if the list if the list has been previously stored in the cache.
	 * @return
	 */
	protected boolean isListEvaluated() {
		return _listEvaluated;
	}
	protected void setListEvaluated(boolean listEvaluated) {
		this._listEvaluated = listEvaluated;
	}
	
	private String _var;
	private String _typeCode;
	private boolean _listEvaluated;
	
}

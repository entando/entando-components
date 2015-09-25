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
package com.agiletec.plugins.jpfastcontentedit.aps.tags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListTagBean;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.UserFilterOptionBean;
import com.agiletec.plugins.jpfastcontentedit.aps.internalservlet.content.helper.IContentActionHelper;
import com.agiletec.plugins.jpfastcontentedit.aps.system.JpFastContentEditSystemConstants;

public class AllowedContentsTag extends TagSupport implements IContentListTagBean {

	private static final Logger _logger = LoggerFactory.getLogger(AllowedContentsTag.class);
	
	public AllowedContentsTag() {
		super();
		this.release();
	}
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		IContentListWidgetHelper contentListHelper = (IContentListWidgetHelper) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_LIST_HELPER, this.pageContext);
		IContentActionHelper helper = (IContentActionHelper) ApsWebApplicationUtils.getBean(JpFastContentEditSystemConstants.FRONT_CONTENT_ACTION_HELPER, this.pageContext);
		try {
			Set<String> contentIds = new HashSet<String>();
			String authorAttributeName = helper.getAuthorAttributeName((HttpServletRequest) request);
			if (null != authorAttributeName && authorAttributeName.trim().length() > 0) {
				EntitySearchFilter authorFilter = new EntitySearchFilter(authorAttributeName, true, currentUser.getUsername(), false);
				this.addFilter(authorFilter);
				contentIds.addAll(contentListHelper.getContentsId(this, reqCtx));
				this._filters = new EntitySearchFilter[0];
			}
			EntitySearchFilter lastEditorFilter = new EntitySearchFilter(IContentManager.CONTENT_LAST_EDITOR_FILTER_KEY, false, currentUser.getUsername(), false);
			this.addFilter(lastEditorFilter);
			contentIds.addAll(contentListHelper.getContentsId(this, reqCtx));
                        this.pageContext.setAttribute(this.getVar(), contentIds);
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error during tag initialization ", t);
		}
		return super.doStartTag();
	}
        
        public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}
	
	@Override
	public String getContentType() {
		return _contentType;
	}
	@Override
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	@Override
	public String getCategory() {
		return null;
	}
	
	@Override
	public void addFilter(EntitySearchFilter filter) {
		int len = this._filters.length;
		EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = this._filters[i];
		}
		newFilters[len] = filter;
		this._filters = newFilters;
	}
	@Override
	public EntitySearchFilter[] getFilters() {
		return this._filters;
	}
	@Override
	public String getListName() {
		return "AllowedContentsTag_list_name_";
	}
	@Override
	public boolean isCacheable() {
		return false;
	}
	@Override
	public void setCategory(String category) {
		//nothing to do
	}
	
	@Override
	public void addCategory(String category) {
		//nothing to do
	}

	@Override
	public String[] getCategories() {
		//nothing to do
		return null;
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	@Override
	public void release() {
		this._var = null;
		this._contentType = null;
		this._filters = new EntitySearchFilter[0];
	}
	
	@Override
	public void addUserFilterOption(UserFilterOptionBean filter) {
		// nothing to do
	}
	@Override
	public List<UserFilterOptionBean> getUserFilterOptions() {
		// nothing to do
		return null;
	}
	
	private String _contentType;
	private EntitySearchFilter[] _filters = new EntitySearchFilter[0];
	
	private String _var;
	
}
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpblog.aps.system.JpblogSystemConstants;
import com.agiletec.plugins.jpblog.aps.system.services.blog.CategoryInfoBean;
import com.agiletec.plugins.jpblog.aps.system.services.blog.IBlogManager;

/**
 * @author spuddu
 */
public class BlogCategoryTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogCategoryTag.class);

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		IBlogManager blogManager = (IBlogManager) ApsWebApplicationUtils.getBean(JpblogSystemConstants.BLOG_MANAGER, this.pageContext);
		try {
			if (null == this.getTypeCode()) this.setTypeCode(this.extractContentType());
			List<String> userGroupCodes = new ArrayList<String>(this.getAllowedGroups(reqCtx));
			//List<String> facetNodeCodes = blogManager.getSpecialCategories();
			List<String> contentTypeCodes = new ArrayList<String>();
			contentTypeCodes.add(this.getTypeCode());

			Map<Category, Integer> blogCategories = blogManager.getOccurrences(contentTypeCodes,  userGroupCodes);
			//this.pageContext.setAttribute(this.getVar(), blogCategories);
			this.pageContext.setAttribute(this.getVar(), this.buildCategoryInfoBeanList(blogCategories));
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Error closing tag ", t);
		}
		this.release();
		return EVAL_PAGE;
	}

	private List<CategoryInfoBean> buildCategoryInfoBeanList(Map<Category, Integer> occurrences) throws Throwable {
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		List<CategoryInfoBean> beans = new ArrayList<CategoryInfoBean>();
		try {
			Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			Iterator<Category> iter = occurrences.keySet().iterator();
			while (iter.hasNext()) {
				Category treeNode = iter.next();
				beans.add(new CategoryInfoBean(treeNode, currentLang.getCode(), occurrences.get(treeNode)));
			}
			BeanComparator comparator = new BeanComparator("title");
			Collections.sort(beans, comparator);
		} catch (Throwable t) {
			_logger.error("error in Error building buildCategoryInfoBeanList", t);
			throw new ApsSystemException("Error building buildCategoryInfoBeanList ", t);
		}
		return beans;
	}

	private Collection<String> getAllowedGroups(RequestContext reqCtx) {
		IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, reqCtx.getRequest());
		UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		List<Group> groups = authManager.getUserGroups(currentUser);
		Set<String> allowedGroup = new HashSet<String>();
		Iterator<Group> iter = groups.iterator();
		while (iter.hasNext()) {
			Group group = iter.next();
			allowedGroup.add(group.getName());
		}
		allowedGroup.add(Group.FREE_GROUP_NAME);
		return allowedGroup;
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
	}
	
	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	public String getTypeCode() {
		return _typeCode;
	}

	private String _var;
	private String _typeCode;
}

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
package com.agiletec.plugins.jpblog.aps.tags.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;

import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jpblog.aps.system.services.blog.BlogArchiveInfoBean;
import com.agiletec.plugins.jpblog.aps.system.services.blog.IBlogManager;
import com.agiletec.plugins.jpblog.aps.tags.IBlogArchiveTag;

public class BlogArchiveTagHelper implements IBlogArchiveTagHelper {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogArchiveTagHelper.class);
	
	@Override
	@Cacheable(value = ICacheInfoManager.CACHE_NAME,
			key = "T(com.agiletec.plugins.jpblog.aps.tags.helper.BlogArchiveTagHelper).buildCacheKey(#tag, #reqCtx)")
	@CacheEvict(value = ICacheInfoManager.CACHE_NAME,
			key = "T(com.agiletec.plugins.jpblog.aps.tags.helper.BlogArchiveTagHelper).buildCacheKey(#tag, #reqCtx)",
			beforeInvocation = true,
			condition = "T(org.entando.entando.aps.system.services.cache.CacheInfoManager).isExpired(T(com.agiletec.plugins.jpblog.aps.tags.helper.BlogArchiveTagHelper).buildCacheKey(#tag, #reqCtx))")
	@CacheableInfo(groups = "T(com.agiletec.plugins.jpblog.aps.tags.helper.BlogArchiveTagHelper).getContentListCacheGroupsCsv(#tag, #reqCtx)", expiresInMinute = 30)
	public List<BlogArchiveInfoBean> getBlogArchiveList(IBlogArchiveTag tag, RequestContext reqCtx) throws JspException {
		List<BlogArchiveInfoBean> list = null;
		try {
			List<String> userGroupCodes = new ArrayList<String>(getAllowedGroups(reqCtx));
			list = this.getBlogManager().getOccurrencesByDate(tag.getTypeCode(), userGroupCodes);
			Collections.reverse(list);
		} catch (Throwable t) {
			_logger.error("errore in caricamento getBlogArchiveList in tag", t);
			throw new JspException("errore in caricamento getBlogArchiveList in tag", t);
		}
		return list;
	}

	public static String getContentListCacheGroupsCsv(IBlogArchiveTag bean, RequestContext reqCtx) {
		StringBuilder builder = new StringBuilder();
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		String pageCacheGroupName = SystemConstants.PAGES_CACHE_GROUP_PREFIX + page.getCode();
		String contentTypeCacheGroupName = JacmsSystemConstants.CONTENTS_ID_CACHE_GROUP_PREFIX + bean.getTypeCode();
		builder.append(pageCacheGroupName).append(",").append(contentTypeCacheGroupName);
		return builder.toString();
	}


	public static String buildCacheKey(IBlogArchiveTag tag, RequestContext reqCtx) {
		List<String> userGroupCodes = new ArrayList<String>(getAllowedGroups(reqCtx));
		return buildCacheKey(tag.getVar(), userGroupCodes, reqCtx);
	}

	private static Collection<String> getAllowedGroups(RequestContext reqCtx) {
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

	private static String buildCacheKey(String listName, Collection<String> userGroupCodes, RequestContext reqCtx) {
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		StringBuilder cacheKey = new StringBuilder(page.getCode());
		Widget currentShowlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
		cacheKey.append("_").append(currentShowlet.getType().getCode());
		Integer frame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		cacheKey.append("_").append(frame.intValue());
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		cacheKey.append("_LANG").append(currentLang.getCode()).append("_");
		List<String> groupCodes = new ArrayList<String>(userGroupCodes);
		if (!groupCodes.contains(Group.FREE_GROUP_NAME)) {
			groupCodes.add(Group.FREE_GROUP_NAME);
		}
		Collections.sort(groupCodes);
		for (int i=0; i<groupCodes.size(); i++) {
			String code = (String) groupCodes.get(i);
			cacheKey.append("_").append(code);
		}
		if (null != currentShowlet.getConfig()) {
			List<String> paramKeys = new ArrayList(currentShowlet.getConfig().keySet());
			Collections.sort(paramKeys);
			for (int i=0; i<paramKeys.size(); i++) {
				if (i==0) {
					cacheKey.append("_SHOWLETPARAM");
				} else {
					cacheKey.append(",");
				}
				String paramkey = (String) paramKeys.get(i);
				cacheKey.append(paramkey).append("=").append(currentShowlet.getConfig().getProperty(paramkey));
			}
		}
		if (null != listName) {
			cacheKey.append("_LISTNAME").append(listName);
		}
		return cacheKey.toString();
	}

	public void setBlogManager(IBlogManager blogManager) {
		this._blogManager = blogManager;
	}
	protected IBlogManager getBlogManager() {
		return _blogManager;
	}

	private IBlogManager _blogManager;

}

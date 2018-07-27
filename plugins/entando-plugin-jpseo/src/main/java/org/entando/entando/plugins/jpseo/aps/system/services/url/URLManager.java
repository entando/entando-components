/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.url;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

import javax.servlet.http.HttpServletRequest;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPageMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class URLManager extends com.agiletec.aps.system.services.url.URLManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(URLManager.class);
	
	@Override
	public PageURL createURL(RequestContext reqCtx) {
		return new PageURL(this, reqCtx);
	}
	
	@Override
	public String getURLString(com.agiletec.aps.system.services.url.PageURL pageUrl, RequestContext reqCtx) {
		if (!(pageUrl instanceof PageURL)) {
			return super.getURLString(pageUrl, reqCtx);
		}
		Lang lang = this.extractLang(pageUrl, reqCtx);
		IPage destPage = this.extractDestPage(pageUrl, reqCtx);
		String friendlyCode = this.extractFriendlyCode(destPage, lang, pageUrl);
		HttpServletRequest request = (null != reqCtx) ? reqCtx.getRequest() : null;
		String url = null;
		if (null == friendlyCode || friendlyCode.trim().length() == 0) {
			url = this.createURL(destPage, lang, pageUrl.getParams(), pageUrl.isEscapeAmp(), request);
		} else {
			url = this.createFriendlyUrl(friendlyCode, lang, pageUrl.getParams(), pageUrl.isEscapeAmp(), request);
		}
		if (null != reqCtx && this.useJsessionId()) {
			HttpServletResponse resp = reqCtx.getResponse();
			return resp.encodeURL(url.toString());  
		} else {
			return url;
		}
	}
	
	private Lang extractLang(com.agiletec.aps.system.services.url.PageURL pageUrl, RequestContext reqCtx) {
		String langCode = pageUrl.getLangCode();
		Lang lang = this.getLangManager().getLang(langCode);
		if (lang == null && null != reqCtx) {
			lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		}
		if (lang == null) {
			lang = this.getLangManager().getDefaultLang();
		}
		return lang;
	}
	
	private IPage extractDestPage(com.agiletec.aps.system.services.url.PageURL pageUrl, RequestContext reqCtx) {
		String pageCode = pageUrl.getPageCode();
		IPage page = this.getPageManager().getOnlinePage(pageCode);
		if (page == null && null != reqCtx) {
			page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		}
		if (page == null) {
			page = this.getPageManager().getOnlineRoot();
		}
		return page;
	}
	
	private String extractFriendlyCode(IPage destPage, Lang lang, com.agiletec.aps.system.services.url.PageURL pageUrl) {
		Map params = pageUrl.getParams();
		return this.extractFriendlyCode(destPage, lang, params);
	}
	
	private String extractFriendlyCode(IPage destPage, Lang lang, Map params) {
		String friendlyCode = null;
		if (params != null) {
			String contentId = (String) params.get("contentId");
			if (contentId!=null) {
				String viewPageCode = this.getContentManager().getViewPage(contentId);
				if (destPage.getCode().equals(viewPageCode)) {
					friendlyCode = this.getSeoMappingManager().getContentReference(contentId, lang.getCode());
				}
			}
		}
		if (friendlyCode==null && destPage.getMetadata() instanceof SeoPageMetadata) {
			friendlyCode = ((SeoPageMetadata) destPage.getMetadata()).getFriendlyCode();
		}
		return friendlyCode;
	}
	
	@Override
	public String createURL(IPage requiredPage, Lang requiredLang, Map<String, String> params, boolean escapeAmp) {
		return this.createURL(requiredPage, requiredLang, params, escapeAmp, null);
	}
	
	@Override
	public String createURL(IPage requiredPage, Lang requiredLang, Map<String, String> params, boolean escapeAmp, HttpServletRequest request) {
		try {
			String friendlyCode = this.extractFriendlyCode(requiredPage, requiredLang, params);
			if (null == friendlyCode || friendlyCode.trim().length() == 0) {
				return super.createURL(requiredPage, requiredLang, params, escapeAmp, request);
			}
			return this.createFriendlyUrl(friendlyCode, requiredLang, params, escapeAmp, request);
		} catch (Throwable t) {
			_logger.error("Error creating url", t);
			throw new RuntimeException("Error creating url", t);
		}
	}
	
	protected String createFriendlyUrl(String friendlyCode, Lang requiredLang, Map<String, String> params, boolean escapeAmp, HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		try {
			this.addBaseURL(url, request);
			if (!url.toString().endsWith("/")) {
				url.append("/");
			}
			url.append("page/");
			url.append(requiredLang.getCode()).append('/');
			url.append(friendlyCode);
			String queryString = this.createQueryString(params, escapeAmp);
			url.append(queryString);
		} catch (Exception t) {
			_logger.error("Error creating friendly url", t);
            throw new RuntimeException("Error creating friendly url", t);
		}
		return url.toString();
	}
	
	protected ISeoMappingManager getSeoMappingManager() {
		return _seoMappingManager;
	}
	public void setSeoMappingManager(ISeoMappingManager seoMappingManager) {
		this._seoMappingManager = seoMappingManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	private ISeoMappingManager _seoMappingManager;
	private IContentManager _contentManager;
	
}
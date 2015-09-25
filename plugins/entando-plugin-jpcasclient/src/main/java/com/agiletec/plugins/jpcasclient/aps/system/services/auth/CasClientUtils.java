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
package com.agiletec.plugins.jpcasclient.aps.system.services.auth;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageUtils;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

/**
 * Utility for generating URLs without ticket parameter.
 * @author G.Cocco
 */
public class CasClientUtils {

	public String getURLStringWithoutTicketParam(PageURL pageUrl, RequestContext reqCtx){
		String langCode = pageUrl.getLangCode();
		ILangManager langManager =
			(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, reqCtx.getRequest());
		BaseConfigManager configManager =
			(BaseConfigManager) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, reqCtx.getRequest());
		ICasClientConfigManager casClientConfigManager =
				(ICasClientConfigManager) ApsWebApplicationUtils.getBean(CasClientPluginSystemCostants.JPCASCLIENT_CONFIG_MANAGER, reqCtx.getRequest());
		IPageManager pageManager =
			(IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, reqCtx.getRequest());
		Lang lang = langManager.getLang(langCode);
		if (lang == null) {
			lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			if (lang == null) {
				lang = langManager.getDefaultLang();
			}
		}
		String pageCode = pageUrl.getPageCode();
		IPage page = pageManager.getPage(pageCode);
		if (page == null) {
			page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		}
		if (page == null) {
			page = pageManager.getRoot();
		}
		StringBuffer url = new StringBuffer();
		String serverBaseUrl = casClientConfigManager.getClientConfig().getServerBaseURL();
		if (serverBaseUrl.endsWith("/")) {
			serverBaseUrl = serverBaseUrl.substring(0, serverBaseUrl.length()-1);
		}

		url.append(serverBaseUrl);
		url.append(reqCtx.getRequest().getContextPath()).append('/');

		if (!this.isUrlStyleBreadcrumbs(configManager)) {
			url.append(lang.getCode()).append('/');
			url.append(page.getCode()).append(".page");
		} else {
			url.append("pages/");
			url.append(lang.getCode()).append('/');
			StringBuffer fullPath = PageUtils.getFullPath(page, "/");
			url.append(fullPath.append("/"));
		}

		String queryString = this.getQueryStringWithoutTicketParam(reqCtx.getRequest());
		url.append(queryString);
		HttpServletResponse resp = reqCtx.getResponse();
		String encUrl = resp.encodeURL(url.toString());
		return encUrl;
	}

	private String getQueryStringWithoutTicketParam(HttpServletRequest request) {
		boolean isFirst = true;
		StringBuffer queryStr = new StringBuffer();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements() ){
			String name = names.nextElement();
			if (!name.equals("ticket")) {
				if (isFirst) {
					queryStr.append("?");
				}
				String value = request.getParameter(name);
				if (!isFirst) {
					queryStr.append("&");
				}
				queryStr.append(name);
				queryStr.append("=");
				queryStr.append(value);
				isFirst = false;
			}
		}
		return queryStr.toString();
	}

	protected boolean isUrlStyleBreadcrumbs(BaseConfigManager configManager) {
		String param = configManager.getParam(SystemConstants.CONFIG_PARAM_URL_STYLE);
		return (param != null && param.trim().equalsIgnoreCase(SystemConstants.CONFIG_PARAM_URL_STYLE_BREADCRUMBS));
	}

}
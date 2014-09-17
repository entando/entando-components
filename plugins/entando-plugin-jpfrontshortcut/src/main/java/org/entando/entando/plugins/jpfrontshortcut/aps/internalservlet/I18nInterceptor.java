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
package org.entando.entando.plugins.jpfrontshortcut.aps.internalservlet;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @author E.Santoboni
 */
public class I18nInterceptor extends AbstractInterceptor {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> params = invocation.getInvocationContext().getParameters();
		ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, request);
		String langCode = (String) this.findParameter(params, "langCode");
		Lang currentLang = (null != langCode) ? langManager.getLang(langCode) : null;
		if (null == currentLang) {
			currentLang = langManager.getDefaultLang();
		}
		Locale locale = new Locale(currentLang.getCode(), "");
		invocation.getInvocationContext().setLocale(locale);
		return invocation.invoke();
	}
	
	private Object findParameter(Map<String, Object> params, String parameterName) {
		Object param = params.get(parameterName);
		if (param != null && param.getClass().isArray() && ((Object[]) param).length == 1) {
			param = ((Object[]) param)[0];
		}
		return param;
	}
	
}
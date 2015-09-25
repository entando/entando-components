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
package com.agiletec.plugins.jpwebmail.apsadmin.system;

import javax.mail.Store;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.IWebMailBaseAction;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @author E.Santoboni
 */
public class WebMailStoreFactoryInterceptor extends AbstractInterceptor {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		IWebMailManager webmailManager = (IWebMailManager) ApsWebApplicationUtils.getBean(JpwebmailSystemConstants.WEBMAIL_MANAGER, ServletActionContext.getRequest());
		WebMailConfig webMailConfig = webmailManager.getConfiguration();
		HttpSession session = ServletActionContext.getRequest().getSession();
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String userPassword = null;
		if (webMailConfig.isUseEntandoUserPassword()) {
			userPassword = currentUser.getPassword();
		} else {
			userPassword = (String) session.getAttribute(JpwebmailSystemConstants.SESSIONPARAM_CURRENT_MAIL_USER_PASSWORD);
			if (null == userPassword) {
				return "executeWebmailLogin";
			}
		}
		Store store = this.getStore(currentUser, userPassword, webmailManager);
		if (null == store) return "noStore";
		IWebMailBaseAction webMailAction = (IWebMailBaseAction) invocation.getAction();
		webMailAction.setStore(store);
		String result = invocation.invoke();
		webMailAction.closeFolders();
		webmailManager.closeConnection(store);
		return result;
	}
	
	private Store getStore(UserDetails currentUser, String userPassword, IWebMailManager webmailManager) {
		Store store = null;
		try {
			store = webmailManager.initInboxConnection(currentUser.getUsername(), userPassword);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getStore");
			return null;
		}
		return store;
	}
	
}
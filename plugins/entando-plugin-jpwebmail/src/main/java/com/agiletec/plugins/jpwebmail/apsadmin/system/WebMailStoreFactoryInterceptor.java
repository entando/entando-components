/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
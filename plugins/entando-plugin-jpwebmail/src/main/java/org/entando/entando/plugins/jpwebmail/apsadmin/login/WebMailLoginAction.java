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
package org.entando.entando.plugins.jpwebmail.apsadmin.login;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;

import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class WebMailLoginAction extends BaseAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(WebMailLoginAction.class);
	
	@Override
	public void validate() {
		super.validate();
		try {
			WebMailConfig webMailConfig = this.getWebMailManager().getConfiguration();
			if (webMailConfig.isUseEntandoUserPassword()) {
				this.getFieldErrors().clear();
				return;
			}
			Store store = this.getStore();
			if (null == store) {
				this.addFieldError("webmailPassword", this.getText("jpwebmail.error.invalidCredentials"));
			}
		} catch (Throwable t) {
			_logger.error("Error validating webmail login", t);
		}
	}
	
	public String login() {
		UserDetails currentUser = super.getCurrentUser();
		if (null == currentUser || currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
			return "noStore";
		}
		return SUCCESS;
	}
	
	public String doLogin() {
		try {
			WebMailConfig webMailConfig = this.getWebMailManager().getConfiguration();
			if (webMailConfig.isUseEntandoUserPassword()) {
				return SUCCESS;
			}
			this.getRequest().getSession().setAttribute(JpwebmailSystemConstants.SESSIONPARAM_CURRENT_MAIL_USER_PASSWORD, this.getWebmailPassword());
		} catch (Throwable t) {
			_logger.error("Error on login webmail box ", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private Store getStore() {
		UserDetails currentUser = super.getCurrentUser();
		Store store = null;
		try {
			store = this.getWebMailManager().initInboxConnection(currentUser.getUsername(), this.getWebmailPassword());
		} catch (Throwable t) {
			_logger.error("Error extracting store", t);
			return null;
		}
		return store;
	}
	
	public String getWebmailPassword() {
		return _webmailPassword;
	}
	public void setWebmailPassword(String webmailPassword) {
		this._webmailPassword = webmailPassword;
	}
	
	protected IWebMailManager getWebMailManager() {
		return _webMailManager;
	}
	public void setWebMailManager(IWebMailManager webMailManager) {
		this._webMailManager = webMailManager;
	}
	
	private String _webmailPassword;
	
	private IWebMailManager _webMailManager;
	
}
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
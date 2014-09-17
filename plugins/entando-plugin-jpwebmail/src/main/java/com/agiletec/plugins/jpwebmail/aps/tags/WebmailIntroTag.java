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
package com.agiletec.plugins.jpwebmail.aps.tags;

import javax.mail.Folder;
import javax.mail.Store;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;
import com.agiletec.plugins.jpwebmail.aps.tags.util.WebMailIntroInfo;

/**
 * Tag estrattore delle informazioni da erogare nel widget introduttivo al servizio webmail.
 * @author E.Santoboni
 */
public class WebmailIntroTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		IWebMailManager webmailManager = (IWebMailManager) ApsWebApplicationUtils.getBean(JpwebmailSystemConstants.WEBMAIL_MANAGER, pageContext);
		Store store = null;
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		WebMailConfig webMailConfig = null;
		WebMailIntroInfo info = new WebMailIntroInfo();
		try {
			webMailConfig = webmailManager.getConfiguration();
			if (currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				return super.doStartTag();
			}
			if (currentUser.getUsername().equals(SystemConstants.ADMIN_USER_NAME) && !this.isCheckAdmin()) {
				return super.doStartTag();
			}
			String userPassword = null;
			if (webMailConfig.isUseEntandoUserPassword()) {
				userPassword = currentUser.getPassword();
			} else {
				String passwordSessionParam = JpwebmailSystemConstants.SESSIONPARAM_CURRENT_MAIL_USER_PASSWORD;
				String userPasswordRequestParam = this.pageContext.getRequest().getParameter("webmailPassword");
				if (null != userPasswordRequestParam) {
					session.setAttribute(passwordSessionParam, userPasswordRequestParam);
				}
				userPassword = (String) session.getAttribute(passwordSessionParam);
				if (null == userPassword) {
					this.pageContext.setAttribute(this.getRequiredLoginVar(), Boolean.TRUE);
					return super.doStartTag();
				}
			}
			store = webmailManager.initInboxConnection(currentUser.getUsername(), userPassword);
		} catch (Throwable t) {
			webmailManager.closeConnection(store);
			this.pageContext.setAttribute(this.getVar(), info);
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			if (!currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME) 
					&& null != webMailConfig 
					&& !webMailConfig.isUseEntandoUserPassword()) {
				this.pageContext.setAttribute(this.getRequiredLoginVar(), Boolean.TRUE);
			}
			return super.doStartTag();
		}
		try {
			Folder inboxFolder = store.getFolder(JpwebmailSystemConstants.INBOX_FOLDER);
			info.setExistMailbox(true);
			info.setMessageCount(inboxFolder.getMessageCount());
			info.setNewMessageCount(inboxFolder.getNewMessageCount());
			info.setUnreadMessageCount(inboxFolder.getUnreadMessageCount());
			this.pageContext.setAttribute(this.getVar(), info);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Errore inizializzazione tag", t);
		} finally {
			webmailManager.closeConnection(store);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this._var = null;
		this.setRequiredLoginVar(null);
		this._checkAdmin = false;
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	public String getRequiredLoginVar() {
		if (null == this._requiredLoginVar) {
			return "jpwebmail_loginRequired";
		}
		return _requiredLoginVar;
	}
	public void setRequiredLoginVar(String requiredLoginVar) {
		this._requiredLoginVar = requiredLoginVar;
	}
	
	public boolean isCheckAdmin() {
		return _checkAdmin;
	}
	public void setCheckAdmin(boolean checkAdmin) {
		this._checkAdmin = checkAdmin;
	}
	
	private String _var;
	private String _requiredLoginVar;
	private boolean _checkAdmin = false;
	
}
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
package com.agiletec.plugins.jpmail.apsadmin.mail;

import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;
import com.agiletec.plugins.jpmail.aps.services.mail.util.EmailAddressValidator;

/**
 * Implementation for the actions of editing senders of MailManager configuration.<br />
 * The saving action doesn't save the sender permanently but only in a session object, so must be followed by save operation in MailConfigAction.
 * @author E.Mezzano
 */
public class MailSenderConfigAction extends BaseAction {
	
	@Override
	public void validate() {
		super.validate();
		try {
			String code = this.getCode();
			String mail = this.getMail();
			if (code != null && code.length() > 0 && ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
				if (null != this.getConfig().getSender(code)) {
					this.addFieldError("code", this.getText("error.config.sender.code.duplicated"));
				}
			}
			if (mail!=null && mail.length() > 0 && !EmailAddressValidator.isValidEmailAddress(mail)) {
				String[] args = {mail};
				this.addFieldError("mail", this.getText("error.config.sender.mail.notValid", args));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate");
			throw new RuntimeException("Error saving mail address configuration", t);
		}
	}
	
	public String newSender() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}
	
	public String edit() {
		try {
			MailConfig config = this.getConfig();
			String code = this.getCode();
			String mail = config.getSender(code);
			if (mail==null || mail.length()==0) {
				this.addActionError(this.getText("error.config.sender.notExists"));
				return ERROR;
			}
			this.setCode(code);
			this.setMail(mail);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			MailConfig config = this.prepareConfig();
			this.getMailManager().updateMailConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			MailConfig config = this.getConfig();
			String code = this.getCode();
			if (null == code && null == config.getSender(code)) {
				this.addActionError(this.getText("error.config.sender.notExists"));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "remove");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			MailConfig config = this.getConfig();
			String code = this.getCode();
			if (null == code && null == config.getSender(code)) {
				this.addActionError(this.getText("error.config.sender.notExists"));
				return INPUT;
			} else {
				config.getSenders().remove(code);
				this.getMailManager().updateMailConfig(config);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "remove");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Prepares a MailConfig starting from the action form fields.
	 * @return a MailConfig starting from the action form fields.
	 * @throws ApsSystemException In case of errors.
	 */
	protected MailConfig prepareConfig() throws ApsSystemException {
		MailConfig config = this.getConfig();
		Map<String, String> senders = config.getSenders();
		String code = this.getCode();
		String mail = this.getMail();
		senders.put(code, mail);
		return config;
	}
	
	/**
	 * Returns the mail configuration.
	 * @return The mail configuration.
	 */
	public MailConfig getConfig() {
		if (null==this._mailConfig) {
			try {
				this._mailConfig = this.getMailManager().getMailConfig();
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getConfig");
				throw new RuntimeException("Error loading mailConfig", t);
			}
		}
		return this._mailConfig;
	}
	
	/**
	 * Returns the sender code.
	 * @return The sender code.
	 */
	public String getCode() {
		return _code;
	}
	/**
	 * Sets the sender code.
	 * @param code The sender code.
	 */
	public void setCode(String code) {
		this._code = code;
	}
	
	/**
	 * Returns the mail address.
	 * @return The mail address.
	 */
	public String getMail() {
		return _mail;
	}
	/**
	 * Sets the mail address.
	 * @param mail The mail address.
	 */
	public void setMail(String mail) {
		this._mail = mail;
	}
	
	/**
	 * Returns the Struts action type.
	 * @return The Struts action type.
	 */
	public int getStrutsAction() {
		return _strutsAction;
	}
	/**
	 * Sets the Struts action type. Could be one of {@link ApsAdminSystemConstants#ADD} and {@link ApsAdminSystemConstants#EDIT}.
	 * @param strutsAction The Struts action type.
	 */
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	/**
	 * Returns the IMailManager service.
	 * @return The IMailManager service.
	 */
	public IMailManager getMailManager() {
		return _mailManager;
	}
	/**
	 * Set method for Spring bean injection.<br /> Sets the IMailManager service.
	 * @param mailManager The IMailManager service.
	 */
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	private String _code;
	private String _mail;
	private int _strutsAction;
	
	private MailConfig _mailConfig;
	
	private IMailManager _mailManager;
	
}
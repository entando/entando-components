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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

public class SmtpConfigAction extends BaseAction {

	public String edit() {
		try {
			MailConfig config = this.getMailManager().getMailConfig();
			this.populateForm(config);
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
			this.addActionMessage(this.getText("message.eMailConfig.savedConfirm"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String testSmtp() {
		MailConfig config;
		try {
			config = this.prepareConfig();
			boolean smtpServerTest = this.getMailManager().smtpServerTest(config);
			if (!smtpServerTest) {
				this.addActionError(this.getText("note.smtp.ko"));
			} else {
				this.addActionMessage(this.getText("note.smtp.ok"));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "testSmtp");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String testMail() {
		if (hasEmailCurrentUser()) {
			IUserProfile userProfile = (IUserProfile) this.getCurrentUser().getProfile();
			String mail = userProfile.getValue(userProfile.getMailAttributeName()).toString();
			String mailText = this.getText("test.mail.subject");
			String mailSubject = this.getText("test.mail.text");
			String[] mailAddresses = {mail};
			try {
				String sender = null;
				Map<String, String> senders = this.getMailManager().getMailConfig().getSenders();
				if (null != senders && !senders.isEmpty()) {
					List<String> codes = new ArrayList<String>();
					codes.addAll(senders.keySet());
					sender = codes.get(0);
				}
				if (null == sender) {
					this.addActionError(this.getText("note.mail.nosender"));
					return INPUT;
				}
				this.getMailManager().sendMailForTest(mailText, mailSubject, mailAddresses, sender);
				this.addActionMessage(this.getText("note.mail.ok"));
			} catch (Throwable t) {
				this.addActionError(this.getText("note.mail.ko"));
				ApsSystemUtils.logThrowable(t, this, "testMail");
			}
		} else {
			this.addActionError(this.getText("note.mail.missing"));
		}
		return SUCCESS;
	}
	
	/**
	 * Populate the action with the content of the given MailConfig.
	 * @param config The configuration used to populate the action.
	 */
	protected void populateForm(MailConfig config) {
		if (config != null) {
			this.setActive(config.isActive());
			this.setDebug(config.isDebug());
			this.setSmtpHost(config.getSmtpHost());
			this.setSmtpPort(config.getSmtpPort());
			this.setSmtpTimeout(config.getSmtpTimeout());
			this.setSmtpUserName(config.getSmtpUserName());
			this.setSmtpPassword(config.getSmtpPassword());
			this.setSmtpProtocol(config.getSmtpProtocol());
		} else {
			config = new MailConfig();
		}
	}
	
	/**
	 * Prepares a MailConfig starting from the action form fields.
	 * @return a MailConfig starting from the action form fields.
	 * @throws ApsSystemException In case of errors.
	 */
	protected MailConfig prepareConfig() throws ApsSystemException {
		MailConfig config = this.getMailManager().getMailConfig();
		config.setActive(this.isActive());
		config.setDebug(this.isDebug());
		config.setSmtpHost(this.getSmtpHost());
		config.setSmtpPort(this.getSmtpPort());
		config.setSmtpTimeout(this.getSmtpTimeout());
		config.setSmtpProtocol(this.getSmtpProtocol());
		if (StringUtils.isBlank(this.getSmtpUserName()) && StringUtils.isBlank(this.getSmtpPassword())) {
			config.setSmtpUserName(this.getSmtpUserName());
			config.setSmtpPassword(this.getSmtpPassword());
		} else {
			if (StringUtils.isNotBlank(this.getSmtpPassword())) {
				config.setSmtpPassword(this.getSmtpPassword());
			}
			if (!config.getSmtpUserName().equals(this.getSmtpUserName())) {
				config.setSmtpUserName(this.getSmtpUserName());
			}
		}
		return config;
	}

	public boolean hasEmailCurrentUser() {
		IUserProfile userProfile = (IUserProfile) this.getCurrentUser().getProfile();
		if (null != userProfile) {
			Object mailAttribute = userProfile.getValue(userProfile.getMailAttributeName());
			if (null != mailAttribute && StringUtils.isNotBlank(mailAttribute.toString())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Validation Service method
	 */
	public boolean isValidProtocol() {
		return (null != this.getSmtpProtocol()
				&& this.getSmtpProtocol().intValue() >= 0
				&& this.getSmtpProtocol().intValue() <= 2);
	}
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	/**
	 * Returns the smtp host name.
	 * @return The smtp host name.
	 */
	public String getSmtpHost() {
		return _smtpHost;
	}
	
	/**
	 * Sets the smtp host name.
	 * @param smtpHost The smtp host name.
	 */
	public void setSmtpHost(String smtpHost) {
		this._smtpHost = smtpHost;
	}
	
	/**
	 * Return the smtp port.
	 * @return The smtp port.
	 */
	public Integer getSmtpPort() {
		return _smtpPort;
	}
	
	/**
	 * Sets the smtp port.
	 * @param smtpPort The smtp port.
	 */
	public void setSmtpPort(Integer smtpPort) {
		this._smtpPort = smtpPort;
	}

	/**
	 * Return the smtp timeout.
	 * @return The smtp timeout.
	 */
	public Integer getSmtpTimeout() {
		return _smtpTimeout;
	}
	
	/**
	 * Sets the smtp timeout. If 0 or null uses default.
	 * @param smtpTimeout The smtp timeout.
	 */
	public void setSmtpTimeout(Integer smtpTimeout) {
		this._smtpTimeout = smtpTimeout;
	}
	
	/**
	 * Returns the smtp username.
	 * @return The smtp username.
	 */
	public String getSmtpUserName() {
		return _smtpUserName;
	}
	
	/**
	 * Sets the smtp username.
	 * @param smtpUserName The smtp username.
	 */
	public void setSmtpUserName(String smtpUserName) {
		this._smtpUserName = smtpUserName;
	}
	
	/**
	 * Returns the smtp password.
	 * @return The smtp password.
	 */
	public String getSmtpPassword() {
		return _smtpPassword;
	}
	
	/**
	 * Sets the smtp password.
	 * @param smtpPassword The smtp password.
	 */
	public void setSmtpPassword(String smtpPassword) {
		this._smtpPassword = smtpPassword;
	}
	
	/**
	 * Returns the debug flag, used to trace debug informations.
	 * @return The debug flag.
	 */
	public boolean isDebug() {
		return _debug;
	}
	
	/**
	 * Sets the debug flag, used to trace debug informations.
	 * @param debug The debug flag.
	 */
	public void setDebug(boolean debug) {
		this._debug = debug;
	}
	
	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}

	/**
	 * Set the transport security layer protocol
	 * @param smtpProtocol the smtp protocol
	 */
	public void setSmtpProtocol(Integer smtpProtocol) {
		this._smtpProtocol = smtpProtocol;
	}
	
	/**
	 * Get the transport security layer protocol
	 * @return the smtp protocol
	 */
	public Integer getSmtpProtocol() {
		return _smtpProtocol;
	}
	
	public InputStream getInputStream() {
		return _inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}
	
	private boolean _active;
	private String _smtpHost;
	private Integer _smtpPort;
	private Integer _smtpTimeout;
	private String _smtpUserName;
	private String _smtpPassword;
	private Integer _smtpProtocol;
	private boolean _debug;
	private IMailManager _mailManager;
	private InputStream _inputStream;
	
}

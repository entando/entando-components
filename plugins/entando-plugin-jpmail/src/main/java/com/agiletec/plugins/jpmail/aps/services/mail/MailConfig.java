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
package com.agiletec.plugins.jpmail.aps.services.mail;

import java.util.Map;
import java.util.TreeMap;

import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;

/**
 * Bean class containing the basic configuration for the IMailManager service.
 * @author E.Santoboni, E.Mezzano
 */
public class MailConfig implements Cloneable {
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		MailConfig config = new MailConfig();
		config.setActive(this.isActive());
		config.setDebug(this.isDebug());
		config.setSmtpHost(this.getSmtpHost());
		config.setSmtpPort(this.getSmtpPort());
		config.setSmtpTimeout(this.getSmtpTimeout());
		config.setSmtpUserName(this.getSmtpUserName());
		config.setSmtpUserName(this.getSmtpUserName());
		config.setSmtpPassword(this.getSmtpPassword());
		config.setSmtpProtocol(this.getSmtpProtocol());
		config.setSenders(new TreeMap<String, String>(this.getSenders()));
		return config;
	}
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	/**
	 * Return true if mail configuration expects an anonymous authentication.<br/>
	 * NOTE: an anonymous authentication occurs whenever the username and the
	 * associated password are <b>not</b> provided <b>and</b> no security
	 * encapsulation protocol is specified.
	 * @return True if the username and the password are not provided
	 */
	public boolean hasAnonimousAuth() {
		return (((null == this._smtpUserName || this._smtpUserName.length() == 0)
				&& (null == this._smtpPassword || this._smtpPassword.length() == 0)) && 
				!hasSecureSmtp());
	}
	
	/**
	 * Return true if the mail transport uses a secure connection.
	 * @return true if the selected encapsulation protocol is SSL or TLS
	 */
	public boolean hasSecureSmtp() {
		if (null == this._smtpProtocol || 
				this._smtpProtocol == JpmailSystemConstants.PROTO_STD) return false;
		return true;
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
	 * @param port The smtp port.
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
	 * @param port The smtp timeout.
	 */
	public void setSmtpTimeout(Integer smtpTimeout) {
		this._smtpTimeout = smtpTimeout;
	}
	
	/**
	 * Returns the password for the smtp access.
	 * @return The password for the smtp access.
	 */
	public String getSmtpPassword() {
		return _smtpPassword;
	}
	/**
	 * Sets the password for the smtp access.
	 * @param smtpPassword The password for the smtp access.
	 */
	public void setSmtpPassword(String smtpPassword) {
		this._smtpPassword = smtpPassword;
	}
	
	/**
	 * Returns the username for the smtp access.
	 * @return The username for the smtp access.
	 */
	public String getSmtpUserName() {
		return _smtpUserName;
	}
	/**
	 * Sets the username for the smtp access.
	 * @param smtpUserName The username for the smtp access.
	 */
	public void setSmtpUserName(String smtpUserName) {
		this._smtpUserName = smtpUserName;
	}
	
	/**
	 * Returns the senders, mapped as code/address.
	 * @return The senders, mapped as code/address.
	 */
	public Map<String, String> getSenders() {
		return _senders;
	}
	/**
	 * Sets the senders, mapped as code/address.
	 * @param senders The senders, mapped as code/address.
	 */
	public void setSenders(Map<String, String> senders) {
		this._senders = senders;
	}
	/**
	 * Add a sender address.
	 * @param code The code of the sender.
	 * @param sender The address of the sender.
	 */
	public void addSender(String code, String sender) {
		this._senders.put(code, sender);
	}
	/**
	 * The sender address with given code.
	 * @param code the code of the sender.
	 * @return The sender with given code.
	 */
	public String getSender(String code) {
		return (String) this._senders.get(code);
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
	
	public void setSmtpProtocol(Integer smtpProtocol) {
		this._smtpProtocol = smtpProtocol;
	}
	public Integer getSmtpProtocol() {
		return _smtpProtocol;
	}
	
	private boolean _active;
	private Map<String, String> _senders = new TreeMap<String, String>();
	private String _smtpHost;
	private Integer _smtpPort;
	private Integer _smtpTimeout;
	private String _smtpUserName;
	private String _smtpPassword;
	private Integer _smtpProtocol;
	private boolean _debug;
	
}
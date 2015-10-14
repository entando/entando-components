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

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.parse.MailConfigDOM;

/**
 * Implementation for the manager providing email sending functions.
 * @author E.Santoboni, E.Mezzano
 */
public class MailManager extends AbstractService implements IMailManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(MailManager.class);
	
	@Override
	public void init() throws Exception {
		try {
			this.loadConfigs();
			_logger.debug("{} ready: active {}", this.getClass().getName(), this.isActive());
		} catch (Throwable t) {
			_logger.error("{} Manager: Error on initialization", this.getClass().getName(), t);
			this.setActive(false);
		}
	}
	
	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpmailSystemConstants.MAIL_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpmailSystemConstants.MAIL_CONFIG_ITEM);
			}
			MailConfigDOM configDOM = new MailConfigDOM();
			this.setConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error in loadConfigs", t);
			throw new ApsSystemException("Error in loadConfigs", t);
		}
	}
	
	@Override
	public MailConfig getMailConfig() throws ApsSystemException {
		try {
			return (MailConfig) this._config.clone();
		} catch (Throwable t) {
			_logger.error("Error loading mail service configuration", t);
			throw new ApsSystemException("Error loading mail service configuration", t);
		}
	}
	
	@Override
	public void updateMailConfig(MailConfig config) throws ApsSystemException {
		try {
			String xml = new MailConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpmailSystemConstants.MAIL_CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error updating configs", t);
			throw new ApsSystemException("Error updating configs", t);
		}
	}
	
	@Override
	public boolean sendMail(String text, String subject, String[] recipientsTo,
			String[] recipientsCc, String[] recipientsBcc, String senderCode) throws ApsSystemException {
		return this.sendMail(text, subject, CONTENTTYPE_TEXT_PLAIN, null, recipientsTo, recipientsCc, recipientsBcc, senderCode);
	}
	
	@Override
	public boolean sendMail(String text, String subject, String[] recipientsTo,
			String[] recipientsCc, String[] recipientsBcc, String senderCode, String contentType) throws ApsSystemException {
		return this.sendMail(text, subject, contentType, null, recipientsTo, recipientsCc, recipientsBcc, senderCode);
	}
	
	@Override
	public boolean smtpServerTest(MailConfig mailConfig) {
		try {
			Session session = prepareSession(mailConfig);
			Transport bus = session.getTransport("smtp");
			if (mailConfig.hasAnonimousAuth()) {
				bus.connect();
			} else {
				bus.connect(mailConfig.getSmtpHost(), mailConfig.getSmtpPort(), mailConfig.getSmtpUserName(), mailConfig.getSmtpPassword());
			}
			bus.close();
			return true;
		} catch (Exception e) {
			_logger.error("error in test smptserver", e);
			return false;
		}
	}
	
	@Override
	public boolean sendMail(String text, String subject, String contentType, Properties attachmentFiles, String[] recipientsTo,
			String[] recipientsCc, String[] recipientsBcc, String senderCode) throws ApsSystemException {
		if (!isActive()) {
			_logger.info("Sender function disabled : mail Subject {}", subject);
			return true;
		}
		return send(text, subject, recipientsTo, recipientsCc, recipientsBcc, senderCode, attachmentFiles, contentType);
	}
	
	@Override
	public boolean sendMailForTest(String text, String subject, String[] recipientsTo, String senderCode) throws ApsSystemException {
		return this.send(text, subject, recipientsTo, null, null, senderCode,null, CONTENTTYPE_TEXT_PLAIN);
	}
	
	private boolean send(String text, String subject, String[] recipientsTo, String[] recipientsCc, String[] recipientsBcc, String senderCode, Properties attachmentFiles, String contentType) throws ApsSystemException {
		Transport bus = null;
		try {
			Session session = this.prepareSession(this.getConfig());
			bus = this.prepareTransport(session, this.getConfig());
			MimeMessage msg = this.prepareVoidMimeMessage(session, subject, recipientsTo, recipientsCc, recipientsBcc, senderCode);
			if (attachmentFiles == null || attachmentFiles.isEmpty()) {
				msg.setContent(text, contentType + "; charset=utf-8");
			} else {
				Multipart multiPart = new MimeMultipart();
				this.addBodyPart(text, contentType, multiPart);
				this.addAttachments(attachmentFiles, multiPart);
				msg.setContent(multiPart);
			}
			msg.saveChanges();
			bus.send(msg);
		} catch (Throwable t) {
			throw new ApsSystemException("Error sending mail", t);
		} finally {
			closeTransport(bus);
		}
		return true;
	}
	
	@Override
	public boolean sendMixedMail(String simpleText, String htmlText, String subject, Properties attachmentFiles,
			String[] recipientsTo, String[] recipientsCc, String[] recipientsBcc, String senderCode) throws ApsSystemException {
		if (!isActive()) {
			_logger.info("Sender function disabled : mail Subject " + subject);
			return true;
		}
		Transport bus = null;
		try {
			Session session = this.prepareSession(this.getConfig());
			bus = this.prepareTransport(session, this.getConfig());
			MimeMessage msg = this.prepareVoidMimeMessage(session, subject, recipientsTo, recipientsCc, recipientsBcc, senderCode);
			boolean hasAttachments = attachmentFiles != null && attachmentFiles.size() > 0;
			String multipartMimeType = hasAttachments ? "mixed" : "alternative";
			MimeMultipart multiPart = new MimeMultipart(multipartMimeType);
			this.addBodyPart(simpleText, CONTENTTYPE_TEXT_PLAIN, multiPart);
			this.addBodyPart(htmlText, CONTENTTYPE_TEXT_HTML, multiPart);
			if (hasAttachments) {
				this.addAttachments(attachmentFiles, multiPart);
			}
			msg.setContent(multiPart);
			msg.saveChanges();
			bus.send(msg);
		} catch (Throwable t) {
			throw new ApsSystemException("Error sending mail", t);
		} finally {
			closeTransport(bus);
		}
		return true;
	}
	
	/**
	 * Prepare a Transport object ready for use.
	 * @param session A session object.
	 * @param config The configuration
	 * @return The Transport object ready for use.
	 * @throws Exception In case of errors opening the Transport object.
	 */
	protected Transport prepareTransport(Session session, MailConfig config) throws Exception {
		Transport bus = session.getTransport("smtp");
		if (config.hasAnonimousAuth()) {
			bus.connect();
		}
		return bus;
	}

	/**
	 * Prepare a Session object ready for use.
	 * @param config The configuration
	 * @return The Session object ready for use.
	 */
	protected Session prepareSession(MailConfig config) {
		Properties props = System.getProperties();
		Session session = null;
		// Timeout
		int timeout = DEFAULT_SMTP_TIMEOUT;
		Integer timeoutParam = config.getSmtpTimeout();
		if (null != timeoutParam && timeoutParam.intValue() != 0) {
			timeout = timeoutParam;
		}
		props.put("mail.smtp.connectiontimeout", timeout);
		props.put("mail.smtp.timeout", timeout);
		// Debug
		if (config.isDebug()) {
			props.put("mail.debug", "true");
		}
		// port
		Integer port = config.getSmtpPort();
		if (null != port && port.intValue() > 0) {
			props.put("mail.smtp.port", port.toString());
		} else {
			props.put("mail.smtp.port", JpmailSystemConstants.DEFAULT_SMTP_PORT.toString());
		}
		// host
		props.put("mail.smtp.host", config.getSmtpHost());
		// auth
		if (!config.hasAnonimousAuth()) {
			props.put("mail.smtp.auth", "true");
			switch (config.getSmtpProtocol()) {
				case JpmailSystemConstants.PROTO_SSL:
					props.put("mail.smtp.socketFactory.port", port);
					props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
					props.put("mail.transport.protocol", "smtps");
					break;
				case JpmailSystemConstants.PROTO_TLS:
					props.put("mail.smtp.starttls.enable", "true");
					break;
				case JpmailSystemConstants.PROTO_STD:
				// do nothing just use previous properties WITH the authenticator
			}
			Authenticator auth = new SMTPAuthenticator(config);
			session = Session.getInstance(props, auth);
		} else {
			session = Session.getDefaultInstance(props);
		}
		return session;
	}
	
	/**
	 * Prepare a MimeMessage complete of sender, recipient addresses, subject
	 * and current date but lacking in the message text content.
	 * @param session The session object.
	 * @param subject The e-mail subject.
	 * @param recipientsTo The e-mail main destination addresses.
	 * @param recipientsCc The e-mail 'carbon-copy' destination addresses.
	 * @param recipientsBcc The e-mail 'blind carbon-copy' destination addresses.
	 * @param senderCode The sender code, as configured in the service configuration.
	 * @return A mime message without message text content.
	 * @throws AddressException In case of non-valid e-mail addresses.
	 * @throws MessagingException In case of errors preparing the mail message.
	 */
	protected MimeMessage prepareVoidMimeMessage(Session session, String subject, String[] recipientsTo,
			String[] recipientsCc, String[] recipientsBcc, String senderCode) throws AddressException, MessagingException {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(this.getConfig().getSender(senderCode)));
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		this.addRecipients(msg, Message.RecipientType.TO, recipientsTo);
		this.addRecipients(msg, Message.RecipientType.CC, recipientsCc);
		this.addRecipients(msg, Message.RecipientType.BCC, recipientsBcc);
		msg.saveChanges();
		return msg;
	}
	
	/**
	 * Add a BodyPart to the Multipart container.
	 * @param text The text content.
	 * @param contentType The text contentType.
	 * @param multiPart The Multipart container.
	 * @throws MessagingException In case of errors adding the body part.
	 */
	protected void addBodyPart(String text, String contentType, Multipart multiPart) throws MessagingException {
		MimeBodyPart textBodyPart = new MimeBodyPart();
		textBodyPart.setContent(text, contentType + "; charset=utf-8");
		multiPart.addBodyPart(textBodyPart);
	}

	/**
	 * Add the attachments to the Multipart container.
	 * @param attachmentFiles The attachments mapped as fileName/filePath.
	 * @param multiPart The Multipart container.
	 * @throws MessagingException In case of errors adding the attachments.
	 */
	protected void addAttachments(Properties attachmentFiles, Multipart multiPart) throws MessagingException {
		Iterator filesIter = attachmentFiles.entrySet().iterator();
		while (filesIter.hasNext()) {
			Entry fileEntry = (Entry) filesIter.next();
			MimeBodyPart fileBodyPart = new MimeBodyPart();
			DataSource dataSource = new FileDataSource((String) fileEntry.getValue());
			fileBodyPart.setDataHandler(new DataHandler(dataSource));
			fileBodyPart.setFileName((String) fileEntry.getKey());
			multiPart.addBodyPart(fileBodyPart);
		}
	}

	/**
	 * Add recipient addresses to the e-mail.
	 * @param msg The mime message to which add the addresses.
	 * @param recType The specific recipient type to which add the addresses.
	 * @param recipients The recipient addresses.
	 */
	protected void addRecipients(MimeMessage msg, RecipientType recType, String[] recipients) {
		if (null != recipients) {
			try {
				Address[] addresses = new Address[recipients.length];
				for (int i = 0; i < recipients.length; i++) {
					Address address = new InternetAddress(recipients[i]);
					addresses[i] = address;
				}
				msg.setRecipients(recType, addresses);
			} catch (MessagingException e) {
				throw new RuntimeException("Error adding recipients", e);
			}
		}
	}
	
	/**
	 * Close the transport.
	 * @param transport The transport.
	 * @throws ApsSystemException In case of errors closing the transport.
	 */
	protected void closeTransport(Transport transport) throws ApsSystemException {
		if (transport != null) {
			try {
				transport.close();
			} catch (MessagingException e) {
				throw new ApsSystemException("Error closing connection", e);
			}
		}
	}

	/**
	 * returns the mail service configuration.
	 * @return The mail service configuration.
	 */
	protected MailConfig getConfig() {
		return _config;
	}

	/**
	 * Set the mail service configuration.
	 * @param config The mail service configuration.
	 */
	protected void setConfig(MailConfig config) {
		this._config = config;
	}

	protected Boolean isActive() {
		if (null != this._active) {
			return this._active.booleanValue();
		}
		return this.getConfig().isActive();
	}
	
	public void setActive(Boolean active) {
		this._active = active;
	}
	
	/**
	 * Returns the configuration manager.
	 * @return The Configuration manager.
	 */
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	
	/**
	 * Set method for Spring bean injection.<br /> Set the Configuration manager.
	 * @param configManager The Configuration manager.
	 */
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	private Boolean _active;
	private MailConfig _config;
	private ConfigInterface _configManager;
	
	/*
	 * Default Timeout in milliseconds
	 */
	public static final int DEFAULT_SMTP_TIMEOUT = 5000;
	
}

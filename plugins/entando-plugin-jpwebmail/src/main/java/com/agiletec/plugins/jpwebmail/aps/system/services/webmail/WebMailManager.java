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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.parse.WebMailConfigDOM;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.utils.CertificateHandler;
import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;

/**
 * Servizio gestore della WebMail.
 * @author E.Santoboni
 */
public class WebMailManager extends AbstractService implements IWebMailManager {
	
	private static final org.slf4j.Logger _logger = LoggerFactory.getLogger(WebMailManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadConfigs();
		_logger.debug(this.getClass().getName() + ": inizialized");
	}
	
	private void loadConfigs() throws ApsSystemException {
		try {
			String xml = this.getConfigManager().getConfigItem(JpwebmailSystemConstants.WEBMAIL_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Missing confi item: " + JpwebmailSystemConstants.WEBMAIL_CONFIG_ITEM);
			}
			WebMailConfigDOM contactConfigDom = new WebMailConfigDOM();
			this.setConfig(contactConfigDom.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error loading config", t);
			//ApsSystemUtils.logThrowable(t, this, "loadConfigs");
			throw new ApsSystemException("Error loading config", t);
		}
	}
	
	@Override
	@Deprecated
	public WebMailConfig loadConfig() throws ApsSystemException {
		return this.getConfiguration();
	}
	
	@Override
	public WebMailConfig getConfiguration() throws ApsSystemException {
		return this.getConfig().clone();
	}
	
	@Override
	public void updateConfig(WebMailConfig config) throws ApsSystemException {
		try {
			String xml = new WebMailConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpwebmailSystemConstants.WEBMAIL_CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error updating Web Mail Service configuration", t);
			//ApsSystemUtils.logThrowable(t, this, "updateConfig", "Error updating Web Mail Service configuration");
			throw new ApsSystemException("Error updating Web Mail Service configuration", t);
		}
	}
	
	@Override
	public Store initInboxConnection(String username, String password) throws ApsSystemException {
		//Logger log = ApsSystemUtils.getLogger();
		Store store = null;
		try {
			// Get session
			Session session = this.createSession(false, null, null);
			// Get the store
			store = session.getStore(this.getConfig().getImapProtocol());
			// Connect to store
			//if (log.isLoggable(Level.INFO)) {
			//	log.info("Connection of user " + username);
			//}
			_logger.info("Connection of user " + username);
//			 System.out.print("** tentivo di connessione con protocollo"+this.getConfig().getImapProtocol()+"" +
//			 		" a "+this.getConfig().getImapHost()+" ["+this.getConfig().getImapPort()+"]\n");
			store.connect(this.getConfig().getImapHost(), username, password);
		} catch (NoSuchProviderException e) {
			_logger.error("Error opening Provider connection", e);
			//ApsSystemUtils.logThrowable(e, this, "initInboxConnection", "Provider " + this.getConfig().getImapHost() + " non raggiungibile");
			throw new ApsSystemException("Error opening Provider connection", e);
		} catch (Throwable t) {
			_logger.error("Error opening Provider connection", t);
			//ApsSystemUtils.logThrowable(t, this, "initInboxConnection", "Error opening Provider connection");
			throw new ApsSystemException("Error opening Provider connection", t);
		}
		return store;
	}
	
	@Override
	public MimeMessage createNewEmptyMessage(String username, String password) throws ApsSystemException {
		try {
			String smtpUsername = this.checkSmtpAuthUsername(username);
			String smtpPassword = this.checkSmtpAuthUserPassword(password);
			Session session = this.createSession(true, smtpUsername, smtpPassword);
			return new JpMimeMessage(session);
		} catch (Throwable t) {
			_logger.error("Error creating void message", t);
			//ApsSystemUtils.logThrowable(t, this, "createNewEmptyMessage", "Error creating void message");
			throw new ApsSystemException("Error creating void message", t);
		}
	}
	
	@Override
	public void sendMail(MimeMessage msg, String username, String password) throws ApsSystemException {
		//WebMailConfig config = this.getConfig();
		String smtpUsername = this.checkSmtpAuthUsername(username);
		String smtpPassword = this.checkSmtpAuthUserPassword(password);
		//String smtpUsername = (config.isSmtpEntandoUserAuth()) ? username : config.getSmtpUserName();
		//String smtpPassword = (config.isSmtpEntandoUserAuth()) ? password : config.getSmtpPassword();
		//Session session = this.createSession(true, smtpUsername, smtpPassword);
		Session session = (msg instanceof JpMimeMessage) ? ((JpMimeMessage) msg).getSession() : this.createSession(true, smtpUsername, smtpPassword);
		Transport bus = null;
		try {
			bus = session.getTransport("smtp");
			//StringUtils.isEmpty(bus)
			if (StringUtils.isBlank(smtpUsername) && StringUtils.isBlank(smtpPassword)) {
				bus.connect();
			}
			/*
			if ((smtpUsername != null && smtpUsername.trim().length()>0) && 
					(smtpPassword != null && smtpPassword.trim().length()>0)) {
				if (port != null && port.intValue() > 0) {
					bus.connect(config.getSmtpHost(), port.intValue(), smtpUsername, smtpPassword);
				} else {
					bus.connect(config.getSmtpHost(), smtpUsername, smtpPassword);
				}
			} else {
				bus.connect();
			}
			*/
			//bus.connect();
			msg.saveChanges();
			bus.send(msg);
		} catch (Throwable t) {
			_logger.error("Error sending mail", t);
			//ApsSystemUtils.logThrowable(t, this, "sendMail", "Error sending mail");
			throw new ApsSystemException("Error sending mail", t);
		} finally {
			closeTransport(bus);
		}
	}
	
	private String checkSmtpAuthUsername(String username) {
		WebMailConfig config = this.getConfig();
		String usernameToReturn = null;
		switch (config.getSmtpAuth()) {
			case WebMailConfig.SMTP_AUTH_ANONYMOUS:
				break;
			case WebMailConfig.SMTP_AUTH_ENTANDO_USER:
				usernameToReturn = username;
				break;
			case WebMailConfig.SMTP_AUTH_ENTANDO_USER_WITH_DOMAIN:
				usernameToReturn = username;
				String suffix = "@"+config.getDomainName();
				if (!usernameToReturn.endsWith(suffix)) {
					usernameToReturn += suffix;
				}
				break;
			case WebMailConfig.SMTP_AUTH_CUSTOM:
				usernameToReturn = config.getSmtpUserName();
				break;
		}
		return usernameToReturn;
	}
	
	private String checkSmtpAuthUserPassword(String password) {
		WebMailConfig config = this.getConfig();
		String userPasswordToReturn = null;
		switch (config.getSmtpAuth()) {
			case WebMailConfig.SMTP_AUTH_ANONYMOUS:
				break;
			case WebMailConfig.SMTP_AUTH_ENTANDO_USER:
				userPasswordToReturn = password;
				break;
			case WebMailConfig.SMTP_AUTH_ENTANDO_USER_WITH_DOMAIN:
				userPasswordToReturn = password;
				break;
			case WebMailConfig.SMTP_AUTH_CUSTOM:
				userPasswordToReturn = config.getSmtpPassword();
				break;
		}
		return userPasswordToReturn;
	}
	
	/**
	 * Prepara la Session per l'invio della mail, inoltre effettua l'handshake SSL con l'host di destinazione quando richiesto. 
	 * @return La Session pronta per l'uso.
	 */
	protected Session createSession(boolean sentMail, String username, String password) throws ApsSystemException {
		//Properties properties = System.getProperties();
		Properties properties = new Properties();
		WebMailConfig config = this.getConfig();
		String imapProtocol = config.getImapProtocol();
		String host = config.getImapHost();
		Integer port = config.getImapPort();
		
		if (null != imapProtocol && imapProtocol.trim().length()>0) {
			properties.setProperty("mail.store.protocol", imapProtocol);
		} else {
			throw new ApsSystemException("IMAP Protocoll missing");
		}
		if (null != host && host.trim().length()>0) {
			properties.setProperty("mail.imap.host", host);
		} else {
			throw new ApsSystemException("IMAP host missing");
		}
		if (null != port && port.intValue()>0) {
			properties.setProperty("mail.imap.port", port.toString());
		} else {
			throw new ApsSystemException("IMAP port missing");
		}
		// analizza il certificato dell'host. Se l'handshake è già stato effettuato ritorna immediatamente
		this.getCertificateHandler().aquireCertificate(host, port.intValue(), imapProtocol, config);
		// verifica se si possa procedere in sicurezza con la connessione all'host
		if (!this.getCertificateHandler().proceedWithConnection()) {
			_logger.info("Connection to host '" + host + "' not trusted");
			//ApsSystemUtils.getLogger().info("Connection to host '" + host + "' not trusted");
		} else {
			_logger.info("Connection to host '" + host + "' trusted");
			//ApsSystemUtils.getLogger().info("Connection to host '" + host + "' trusted");
		}
		properties.setProperty("mail.imap.timeout", "5000");
		if (imapProtocol.equalsIgnoreCase("imaps")) {			
			properties.setProperty("mail.imap.ssl.protocols", "SSL");
			properties.setProperty("mail.imap.starttls.enable", "true");
		}
		
		if (config.getLocalhost() != null && config.getLocalhost().trim().length() > 0) {
			properties.put("mail.smtp.localhost", config.getLocalhost());
		}
		
		//SMTP START
		properties.put("mail.smtp.host", config.getSmtpHost());
		Integer smtpPort = config.getSmtpPort();
		if (smtpPort != null && smtpPort.intValue()>0) {
			properties.put("mail.smtp.port", smtpPort.toString());
		}
		
		int timeout = DEFAULT_SMTP_TIMEOUT;
		Integer timeoutParam = null;//config.getSmtpTimeout();
		if (null != timeoutParam && timeoutParam.intValue() != 0) {
			timeout = timeoutParam;
		}
		properties.put("mail.smtp.connectiontimeout", timeout);
		properties.put("mail.smtp.timeout", timeout);
		
		Session session = null;
		
		if (sentMail) {
			properties.put("mail.smtp.auth", "true");
			switch (config.getSmtpProtocol()) {
				case WebMailConfig.PROTO_SSL:
					properties.put("mail.smtp.socketFactory.port", smtpPort.toString());
					properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
					properties.put("mail.transport.protocol", "smtps");
					break;
				case WebMailConfig.PROTO_TLS:
					properties.put("mail.smtp.starttls.enable", "true");
					break;
				case WebMailConfig.PROTO_STD:
					//do nothing just use previous properties WITH the authenticator
					break;
			}
			Authenticator auth = new SMTPAuthenticator(username, password);
			session = Session.getInstance(properties, auth);
		} else {
			session = Session.getDefaultInstance(properties);
		}
		return session;
	}
	
	/**
	 * Effettua la chiusura controllata del transport.
	 * @param transport Il Transport da chiudere.
	 * @throws ApsSystemException In caso di errore in chiusura.
	 */
	protected void closeTransport(Transport transport) throws ApsSystemException {
		if (transport != null) {
			try {
				transport.close();
			} catch (MessagingException e) {
				_logger.error("Error closing connection", e);
				throw new ApsSystemException("Error closing connection", e);
			}
		}
	}
	
	@Override
	public void closeConnection(Store store) {
		try {
			if (null != store) store.close();
		} catch (Throwable t) {
			_logger.error("Error closing connection", t);
			ApsSystemUtils.logThrowable(t, this, "closeConnection", "Error closing connection");
		}
	}
	
	@Override
	public String getSentFolderName() {
		return this.getConfig().getSentFolderName();
	}
	
	@Override
	public String getTrashFolderName() {
		return this.getConfig().getTrashFolderName();
	}
	
	@Override
	public String getDomainName() {
		return this.getConfig().getDomainName();
	}
	
	protected WebMailConfig getConfig() {
		return _config;
	}
	protected void setConfig(WebMailConfig config) {
		this._config = config;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected CertificateHandler getCertificateHandler() {
		return _certificateHandler;
	}
	public void setCertificateHandler(CertificateHandler certificateHandler) {
		this._certificateHandler = certificateHandler;
	}
	
	private WebMailConfig _config;
	private CertificateHandler  _certificateHandler;
	
	private ConfigInterface _configManager;
	
	public static final int DEFAULT_SMTP_TIMEOUT = 5000;
	
}

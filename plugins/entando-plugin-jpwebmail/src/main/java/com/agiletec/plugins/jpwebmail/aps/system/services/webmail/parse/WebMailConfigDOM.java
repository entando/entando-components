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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail.parse;

import java.io.StringReader;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;
import org.jdom.output.Format;

/**
 * @author E.Santoboni
 */
public class WebMailConfigDOM {
	
	/**
	 * Extract the jpwebmail configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The jpwebmail configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public WebMailConfig extractConfig(String xml) throws ApsSystemException {
		WebMailConfig config = new WebMailConfig();
		try {
			Element root = this.getRootElement(xml);
			
			Element domainNameElem = root.getChild(DOMAIN_ELEM);
			if (null != domainNameElem) {
				String domainName = domainNameElem.getText();
				config.setDomainName(domainName);
			}
			
			Element localhostElem = root.getChild(LOCALHOST_ELEM);
			if (null != localhostElem) {
				String localhost = localhostElem.getText();
				config.setLocalhost(localhost);
			}
			
			String useEntandoUserPasswordElem = root.getChildText(ENTANDO_PASSWORD_ELEM);
			if (null != useEntandoUserPasswordElem) {
				config.setUseEntandoUserPassword(Boolean.parseBoolean(useEntandoUserPasswordElem));
			} else {
				config.setUseEntandoUserPassword(true);
			}
			
			Element smtpElem = root.getChild(SMTP_ELEM);
			String debug = smtpElem.getAttributeValue(SMTP_DEBUG_ATTR);
			config.setDebug(Boolean.parseBoolean(debug));
			
			/*
			String entandoUserAuth = smtpElem.getAttributeValue(SMTP_JAPS_USER_AUTH_ATTR);
			if (null == entandoUserAuth) {
				entandoUserAuth = smtpElem.getAttributeValue(SMTP_ENTANDO_USER_AUTH_ATTR);
			}
			config.setSmtpEntandoUserAuth(Boolean.parseBoolean(entandoUserAuth));
			*/
			String auth = smtpElem.getAttributeValue(SMTP_AUTH_ATTR);
			if (null != auth) {
				config.setSmtpAuth(Integer.parseInt(auth));
			} else {
				config.setSmtpAuth(WebMailConfig.SMTP_AUTH_ANONYMOUS);
			}
			
			config.setSmtpHost(smtpElem.getChildText(SMTP_HOST_CHILD));
			config.setSmtpUserName(smtpElem.getChildText(SMTP_USER_CHILD));
			config.setSmtpPassword(smtpElem.getChildText(SMTP_PASSWORD_CHILD));
			String smtpPort = smtpElem.getChildText(SMTP_PORT_CHILD);
			Integer smtpPortValue = (smtpPort==null || smtpPort.length()==0) ? null : new Integer(smtpPort);
			config.setSmtpPort(smtpPortValue);
			
			String proto = smtpElem.getChildText(SMTP_PROTOCOL_CHILD);
			if (null != proto) {
				if (proto.equalsIgnoreCase(PROTO_SSL)) {
					config.setSmtpProtocol(WebMailConfig.PROTO_SSL);
				} else if (proto.equalsIgnoreCase(PROTO_TLS)) {
					config.setSmtpProtocol(WebMailConfig.PROTO_TLS);
				} else {
					// any unknown protocol will disable encryption
					config.setSmtpProtocol(WebMailConfig.PROTO_STD);
				}
			}
			
			Element certElem = root.getChild(CERTIFICATES_ELEM);
			String enable = certElem.getChildText(CERTIFICATES_ENABLE_CHILD);
			config.setCertificateEnable(Boolean.parseBoolean(enable));
			String lazyCheck = 	certElem.getChildText(CERTIFICATES_LAZYCHECK_CHILD);
			config.setCertificateLazyCheck(Boolean.parseBoolean(lazyCheck));
			String certificatePath = certElem.getChildText(CERTIFICATES_CERTPATH_CHILD);
			config.setCertificatePath(certificatePath);
			String debugOnConsole = certElem.getChildText(CERTIFICATES_DEBUGONCONSOLE_CHILD);
			config.setCertificateDebugOnConsole(Boolean.parseBoolean(debugOnConsole));
			
			Element imapElem = root.getChild(IMAP_ELEM);
			config.setImapHost(imapElem.getChildText(IMAP_HOST_CHILD));
			String imapPort = imapElem.getChildText(IMAP_PORT_CHILD);
			Integer imapPortValue = (imapPort==null || imapPort.length()==0) ? null : new Integer(imapPort);
			config.setImapPort(imapPortValue);
			config.setImapProtocol(imapElem.getChildText(IMAP_PROTOCOL_CHILD));
			
			Element folderElem = root.getChild(FOLDER_ELEM);
			if (null != folderElem) {
				config.setTrashFolderName(folderElem.getChildText(FOLDER_TRASH_CHILD));
				config.setSentFolderName(folderElem.getChildText(FOLDER_SENT_CHILD));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "extractConfig");
			throw new ApsSystemException("Error extracting config", t);
		}
		return config;
	}
	
	/**
	 * Create an xml containing the jpwebmail configuration.
	 * @param config The jpwebmail configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(WebMailConfig config) throws ApsSystemException {
		Element root = new Element(ROOT);
		try {
			if (config.getLocalhost() != null && config.getLocalhost().trim().length() > 0) {
				Element localhostElem = new Element(LOCALHOST_ELEM);
				localhostElem.addContent(config.getLocalhost().trim());
				root.addContent(localhostElem);
			}
			if (config.getDomainName() != null && config.getDomainName().trim().length() > 0) {
				Element domainElem = new Element(DOMAIN_ELEM);
				domainElem.addContent(new CDATA(config.getDomainName()));
				root.addContent(domainElem);
			}
			Element entandoUserPasswordElem = new Element(ENTANDO_PASSWORD_ELEM);
			entandoUserPasswordElem.addContent(String.valueOf(config.isUseEntandoUserPassword()));
			root.addContent(entandoUserPasswordElem);
			
			Element certificatesElem = new Element(CERTIFICATES_ELEM);
			Element enableElem = new Element(CERTIFICATES_ENABLE_CHILD);
			enableElem.addContent(String.valueOf(config.isCertificateEnable()));
			certificatesElem.addContent(enableElem);
			Element lazyCheckElem = new Element(CERTIFICATES_LAZYCHECK_CHILD);
			lazyCheckElem.addContent(String.valueOf(config.isCertificateLazyCheck()));
			certificatesElem.addContent(lazyCheckElem);
			Element certPathElem = new Element(CERTIFICATES_CERTPATH_CHILD);
			certPathElem.addContent(new CDATA(config.getCertificatePath()));
			certificatesElem.addContent(certPathElem);
			Element debugOnConsoleElem = new Element(CERTIFICATES_DEBUGONCONSOLE_CHILD);
			debugOnConsoleElem.addContent(String.valueOf(config.isCertificateDebugOnConsole()));
			certificatesElem.addContent(debugOnConsoleElem);
			root.addContent(certificatesElem);
			
			Element imapElem = new Element(IMAP_ELEM);
			Element imapHostElem = new Element(IMAP_HOST_CHILD);
			imapHostElem.addContent(new CDATA(config.getImapHost()));
			imapElem.addContent(imapHostElem);
			Element imapProtocolElem = new Element(IMAP_PROTOCOL_CHILD);
			imapProtocolElem.addContent(config.getImapProtocol());
			imapElem.addContent(imapProtocolElem);
			Element imapPortElem = new Element(IMAP_PORT_CHILD);
			Integer imapPort = config.getImapPort();
			imapPortElem.addContent(imapPort==null ? "" : imapPort.toString());
			imapElem.addContent(imapPortElem);
			root.addContent(imapElem);
			
			Element smtpElem = new Element(SMTP_ELEM);
			smtpElem.setAttribute(SMTP_DEBUG_ATTR, String.valueOf(config.isDebug()));
			smtpElem.setAttribute(SMTP_AUTH_ATTR, String.valueOf(config.getSmtpAuth()));
			Element smtpHostElem = new Element(IMAP_HOST_CHILD);
			smtpHostElem.addContent(new CDATA(config.getSmtpHost()));
			smtpElem.addContent(smtpHostElem);
			Element smtpUserElem = new Element(SMTP_USER_CHILD);
			smtpUserElem.addContent(config.getSmtpUserName());
			smtpElem.addContent(smtpUserElem);
			Element smtpPasswordElem = new Element(SMTP_PASSWORD_CHILD);
			smtpPasswordElem.addContent(config.getSmtpPassword());
			smtpElem.addContent(smtpPasswordElem);
			Element smtpPortElem = new Element(SMTP_PORT_CHILD);
			Integer smtpPort = config.getSmtpPort();
			smtpPortElem.addContent(smtpPort==null ? "" : smtpPort.toString());
			smtpElem.addContent(smtpPortElem);
			if (null != config.getSmtpProtocol()) {
				Element protocolElem = new Element(SMTP_PROTOCOL_CHILD);
				if (config.getSmtpProtocol() == WebMailConfig.PROTO_SSL) {
					protocolElem.addContent(PROTO_SSL);
				} else if (config.getSmtpProtocol() == WebMailConfig.PROTO_TLS) {
					protocolElem.addContent(PROTO_TLS);
				} else {
					// any other (unsupported) protocol falls back to STD (no security transport layer)
					protocolElem.addContent(PROTO_STD);
				}
				smtpElem.addContent(protocolElem);
			}
			root.addContent(smtpElem);
			
			Element folderElem = new Element(FOLDER_ELEM);
			Element trashFolderElem = new Element(FOLDER_TRASH_CHILD);
			trashFolderElem.addContent(config.getTrashFolderName());
			folderElem.addContent(trashFolderElem);
			Element sentFolderElem = new Element(FOLDER_SENT_CHILD);
			sentFolderElem.addContent(config.getSentFolderName());
			folderElem.addContent(sentFolderElem);
			root.addContent(folderElem);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createConfigXml");
			throw new ApsSystemException("Error creating config", t);
		}
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
	
	/**
	 * Returns the Xml element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException In case of parsing exceptions.
	 */
	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("Error parsing xml: " + t.getMessage());
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}
	
	private static final String ROOT = "webmailConfig";
	
	private static final String CERTIFICATES_ELEM = "certificates";
	private static final String CERTIFICATES_ENABLE_CHILD = "enable";
	private static final String CERTIFICATES_LAZYCHECK_CHILD = "lazyCheck";
	private static final String CERTIFICATES_CERTPATH_CHILD = "certPath";
	private static final String CERTIFICATES_DEBUGONCONSOLE_CHILD = "debugOnConsole";
	
	private static final String LOCALHOST_ELEM = "localhost";
	private static final String DOMAIN_ELEM = "domain";
	private static final String ENTANDO_PASSWORD_ELEM = "entandoUserPassword";
	
	private static final String IMAP_ELEM = "imap";
	private static final String IMAP_HOST_CHILD = "host";
	private static final String IMAP_PROTOCOL_CHILD = "protocol";
	private static final String IMAP_PORT_CHILD = "port";
	
	private static final String SMTP_ELEM = "smtp";
	private static final String SMTP_DEBUG_ATTR = "debug";
	//@Deprecated
	//private static final String SMTP_JAPS_USER_AUTH_ATTR = "japsUserAuth";
	//private static final String SMTP_ENTANDO_USER_AUTH_ATTR = "entandoUserAuth";
	private static final String SMTP_AUTH_ATTR = "auth";
	private static final String SMTP_HOST_CHILD= "host";
	private static final String SMTP_USER_CHILD= "user";
	private static final String SMTP_PASSWORD_CHILD= "password";
	private static final String SMTP_PORT_CHILD= "port";
	private static final String SMTP_PROTOCOL_CHILD = "security";

	private static final String FOLDER_ELEM = "folder";
	private static final String FOLDER_TRASH_CHILD = "trash";
	private static final String FOLDER_SENT_CHILD = "sent";
	
	private static final String PROTO_SSL = "ssl";
	private static final String PROTO_TLS = "tls";
	private static final String PROTO_STD = "std";
	
}
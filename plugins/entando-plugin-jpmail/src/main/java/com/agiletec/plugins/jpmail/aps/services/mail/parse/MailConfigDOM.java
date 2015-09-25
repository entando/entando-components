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
package com.agiletec.plugins.jpmail.aps.services.mail.parse;

import java.io.StringReader;
import java.util.List;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;
import java.util.Iterator;
import org.jdom.output.Format;

/*
<mailConfig>
	<active>true</active>
	<senders>
		<sender code="CODE1">EMAIL1@EMAIL.COM</sender>
		<sender code="CODE2">EMAIL2@EMAIL.COM</sender>
	</senders>
	<smtp debug="false" >
		<host>SMTP.EMAIL.COM</host>
		<port>25</port>
		<timeout>10000</timeout>
		<user>USER</user>
		<password>PASSWORD</password>
		<security>STD|SSL|TLS</security>
	</smtp>
</mailConfig>
 */

/**
 * Class that provides read and update operations for the jpmail plugin xml configuration.
 * @author E.Santoboni, E.Mezzano
 */
public class MailConfigDOM {
	
	/**
	 * Extract the jpmail configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The jpmail configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public MailConfig extractConfig(String xml) throws ApsSystemException {
		MailConfig config = new MailConfig();
		Element root = this.getRootElement(xml);
		Element activeElem = root.getChild(ACTIVE_ELEM);
		if (activeElem != null) {
			String active = activeElem.getText();
			config.setActive(null != active && active.equalsIgnoreCase("true"));
		}
		this.extractSenders(root, config);
		this.extractSmtp(root, config);
		return config;
	}
	
	/**
	 * Create an xml containing the jpmail configuration.
	 * @param config The jpmail configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(MailConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
	
	/**
	 * Extract the senders from the xml element and save it into the MailConfig object.
	 * @param root The xml root element containing the senders configuration.
	 * @param config The configuration.
	 */
	private void extractSenders(Element root, MailConfig config) {
		Element sendersRootElem = root.getChild(SENDERS_ELEM);
		if (sendersRootElem!=null) {
			List sendersElem = sendersRootElem.getChildren(SENDER_CHILD);
			for (int i=0; i<sendersElem.size(); i++) {
				Element senderElem = (Element) sendersElem.get(i);
				String code = senderElem.getAttributeValue(SENDER_CODE_ATTR);
				String sender = senderElem.getText();
				config.addSender(code, sender);
			}
		}
	}
	
	/**
	 * Extract the smtp configuration from the xml element and save it into the MailConfig object.
	 * @param root The xml root element containing the smtp configuration.
	 * @param config The configuration.
	 */
	private void extractSmtp(Element root, MailConfig config) {
		Element smtpElem = root.getChild(SMTP_ELEM);
		if (smtpElem != null) {
			String debug = smtpElem.getAttributeValue(SMTP_DEBUG_ATTR);
			config.setDebug("true".equalsIgnoreCase(debug));
			config.setSmtpHost(smtpElem.getChildText(SMTP_HOST_CHILD));
			String port = smtpElem.getChildText(SMTP_PORT_CHILD);
			if (port != null && port.trim().length() > 0) {
				config.setSmtpPort(new Integer(port.trim()));
			}
			String timeout = smtpElem.getChildText(SMTP_TIMEOUT_CHILD);
			if (timeout != null && timeout.trim().length() > 0) {
				config.setSmtpTimeout(new Integer(timeout.trim()));
			}
			config.setSmtpUserName(smtpElem.getChildText(SMTP_USER_CHILD));
			config.setSmtpPassword(smtpElem.getChildText(SMTP_PASSWORD_CHILD));
			String proto = smtpElem.getChildText(SMTP_PROTOCOL_CHILD);
			if (null != proto) {
				if (proto.equalsIgnoreCase(PROTO_SSL)) {
					config.setSmtpProtocol(JpmailSystemConstants.PROTO_SSL);
				} else if (proto.equalsIgnoreCase(PROTO_TLS)) {
					config.setSmtpProtocol(JpmailSystemConstants.PROTO_TLS);
				} else {
					// any unknown protocol will disable encryption
					config.setSmtpProtocol(JpmailSystemConstants.PROTO_STD);
				}
			}
		}
	}
	
	/**
	 * Extract the smtp configuration from the xml element and save it into the MailConfig object.
	 * @param root The xml root element containing the smtp configuration.
	 * @param config The configuration.
	 */
	private Element createConfigElement(MailConfig config) {
		Element configElem = new Element(ROOT);
		Element activeElement = new Element(ACTIVE_ELEM);
		activeElement.setText(String.valueOf(config.isActive()));
		configElem.addContent(activeElement);
		Element sendersElem = this.createSendersElement(config);
		configElem.addContent(sendersElem);
		Element smtpElem = this.createSmtpElement(config);
		configElem.addContent(smtpElem);
		return configElem;
	}
	
	/**
	 * Create the senders element starting from the given MailConfig.
	 * @param config The configuration.
	 */
	private Element createSendersElement(MailConfig config) {
		Element sendersElem = new Element(SENDERS_ELEM);
		if (null != config.getSenders()) {
			Iterator<String> codeIter = config.getSenders().keySet().iterator();
			while (codeIter.hasNext()) {
				String key = codeIter.next();
				Element senderElement = new Element(SENDER_CHILD);
				senderElement.setAttribute(SENDER_CODE_ATTR, key);
				senderElement.addContent(config.getSenders().get(key));
				sendersElem.addContent(senderElement);
			}
		}
		return sendersElem;
	}
	
	/**
	 * Create the smtp element starting from the given MailConfig.
	 * @param config The configuration.
	 */
	private Element createSmtpElement(MailConfig config) {
		Element smtpElem = new Element(SMTP_ELEM);
		smtpElem.setAttribute(SMTP_DEBUG_ATTR, String.valueOf(config.isDebug()));
		
		Element hostElem = new Element(SMTP_HOST_CHILD);
		hostElem.addContent(config.getSmtpHost());
		smtpElem.addContent(hostElem);
		
		Integer port = config.getSmtpPort();
		if (null != port && port.intValue() != 0) {
			Element portElem = new Element(SMTP_PORT_CHILD);
			portElem.addContent(config.getSmtpPort().toString());
			smtpElem.addContent(portElem);
		}
		
		Integer timeout = config.getSmtpTimeout();
		if (null != timeout && timeout.intValue() != 0) {
			Element timeoutElem = new Element(SMTP_TIMEOUT_CHILD);
			timeoutElem.addContent(config.getSmtpTimeout().toString());
			smtpElem.addContent(timeoutElem);
		}
		
		Element userElem = new Element(SMTP_USER_CHILD);
		userElem.addContent(config.getSmtpUserName());
		smtpElem.addContent(userElem);
		
		Element passwordElem = new Element(SMTP_PASSWORD_CHILD);
		passwordElem.addContent(config.getSmtpPassword());
		smtpElem.addContent(passwordElem);
		
		if (null != config.getSmtpProtocol()) {
			Element protocolElem = new Element(SMTP_PROTOCOL_CHILD);
			if (config.getSmtpProtocol() == JpmailSystemConstants.PROTO_SSL) {
				protocolElem.addContent(PROTO_SSL);
			} else if (config.getSmtpProtocol() == JpmailSystemConstants.PROTO_TLS) {
				protocolElem.addContent(PROTO_TLS);
			} else {
				// any other (unsupported) protocol falls back to STD (no security transport layer)
				protocolElem.addContent(PROTO_STD);
			}
			smtpElem.addContent(protocolElem);
		}
		
		return smtpElem;
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
	
	private final String ROOT = "mailConfig";
	
	private final String SENDERS_ELEM = "senders";
	private final String SENDER_CHILD = "sender";
	private final String SENDER_CODE_ATTR = "code";
	
	private final String ACTIVE_ELEM = "active";
	
	private final String SMTP_ELEM = "smtp";
	private final String SMTP_DEBUG_ATTR = "debug";
	private final String SMTP_HOST_CHILD = "host";
	private final String SMTP_PORT_CHILD = "port";
	private final String SMTP_TIMEOUT_CHILD = "timeout";
	private final String SMTP_USER_CHILD = "user";
	private final String SMTP_PASSWORD_CHILD = "password";
	private final String SMTP_PROTOCOL_CHILD = "security";
	
	private final String PROTO_SSL = "ssl";
	private final String PROTO_TLS = "tls";
	private final String PROTO_STD = "std";
	
	
}
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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.parse;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageModel;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageTypeNotifierConfig;

public class MessageNotifierConfigDOM {
	
	/**
	 * Extract the configuration elements for each message type.
	 * @param xml The xml containing the configuration.
	 * @return The configuration elements for each message type.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public Map<String, MessageTypeNotifierConfig> extractConfig(String xml) throws ApsSystemException {
		Map<String, MessageTypeNotifierConfig> config = new HashMap<String, MessageTypeNotifierConfig>();
		Element root = this.getRootElement(xml);
		List<Element> messageTypeElements = root.getChildren(MESSAGETYPE_ELEM);
		for (Element messageTypeElem : messageTypeElements) {
			MessageTypeNotifierConfig messageTypeConfig = this.extractMessageTypeNotifierConfig(messageTypeElem);
			config.put(messageTypeConfig.getTypeCode(), messageTypeConfig);
		}
		return config;
	}
	
	/**
	 * Create an xml containing the notifier configuration.
	 * @param config The jpmail configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(Map<String, MessageTypeNotifierConfig> config) throws ApsSystemException {
		Element root = new Element(ROOT);
		for (MessageTypeNotifierConfig messageTypeConfig : config.values()) {
			Element configElement = this.createConfigElement(messageTypeConfig);
			root.addContent(configElement);
		}
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}
	
	protected MessageTypeNotifierConfig extractMessageTypeNotifierConfig(Element messageTypeElem) {
		MessageTypeNotifierConfig messageTypeConfig = new MessageTypeNotifierConfig();
		
		String typeCode = messageTypeElem.getAttributeValue(MESSAGETYPE_TYPECODE_ATTR);
		messageTypeConfig.setTypeCode(typeCode);
		
		String store = messageTypeElem.getAttributeValue(MESSAGETYPE_STORE_ATTR);
		messageTypeConfig.setStore(store == null || "true".equalsIgnoreCase(store));
		
		String mailAttrName = messageTypeElem.getAttributeValue(MESSAGETYPE_MAILATTRNAME_ATTR);
		messageTypeConfig.setMailAttrName(mailAttrName);
		
		String notifiable = messageTypeElem.getAttributeValue(MESSAGETYPE_NOTIFIABLE_ATTR);
		messageTypeConfig.setNotifiable(notifiable == null || "true".equalsIgnoreCase(notifiable));
		
		String senderCode = messageTypeElem.getAttributeValue(MESSAGETYPE_SENDERCODE_ATTR);
		messageTypeConfig.setSenderCode(senderCode);
		
		Element recipentsElem = messageTypeElem.getChild(RECIPIENTS_ELEM);
		messageTypeConfig.setRecipientsTo(this.extractRecipients(recipentsElem, RECIPIENTS_TO_CHILD));
		messageTypeConfig.setRecipientsCc(this.extractRecipients(recipentsElem, RECIPIENTS_CC_CHILD));
		messageTypeConfig.setRecipientsBcc(this.extractRecipients(recipentsElem, RECIPIENTS_BCC_CHILD));
		
		Element modelElem = messageTypeElem.getChild(MODEL_ELEM);
		if (null != modelElem) {
			MessageModel messageModel = this.extractMessageModel(modelElem);
			messageTypeConfig.setMessageModel(messageModel);
		}
		return messageTypeConfig;
	}
	
	protected String[] extractRecipients(Element recipentsElem, String type) {
		List<Element> recipentsElements = recipentsElem.getChildren(type);
		String[] recipents = new String[recipentsElements.size()];
		int index = 0;
		for (Element recipentElem : recipentsElements) {
			recipents[index++] = recipentElem.getText();
		}
		return recipents;
	}
	
	protected MessageModel extractMessageModel(Element langModelElem) {
		MessageModel messageModel = new MessageModel();
		messageModel.setBodyModel(langModelElem.getChildText(MODEL_BODY_CHILD));
		messageModel.setSubjectModel(langModelElem.getChildText(MODEL_SUBJECT_CHILD));
		return messageModel;
	}
	
	/**
	 * Extract the smtp configuration from the xml element and save it into the MailConfig object.
	 * @param root The xml root element containing the smtp configuration.
	 * @param config The configuration.
	 */
	protected Element createConfigElement(MessageTypeNotifierConfig config) {
		Element configElem = new Element(MESSAGETYPE_ELEM);
		configElem.setAttribute(MESSAGETYPE_TYPECODE_ATTR, config.getTypeCode());
		configElem.setAttribute(MESSAGETYPE_STORE_ATTR, String.valueOf(config.isStore()));
		if (config.getMailAttrName()!=null) {
			configElem.setAttribute(MESSAGETYPE_MAILATTRNAME_ATTR, config.getMailAttrName());
		}
		
		configElem.setAttribute(MESSAGETYPE_NOTIFIABLE_ATTR, String.valueOf(config.isNotifiable()));
		if (config.getSenderCode()!=null) {
			configElem.setAttribute(MESSAGETYPE_SENDERCODE_ATTR, config.getSenderCode());
		}
		
		Element recipientsElem = this.createRecipientsElement(config);
		configElem.addContent(recipientsElem);
		
		MessageModel model = config.getMessageModel();
		if (null != model) {
			Element modelElem = this.createModelElement(model);
			configElem.addContent(modelElem);
		}
		
		return configElem;
	}
	
	/**
	 * Create the senders element starting from the given MailConfig.
	 * @param config The configuration.
	 */
	protected Element createRecipientsElement(MessageTypeNotifierConfig config) {
		Element recipientsElem = new Element(RECIPIENTS_ELEM);
		this.addRecipients(config.getRecipientsTo(), RECIPIENTS_TO_CHILD, recipientsElem);
		this.addRecipients(config.getRecipientsCc(), RECIPIENTS_CC_CHILD, recipientsElem);
		this.addRecipients(config.getRecipientsBcc(), RECIPIENTS_BCC_CHILD, recipientsElem);
		return recipientsElem;
	}
	
	protected void addRecipients(String[] recipients, String elementName, Element recipientsElem) {
		if (recipients!=null) {
			for (String address : recipients) {
				Element addressElem = new Element(elementName);
				addressElem.addContent(new CDATA(address));
				recipientsElem.addContent(addressElem);
			}
		}
	}
	
	/**
	 * Create the senders element starting from the given MailConfig.
	 * @param config The configuration.
	 */
	protected Element createModelElement(MessageModel model) {
		Element modelElem = new Element(MODEL_ELEM);
		
		Element subjectElem = new Element(MODEL_SUBJECT_CHILD);
		subjectElem.addContent(new CDATA(model.getSubjectModel()));
		modelElem.addContent(subjectElem);
		
		Element bodyElem = new Element(MODEL_BODY_CHILD);
		bodyElem.addContent(new CDATA(model.getBodyModel()));
		modelElem.addContent(bodyElem);
		
		return modelElem;
	}
	
	/**
	 * Returns the Xml element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException In case of parsing exceptions.
	 */
	protected Element getRootElement(String xmlText) throws ApsSystemException {
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
	
	private final String ROOT = "messagetypes";
	
	private final String MESSAGETYPE_ELEM = "messagetype";
	
	private final String MESSAGETYPE_NOTIFIABLE_ATTR = "notifiable";
	private final String MESSAGETYPE_TYPECODE_ATTR = "typeCode";
	private final String MESSAGETYPE_SENDERCODE_ATTR = "senderCode";
	private final String MESSAGETYPE_MAILATTRNAME_ATTR = "mailAttrName";
	private final String MESSAGETYPE_STORE_ATTR = "store";
	
	private final String RECIPIENTS_ELEM = "recipients";
	private final String RECIPIENTS_TO_CHILD = "to";
	private final String RECIPIENTS_CC_CHILD = "cc";
	private final String RECIPIENTS_BCC_CHILD = "bcc";
	
	private final String MODEL_ELEM = "model";
	private final String MODEL_BODY_CHILD = "body";
	private final String MODEL_SUBJECT_CHILD = "subject";
	
}
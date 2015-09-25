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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.parse;

import java.io.StringReader;
import java.util.Date;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.NotifierConfig;

/**
 * @author E.Mezzano
 */
public class WorkflowNotifierDOM {
	
	public NotifierConfig extractConfig(String xml) throws ApsSystemException {
		NotifierConfig notifierConfig = new NotifierConfig();
		Element root = this.getRootElement(xml);
		Element schedulerElement = root.getChild(SCHEDULER_CHILD);
		this.extractSchedulerConfig(notifierConfig, schedulerElement);
		Element mailElement = root.getChild(MAIL_CHILD);
		this.extractMailConfig(notifierConfig, mailElement);
		return notifierConfig;
	}
	
	public String createConfigXml(NotifierConfig notifierConfig) throws ApsSystemException {
		Element root = new Element(ROOT);
		Element schedulerElement = this.prepareSchedulerElement(notifierConfig);
		root.addContent(schedulerElement);
		Element mailElement = this.prepareMailElement(notifierConfig);
		root.addContent(mailElement);
		
		XMLOutputter out = new XMLOutputter();
		String xml = out.outputString(new Document(root));
		return xml;
	}
	
	protected void extractSchedulerConfig(NotifierConfig notifierConfig, Element schedulerElement) {
		Element activeElem = schedulerElement.getChild(SCHEDULER_ACTIVE_CHILD);
		if (activeElem != null) {
			notifierConfig.setActive("true".equalsIgnoreCase(activeElem.getAttributeValue(SCHEDULER_VALUE_ATTR)));
		}
		Element delayElem = schedulerElement.getChild(SCHEDULER_DELAY_CHILD);
		int hoursDelay = 1;
		if (delayElem != null) {
			hoursDelay = Integer.parseInt(delayElem.getAttributeValue(SCHEDULER_VALUE_ATTR));
		}
		notifierConfig.setHoursDelay(hoursDelay);
		Element startElem = schedulerElement.getChild(SCHEDULER_START_CHILD);
		if (startElem != null) {
			String value = startElem.getAttributeValue(SCHEDULER_VALUE_ATTR);
			Date extractedStart = DateConverter.parseDate(value, DATE_SCHEDULER_CONFIG_PATTERN);
			notifierConfig.setStartScheduler(extractedStart);
		}
	}
	
	protected void extractMailConfig(NotifierConfig notifierConfig, Element mailElement) {
		notifierConfig.setSenderCode(mailElement.getAttributeValue(MAIL_SENDERCODE_ATTR));
		//notifierConfig.setMailAttrName(mailElement.getAttributeValue(MAIL_MAILATTRNAME_ATTR));
		String html = mailElement.getAttributeValue(MAIL_HTML_ATTR);
		notifierConfig.setHtml(html!=null && "true".equalsIgnoreCase(html));
		notifierConfig.setSubject(mailElement.getChild(MAIL_SUBJECT_CHILD).getText());
		notifierConfig.setHeader(mailElement.getChild(MAIL_HEADER_CHILD).getText());
		notifierConfig.setTemplate(mailElement.getChild(MAIL_TEMPLATE_CHILD).getText());
		notifierConfig.setFooter(mailElement.getChild(MAIL_FOOTER_CHILD).getText());
	}
	
	protected Element prepareSchedulerElement(NotifierConfig notifierConfig) {
		Element schedulerElement = new Element(SCHEDULER_CHILD);
		
		Element activeElement = new Element(SCHEDULER_ACTIVE_CHILD);
		activeElement.setAttribute(SCHEDULER_VALUE_ATTR, String.valueOf(notifierConfig.isActive()));
		schedulerElement.addContent(activeElement);
		
		Element delayElement = new Element(SCHEDULER_DELAY_CHILD);
		delayElement.setAttribute(SCHEDULER_VALUE_ATTR, String.valueOf(notifierConfig.getHoursDelay()));
		schedulerElement.addContent(delayElement);
		
		Element startElement = new Element(SCHEDULER_START_CHILD);
		startElement.setAttribute(SCHEDULER_VALUE_ATTR, DateConverter.getFormattedDate(
				notifierConfig.getStartScheduler(), DATE_SCHEDULER_CONFIG_PATTERN));
		schedulerElement.addContent(startElement);
		
		return schedulerElement;
	}
	
	protected Element prepareMailElement(NotifierConfig notifierConfig) {
		Element mailElement = new Element(MAIL_CHILD);
		mailElement.setAttribute(MAIL_SENDERCODE_ATTR, notifierConfig.getSenderCode());
		//mailElement.setAttribute(MAIL_MAILATTRNAME_ATTR, notifierConfig.getMailAttrName());
		mailElement.setAttribute(MAIL_HTML_ATTR, String.valueOf(notifierConfig.isHtml()));
		
		Element subjectElement = new Element(MAIL_SUBJECT_CHILD);
		subjectElement.addContent(new CDATA(notifierConfig.getSubject()));
		mailElement.addContent(subjectElement);
		
		Element headerElement = new Element(MAIL_HEADER_CHILD);
		headerElement.addContent(new CDATA(notifierConfig.getHeader()));
		mailElement.addContent(headerElement);
		
		Element templateElement = new Element(MAIL_TEMPLATE_CHILD);
		templateElement.addContent(new CDATA(notifierConfig.getTemplate()));
		mailElement.addContent(templateElement);
		
		Element footerElement = new Element(MAIL_FOOTER_CHILD);
		footerElement.addContent(new CDATA(notifierConfig.getFooter()));
		mailElement.addContent(footerElement);
		
		return mailElement;
	}
	
	protected Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("Errore nel parsing: " + t.getMessage());
			throw new ApsSystemException("Errore nel parsing della configurazione Dimensioni di resize", t);
		}
		return root;
	}
	
	protected Document decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			return builder.build(reader);
		} catch (Throwable t) {
			throw new ApsSystemException("Errore nel parsing dell'xml", t);
		}
	}
	
	private static final String ROOT = "notifierConfig";
	
	private static final String SCHEDULER_CHILD = "scheduler";
	private static final String SCHEDULER_ACTIVE_CHILD = "active";
	private static final String SCHEDULER_DELAY_CHILD = "delay";
	private static final String SCHEDULER_START_CHILD = "start";
	private static final String SCHEDULER_VALUE_ATTR = "value";
	
	private static final String MAIL_CHILD = "mail";
	private static final String MAIL_SENDERCODE_ATTR = "senderCode";
	//private static final String MAIL_MAILATTRNAME_ATTR = "mailAttributeName";
	private static final String MAIL_HTML_ATTR = "html";
	private static final String MAIL_SUBJECT_CHILD = "subject";
	private static final String MAIL_HEADER_CHILD = "header";
	private static final String MAIL_TEMPLATE_CHILD = "template";
	private static final String MAIL_FOOTER_CHILD = "footer";
	
	private final String DATE_SCHEDULER_CONFIG_PATTERN = "dd/MM/yyyy HH:mm";
	
}
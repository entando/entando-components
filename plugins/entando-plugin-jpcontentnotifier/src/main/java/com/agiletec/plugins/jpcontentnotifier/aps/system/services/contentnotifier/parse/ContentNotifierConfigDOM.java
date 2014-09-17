/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.parse;

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



import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.NotifierConfig;

/*
<notifierConfig>
	<scheduler active="true" onlyOwner="true" delayHours="24" start="13/01/2010 17:39" />
	<mail senderCode="CODE1" mailAttrName="eMail" html="false" >
		<subject><![CDATA[Oggetto della mail di notifica]]></subject>
		<header><![CDATA[Inizio Mail (testata)<br/>]]></header>
		<templateInsert><![CDATA[
<br />
INSERIMENTO NUOVO CONTENUTO
<br />
Contenuto tipo {type} - {descr}
<br />
Data Operazione {date} {time}
<br />
{link}
<br />
		]]></templateInsert>
		<templateUpdate><![CDATA[
<br />
AGGIORNAMENTO CONTENUTO
<br />
Contenuto tipo {type} - {descr}
<br />
Data Operazione {date} {time}
<br />
<a href="{link}">Link</a>
<br />
		]]></templateUpdate>

		<!-- Non obbligatorio -->
		<templateRemove><![CDATA[
<br />
Rimozione Contenuto tipo {type} - {descr} <br />
Data Operazione {date} {time}
<br />
		]]></templateRemove>
		<footer><![CDATA[<br />Fine Mail (footer)]]></footer>
	</mail>
</notifierConfig>
 */
/**
 *
 * @author E.Santoboni
 *
 *
 */
public class ContentNotifierConfigDOM {

	/**
	 * Extract the newsletter configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The newsletter configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public NotifierConfig extractConfig(String xml) throws ApsSystemException {
		NotifierConfig config = new NotifierConfig();
		Element root = this.getRootElement(xml);
		this.extractScheduler(root, config);
		this.extractMailConfig(root, config);
		return config;
	}

	/**
	 * Create an xml containing the newsletter configuration.
	 * @param config The newsletter configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(NotifierConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		String xml = new XMLOutputter().outputString(doc);
		return xml;
	}

	private void extractScheduler(Element root, NotifierConfig config) {
		Element schedulerElement = root.getChild(SCHEDULER_ELEM);

		String active = schedulerElement.getAttributeValue(SCHEDULER_ACTIVE_ATTR);
		config.setActive(active == null || active.equalsIgnoreCase("true"));

		String onlyOwner = schedulerElement.getAttributeValue(SCHEDULER_ONLYOWNER_ATTR);
		config.setOnlyOwner(onlyOwner != null && onlyOwner.equalsIgnoreCase("true"));

		String delayHours = schedulerElement.getAttributeValue(SCHEDULER_DELAYHOURS_ATTR);
		int hours = 1;
		if (delayHours != null && delayHours.length()>0) {
			hours = Integer.parseInt(delayHours);
		}
		config.setHoursDelay(hours);

		String start = schedulerElement.getAttributeValue(SCHEDULER_START_ATTR);
		if (start!=null && start.length()>0) {
			config.setStartScheduler(DateConverter.parseDate(start, SCHEDULER_STARTDATE_CONFIG_PATTERN));
		} else {
			config.setStartScheduler(new Date());
		}
	}

	/**
	 * Estrae la parte di configurazione relativa alla mail.
	 * @param root L'elemento radice contenente il sottoelemento relativo alle mail.
	 * @param config La configurazione del servizio newsletter.
	 */
	private void extractMailConfig(Element root, NotifierConfig config) {
		Element mailElem = root.getChild(MAIL_ELEM);

		String senderCode = mailElem.getAttributeValue(MAIL_SENDERCODE_ATTR);
		config.setSenderCode(senderCode);

		String mailAttrName = mailElem.getAttributeValue(MAIL_MAILATTRNAME_ATTR);
		config.setMailAttrName(mailAttrName);

		String html = mailElem.getAttributeValue(MAIL_HTML_ATTR);
		config.setHtml(html != null && "true".equalsIgnoreCase(html));

		config.setSubject(mailElem.getChildText(MAIL_SUBJECT_CHILD));

		config.setHeader(mailElem.getChildText(MAIL_HEADER_CHILD));
		config.setFooter(mailElem.getChildText(MAIL_FOOTER_CHILD));

		config.setTemplateInsert(mailElem.getChild(MAIL_TEMPLATE_INSERT_CHILD).getText());
		config.setTemplateUpdate(mailElem.getChild(MAIL_TEMPLATE_UPDATE_CHILD).getText());
		Element templateRemoveElem = mailElem.getChild(MAIL_TEMPLATE_REMOVE_CHILD);
		if (templateRemoveElem!=null && templateRemoveElem.getText().length()>0) {
			config.setTemplateRemove(templateRemoveElem.getText());
		}
	}

	/**
	 * Crea l'elemento della configurazione del servizio di newsletter.
	 * @param config La configurazione del servizio newsletter.
	 * @return L'elemento della configurazione del servizio di newsletter.
	 */
	private Element createConfigElement(NotifierConfig config) {
		Element configElem = new Element(ROOT);

		Element schedulerElem = this.createSchedulerElement(config);
		configElem.addContent(schedulerElem);

		Element mailElem = this.createMailElement(config);
		configElem.addContent(mailElem);

		return configElem;
	}

	private Element createSchedulerElement(NotifierConfig config) {
		Element schedulerElement = new Element(SCHEDULER_ELEM);
		schedulerElement.setAttribute(SCHEDULER_ACTIVE_ATTR, String.valueOf(config.isActive()));
		schedulerElement.setAttribute(SCHEDULER_ONLYOWNER_ATTR, String.valueOf(config.isOnlyOwner()));
		schedulerElement.setAttribute(SCHEDULER_DELAYHOURS_ATTR, String.valueOf(config.getHoursDelay()));
		schedulerElement.setAttribute(SCHEDULER_START_ATTR, DateConverter.getFormattedDate(config.getStartScheduler(), SCHEDULER_STARTDATE_CONFIG_PATTERN));
		return schedulerElement;
	}

	/**
	 * Crea l'elemento della configurazione relativa alle mail.
	 * @param config La configurazione del servizio newsletter.
	 * @return L'elemento di configurazione relativo alle mail.
	 */
	private Element createMailElement(NotifierConfig config) {
		Element mailElem = new Element(MAIL_ELEM);

		mailElem.setAttribute(MAIL_SENDERCODE_ATTR, config.getSenderCode());
		mailElem.setAttribute(MAIL_MAILATTRNAME_ATTR, config.getMailAttrName());
		mailElem.setAttribute(MAIL_HTML_ATTR, String.valueOf(config.isHtml()));

		Element subject = new Element(MAIL_SUBJECT_CHILD);
		subject.addContent(new CDATA(config.getSubject()));
		mailElem.addContent(subject);

		Element htmlHeader = new Element(MAIL_HEADER_CHILD);
		htmlHeader.addContent(new CDATA(config.getHeader()));
		mailElem.addContent(htmlHeader);

		Element htmlFooter = new Element(MAIL_FOOTER_CHILD);
		htmlFooter.addContent(new CDATA(config.getFooter()));
		mailElem.addContent(htmlFooter);

		Element templateInsert = new Element(MAIL_TEMPLATE_INSERT_CHILD);
		templateInsert.addContent(new CDATA(config.getTemplateInsert()));
		mailElem.addContent(templateInsert);

		Element templateUpdate = new Element(MAIL_TEMPLATE_UPDATE_CHILD);
		templateUpdate.addContent(new CDATA(config.getTemplateUpdate()));
		mailElem.addContent(templateUpdate);

		if (config.isNotifyRemove()) {
			Element templateRemove = new Element(MAIL_TEMPLATE_REMOVE_CHILD);
			templateRemove.addContent(new CDATA(config.getTemplateRemove()));
			mailElem.addContent(templateRemove);
		}

		return mailElem;
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

	private static final String ROOT = "notifierConfig";

	private static final String SCHEDULER_ELEM = "scheduler";
	private static final String SCHEDULER_ACTIVE_ATTR = "active";
	private static final String SCHEDULER_ONLYOWNER_ATTR = "onlyOwner";
	private static final String SCHEDULER_DELAYHOURS_ATTR = "delayHours";
	private static final String SCHEDULER_START_ATTR = "start";

	private static final String MAIL_ELEM = "mail";
	private static final String MAIL_SENDERCODE_ATTR = "senderCode";
	private static final String MAIL_MAILATTRNAME_ATTR = "mailAttrName";
	private static final String MAIL_HTML_ATTR = "html";

	private static final String MAIL_SUBJECT_CHILD = "subject";
	private static final String MAIL_HEADER_CHILD = "header";
	private static final String MAIL_FOOTER_CHILD = "footer";
	private static final String MAIL_TEMPLATE_INSERT_CHILD = "templateInsert";
	private static final String MAIL_TEMPLATE_UPDATE_CHILD = "templateUpdate";
	private static final String MAIL_TEMPLATE_REMOVE_CHILD = "templateRemove";

	private static final String SCHEDULER_STARTDATE_CONFIG_PATTERN = "dd/MM/yyyy HH:mm";

}
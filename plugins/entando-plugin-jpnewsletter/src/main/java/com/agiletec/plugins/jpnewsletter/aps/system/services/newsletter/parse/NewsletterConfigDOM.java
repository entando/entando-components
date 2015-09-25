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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.parse;

import java.io.StringReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentType;



/*

<newsletterConfig>
	<scheduler active="true" onlyOwner="false" delayHours="24" start="01/03/2007 11:08" />
	<subscriptions allAttributeName="newsletter_allContents">
		<descr>
			mappa delle corrispondeze tra attributo buleano 
			'sottoscrizione categoria newsletter' di profilo utente 
			e categoria di contenuto/tematismo-newsletter
		</descr>
		<subscription categoryCode="food" attributeName="newsletter_food" />
		<subscription categoryCode="travel" attributeName="newsletter_travel" />
		......
	</subscriptions>

	<contentTypes>
		<contentType code="NWL" defaultModel="1" htmlModel="2" />
		<contentType code="FOO" defaultModel="21" htmlModel="22" />
		.......
	</contentTypes>

	<mail senderCode="CODE1" mailAttrName="email" alsoHtml="true" unsubscriptionPage="newsletter_unsubscribe" >
		<subject><![CDATA[Oggetto della mail]]></subject>
		<htmlHeader><![CDATA[
<br />Header html della mail<br />
		]]></htmlHeader>
		<htmlFooter><![CDATA[
<br />Footer html della mail<br />
		]]></htmlFooter>
		<htmlSeparator><![CDATA[
Separatore html tra contenuti
		]]></htmlSeparator>
		<textHeader><![CDATA[
Header text della mail
		]]></textHeader>
		<textFooter><![CDATA[
Footer text della mail
		]]></textFooter>
		<textSeparator><![CDATA[
Separatore text tra contenuti
		]]></textSeparator>
		<subscriberHtmlFooter><![CDATA[Clicca sul link per cancellare la sottoscrizione <a href="{unsubscribeLink}" >CONFERMA</a></body></html>]]></htmlFooter>
		<subscriberTextFooter><![CDATA[Clicca sul link per cancellare la sottoscrizione <a href="{unsubscribeLink}" >CONFERMA</a>]]></textFooter>
		<subscription pageCode="newsletter_terminatereg" tokenValidityDays="90" >
			<subject><![CDATA[Conferma la sottoscrizione al servizio di Newsletter]]></subject>
			<htmlBody><![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head></head>
<body><p>Clicca sul link per confermare <a href="{subscribeLink}" >***CONFERMA***</a></p> </body></html>]]></htmlBody>
			<textBody><![CDATA[Clicca sul link per confermare <a href="{subscribeLink}" >***CONFERMA***</a>]]></textBody>
		</subscription>
	</mail>
</newsletterConfig>
 */

/**
 * @author E.Santoboni, E.Mezzano
 */
public class NewsletterConfigDOM {
	
	/**
	 * Extract the newsletter configuration from an xml.
	 * @param xml The xml containing the configuration.
	 * @return The newsletter configuration.
	 * @throws ApsSystemException In case of parsing errors.
	 */
	public NewsletterConfig extractConfig(String xml) throws ApsSystemException {
		NewsletterConfig config = new NewsletterConfig();
		Element root = this.getRootElement(xml);
		this.extractScheduler(root, config);
		this.extractSubscriptions(root, config);
		this.extractContentTypes(root, config);
		this.extractMailConfig(root, config);
		return config;
	}
	
	/**
	 * Create an xml containing the newsletter configuration.
	 * @param config The newsletter configuration.
	 * @return The xml containing the configuration.
	 * @throws ApsSystemException In case of errors.
	 */
	public String createConfigXml(NewsletterConfig config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		String xml = out.outputString(doc);
		return xml;
	}
	
	private void extractScheduler(Element root, NewsletterConfig config) {
		Element schedulerElement = root.getChild(SCHEDULER_ELEM);
		String active = schedulerElement.getAttributeValue(SCHEDULER_ACTIVE_ATTR);
		config.setActive(active != null && active.equalsIgnoreCase("true"));
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
	
	private void extractSubscriptions(Element root, NewsletterConfig config) {
		Element subscriptionsElem = root.getChild(SUBSCRIPTIONS_ELEM);
		String allContentsAttribute = subscriptionsElem.getAttributeValue(SUBSCRIPTIONS_ALL_ATTR);
		allContentsAttribute = (allContentsAttribute != null && allContentsAttribute.trim().length() > 0) ? allContentsAttribute.trim() : null;
		config.setAllContentsAttributeName(allContentsAttribute);
		List<Element> subscriptionElems = subscriptionsElem.getChildren(SUBSCRIPTION_CHILD);
		if (null != subscriptionElems) {
			for (int i=0; i<subscriptionElems.size(); i++) {
				Element subscriptionElem = (Element) subscriptionElems.get(i);
				String categoryCode = subscriptionElem.getAttributeValue(SUBSCRIPTION_CATEGORYCODE_ATTR);
				String attributeName = subscriptionElem.getAttributeValue(SUBSCRIPTION_ATTRIBUTENAME_ATTR);
				config.addSubscription(categoryCode, attributeName);
			}
		}
	}
	
	private void extractContentTypes(Element root, NewsletterConfig config) {
		Element contentTypesElem = root.getChild(CONTENTTYPES_ELEM);
		if (null != contentTypesElem) {
			List<Element> contentTypeElements = contentTypesElem.getChildren(CONTENTTYPE_CHILD);
			if (null != contentTypeElements) {
				for (int i=0; i<contentTypeElements.size(); i++) {
					Element contentTypeElem = contentTypeElements.get(i);
					config.addContentType(this.extractContentType(contentTypeElem));
				}
			}
		}
	}
	
	private void extractMailConfig(Element root, NewsletterConfig config) {
		Element mailElem = root.getChild(MAIL_ELEM);
		String alsoHtml = mailElem.getAttributeValue(MAIL_ALSOHTML_ATTR);
		config.setAlsoHtml(alsoHtml != null && "true".equalsIgnoreCase(alsoHtml));
		String senderCode = mailElem.getAttributeValue(MAIL_SENDERCODE_ATTR);
		config.setSenderCode(senderCode);
		String unsubscriptionPage = mailElem.getAttributeValue(MAIL_UNSUBSCRIPTIONPAGE_ATTR);
		config.setUnsubscriptionPageCode(unsubscriptionPage);
		config.setSubject(mailElem.getChildText(MAIL_SUBJECT_CHILD));
		config.setHtmlHeader(mailElem.getChildText(MAIL_HTML_HEADER_CHILD));
		config.setHtmlFooter(mailElem.getChildText(MAIL_HTML_FOOTER_CHILD));
		config.setHtmlSeparator(mailElem.getChildText(MAIL_HTML_SEPARATOR_CHILD));
		config.setTextHeader(mailElem.getChildText(MAIL_TEXT_HEADER_CHILD));
		config.setTextFooter(mailElem.getChildText(MAIL_TEXT_FOOTER_CHILD));
		config.setTextSeparator(mailElem.getChildText(MAIL_TEXT_SEPARATOR_CHILD));
		config.setSubscribersHtmlFooter(mailElem.getChildText(MAIL_SUBSCRIBER_HTML_FOOTER_CHILD));
		config.setSubscribersTextFooter(mailElem.getChildText(MAIL_SUBSCRIBER_TEXT_FOOTER_CHILD));
		Element subscriptionElem = mailElem.getChild(MAIL_SUBSCRIPTION_CHILD);
		if (subscriptionElem!=null) {
			String pageCode = subscriptionElem.getAttributeValue(MAIL_SUBSCRIPTION_PAGECODE_ATTR);
			config.setSubscriptionPageCode(pageCode);
			String tokenValidity = subscriptionElem.getAttributeValue(MAIL_SUBSCRIPTION_TOKENVALIDITY_ATTR);
			config.setSubscriptionTokenValidityDays(Integer.parseInt(tokenValidity));
			config.setSubscriptionSubject(subscriptionElem.getChildText(MAIL_SUBSCRIPTION_SUBJECT_CHILD));
			config.setSubscriptionHtmlBody(subscriptionElem.getChildText(MAIL_SUBSCRIPTION_HTML_CHILD));
			config.setSubscriptionTextBody(subscriptionElem.getChildText(MAIL_SUBSCRIPTION_TEXT_CHILD));
		}
	}
	
	protected NewsletterContentType extractContentType(Element contentTypeElem) {
		NewsletterContentType contentType = new NewsletterContentType();
		String contentTypeCode = contentTypeElem.getAttributeValue(CONTENTTYPE_CODE_ATTR);
		contentType.setContentTypeCode(contentTypeCode);
		String defaultModel = contentTypeElem.getAttributeValue(CONTENTTYPE_DEFMODEL_ATTR);
		contentType.setSimpleTextModel(Integer.parseInt(defaultModel));
		String htmlModel = contentTypeElem.getAttributeValue(CONTENTTYPE_HTMLMODEL_ATTR);
		if (htmlModel!=null && htmlModel.length()>0) {
			contentType.setHtmlModel(Integer.parseInt(htmlModel));
		}
		return contentType;
	}
	
	private Element createConfigElement(NewsletterConfig config) {
		Element configElem = new Element(ROOT);
		Element shcedulerElem = this.createSchedulerElement(config);
		configElem.addContent(shcedulerElem);
		Element subscriptionsElem = this.createSubscriptionsElement(config);
		configElem.addContent(subscriptionsElem);
		Element contentTypesElem = this.createContentTypesElement(config);
		configElem.addContent(contentTypesElem);
		Element mailElem = this.createMailElement(config);
		configElem.addContent(mailElem);
		return configElem;
	}
	
	private Element createSubscriptionsElement(NewsletterConfig config) {
		Element subscriptionsElem = new Element(SUBSCRIPTIONS_ELEM);
		String allContentsAttribute = config.getAllContentsAttributeName();
		allContentsAttribute = allContentsAttribute != null ? allContentsAttribute.trim() : "";
		subscriptionsElem.setAttribute(SUBSCRIPTIONS_ALL_ATTR, allContentsAttribute);
		Iterator subscriptionsIter = config.getSubscriptions().entrySet().iterator();
		while (subscriptionsIter.hasNext()) {
			Entry subscriptionEntry = (Entry) subscriptionsIter.next();
			Element subscriptionChild = new Element(SUBSCRIPTION_CHILD);
			String categoryCode = (String) subscriptionEntry.getKey();
			subscriptionChild.setAttribute(SUBSCRIPTION_CATEGORYCODE_ATTR, categoryCode);
			String attributeName = (String) subscriptionEntry.getValue();
			subscriptionChild.setAttribute(SUBSCRIPTION_ATTRIBUTENAME_ATTR, attributeName);
			subscriptionsElem.addContent(subscriptionChild);
		}
		return subscriptionsElem;
	}
	
	private Element createSchedulerElement(NewsletterConfig config) {
		Element schedulerElement = new Element(SCHEDULER_ELEM);
		schedulerElement.setAttribute(SCHEDULER_ACTIVE_ATTR, String.valueOf(config.isActive()));
		schedulerElement.setAttribute(SCHEDULER_ONLYOWNER_ATTR, String.valueOf(config.isOnlyOwner()));
		schedulerElement.setAttribute(SCHEDULER_DELAYHOURS_ATTR, String.valueOf(config.getHoursDelay()));
		schedulerElement.setAttribute(SCHEDULER_START_ATTR, DateConverter.getFormattedDate(config.getStartScheduler(), SCHEDULER_STARTDATE_CONFIG_PATTERN));
		return schedulerElement;
	}
	
	protected Element createContentTypesElement(NewsletterConfig config) {
		Element contentTypesElem = new Element(CONTENTTYPES_ELEM);
		Iterator<NewsletterContentType> contentTypesIter = config.getContentTypes().values().iterator();
		while (contentTypesIter.hasNext()) {
			NewsletterContentType contentType = contentTypesIter.next();
			Element contentTypeChild = new Element(CONTENTTYPE_CHILD);
			contentTypeChild.setAttribute(CONTENTTYPE_CODE_ATTR, contentType.getContentTypeCode());
			String textModel = String.valueOf(contentType.getSimpleTextModel());
			contentTypeChild.setAttribute(CONTENTTYPE_DEFMODEL_ATTR, textModel);
			String htmlModel = String.valueOf(contentType.getHtmlModel());
			contentTypeChild.setAttribute(CONTENTTYPE_HTMLMODEL_ATTR, htmlModel);
			contentTypesElem.addContent(contentTypeChild);
		}
		return contentTypesElem;
	}
	
	private Element createMailElement(NewsletterConfig config) {
		Element mailElem = new Element(MAIL_ELEM);
		mailElem.setAttribute(MAIL_ALSOHTML_ATTR, String.valueOf(config.isAlsoHtml()));
		mailElem.setAttribute(MAIL_SENDERCODE_ATTR, config.getSenderCode());
		mailElem.setAttribute(MAIL_UNSUBSCRIPTIONPAGE_ATTR, config.getUnsubscriptionPageCode());
		Element subject = new Element(MAIL_SUBJECT_CHILD);
		subject.addContent(new CDATA(config.getSubject()));
		mailElem.addContent(subject);
		Element htmlHeader = new Element(MAIL_HTML_HEADER_CHILD);
		htmlHeader.addContent(new CDATA(config.getHtmlHeader()));
		mailElem.addContent(htmlHeader);
		Element htmlFooter = new Element(MAIL_HTML_FOOTER_CHILD);
		htmlFooter.addContent(new CDATA(config.getHtmlFooter()));
		mailElem.addContent(htmlFooter);
		Element htmlSeparator = new Element(MAIL_HTML_SEPARATOR_CHILD);
		htmlSeparator.addContent(new CDATA(config.getHtmlSeparator()));
		mailElem.addContent(htmlSeparator);
		Element textHeader = new Element(MAIL_TEXT_HEADER_CHILD);
		textHeader.addContent(new CDATA(config.getTextHeader()));
		mailElem.addContent(textHeader);
		Element textFooter = new Element(MAIL_TEXT_FOOTER_CHILD);
		textFooter.addContent(new CDATA(config.getTextFooter()));
		mailElem.addContent(textFooter);
		Element textSeparator = new Element(MAIL_TEXT_SEPARATOR_CHILD);
		textSeparator.addContent(new CDATA(config.getTextSeparator()));
		mailElem.addContent(textSeparator);
		Element subscribersHtmlFooter = new Element(MAIL_SUBSCRIBER_HTML_FOOTER_CHILD);
		subscribersHtmlFooter.addContent(new CDATA(config.getSubscribersHtmlFooter()));
		mailElem.addContent(subscribersHtmlFooter);
		Element subscribersTextFooter = new Element(MAIL_SUBSCRIBER_TEXT_FOOTER_CHILD);
		subscribersTextFooter.addContent(new CDATA(config.getSubscribersTextFooter()));
		mailElem.addContent(subscribersTextFooter);
		Element subscriptionElem = new Element(MAIL_SUBSCRIPTION_CHILD);
		subscriptionElem.setAttribute(MAIL_SUBSCRIPTION_PAGECODE_ATTR, config.getSubscriptionPageCode());
		subscriptionElem.setAttribute(MAIL_SUBSCRIPTION_TOKENVALIDITY_ATTR, String.valueOf(config.getSubscriptionTokenValidityDays()));
		Element subscriptionSubject = new Element(MAIL_SUBSCRIPTION_SUBJECT_CHILD);
		subscriptionSubject.addContent(new CDATA(config.getSubscriptionSubject()));
		subscriptionElem.addContent(subscriptionSubject); 
		Element subscriptionTextBody = new Element(MAIL_SUBSCRIPTION_HTML_CHILD);
		subscriptionTextBody.addContent(new CDATA(config.getSubscriptionHtmlBody()));
		subscriptionElem.addContent(subscriptionTextBody); 
		Element subscriptionHtmlBody = new Element(MAIL_SUBSCRIPTION_TEXT_CHILD);
		subscriptionHtmlBody.addContent(new CDATA(config.getSubscriptionTextBody()));
		subscriptionElem.addContent(subscriptionHtmlBody); 
		mailElem.addContent(subscriptionElem);
		return mailElem;
	}
	
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
	
	private static final String ROOT = "newsletterConfig";
	private static final String SCHEDULER_ELEM = "scheduler";
	private static final String SCHEDULER_ACTIVE_ATTR = "active";
	private static final String SCHEDULER_ONLYOWNER_ATTR = "onlyOwner";
	private static final String SCHEDULER_DELAYHOURS_ATTR = "delayHours";
	private static final String SCHEDULER_START_ATTR = "start";
	
	private static final String SUBSCRIPTIONS_ELEM = "subscriptions";
	private static final String SUBSCRIPTIONS_ALL_ATTR = "allAttributeName";
	private static final String SUBSCRIPTION_CHILD = "subscription";
	private static final String SUBSCRIPTION_CATEGORYCODE_ATTR = "categoryCode";
	private static final String SUBSCRIPTION_ATTRIBUTENAME_ATTR = "attributeName";
	
	protected static final String CONTENTTYPES_ELEM = "contentTypes";
	protected static final String CONTENTTYPE_CHILD = "contentType";
	protected static final String CONTENTTYPE_CODE_ATTR = "code";
	protected static final String CONTENTTYPE_DEFMODEL_ATTR = "defaultModel";
	protected static final String CONTENTTYPE_HTMLMODEL_ATTR = "htmlModel";
	
	private static final String MAIL_ELEM = "mail";
	private static final String MAIL_ALSOHTML_ATTR = "alsoHtml";
	private static final String MAIL_SENDERCODE_ATTR = "senderCode";
	private static final String MAIL_UNSUBSCRIPTIONPAGE_ATTR = "unsubscriptionPage";
	private static final String MAIL_SUBJECT_CHILD = "subject";
	private static final String MAIL_HTML_HEADER_CHILD = "htmlHeader";
	private static final String MAIL_HTML_FOOTER_CHILD = "htmlFooter";
	private static final String MAIL_HTML_SEPARATOR_CHILD = "htmlSeparator";
	private static final String MAIL_TEXT_HEADER_CHILD = "textHeader";
	private static final String MAIL_TEXT_FOOTER_CHILD = "textFooter";
	private static final String MAIL_TEXT_SEPARATOR_CHILD = "textSeparator";
	private static final String MAIL_SUBSCRIBER_HTML_FOOTER_CHILD = "subscriberHtmlFooter";
	private static final String MAIL_SUBSCRIBER_TEXT_FOOTER_CHILD = "subscriberTextFooter";

	private static final String MAIL_SUBSCRIPTION_CHILD = "subscription";
	private static final String MAIL_SUBSCRIPTION_PAGECODE_ATTR = "pageCode";
	private static final String MAIL_SUBSCRIPTION_TOKENVALIDITY_ATTR = "tokenValidityDays";
	private static final String MAIL_SUBSCRIPTION_SUBJECT_CHILD = "subject";
	private static final String MAIL_SUBSCRIPTION_HTML_CHILD = "htmlBody";
	private static final String MAIL_SUBSCRIPTION_TEXT_CHILD = "textBody";
	
	private static final String SCHEDULER_STARTDATE_CONFIG_PATTERN = "dd/MM/yyyy HH:mm";
	
}
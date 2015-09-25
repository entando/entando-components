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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author E.Santoboni
 */
public class NewsletterConfig {

	@Override
	public NewsletterConfig clone() {
		NewsletterConfig config = new NewsletterConfig();
		config.setActive(this.isActive());
		config.setAllContentsAttributeName(this.getAllContentsAttributeName());
		config.setAlsoHtml(this.isAlsoHtml());
		config.setHoursDelay(this.getHoursDelay());
		config.setHtmlFooter(this.getHtmlFooter());
		config.setHtmlHeader(this.getHtmlHeader());
		config.setHtmlSeparator(this.getHtmlSeparator());
		config.setOnlyOwner(this.isOnlyOwner());
		config.setSenderCode(this.getSenderCode());
		config.setStartScheduler(this.getStartScheduler());
		config.setUnsubscriptionPageCode(this.getUnsubscriptionPageCode());
		config.setSubject(this.getSubject());
		config.setTextFooter(this.getTextFooter());
		config.setTextHeader(this.getTextHeader());
		config.setTextSeparator(this.getTextSeparator());
		Map<String, NewsletterContentType> contentTypes = new HashMap<String, NewsletterContentType>();
		contentTypes.putAll(this.getContentTypes());
		config.setContentTypes(contentTypes);
		Properties subscriptions = new Properties();
		subscriptions.putAll(this.getSubscriptions());
		config.setSubscription(subscriptions);
		config.setSubscribersHtmlFooter(this.getSubscribersHtmlFooter());
		config.setSubscribersTextFooter(this.getSubscribersTextFooter());
		config.setSubscriptionPageCode(this.getSubscriptionPageCode());
		config.setSubscriptionTokenValidityDays(this.getSubscriptionTokenValidityDays());
		config.setSubscriptionSubject(this.getSubscriptionSubject());
		config.setSubscriptionTextBody(this.getSubscriptionTextBody());
		config.setSubscriptionHtmlBody(this.getSubscriptionHtmlBody());
		return config;
	}

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		this._active = active;
	}

	public int getHoursDelay() {
		return _hoursDelay;
	}

	public void setHoursDelay(int hoursDelay) {
		this._hoursDelay = hoursDelay;
	}

	public boolean isOnlyOwner() {
		return _onlyOwner;
	}

	public void setOnlyOwner(boolean onlyOwner) {
		this._onlyOwner = onlyOwner;
	}

	public Date getStartScheduler() {
		return _startScheduler;
	}

	public void setStartScheduler(Date startScheduler) {
		this._startScheduler = startScheduler;
	}

	public String[] getContentTypesArray() {
		String[] contentTypesArray = new String[this._contentTypes.size()];
		int i = 0;
		for (String code : this._contentTypes.keySet()) {
			contentTypesArray[i] = code.toString();
			i++;
		}
		return contentTypesArray;
	}

	protected void setContentTypes(Map<String, NewsletterContentType> contentTypes) {
		this._contentTypes.putAll(contentTypes);
	}

	public Map<String, NewsletterContentType> getContentTypes() {
		return this._contentTypes;
	}

	public NewsletterContentType getContentType(String contentTypeCode) {
		return (NewsletterContentType) this._contentTypes.get(contentTypeCode);
	}

	public void addContentType(NewsletterContentType contentType) {
		this._contentTypes.put(contentType.getContentTypeCode(), contentType);
	}

	public String[] getCategoriesArray() {
		Set<Object> categories = this._subscriptions.keySet();
		String[] categoriesArray = new String[categories.size()];
		Iterator<Object> iter = categories.iterator();
		int i = 0;
		while (iter.hasNext()) {
			Object object = (Object) iter.next();
			categoriesArray[i] = object.toString();
			i++;
		}
		return categoriesArray;
	}

	protected void setSubscription(Properties subscriptions) {
		this._subscriptions.putAll(subscriptions);
	}

	public Properties getSubscriptions() {
		return _subscriptions;
	}

	public void addSubscription(String categoryCode, String attributeName) {
		this._subscriptions.setProperty(categoryCode, attributeName);
	}

	public String getSenderCode() {
		return _senderCode;
	}

	public void setSenderCode(String senderCode) {
		this._senderCode = senderCode;
	}

	public boolean isAlsoHtml() {
		return _alsoHtml;
	}

	public void setAlsoHtml(boolean alsoHtml) {
		this._alsoHtml = alsoHtml;
	}

	public String getSubject() {
		return _subject;
	}

	public void setSubject(String subject) {
		this._subject = subject;
	}

	public String getHtmlHeader() {
		return _htmlHeader;
	}

	public void setHtmlHeader(String htmlHeader) {
		this._htmlHeader = htmlHeader;
	}

	public String getHtmlFooter() {
		return _htmlFooter;
	}

	public void setHtmlFooter(String htmlFooter) {
		this._htmlFooter = htmlFooter;
	}

	public String getHtmlSeparator() {
		return _htmlSeparator;
	}

	public void setHtmlSeparator(String htmlSeparator) {
		this._htmlSeparator = htmlSeparator;
	}

	public String getTextHeader() {
		return _textHeader;
	}

	public void setTextHeader(String textHeader) {
		this._textHeader = textHeader;
	}

	public String getTextFooter() {
		return _textFooter;
	}

	public void setTextFooter(String textFooter) {
		this._textFooter = textFooter;
	}

	public String getTextSeparator() {
		return _textSeparator;
	}

	public void setTextSeparator(String textSeparator) {
		this._textSeparator = textSeparator;
	}

	public String getSubscribersTextFooter() {
		return _subscribersTextFooter;
	}

	public void setSubscribersTextFooter(String subscribersTextFooter) {
		this._subscribersTextFooter = subscribersTextFooter;
	}

	public String getSubscribersHtmlFooter() {
		return _subscribersHtmlFooter;
	}

	public void setSubscribersHtmlFooter(String subscribersHtmlFooter) {
		this._subscribersHtmlFooter = subscribersHtmlFooter;
	}

	public String getSubscriptionSubject() {
		return _subscriptionSubject;
	}

	public void setSubscriptionSubject(String subscriptionSubject) {
		this._subscriptionSubject = subscriptionSubject;
	}

	public String getSubscriptionTextBody() {
		return _subscriptionTextBody;
	}

	public void setSubscriptionTextBody(String subscriptionTextBody) {
		this._subscriptionTextBody = subscriptionTextBody;
	}

	public String getSubscriptionHtmlBody() {
		return _subscriptionHtmlBody;
	}

	public void setSubscriptionHtmlBody(String subscriptionHtmlBody) {
		this._subscriptionHtmlBody = subscriptionHtmlBody;
	}

	public String getSubscriptionPageCode() {
		return _subscriptionPageCode;
	}

	public void setSubscriptionPageCode(String subscriptionPageCode) {
		this._subscriptionPageCode = subscriptionPageCode;
	}

	public int getSubscriptionTokenValidityDays() {
		return _subscriptionTokenValidityDays;
	}

	public void setSubscriptionTokenValidityDays(int subscriptionTokenValidityDays) {
		this._subscriptionTokenValidityDays = subscriptionTokenValidityDays;
	}

	public String getUnsubscriptionPageCode() {
		return _unsubscriptionPageCode;
	}

	public void setUnsubscriptionPageCode(String unsubscriptionPageCode) {
		this._unsubscriptionPageCode = unsubscriptionPageCode;
	}

	public String getAllContentsAttributeName() {
		return _allContentsAttributeName;
	}

	public void setAllContentsAttributeName(String allContentsAttributeName) {
		this._allContentsAttributeName = allContentsAttributeName;
	}

	public Date getNextTaskTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.getStartScheduler());
		if (Calendar.getInstance().after(calendar)) {
			do {
				calendar.add(Calendar.HOUR, this.getHoursDelay());
			} while (Calendar.getInstance().after(calendar));
			return calendar.getTime();
		} else {
			return this.getStartScheduler();
		}
	}

	// scheduler
	private boolean _active = false;

	private Date _startScheduler = new Date();

	private int _hoursDelay = 1;

	private boolean _onlyOwner = false;

	private Map<String, NewsletterContentType> _contentTypes = new HashMap<String, NewsletterContentType>();

	private Properties _subscriptions = new Properties();

	// mail config
	private String _senderCode;

	private boolean _alsoHtml;

	// template
	private String _subject;

	private String _htmlHeader = "";

	private String _htmlFooter = "";

	private String _htmlSeparator = "";

	private String _textHeader = "";

	private String _textFooter = "";

	private String _textSeparator = "";

	private String _subscribersTextFooter = "";

	private String _subscribersHtmlFooter = "";

	// Subscription confirm mail
	private String _subscriptionSubject;

	private String _subscriptionTextBody;

	private String _subscriptionHtmlBody;

	private String _subscriptionPageCode;

	private int _subscriptionTokenValidityDays;

	// Unsubscription page
	private String _unsubscriptionPageCode;

	// Variabili ricavate dale precedenti
	private String _allContentsAttributeName;

}

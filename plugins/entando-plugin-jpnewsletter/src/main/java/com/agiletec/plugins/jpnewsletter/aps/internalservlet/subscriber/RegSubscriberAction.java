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
package com.agiletec.plugins.jpnewsletter.aps.internalservlet.subscriber;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

public class RegSubscriberAction extends BaseAction {
	
	public String addSubscription() {
		try {
			String mailAddress = this.getMailAddress();
			INewsletterManager newsletterManager = this.getNewsletterManager();
			Subscriber subscriber = newsletterManager.loadSubscriber(mailAddress);
			if (subscriber == null) {
				newsletterManager.addSubscriber(mailAddress);
			} else if (!subscriber.isActive()) {
				newsletterManager.resetSubscriber(mailAddress);
			} else {
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addSubscription", "Errore durante l'aggiunta di una sottoscrizione");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String activateSubscription() {
		try {
			String token = this.getToken();
			INewsletterManager newsletterManager = this.getNewsletterManager();
			String mailAddress = newsletterManager.getAddressFromToken(token);
			if (mailAddress!=null && mailAddress.equals(this.getMailAddress())) {
				newsletterManager.activateSubscriber(mailAddress, token);
			} else {
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "activateSubscription", "Errore durante l'aggiunta di una sottoscrizione");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public Boolean isDuplicated(String mailAddress) throws Throwable {
		Boolean isDuplicated = true;
		try {
			Subscriber duplicated = this.getNewsletterManager().loadSubscriber(mailAddress);
			if (duplicated == null || !duplicated.isActive()) {
				isDuplicated = false;
			}
		} catch (Throwable t) {
			throw t;
		}
		return isDuplicated;
	}
	
	public Boolean isAlreadyAnUser(String mailAddress) throws Throwable {
		Boolean isAlreadyAnUser = true;
		try {
			isAlreadyAnUser = this.getNewsletterManager().isAlreadyAnUser(mailAddress);
			if (isAlreadyAnUser == false) {
				isAlreadyAnUser = false;
				return isAlreadyAnUser;
			}
		} catch (Throwable t) {
			throw t;
		}
		return isAlreadyAnUser;
	}
	
	public String trashSubscription() {
		return SUCCESS;
	}
	
	public String deleteSubscription() {
		try {
			INewsletterManager newsletterManager = this.getNewsletterManager();
			Subscriber subscriber = newsletterManager.loadSubscriber(this.getMailAddress());
			if (null != subscriber) {
				newsletterManager.deleteSubscriber(this.getMailAddress());
			} else {
				String[] args = {this.getMailAddress()};
				this.addActionError(this.getText("jpnewsletter.messsage.emailNotPresent", args));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteSubscriber");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String getMailAddress() {
		return _mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		_mailAddress = mailAddress;
	}
	
	public String getToken() {
		return _token;
	}
	public void setToken(String token) {
		this._token = token;
	}
	
	protected INewsletterManager getNewsletterManager() {
		return _newsletterManager;
	}
	public void setNewsletterManager(INewsletterManager newsletterManager) {
		this._newsletterManager = newsletterManager;
	}
	
	private String _mailAddress;
	private String _token;
	private INewsletterManager _newsletterManager;
	
}
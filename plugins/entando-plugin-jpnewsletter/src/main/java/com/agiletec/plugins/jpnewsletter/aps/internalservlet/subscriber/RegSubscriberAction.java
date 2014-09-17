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
*/
package com.agiletec.plugins.jpnewsletter.aps.internalservlet.subscriber;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

public class RegSubscriberAction extends BaseAction implements IRegSubscriberAction {
	
	@Override
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
	
	@Override
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
	
	@Override
	public String trashSubscription() {
		return SUCCESS;
	}
	
	@Override
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
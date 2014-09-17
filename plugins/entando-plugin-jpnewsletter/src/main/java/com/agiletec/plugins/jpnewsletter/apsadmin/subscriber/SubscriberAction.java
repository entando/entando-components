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
package com.agiletec.plugins.jpnewsletter.apsadmin.subscriber;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;

/**
 * Classe per la gestione dei sottoscritti al servizio generico di newsletter in
 * Back-End.
 * 
 * @author A.Turrini
 */
public class SubscriberAction extends BaseAction implements ISubscriberAction {
	
	@Override
	public String trashSubscriber() {
		return SUCCESS;
	}
	
	@Override
	public String deleteSubscriber() {
		try {
			this.getNewsletterManager().deleteSubscriber(this.getMailAddress());
			return SUCCESS;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteSubscriber");
			return FAILURE;
		}

	}
	
	public String getMailAddress() {
		return _mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		_mailAddress = mailAddress;
	}
	
	protected INewsletterManager getNewsletterManager() {
		return _newsletterManager;
	}
	public void setNewsletterManager(INewsletterManager newsletterManager) {
		_newsletterManager = newsletterManager;
	}
	
	private String _mailAddress;
	
	private INewsletterManager _newsletterManager;
	
}

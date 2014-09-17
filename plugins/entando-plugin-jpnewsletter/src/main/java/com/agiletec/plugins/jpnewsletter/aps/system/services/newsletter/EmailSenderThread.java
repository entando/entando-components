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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter;

import com.agiletec.aps.system.ApsSystemUtils;

public class EmailSenderThread extends Thread {
	
	public EmailSenderThread(String mailAddress, String token, NewsletterManager newsletterManager) {
		this._mailAddress = mailAddress;
		this._token = token;
		this._newsletterManager  = newsletterManager;
	}
	
	@Override
	public void run() {
		try {
			this._newsletterManager.sendSubscriptionFormThread(_mailAddress, _token);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "run");
		}
	}
	
	private NewsletterManager _newsletterManager;
	private String _mailAddress;
	private String _token;
	
}
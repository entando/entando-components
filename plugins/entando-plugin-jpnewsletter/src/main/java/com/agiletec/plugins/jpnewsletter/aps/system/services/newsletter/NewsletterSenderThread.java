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

/**
 * @author E.Santoboni
 */
public class NewsletterSenderThread extends Thread {
	
	public NewsletterSenderThread(NewsletterManager sender) {
		this._sender = sender;
	}
	
	@Override
	public void run() {
		try {
			this._sender.sendNewsletterFromThread();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "run");
		}
	}
	
	private NewsletterManager _sender;
	
}
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
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.scheduler;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.IContentNotifierManager;

public class MailSenderTask extends Task {
	
	public MailSenderTask(IContentNotifierManager notifierManager) {
		this._notifierManager = notifierManager;
	}
	
	public void execute() {
		try {
			this._notifierManager.sendEMails();
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "execute");
		}
	}
	
	private IContentNotifierManager _notifierManager;
	
}
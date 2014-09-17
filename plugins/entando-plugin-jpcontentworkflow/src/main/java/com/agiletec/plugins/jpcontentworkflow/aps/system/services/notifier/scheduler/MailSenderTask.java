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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.IWorkflowNotifierManager;

/**
 * @author E.Santoboni
 */
public class MailSenderTask extends Task {

	private static final Logger _logger = LoggerFactory.getLogger(MailSenderTask.class);
	
	public MailSenderTask(IWorkflowNotifierManager notifierManager) {
		this._notifierManager = notifierManager;
	}
	
	@Override
	public void execute() {
		try {
			this._notifierManager.sendMails();
		} catch (ApsSystemException e) {
			_logger.error("error in execute", e);
		}
	}
	
	private IWorkflowNotifierManager _notifierManager;
	
}
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
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier;

import java.util.Date;

import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.NotifierConfig;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.scheduler.MailSenderTask;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.scheduler.Scheduler;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.scheduler.Task;

public class MockContentNotifierManager extends ContentNotifierManager {
	
	protected void openScheduler() {
		NotifierConfig schedulerConfig = this.getConfig();
		if (schedulerConfig.isActive()) {
			long milliSecondsDelay = schedulerConfig.getHoursDelay() * 60 * 60 * 1000;
			Task task = new MailSenderTask(this);
			_mailSenderScheduler = new Scheduler(task, new Date(), milliSecondsDelay);
		}
	}
	
}
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

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * Classe Scheduler
 * @author M.Casari - E.Santoboni
 */
public class Scheduler extends TimerTask {
	
	public Scheduler(INewsletterManager newsletterManager, Date start, long horsDelay) {
		this._newsletterManager = newsletterManager;
		this._timer = new Timer();
		this._timer.schedule(this, start, horsDelay*60*60*1000);
	}
	
	@Override
	public boolean cancel() {
		this._timer.cancel();
		return super.cancel();
	}
	
	@Override
	public void run() {
		try {
			this._newsletterManager.sendNewsletter();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "run");
			throw new RuntimeException("Error on executing TimerTask", t);
		}
	}
	
	private Timer _timer;
	private INewsletterManager _newsletterManager;
	
}
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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.agiletec.aps.system.ApsSystemUtils;

/**
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
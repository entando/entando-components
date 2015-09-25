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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.scheduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.DateConverter;

/**
 * Classe Scheduler
 * @author M.Casari - E.Santoboni
 */
public class Scheduler extends TimerTask {
	
	/**
	 * Costruttore dello scheduler
	 * @param task Task da eseguire.
	 * @param start Data di partenza dello scheduler
	 * @param delay Intervallo di schedulazione in millisecondi
	 */
	public Scheduler(Task task, Date start, long delay) {
		this._timer = new Timer();
		this._task = task;
		this._timer.schedule(this, start, delay);
		ApsSystemUtils.getLogger().trace("jpcontentworkflow: Scheduler - StartTime: " + DateConverter.getFormattedDate(start, "dd/MM/yyyy HH:mm:ss") + " - Delay: " + delay);
	}
	
	@Override
	public boolean cancel() {
		this._task = null;
		this._timer.cancel();
		this._timer.purge();
		return super.cancel();
	}
	
	@Override
	public void run() {
		if (null != _task) _task.execute();
	}
	
	private Task _task;
	private Timer _timer;
}
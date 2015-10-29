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
package org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator;

import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class maintains the source updated.
 * Checks the last time that the channel was updated against the channel's delay.
 */
public class RssTimerTask extends TimerTask {
	
	private static final Logger _logger = LoggerFactory.getLogger(RssTimerTask.class);
	
	public RssTimerTask(ApsAggregatorItem item, IAggregatorManager aggregatorManager) {
		this.setItem(item);
		this.setAggregatorManager(aggregatorManager);
	}
	
	@Override
	public void run() {
		try {
			if (this.isToUpdate(this.getItem())) {
				this.getAggregatorManager().updateSource(this.getItem());
			}
		} catch (Throwable t) {
			_logger.error("error in run", t);
			throw new RuntimeException("error in run", t);
		}
	}
	
	private boolean isToUpdate(ApsAggregatorItem item) {
		long lastUpdate = item.getLastUpdate().getTime();
		long now = new Date().getTime();
		long delay = new Long(item.getDelay()).longValue();
		long updateTime = lastUpdate + (delay * 1000);
		if (updateTime <= now ) return true;
		return false;
	}
	
	public void setItem(ApsAggregatorItem item) {
		this._item = item;
	}
	public ApsAggregatorItem getItem() {
		return _item;
	}
	
	public void setAggregatorManager(IAggregatorManager aggregatorManager) {
		this._aggregatorManager = aggregatorManager;
	}
	public IAggregatorManager getAggregatorManager() {
		return _aggregatorManager;
	}
	
	private IAggregatorManager _aggregatorManager;
	private ApsAggregatorItem _item;
	
}
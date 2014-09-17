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
package com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator;

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
			throw new RuntimeException(t);
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

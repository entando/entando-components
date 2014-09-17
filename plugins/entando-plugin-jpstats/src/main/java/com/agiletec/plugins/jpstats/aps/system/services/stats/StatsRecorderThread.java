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
package com.agiletec.plugins.jpstats.aps.system.services.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author E.Santoboni
 */
public class StatsRecorderThread extends Thread {

	private static final Logger _logger = LoggerFactory.getLogger(StatsRecorderThread.class);
	
	public StatsRecorderThread(StatsManager statsManager, StatsRecord statsRecord) {
		this.statsManager = statsManager;
		this.statsRecord = statsRecord;
	}
	
	public void run() {
		try {
			this.statsManager.addStatsRecordFromThread(this.statsRecord);
		} catch (Throwable t) {
			_logger.error("error in run", t);
		}
	}
	
	private StatsManager statsManager;
	private StatsRecord statsRecord;
	
}

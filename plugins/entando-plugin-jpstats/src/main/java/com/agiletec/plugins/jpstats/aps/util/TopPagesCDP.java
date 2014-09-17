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
package com.agiletec.plugins.jpstats.aps.util;

import java.util.HashMap;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpstats.aps.system.services.stats.IStatsManager;

/**
 * Class that provides the data to render the TopPages Chart 
 */
public class TopPagesCDP extends AbstractCategoryDataProducer {
	
	/**
	 * @param manager The StatsManager
	 * @param bean IStatsDataBean object that holds the date interval to render
	 */
	public TopPagesCDP(IStatsManager manager, IStatsDataBean bean) {
		super(manager, bean);
	}
	
	protected Map getResultset() throws ApsSystemException {
		Map<String, Integer> result = new HashMap<String, Integer>();
		IStatsDataBean bean = super.getDataBean();
		result = super.getStatsManager().getTopPages(bean.getStart(), bean.getEnd());
		return result;
	}
	
}
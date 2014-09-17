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

import java.util.Date;
import java.util.Map;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpstats.aps.system.services.stats.IStatsManager;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * Class that provides the data to render the Hits Chart 
 */
public class HitsTDP implements DatasetProducer {

	/**
	 * @param manager The StatsManager
	 * @param bean IStatsDataBean object that holds the date interval to render
	 */
	public HitsTDP(IStatsManager statsManager, IStatsDataBean dataBean) {
		this._statsManager = statsManager;
		this._dataBean = dataBean;
	}

	public Object produceDataset(Map params) throws DatasetProduceException {
		TimeSeries ts = null;
		try {
			ts = getTimeseries();
		} catch (Throwable e) {
			throw new DatasetProduceException("An error occurred generating the TimeSeries Object " + this.getClass().getName());
		}

		return new TimeSeriesCollection(ts);
	}

	public String getProducerId() {
		return "TimeDataProducer";
	}

	public boolean hasExpired(Map params, Date since) {
		return (System.currentTimeMillis() - since.getTime())  > 5000;
	}

	public TimeSeries getTimeseries() throws ApsSystemException {
		TimeSeries timeseries=null;
		try {
			timeseries = _statsManager.getHitsByInterval(_dataBean.getStart(), _dataBean.getEnd());
		} catch (Throwable e) {
			throw new ApsSystemException("An error occurred getting statistics data ", e);
		}
		return timeseries;
	}
	
	private IStatsManager _statsManager;
	private IStatsDataBean _dataBean;
	
}
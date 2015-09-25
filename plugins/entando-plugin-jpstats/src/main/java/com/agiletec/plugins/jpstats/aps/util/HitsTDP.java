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
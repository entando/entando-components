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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.data.category.DefaultIntervalCategoryDataset;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpstats.aps.system.services.stats.IStatsManager;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * Abstract class for building CategoryDataProducer type charts
 * All the classes that extends this one must implement the method 
 * getResultSet, that must return a Map with the data to render
 */
public abstract class AbstractCategoryDataProducer implements DatasetProducer {
	
	/**
	 * @param manager The StatsManager
	 * @param bean IStatsDataBean object that holds the date interval to render
	 */
	public AbstractCategoryDataProducer(IStatsManager manager, IStatsDataBean bean) {
		this._statsManager = manager;
		this._dataBean = bean;
	}
	
	public String getProducerId() {
		return "CategoryDataProducer";
	}
	
	public boolean hasExpired(Map params, Date since) {
		return (System.currentTimeMillis() - since.getTime())  > 5000;
	}
	
	public Object produceDataset(Map params) throws DatasetProduceException {
		DefaultIntervalCategoryDataset ds = null;
		try{
			ds = getDefaultIntervalCategoryDataset();
		} catch (Throwable e) {
			throw new DatasetProduceException("An error occurred generating the DefaultIntervalCategoryDataset object " + this.getClass().getName());
		}
		return ds;
	}
	
	protected abstract Map<String, Integer> getResultset() throws ApsSystemException, SQLException;
	
	/**
	 * The data that is meant to be rendered in a 
	 * CategoryDataProducer chart type must be a Map
	 * @param result Map the data to render in the chart
	 * @return List of two Arrays, categories and values
	 * used to build DefaultIntervalCategoryDataset object
	 */
	private List getDataFromResultset(Map<String, Integer> result) {
		List data = new ArrayList();
		int size = result.size();
		Integer[] hits;
		String[] categories; 
		categories = new String[size];
		hits = new Integer[size];
		int i = 0;
		for (Entry<String, Integer> item : result.entrySet()) {
			categories[i]= item.getKey();
			hits[i] = item.getValue();
			i++;
		}
		data.add(categories);
		data.add(hits);
		return data;
	}
	
	private DefaultIntervalCategoryDataset getDefaultIntervalCategoryDataset() throws ApsSystemException {
		DefaultIntervalCategoryDataset ds = null;
		try {
			Map<String, Integer> result = this.getResultset();
			List data = this.getDataFromResultset(result);
			String[] categories = (String[]) data.get(0);
			Integer[] hits = (Integer[]) data.get(1);
			final Integer[][] startValues = new Integer[_seriesNames.length][categories.length];
			final Integer[][] endValues = new Integer[_seriesNames.length][categories.length];
			int i;
			for (int series = 0; series < _seriesNames.length; series++) {
				for ( i = 0; i < categories.length; i++) {
					startValues[series][i] = new Integer(0);
					endValues[series][i] = hits[i]; 
				}
			}
			ds = new DefaultIntervalCategoryDataset(_seriesNames, categories, startValues, endValues);	   
		} catch (Throwable e) {
			throw new ApsSystemException("An error occurred getting statistics data ", e);
		}
		return ds;		
	}

	public IStatsDataBean getDataBean() {
		return _dataBean;
	}
	
	public IStatsManager getStatsManager() {
		return _statsManager;
	}
	
	public String[] getSeriesNames() {
		return _seriesNames;
	}
	
	public void setSeriesNames(String[] names) {
		_seriesNames = names;
	}
	
	private IStatsManager _statsManager;
	private IStatsDataBean _dataBean;
	private String[] _seriesNames = {""};
}

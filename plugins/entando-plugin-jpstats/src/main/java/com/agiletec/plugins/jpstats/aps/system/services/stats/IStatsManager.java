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
package com.agiletec.plugins.jpstats.aps.system.services.stats;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.data.time.TimeSeries;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.VisitsStat;

/**
 * Interface for manager that handles the statistic service
 * @version 1.2
 * @author M.Lisci - E.Santoboni
 */
public interface IStatsManager {
	
	public List<StatsRecord> loadStatsRecord(Date from, Date to) throws ApsSystemException;
	
	/**
	 * Adds a record to the statistic table
	 * @param statsRecord the object that contains the info to save
	 * @throws ApsSystemException in case of error during the access to the db
	 */
	public void addStatsRecord(StatsRecord statsRecord) throws ApsSystemException;
	
	/**
	 * Delete records from the statistic table
	 * @param from 
	 * @param to 
	 * @throws ApsSystemException in case of error during the access to the db.
	 */
	public void deleteStatsRecord(Date from, Date to) throws ApsSystemException;
	
	public String getAverageTimeSite(Date from, Date to) throws ApsSystemException;
	
	public List<VisitsStat> searchVisitsForDate(Date from, Date to) throws ApsSystemException;
	
	public List<VisitsStat> searchVisitsForPages(Date from, Date to) throws ApsSystemException;
	
	public List<VisitsStat> searchVisitsForContents(Date from, Date to) throws ApsSystemException;
	
	/**
	 * Gets the hits between two dates
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return TimeSeries object used to render the chart
	 * @throws ApsSystemException
	 */
	public TimeSeries getHitsByInterval(Calendar start, Calendar end) throws ApsSystemException;
	
	/**
	 * Gets the average time spent on the site by session
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return a string in the format hh:mm:ss that represents
	 * the average time spent on the site by session
	 * @throws ApsSystemException
	 */
	public String getAverageTimeSite(Calendar start, Calendar end) throws ApsSystemException;
	
	/**
	 * Gets the average time spent on a page by pagecode and by session
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return a string in the format hh:mm:ss that represents
	 * the average time spent on a page by pagecode and by session
	 * @throws ApsSystemException
	 */
	public String getAverageTimePage(Calendar start, Calendar end) throws ApsSystemException;
	
	/**
	 * Gets the average amount of pages visited in each session
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return integer the average amount of pages visited in each session
	 * @throws ApsSystemException
	 */
	public int getNumPageSession(Calendar start, Calendar end) throws ApsSystemException;
	
	/**
	 * Gets the top ten pages by hits
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return a Map (pagecode;hits) used to render the chart
	 * @throws ApsSystemException
	 */
	public Map<String, Integer> getTopPages(Calendar start, Calendar end) throws ApsSystemException;
	
	/**
	 * Gets the top ten Contents by hits
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return a Map (content description;hits) used to render the chart
	 * @throws ApsSystemException
	 */
	public Map<String, Integer> getTopContents(Calendar start, Calendar end) throws ApsSystemException;
	
	/**
	 * Gets the count of different Ip addresses that visited the site
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return integer value of the count of different Ip addresses that visited the site
	 * @throws ApsSystemException
	 */
	public int getIPByDateInterval(Calendar start,Calendar end) throws ApsSystemException;
	
	/**
	 * Gets the oldest date stored in the statistic table
	 * This is the default value for the queries
	 * @return Calendar object that represents the oldest date stored in the statistic table
	 */
	public Calendar getFirstCalendarDay();
	
}
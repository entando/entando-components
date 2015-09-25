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
 * Interface for Data Access Object of Statistics Manager dao
 * @version 1.2
 * @author M.Lisci - E.Santoboni
 */
public interface IStatsDAO {
	
	public List<StatsRecord> loadStatsRecord(Date from, Date to);
	
	public void addStatsRecord(StatsRecord statsRecord);
	
	public void deleteStatsRecord(Date from, Date to);
	
	/**
	 * Gets the average time spent on the site by session
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a string whith the format hh:mm:ss
	 * @author IFTS
	 */
	public String getAverageTimeSite(Calendar start, Calendar end);
	
	/**
	 * Gets the average time spent on a page by pagecode and by session
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a string whith the format hh:mm:ss
	 * @author IFTS
	 */
	public String getAverageTimePage(Calendar start, Calendar end);
	
	/**
	 * Gets the average amount of pages visited in each session
	 * @param start Calendar 
	 * @param end Calendar
	 * @return int the average amount of pages visited in each session
	 * @author IFTS
	 */
	public int getNumPageSession(Calendar start, Calendar end);
	
	public List<VisitsStat> searchVisitsForDate(Date start, Date end);
	
	public List<VisitsStat> searchVisitsForPages(Date from, Date to);
	
	public List<VisitsStat> searchVisitsForContents(Date from, Date to);
	
	/**
	 * Gets the ten most visited pages 
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a map (pagecode:hits) used to render the chart
	 */
	public Map<String, Integer> getTopPages(Calendar start, Calendar end);
	
	/**
	 * Gets the ten most visited contents 
	 * If the content does not exists anymore the function
	 * prints [DELETED] instead of the description
	 * @param start Calendar 
	 * @param end Calendar
	 * @param contentManager 
	 * @return a map (content_descr:hits) used to render the chart
	 */
	public Map<String, Integer> getTopContents(Calendar start, Calendar end);
	
	/**
	 * Gets a map of Ip Address (ip,hits) 
	 * @param start Calendar
	 * @param end Calendar
	 * @return a map of Ip (ip,hits) 
	 */
	public Map<String, Integer> getIPByDateInterval(Calendar start, Calendar end);
	
	/**
	 * 
	 * @return Calendar the first date stored in the statistic table
	 * If the table is empty returns the current date
	 */
	public Calendar getFirstCalendarDay();
	
	/**
	 * Gets the hits between two dates
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a TimeSeries object, used to render the chart
	 * @throws ApsSystemException
	 */
	public TimeSeries getHitsByInterval(Calendar start, Calendar end) throws ApsSystemException;
	
}
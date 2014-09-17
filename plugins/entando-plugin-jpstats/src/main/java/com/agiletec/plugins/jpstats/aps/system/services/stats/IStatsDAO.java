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
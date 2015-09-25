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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.DateEventInfo;

/**
 * Data Access Object for the service managing
 * data for calendar.
 * @author E.Santoboni
 */
public class CalendarDAO extends AbstractDAO implements ICalendarDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(CalendarDAO.class);
	
	@Override
	public int[] loadCalendar(Calendar requiredMonth, String groupName, String contentType, 
			String attributeNameStart, String attributeNameEnd) throws ApsSystemException {
		int[] eventsForMonth = new int[31];
		List<DateEventInfo> eventsInfo = this.getEventsInfo(requiredMonth, groupName, 
				contentType, attributeNameStart, attributeNameEnd);
		for (int i=0; i<eventsInfo.size(); i++) {
			DateEventInfo info = (DateEventInfo) eventsInfo.get(i);
			if (info.isValidEvent()) {
				for (int j=(info.getStartDay()-1); j<=(info.getEndDay()-1); j++) {
					int eventsOfDay = eventsForMonth[j];
					eventsForMonth[j] = (++eventsOfDay);
				}
			}
		}
		return eventsForMonth;
	}
	
	@Override
	public int getFirstYear(String contentType, String attributeName) throws ApsSystemException {
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		PreparedStatement stat = null;
    	ResultSet res = null;
    	Connection conn = null;
		try {
			conn = this.getConnection();
    		stat = conn.prepareStatement(SEARCH_FIRST_YEAR);
    		stat.setString(1, contentType);
    		stat.setString(2, attributeName);
    		res = stat.executeQuery();
    		if (res.next()) {
    			java.sql.Date date = res.getDate(1);
    			Calendar cal = Calendar.getInstance();
				if (date != null) cal.setTime(date);
				year = cal.get(Calendar.YEAR);
    		}
    	} catch (Throwable t) {
			_logger.error("Error in search for first year. contenttype: {} attribute:{}", contentType, attributeName,  t);
			throw new RuntimeException("Error in search for first year", t);
    	} finally {
    		closeDaoResources(res, stat, conn);
    	}
		return year;
	}
	
	private List<DateEventInfo> getEventsInfo(Calendar requiredMonth, String groupName, String contentType, 
			String attributeNameStart, String attributeNameEnd) throws ApsSystemException {
		Map<String, DateEventInfo> eventsInfoMap = new HashMap<String, DateEventInfo>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Date first = this.getFirstDay(requiredMonth);
		Date last = this.getLastDay(requiredMonth);
		Connection conn = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_DATES);
			stat.setString(1, contentType);
			stat.setString(2, attributeNameStart);
			stat.setString(3, attributeNameEnd);
			stat.setString(4, groupName);
			res = stat.executeQuery();
			while (res.next()) {
				String contentId = res.getString(1);
				DateEventInfo info = (DateEventInfo) eventsInfoMap.get(contentId);
				if (info == null) {
					info = new DateEventInfo(first, last);
					eventsInfoMap.put(contentId, info);
				}
				String attributeName = res.getString(2);
				if (attributeName.equalsIgnoreCase(attributeNameStart)) {
					info.setStart(res.getDate(3));
				} else if (attributeName.equalsIgnoreCase(attributeNameEnd)) {
					info.setEnd(res.getDate(3));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading calendar",  t);
			throw new RuntimeException("Error loading calendar", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		List<DateEventInfo> eventsInfo = new ArrayList<DateEventInfo>();
		eventsInfo.addAll(eventsInfoMap.values());
		return eventsInfo;
	}
	
	private java.sql.Date getFirstDay(Calendar requiredMonth) {
		Calendar first = (Calendar) requiredMonth.clone();
		first.set(Calendar.DAY_OF_MONTH, requiredMonth.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date date = new Date(first.getTime().getTime());
		return date;
	}
	
	private java.sql.Date getLastDay(Calendar requiredMonth) {
		Calendar last = (Calendar) requiredMonth.clone();
		last.set(Calendar.DAY_OF_MONTH, requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date date = new Date(last.getTime().getTime());
		return date;
	}
	
	private final String SEARCH_DATES = 
		"SELECT contents.contentid, contentsearch.attrname, contentsearch.datevalue " +
		"FROM (contents INNER JOIN contentrelations ON contents.contentid = contentrelations.contentid) " +
		"INNER JOIN contentsearch ON contents.contentid = contentsearch.contentid " +
		"WHERE contents.onlinexml IS NOT NULL " +
		"AND contents.contenttype = ? " + // 1 tipo contenuto
		"AND (contentsearch.attrname = ? " + // 2 nome attributo inizio
		"OR contentsearch.attrname = ?) " + // 3 nome attributo fine
		"AND contentrelations.refgroup = ? " + // 4 gruppo
		"ORDER BY contents.contentid";
	
	private final String SEARCH_FIRST_YEAR = 
		"SELECT MIN(contentsearch.datevalue) " +
		"FROM contents INNER JOIN contentsearch ON contents.contentid = contentsearch.contentid " +
		"WHERE contents.onlinexml IS NOT NULL " +
		"AND contents.contenttype = ? " +
		"AND contentsearch.attrname = ? ";
		
}

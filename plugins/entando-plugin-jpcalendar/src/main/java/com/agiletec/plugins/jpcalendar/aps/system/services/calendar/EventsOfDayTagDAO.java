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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.EventsOfDayDataBean;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.SmallEventOfDay;

/**
 * Classe DAO specifica per la gestione del tag EventsOfDay
 * @author E.Santoboni
 */
public class EventsOfDayTagDAO extends AbstractDAO implements IEventsOfDayTagDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(EventsOfDayTagDAO.class);
	
	public List<SmallEventOfDay> loadSmallEventsOfDay(EventsOfDayDataBean bean) 
			throws ApsSystemException {
		List<SmallEventOfDay> smallEvents = new ArrayList<SmallEventOfDay>();
		PreparedStatement stat = null;
		ResultSet result = null;
		Connection conn = this.getConnection();
		try {
			stat = conn.prepareStatement(this.createQueryString(bean));
			int index = 0;
			stat.setString(++index, bean.getContentType());
			stat.setString(++index, bean.getAttributeNameStart());
			stat.setString(++index, bean.getAttributeNameEnd());
			if (bean.getAllowedGroups() != null 
					&& bean.getAllowedGroups().size()>0
					&& !bean.getAllowedGroups().contains(Group.ADMINS_GROUP_NAME)) {
				Iterator<String> groupIter = bean.getAllowedGroups().iterator();
				while (groupIter.hasNext()) {
					String groupName = (String) groupIter.next();
					stat.setString(++index, groupName);
				}
			}
			result = stat.executeQuery();
			smallEvents = this.loadSmallDateContents(result, 
					bean.getAttributeNameStart(), bean.getAttributeNameEnd());
			
		} catch (Throwable t) {
			_logger.error("Error loading smallEvents list",  t);
			throw new RuntimeException("Error loading smallEvents list", t);
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return smallEvents;
	}
	
	private String createQueryString(EventsOfDayDataBean bean) {
		String query = this.LOAD_DATE_CONTENTS;
		if (bean.getAllowedGroups() != null 
				&& bean.getAllowedGroups().size()>0
				&& !bean.getAllowedGroups().contains(Group.ADMINS_GROUP_NAME)) {
			query += APPEND_STRING_GROUP_SEARCH_START;
			int size = bean.getAllowedGroups().size();
			for (int i=0; i<size; i++) {
				if (i!=0) query += APPEND_STRING_OR;
				query += APPEND_STRING_GROUP_SEARCH;
			}
			query += APPEND_STRING_GROUP_SEARCH_END;
			return query;
		}
		query += ORDER_BLOCK;
		return query;
	}
	
	private List<SmallEventOfDay> loadSmallDateContents(ResultSet result, 
			String attributeNameStart, String attributeNameEnd) throws SQLException {
    	List<SmallEventOfDay> smallEvents = new ArrayList<SmallEventOfDay>();
    	SmallEventOfDay smallEvent = null;
    	String prevId = null;
    	while (result.next()) {
    		String id = result.getString(1);
    		if (!id.equals(prevId)) {
    			if (smallEvent != null) {
    				smallEvents.add(smallEvent);
    			}
    			smallEvent = new SmallEventOfDay();
    		}
    		smallEvent.setId(id);
    		String attrName = result.getString(2);
    		if (attrName.equals(attributeNameStart)) {
    			Date dataInizio = this.dateFromSQL(result.getDate(4));
    			smallEvent.setStart(dataInizio);
    		} else if (attrName.equals(attributeNameEnd)) {
    			Date endDate = this.dateFromSQL(result.getDate(4));
    			smallEvent.setEnd(endDate);
    		}
    		prevId = id;
    	}
    	if (smallEvent != null) {
			smallEvents.add(smallEvent);
		}
    	return smallEvents;
    }
	
	private Date dateFromSQL(java.sql.Date sqlDate){
		Date date = null;
		if(sqlDate != null){
			date = new Date(sqlDate.getTime());
		}
		return date;
	}
	
	private final String LOAD_DATE_CONTENTS = 
		"SELECT contentsearch.contentid, contentsearch.attrname, " +
		"contentsearch.textvalue, contentsearch.datevalue, contentrelations.refcategory " +
		"FROM (contents INNER JOIN contentrelations " +
		"ON contents.contentid = contentrelations.contentid) " +
		"INNER JOIN contentsearch ON contents.contentid = contentsearch.contentid " +
		"WHERE contents.contenttype = ? " +
		"AND (contentsearch.attrname = ? OR contentsearch.attrname = ? ) ";
	
	private final String APPEND_STRING_GROUP_SEARCH_START = "AND ( ";
	private final String APPEND_STRING_GROUP_SEARCH = "contentrelations.refgroup = ? ";
	private final String APPEND_STRING_OR = "OR ";
	private final String APPEND_STRING_GROUP_SEARCH_END = ") ";
		
	private final String ORDER_BLOCK = 
		"ORDER BY contentsearch.contentid ";
	
}

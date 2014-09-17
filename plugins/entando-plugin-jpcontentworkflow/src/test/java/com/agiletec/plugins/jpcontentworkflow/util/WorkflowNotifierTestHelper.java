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
package com.agiletec.plugins.jpcontentworkflow.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.ContentStatusChangedEventInfo;

/**
 * @author E.Santoboni
 */
public class WorkflowNotifierTestHelper extends AbstractDAO {
	
	public void deleteContentEvents() throws Exception {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(DELETE_CONTENT_EVENTS);
			stat.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public Map<String, List<ContentStatusChangedEventInfo>> getEventsToNotify() throws ApsSystemException {
		Map<String, List<ContentStatusChangedEventInfo>> eventsToNotify = new HashMap<String, List<ContentStatusChangedEventInfo>>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_CONTENTS_TO_BE_NOTIFY);
			eventsToNotify = this.extractContentMailInfoFromResultSet(res);
		} catch (Throwable t) {
			this.processDaoException(t, "Errore in caricamento contenuti da notificare", "getContentsToNotify");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return eventsToNotify;
	}
	
	public Map<String, List<ContentStatusChangedEventInfo>> getNotifiedEvents() throws Throwable {
		Map<String, List<ContentStatusChangedEventInfo>> eventsToNotify = new HashMap<String, List<ContentStatusChangedEventInfo>>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_NOTIFIED_CONTENTS);
			eventsToNotify = this.extractContentMailInfoFromResultSet(res);
		} catch (Throwable t) {
			this.processDaoException(t, "Errore in caricamento contenuti da notificare", "getContentsToNotify");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return eventsToNotify;
	}
	
	private Map<String, List<ContentStatusChangedEventInfo>> extractContentMailInfoFromResultSet(ResultSet res) throws SQLException {
		Map<String, List<ContentStatusChangedEventInfo>> eventsToNotify = new HashMap<String, List<ContentStatusChangedEventInfo>>();
		while (res.next()) {
			ContentStatusChangedEventInfo event = new ContentStatusChangedEventInfo();
			event.setId(res.getInt(1));
			event.setDate(res.getDate(2));
			event.setContentId(res.getString(3));
			String contentType = res.getString(4);
			event.setContentTypeCode(contentType);
			event.setContentDescr(res.getString(5));
			event.setMainGroup(res.getString(6));
			event.setStatus(res.getString(7));
			List<ContentStatusChangedEventInfo> contentTypeEvents = (List<ContentStatusChangedEventInfo>) eventsToNotify.get(contentType);
			if (contentTypeEvents == null) {
				contentTypeEvents = new ArrayList<ContentStatusChangedEventInfo>();
				eventsToNotify.put(contentType, contentTypeEvents);
			}
			contentTypeEvents.add(event);
		}
		return eventsToNotify;
	}
	
	private final String LOAD_CONTENTS_TO_BE_NOTIFY = 
		"SELECT id, data, contentid, contenttype, descr, maingroup, status " +
		"FROM jpcontentworkflow_events WHERE notified = 0";
	
	private final String LOAD_NOTIFIED_CONTENTS = 
		"SELECT id, data, contentid, contenttype, descr, maingroup, status " +
		"FROM jpcontentworkflow_events WHERE notified = 1 ";
	
	private final String DELETE_CONTENT_EVENTS = 
		"DELETE FROM jpcontentworkflow_events";
	
}
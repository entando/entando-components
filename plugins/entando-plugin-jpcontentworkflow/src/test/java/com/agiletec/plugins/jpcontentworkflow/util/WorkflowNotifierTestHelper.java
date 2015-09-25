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
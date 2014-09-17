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
package com.agiletec.plugins.jpcontentnotifier.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.ContentMailInfo;

public class TestContentNotifierHelper extends AbstractDAO {
	
	public TestContentNotifierHelper(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	public void deleteContentChangingEventsRequest() throws Throwable {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(DELETE_CONTENT_CHANGING_EVENTS);
			stat.executeUpdate();
		} catch (Throwable t) {
			throw t;
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public Map<String, List<ContentMailInfo>> getContentsToNotify() throws ApsSystemException {
		Map<String, List<ContentMailInfo>> contentsToNotify = new HashMap<String, List<ContentMailInfo>>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_CONTENTS_TO_BE_NOTIFY);
			contentsToNotify = this.extractContentMailInfoFromResultSet(res);
		} catch (Throwable t) {
			this.processDaoException(t, "Errore in caricamento contenuti da notificare", "getContentsToNotify");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return contentsToNotify;
	}
	
	public Map<String, List<ContentMailInfo>> getNotifiedContents() throws Throwable {
		Map<String, List<ContentMailInfo>> contentsToNotify = new HashMap<String, List<ContentMailInfo>>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_NOTIFIED_CONTENTS);
			contentsToNotify = this.extractContentMailInfoFromResultSet(res);
		} catch (Throwable t) {
			this.processDaoException(t, "Errore in caricamento contenuti da notificare", "getContentsToNotify");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return contentsToNotify;
	}
	
	private Map<String, List<ContentMailInfo>> extractContentMailInfoFromResultSet(ResultSet res) throws SQLException {
		Map<String, List<ContentMailInfo>> contentsToNotify = new HashMap<String, List<ContentMailInfo>>();
		while (res.next()) {
			ContentMailInfo info = new ContentMailInfo();
			info.setId(res.getInt(1));
			info.setDate(res.getTimestamp(2));
			info.setOperationCode(res.getInt(3));
			info.setContentId(res.getString(4));
			info.setContentTypeCode(res.getString(5));
			info.setContentDescr(res.getString(6));
			info.setMainGroup(res.getString(7));
			String groups = res.getString(8);
			if (groups!=null) {
				info.setGroups(groups.split(","));
			}
			String contentType = info.getContentTypeCode();
			List<ContentMailInfo> infosForContentType = (List<ContentMailInfo>) contentsToNotify.get(contentType);
			if (infosForContentType == null) {
				infosForContentType = new ArrayList<ContentMailInfo>();
				contentsToNotify.put(contentType, infosForContentType);
			}
			infosForContentType.add(info);
		}
		return contentsToNotify;
	}
	
	private final String LOAD_CONTENTS_TO_BE_NOTIFY = 
		"SELECT id, eventdate, operationcode, contentid, contenttype, descr, maingroup, groups " +
		"FROM jpcontentnotifier_contentchangingevents WHERE notified = 0";
	
	private final String LOAD_NOTIFIED_CONTENTS = 
		"SELECT id, eventdate, operationcode, contentid, contenttype, descr, maingroup, groups " +
		"FROM jpcontentnotifier_contentchangingevents WHERE notified = 1";
	
	private final String DELETE_CONTENT_CHANGING_EVENTS = 
		"DELETE FROM jpcontentnotifier_contentchangingevents";
	
}
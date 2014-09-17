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
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.ContentMailInfo;

public class ContentNotifierDAO extends AbstractDAO implements IContentNotifierDAO {
	
	@Override
	public void saveEvent(PublicContentChangedEvent event) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(SAVE_EVENT);
			Content content = (Content) event.getContent();
			stat.setInt(1, this.newId(conn));
			stat.setTimestamp(2, new Timestamp(new Date().getTime()));
			stat.setInt(3, event.getOperationCode());
			stat.setString(4, content.getId());
			stat.setString(5, content.getTypeCode());
			stat.setString(6, content.getDescr());
			stat.setString(7, content.getMainGroup());
			String groups = this.getConcatedString(content.getGroups());
			stat.setString(8, groups);
			stat.setShort(9, (short) 0); // false
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Errore in salvataggio Evento", "saveEvent");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public List<ContentMailInfo> getContentsToNotify() throws ApsSystemException {
		List<ContentMailInfo> contentsToNotify = new ArrayList<ContentMailInfo>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_ALL_CONTENT_TO_BE_NOTIFY);
			while (res.next()) {
				ContentMailInfo info = this.prepareContentInfoFromResultSet(res);
				contentsToNotify.add(info);
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Errore in caricamento contenuti da notificare", "getContentsToNotify");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return contentsToNotify;
	}
	
	@Override
	public void signNotifiedContents(List<ContentMailInfo> contentsNotifiedInfo) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE);
			for (ContentMailInfo info : contentsNotifiedInfo) {
				stat.setShort(1, (short) 1);
				stat.setInt(2, info.getId());
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Errore in aggiunta record tabella", "addContentSearchRecord");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	private String getConcatedString(Collection<String> codes) throws SQLException {
		if (null != codes && !codes.isEmpty()) {
			StringBuffer buffer = new StringBuffer();
			Iterator<String> codesIter = codes.iterator();
			buffer.append(codesIter.next());
			while (codesIter.hasNext()) {
				buffer.append(",");
				buffer.append(codesIter.next());
			}
			return buffer.toString();
		}
		return null;
	}
	
	private ContentMailInfo prepareContentInfoFromResultSet(ResultSet res) throws SQLException {
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
		return info;
	}
	
	private int newId(Connection conn) 
			throws ApsSystemException {
		int id = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			stat = conn.createStatement();
			res = stat.executeQuery(SELECT_NEW_ID);
			res.next();
			id = res.getInt(1) + 1; // N.B.: funziona anche per il primo record
		} catch (Throwable e) {
			this.processDaoException(e, "Errore in estrazione ultimo id", "newId");
		} finally {
			this.closeDaoResources(res, stat);
		}
		return id;
	}
	
	private final String SAVE_EVENT =
		"INSERT INTO jpcontentnotifier_contentchangingevents " +
		"( id, eventdate, operationcode, contentid, contenttype, descr, maingroup, groups, notified ) " +
		"VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
	
	private final String LOAD_ALL_CONTENT_TO_BE_NOTIFY = 
		"SELECT id, eventdate, operationcode, contentid, contenttype, descr, maingroup, groups " +
		"FROM jpcontentnotifier_contentchangingevents WHERE notified = 0";
	
	private final String UPDATE =
		"UPDATE jpcontentnotifier_contentchangingevents SET notified = ? WHERE id = ?";
	
	private final String SELECT_NEW_ID =
		"SELECT MAX(id) FROM jpcontentnotifier_contentchangingevents";
	
}
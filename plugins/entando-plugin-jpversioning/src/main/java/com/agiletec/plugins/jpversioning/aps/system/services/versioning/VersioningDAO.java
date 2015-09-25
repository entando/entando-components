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
package com.agiletec.plugins.jpversioning.aps.system.services.versioning;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * @author E.Santoboni
 */
public class VersioningDAO extends AbstractDAO implements IVersioningDAO {

	private static final Logger _logger = LoggerFactory.getLogger(VersioningDAO.class);
	
	@Override
	public List<Long> getVersions(String contentId) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Long> ids = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SELECT_VERSIONS_BY_CONTENTID);
			stat.setString(1, contentId);
			res = stat.executeQuery();
			while (res.next()) {
				if (ids == null) {
					ids = new ArrayList<Long>();
				}
				ids.add(res.getLong(1));
			}
		} catch (Throwable t) {
			_logger.error("Error loading content version",  t);
			throw new RuntimeException("Error loading content version", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return ids;
	}
	
	@Override
	public List<Long> getLastVersions(String contentType, String descr) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Long> lastVersions = new ArrayList<Long>();
		String query = this.createQueryForGetLastVersions(contentType, descr);
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(query);
			int i = 0;
			if (null != contentType && contentType.trim().length() > 0 ) {
				stat.setString(++i, contentType);
			}
			if (null != descr && descr.trim().length() > 0 ) {
				StringBuffer strBff = new StringBuffer("%");
				strBff.append(descr);
				strBff.append("%");
				stat.setString(++i, strBff.toString());
			}
			res = stat.executeQuery();
			while (res.next()) {
				if (null == lastVersions) {
					lastVersions = new ArrayList<Long>();
				}
				lastVersions.add(res.getLong(1));				
			}			
		} catch (Throwable t) {
			_logger.error("Error loading content versions", t);
			throw new RuntimeException("Error loading content versions", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return lastVersions;
	}
	
	@Override
	public ContentVersion getVersion(long id) {
		Connection conn = null;
		PreparedStatement stat = null;
		ContentVersion contentVersion = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SELECT_VERSION_BY_VERSIONID);
			stat.setLong(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				contentVersion = this.prepareContentVersionFromResultSet(res);
			}			
		} catch (Throwable t) {
			_logger.error("Error loading version", t);
			throw new RuntimeException("Error loading version", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return contentVersion;
	}
	
	@Override
	public ContentVersion getLastVersion(String contentId) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		ContentVersion contentVersion = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SELECT_LAST_VERSION_BY_CONTENTID);
			stat.setString(1, contentId);
			res = stat.executeQuery();
			if (res.next()) {
				contentVersion = this.prepareContentVersionFromResultSet(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading last content version", t);
			throw new RuntimeException("Error loading last content version", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return contentVersion;
	}
	
	@Override
	public void addContentVersion(ContentVersion contentVersion) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			long nextId = this.extractNextId(NEXT_ID, conn);
			contentVersion.setId(nextId);
			stat = conn.prepareStatement(ADD_VERSION_RECORD);
			stat.setLong(1, contentVersion.getId());
			stat.setString(2, contentVersion.getContentId());
			stat.setString(3, contentVersion.getContentType());
			stat.setString(4, contentVersion.getDescr());
			stat.setString(5, contentVersion.getStatus());
			stat.setString(6, contentVersion.getXml());
			stat.setTimestamp(7, new Timestamp(contentVersion.getVersionDate().getTime()));
			stat.setString(8, contentVersion.getVersion());
			stat.setInt(9, contentVersion.getOnlineVersion());
			int approved = contentVersion.isApproved() ? 1 : 0;
			stat.setInt(10, approved);
			stat.setString(11, contentVersion.getUsername());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding version record", t);
			throw new RuntimeException("Error adding version record", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteVersion(long versionId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_VERSION);
			stat.setLong(1, versionId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting version", t);
			throw new RuntimeException("Error deleting version", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteWorkVersions(String contentId, int onlineVersion) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_WORK_VERSION_RECORDS);
			stat.setString(1, contentId);
			stat.setInt(2, onlineVersion);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting work versions", t);
			throw new RuntimeException("Error deleting work versions", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	protected long extractNextId(String query, Connection conn) {
		long id = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			stat = conn.createStatement();
			res = stat.executeQuery(query);
			res.next();
			id = res.getLong(1) + 1; // N.B.: funziona anche per il primo record
		} catch (Throwable t) {
			_logger.error("Error extracting next id",  t);
			throw new RuntimeException("Error extracting next id", t);
		} finally {
			closeDaoResources(res, stat);
		}
		return id;
	}
	
	private String createQueryForGetLastVersions(String contentType, String descr) {
		StringBuilder query = new StringBuilder(SELECT_LAST_VERSIONS_HEAD);
		boolean appendWhere = true;
		if (null != contentType && contentType.trim().length() > 0 ) {
			query.append(APPEND_WHERE);
			query.append(APPEND_CONTENTTYPE);
			appendWhere = false;
		}
		if (null != descr && descr.trim().length() > 0 ) {
			query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
			query.append(APPEND_LIKE_DESCR);
			appendWhere = false;
		}
		query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
		query.append(" id IN ");
		query.append(APPEND_LAST_VERSIONS_TAIL);
		return query.toString();
	}
	
	private ContentVersion prepareContentVersionFromResultSet(ResultSet res) throws SQLException {
		ContentVersion contentVersion = new ContentVersion();
		contentVersion.setId(res.getLong(1));
		contentVersion.setContentId(res.getString(2));
		contentVersion.setContentType(res.getString(3));
		contentVersion.setDescr(res.getString(4));
		contentVersion.setStatus(res.getString(5));
		contentVersion.setXml(res.getString(6));				
		contentVersion.setVersionDate(res.getTimestamp(7));
		contentVersion.setVersion(res.getString(8));
		contentVersion.setOnlineVersion(res.getInt(9));
		contentVersion.setApproved(res.getInt(10) > 0);
		contentVersion.setUsername(res.getString(11));
		return contentVersion;
	}
	
	private final String SELECT_VERSIONS_BY_CONTENTID = 
		"SELECT id FROM jpversioning_versionedcontents WHERE contentid = ? ORDER BY id DESC";
	
	private final String NEXT_ID = 
		"SELECT MAX(id) FROM jpversioning_versionedcontents";
	
	private final String SELECT_LAST_VERSIONS_HEAD = 
		"SELECT id FROM jpversioning_versionedcontents ";
	
	private final String APPEND_WHERE = "WHERE ";
	private final String APPEND_CONTENTTYPE = "contenttype = ? ";
	private final String APPEND_AND = "AND ";
	private final String APPEND_LIKE_DESCR = "descr LIKE ? ";
	private final String APPEND_LAST_VERSIONS_TAIL = 
		"( SELECT MAX(id) AS id FROM jpversioning_versionedcontents GROUP BY contentid ) ";
	
	private final String SELECT_VERSION_BY_VERSIONID = 
		"SELECT id, contentid, contenttype, descr, status, contentxml, versiondate, versioncode, " +
		"onlineversion, approved, username FROM jpversioning_versionedcontents WHERE id = ? ";
	
	private final String SELECT_LAST_VERSION_BY_CONTENTID = 
		"SELECT id, contentid, contenttype, descr, status, contentxml, versiondate, versioncode, " +
		"onlineversion, approved, username FROM jpversioning_versionedcontents " +
		"WHERE contentid = ? ORDER BY versiondate DESC, id DESC ";
	
	private final String ADD_VERSION_RECORD = 
		"INSERT INTO jpversioning_versionedcontents ( id, contentid, contenttype, descr, " +
		"status, contentxml, versiondate, versioncode, onlineversion, approved, username ) " +
		" VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ?, ?, ? ) ";
	
	private final String DELETE_VERSION = "DELETE FROM jpversioning_versionedcontents WHERE id = ? ";
	
	private final String DELETE_WORK_VERSION_RECORDS = 
		"DELETE FROM jpversioning_versionedcontents WHERE contentid = ? AND onlineversion = ? AND approved <> 1 ";
	
}
/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.aps.system.services.mapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class SeoMappingDAO extends AbstractSearcherDAO implements ISeoMappingDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SeoMappingDAO.class);

	@Override
	public Map<String, FriendlyCodeVO> loadMapping() {
		Map<String, FriendlyCodeVO> mapping = new HashMap<String, FriendlyCodeVO>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_MAPPINGS);
			FriendlyCodeVO vo = null;
			while (res.next()) {
				vo = new FriendlyCodeVO();
				vo.setFriendlyCode(res.getString(1));
				vo.setPageCode(res.getString(2));
				vo.setContentId(res.getString(3));
				vo.setLangCode(res.getString(4));
				mapping.put(vo.getFriendlyCode(), vo);
			}
		} catch (Throwable t) {
			_logger.error("Error while loading mapping",  t);
			throw new RuntimeException("Error while loading mapping", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return mapping;
	}
	
	@Override
	public void updateMapping(FriendlyCodeVO vo) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteRecord(vo.getFriendlyCode(), vo.getPageCode(), vo.getContentId(), conn);
			this.addRecord(vo, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error update the mapping",  t);
			throw new RuntimeException("Error update the mapping", t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	@Override
	public void updateMapping(ContentFriendlyCode contentFriendlyCode) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteRecord(null, null, contentFriendlyCode.getContentId(), conn);
			stat = conn.prepareStatement(ADD_MAPPING);
			String contentId = contentFriendlyCode.getContentId();
			Iterator<Entry<String, String>> codes = contentFriendlyCode.getFriendlyCodes().entrySet().iterator();
			while (codes.hasNext()) {
				Entry<String, String> currentCode = codes.next();
				stat.setString(1, currentCode.getValue());
				stat.setString(2, null);
				stat.setString(3, contentId);
				stat.setString(4, currentCode.getKey());
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error update the content mapping", t);
			throw new RuntimeException("Error update the content mapping", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	protected void addRecord(FriendlyCodeVO vo, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_MAPPING);
			stat.setString(1, vo.getFriendlyCode());
			stat.setString(2, vo.getPageCode());
			stat.setString(3, vo.getContentId());
			stat.setString(4, vo.getLangCode());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error adding a record", t);
			throw new RuntimeException("Error adding a record", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	public void deleteMapping(String pageCode, String contentId) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteRecord(null, pageCode, contentId, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting apping",  t);
			throw new RuntimeException("Error deleting apping", t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	protected void deleteRecord(String friendlyCode, String pageCode, String contentId, Connection conn) {
		PreparedStatement stat = null;
		try {
			boolean appendedWhereClause = false;
			StringBuilder query = new StringBuilder("DELETE FROM jpseo_friendlycode ");
			if (null != friendlyCode) {
				query.append(" WHERE friendlycode = ? ");
				appendedWhereClause = true;
			}
			if (null != pageCode) {
				if (appendedWhereClause) {
					query.append(" AND ");
				} else {
					query.append(" WHERE ");
					appendedWhereClause = true;
				}
				query.append(" pagecode = ? ");
			}
			if (null != contentId) {
				if (appendedWhereClause) {
					query.append(" AND ");
				} else {
					query.append(" WHERE ");
					appendedWhereClause = true;
				}
				query.append(" contentid = ? ");
			}
			stat = conn.prepareStatement(query.toString());
			int index = 1;
			if (null != friendlyCode) {
				stat.setString(index++, friendlyCode);
			}
			if (null != pageCode) {
				stat.setString(index++, pageCode);
			}
			if (null != contentId) {
				stat.setString(index++, contentId);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting record - code {}", friendlyCode,  t);
			throw new RuntimeException("Error deleting record - code " + friendlyCode, t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	public List<String> searchFriendlyCode(FieldSearchFilter[] filters) {
		return super.searchId(filters);
	}

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpseo_friendlycode";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "friendlycode";
	}

	private static final String ADD_MAPPING = 
			"INSERT INTO jpseo_friendlycode (friendlycode, pagecode, contentid, langcode) VALUES (?, ?, ?, ?)";
	
	private static final String LOAD_MAPPINGS = 
			"SELECT friendlycode, pagecode, contentid, langcode FROM jpseo_friendlycode";
	
}
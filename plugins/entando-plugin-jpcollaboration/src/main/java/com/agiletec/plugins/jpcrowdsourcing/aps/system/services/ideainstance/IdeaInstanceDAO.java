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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaDAO;

public class IdeaInstanceDAO extends AbstractDAO implements IIdeaInstanceDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaInstanceDAO.class);

	@Override
	public List<String> searchIdeaInstances(Collection<String> groupNames, String code) {
		List<String> ideainstancesId = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		if ((null == groupNames || groupNames.isEmpty()) && StringUtils.isBlank(code)) {
			ideainstancesId = this.loadIdeaInstances();
			return ideainstancesId;
		}
		try {
			conn = this.getConnection();
			StringBuffer sbBuffer = new StringBuffer();
			Iterator<String> it = groupNames.iterator();
			boolean appendWhere = true;

			String q = SEARCH_IDEAINSTANCES_ID;
			if (!groupNames.isEmpty()) {
				q = q.replace("#JOIN_GROUP#", "INNER JOIN jpcollaboration_ideainstance_group g ON i.code=g.code");
				sbBuffer.append(appendWhere ? " WHERE " : " AND ");
				appendWhere = false;
				sbBuffer.append("(");
				boolean appendOr = false;
				while (it.hasNext()) {

					String gname = it.next(); //do not remove
					if (appendOr) sbBuffer.append(" OR ");
					sbBuffer.append("g.groupname=?").append(" ");
					appendOr = true;
				}
				sbBuffer.append(")");
				q = q.replace("#GROUP_CODES#", sbBuffer.toString());
			} else {
				q = q.replace("#GROUP_CODES#", " ");
				q = q.replace("#JOIN_GROUP#", " ");
			}

			if (StringUtils.isNotBlank(code)) {
				sbBuffer = new StringBuffer();
				sbBuffer.append(appendWhere ? " WHERE " : " AND ");
				appendWhere = false;
				sbBuffer.append("i.code like ? ");
				q = q.replace("#CODE_NAME#", sbBuffer.toString());
			} else {
				q = q.replace("#CODE_NAME#", " ");
			}


			stat = conn.prepareStatement(q);
			int index = 1;

			if (!groupNames.isEmpty()) {
				it = groupNames.iterator();
				while (it.hasNext()) {
					String group = it.next();
					stat.setString(index++, group);
				}
			}
			if (StringUtils.isNotBlank(code)) {
				stat.setString(index++, "%" + code + "%");
			}

			res = stat.executeQuery();
			while (res.next()) {
				String codeValue = res.getString("code");
				ideainstancesId.add(codeValue);
			}
		} catch (Throwable t) {
			_logger.error("error in searchIdeaInstances",  t);
			throw new RuntimeException("error in searchIdeaInstances", t);
			//processDaoException(t, "Error! ", "searchIdeaInstances");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return ideainstancesId;
	}

	@Override
	public List<String> loadIdeaInstances() {
		List<String> ideainstancesId = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_IDEAINSTANCES_ID);
			res = stat.executeQuery();
			while (res.next()) {
				String code = res.getString("code");
				ideainstancesId.add(code);
			}
		} catch (Throwable t) {
			_logger.error("error in loadIdeaInstances",  t);
			throw new RuntimeException("error in loadIdeaInstances", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return ideainstancesId;
	}

	@Override
	public void insertIdeaInstance(IdeaInstance ideainstance) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertIdeaInstance(ideainstance, conn);
			this.insertIdeaInstanceGroups(ideainstance.getCode(), ideainstance.getGroups(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error creating ideainstance",  t);
			throw new RuntimeException("Error creating ideainstance", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	private void insertIdeaInstance(IdeaInstance ideainstance, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_IDEAINSTANCE);
			int index = 1;
			stat.setString(index++, ideainstance.getCode());
			if(null != ideainstance.getCreatedat()) {
				Timestamp createdatTimestamp = new Timestamp(ideainstance.getCreatedat().getTime());
				stat.setTimestamp(index++, createdatTimestamp);
			} else {
				stat.setNull(index++, Types.DATE);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error in insert ideainstance",  t);
			throw new RuntimeException("Error in insert ideainstance", t);
		} finally{
			this.closeDaoResources(null, stat, null);
		}
	}


	@Override
	public void updateIdeaInstance(IdeaInstance ideainstance) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateIdeaInstance(ideainstance, conn);
			this.insertIdeaInstanceGroups(ideainstance.getCode(), ideainstance.getGroups(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating ideainstance",  t);
			throw new RuntimeException("Error updating ideainstance", t);
		} finally{
			this.closeDaoResources(null, stat, conn);
		}
	}

	private void updateIdeaInstance(IdeaInstance ideainstance, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_IDEAINSTANCE);
			int index = 1;
			if(null != ideainstance.getCreatedat()) {
				Timestamp createdatTimestamp = new Timestamp(ideainstance.getCreatedat().getTime());
				stat.setTimestamp(index++, createdatTimestamp);
			} else {
				stat.setNull(index++, Types.DATE);
			}
			stat.setString(index++, ideainstance.getCode());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating ideainstance ",  t);
			throw new RuntimeException("Error updating ideainstance ", t);
		} finally{
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeIdeaInstance(String code) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			List<String> ideaList = this.getIdeaDAO().searchIdea(code, null, null, null, null);
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.getIdeaDAO().removeIdeas(ideaList, conn);
			this.removeIdeaInstanceGroups(code, conn);
			this.removeIdeaInstance(code, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting ideainstance",  t);
			throw new RuntimeException("Error deleting ideainstance", t);
		} finally{
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void removeIdeaInstance(String code, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_IDEAINSTANCE);
			int index = 1;
			stat.setString(index++, code);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting ideainstance {}", code,  t);
			throw new RuntimeException("Error deleting ideainstance", t);
		} finally{
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public IdeaInstance loadIdeaInstance(String code, Collection<Integer> ideaStatus) {
		IdeaInstance ideainstance = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_IDEAINSTANCE);
			int index = 1;
			stat.setString(index++, code);
			res = stat.executeQuery();
			if (res.next()) {
				ideainstance = new IdeaInstance();
				ideainstance.setCode(res.getString("code"));
				Timestamp createdatValue = res.getTimestamp("createdat");
				if (null != createdatValue) {
					ideainstance.setCreatedat(new Date(createdatValue.getTime()));
				}
			}
			if (null != ideainstance) {
				List<String> groups = this.loadIdeaInstanceGroups(code, conn);
				ideainstance.setGroups(groups);
				List<Integer> ideas = this.loadIdeaInstanceIdeas(code, ideaStatus, conn);
				ideainstance.setChildren(ideas);
			}

		} catch (Throwable t) {
			_logger.error("Error loading ideainstance with code {}", code,  t);
			throw new RuntimeException("Error loading ideainstance", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return ideainstance;
	}

	private List<String> loadIdeaInstanceGroups(String code, Connection conn) {
		List<String> groups = new ArrayList<String>();

		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_IDEAINSTANCE_GROUPS);
			int index = 1;
			stat.setString(index++, code);
			res = stat.executeQuery();
			while (res.next()) {
				groups.add(res.getString("groupname"));
			}
		} catch (Throwable t) {
			_logger.error("Error loading ideainstance with code {}", code,  t);
			throw new RuntimeException("XXX", t);
			//processDaoException(t, "Error loading ideainstance with code " + code, "loadIdeaInstanceGroups");
		} finally {
			closeDaoResources(res, stat, null);
		}
		return groups;
	}

	private List<Integer> loadIdeaInstanceIdeas(String code, Collection<Integer> ideaStatus, Connection conn) {
		List<Integer> ideaidList = new ArrayList<Integer>();

		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			String query = LOAD_IDEAINSTANCE_IDEAS;
			if (null != ideaStatus && !ideaStatus.isEmpty()) {
				query = query + " AND status IN (" + StringUtils.repeat("?", ", ", ideaStatus.size()) + ") ";
			}
			stat = conn.prepareStatement(query);
			int index = 1;
			stat.setString(index++, code);
			if (null != ideaStatus && !ideaStatus.isEmpty()) {
				Iterator<Integer> it = ideaStatus.iterator();
				while (it.hasNext()) {
					stat.setInt(index++, it.next());
				}
			}
			res = stat.executeQuery();
			while (res.next()) {
				ideaidList.add(res.getInt("id"));
			}
		} catch (Throwable t) {
			_logger.error("Error loading ideainstance related ideas {}", code,  t);
			throw new RuntimeException("Error loading ideainstance related ideas", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return ideaidList;
	}

	private void insertIdeaInstanceGroups(String code, List<String> groups, Connection conn) {
		try {
			this.removeIdeaInstanceGroups(code, conn);
			this.addIdeaInstanceGroups(code, groups, conn);
		} catch (Throwable t) {
			_logger.error("Error adding ideainstance groups",  t);
			throw new RuntimeException("Error adding ideainstance groups", t);
		} finally{
			this.closeDaoResources(null, null, null);
		}
	}

	private void addIdeaInstanceGroups(String code, List<String> groups, Connection conn) {
		if (null == groups || groups.isEmpty()) return;
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_IDEAINSTANCE_GROUP);
			Iterator<String> groupIter = groups.iterator();

			while (groupIter.hasNext()) {
				String groupName = groupIter.next();
				int i = 1;
				stat.setString(i++, code);
				stat.setString(i++, groupName);
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (Throwable t) {
			_logger.error("Error creating ideainstance-group relation for {}", code,  t);
			throw new RuntimeException("Error creating ideainstance-group relation for " + code, t);
		} finally{
			this.closeDaoResources(null, stat, null);
		}
	}

	private void removeIdeaInstanceGroups(String code, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(REMOVE_IDEAINSTANCE_GROUP);
			int index = 1;
			stat.setString(index++, code);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting ideainstance-group relation for {}", code,  t);
			throw new RuntimeException("Error deleting ideainstance-group relation", t);
		} finally{
			this.closeDaoResources(null, stat, null);
		}
	}

	protected IIdeaDAO getIdeaDAO() {
		return _ideaDAO;
	}
	public void setIdeaDAO(IIdeaDAO ideaDAO) {
		this._ideaDAO = ideaDAO;
	}

	private IIdeaDAO _ideaDAO;

	private static final String ADD_IDEAINSTANCE = "INSERT INTO jpcollaboration_ideainstance (code, createdat ) VALUES (?, ? )";

	private static final String UPDATE_IDEAINSTANCE = "UPDATE jpcollaboration_ideainstance SET createdat=? WHERE code = ?";

	private static final String DELETE_IDEAINSTANCE = "DELETE FROM jpcollaboration_ideainstance WHERE code = ?";

	private static final String LOAD_IDEAINSTANCE = "SELECT code, createdat  FROM jpcollaboration_ideainstance WHERE code = ?";

	private static final String LOAD_IDEAINSTANCES_ID  = "SELECT code FROM jpcollaboration_ideainstance order by createdat";

	private static final String SEARCH_IDEAINSTANCES_ID  = "SELECT i.code FROM jpcollaboration_ideainstance i #JOIN_GROUP# #GROUP_CODES# #CODE_NAME# ORDER BY createdat";

	private static final String REMOVE_IDEAINSTANCE_GROUP = "delete FROM jpcollaboration_ideainstance_group where code = ?";
	private static final String ADD_IDEAINSTANCE_GROUP = "INSERT INTO jpcollaboration_ideainstance_group (code, groupname) values (?, ?)";
	private static final String LOAD_IDEAINSTANCE_GROUPS = "SELECT groupname FROM jpcollaboration_ideainstance_group where code =?";
	private static final String LOAD_IDEAINSTANCE_IDEAS = "SELECT id FROM jpcollaboration_idea where instancecode =?";

}
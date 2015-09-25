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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;

public class IdeaCommentDAO extends AbstractDAO implements IIdeaCommentDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaCommentDAO.class);

	@Override
	public void insertComment(IIdeaComment ideaComment) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(INSERT_COMMENT);
			this.buildInsertStat(stat, ideaComment);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding IdeaComment",  t);
			throw new RuntimeException("Error adding IdeaComment", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void updateComment(IIdeaComment ideaComment) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_COMMENT);
			this.buildUpdateStat(stat, ideaComment);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding IdeaComment",  t);
			throw new RuntimeException("Error adding IdeaComment", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	private void buildInsertStat(PreparedStatement stat, IIdeaComment ideaComment) throws Throwable {
		int index = 1;
		try {
			//id, ideaid, creationdate, commenttext, status, username,
			stat.setInt(index++, ideaComment.getId());
			stat.setString(index++, ideaComment.getIdeaId());
			stat.setTimestamp(index++, new Timestamp(ideaComment.getCreationDate().getTime()));
			stat.setString(index++, ideaComment.getComment());
			stat.setInt(index++, ideaComment.getStatus());
			stat.setString(index++, ideaComment.getUsername());
		} catch (Throwable t) {
			_logger.error("buildInsertStat",  t);
			throw t;
		}
	}

	protected void buildUpdateStat(PreparedStatement stat, IIdeaComment ideaComment) throws Throwable {
		int index = 1;
		try {
			//commenttext = ?, status = ? where id = ?
			stat.setString(index++, ideaComment.getComment());
			stat.setInt(index++, ideaComment.getStatus());
			stat.setInt(index++, ideaComment.getId());
		} catch (Throwable t) {
			_logger.error("error in buildUpdateStat", t);
			throw t;
		}
	}

	@Override
	public IIdeaComment loadComment(int id) {
		IIdeaComment ideaComment = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_COMMENT);
			stat.setInt(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				ideaComment = this.buildIdeaCommentFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Comment {}", id, t);
			throw new RuntimeException("Error loading Comment", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return ideaComment;
	}

	protected IIdeaComment buildIdeaCommentFromRes(ResultSet res) throws SQLException {
		IIdeaComment ideaComment = new IdeaComment();
		//id, ideaid, creationdate, commenttext, status, username
		ideaComment.setComment(res.getString("commenttext"));
		ideaComment.setCreationDate(new Date(res.getTimestamp("creationdate").getTime()));
		ideaComment.setId(res.getInt("id"));
		ideaComment.setIdeaId(res.getString("ideaid"));
		ideaComment.setStatus(res.getInt("status"));
		ideaComment.setUsername(res.getString("username"));
		return ideaComment;
	}

	@Override
	public void removeComment(int id) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_COMMENT);
			stat.setInt(1, id);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting IdeaComment {}", id,  t);
			throw new RuntimeException("Error deleting IdeaComment", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void removeComments(String ideaId, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_COMMENTS);
			stat.setString(1, ideaId);
			stat.executeUpdate();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting IdeaComments",  t);
			throw new RuntimeException("Error deleting IdeaComments", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	@Override
	public List<Integer> searchComments(Integer status, String commentText, String ideaId) {
		List<Integer> list = new ArrayList<Integer>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = BASE_SEARCH_COMMENT;
			StringBuffer whereBuffer = new StringBuffer();
			StringBuffer sortBuffer = new StringBuffer();

			boolean first = true;
			if (null != status) {
				first = this.appendSqlConjunction(whereBuffer, first);
				whereBuffer.append(" status = ? ");
			}
			if (null != commentText && commentText.trim().length() > 0) {
				first = this.appendSqlConjunction(whereBuffer, first);
				whereBuffer.append(" commenttext LIKE ? ");
			}
			if (null != ideaId && ideaId.trim().length() > 0) {
				first = this.appendSqlConjunction(whereBuffer, first);
				whereBuffer.append(" ideaid = ? ");
			}

			sortBuffer.append(" creationdate DESC ");

			query = query.replaceAll("#FILTERS#", whereBuffer.toString());
			query = query.replaceAll("#SORTING#", sortBuffer.toString());
			stat = conn.prepareStatement(query);
			int index = 1;
			if (null != status) {
				stat.setInt(index++, status);
			}
			if (null != commentText && commentText.trim().length() > 0) {
				stat.setString(index++, "%" + commentText + "%");
			}
			if (null != ideaId && ideaId.trim().length() > 0) {
				stat.setString(index++, ideaId);
			}
			res = stat.executeQuery();
			while (res.next()) {
				list.add(res.getInt(1));
			}
		} catch (Throwable t) {
			_logger.error("Error searching comment",  t);
			throw new RuntimeException("Error searching comment", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return list;
	}

	protected boolean appendSqlConjunction(StringBuffer filterBlock, Boolean first) {
		if (first == true) {
			filterBlock.append( " WHERE ");
			first = false;
		} else {
			filterBlock.append( " AND ");
		}
		return first;
	}

	@Override
	public int getActiveComments(Collection<String> instances, Connection conn) {
		int comments = 0;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			String q = LOAD_ACTIVE_COMMENTS_COUNT;
			if (null != instances && !instances.isEmpty()) {
				StringBuffer sbBuffer = new StringBuffer();
				sbBuffer.append(" AND ");
				sbBuffer.append("(");
				boolean first = true;
				for (int i = 0; i < instances.size(); i++) {
					if (!first) sbBuffer.append(" OR ");
					sbBuffer.append(" i.code=? ");
					first = false;
				}
				sbBuffer.append(")");
				q = q.replace("#INSTANCES#", sbBuffer.toString());
			} else {
				q = q.replace("#INSTANCES#", " ");
			}

			stat = conn.prepareStatement(q);
			int index = 1;
			stat.setInt(index++, IIdea.STATUS_APPROVED);
			stat.setInt(index++, IIdea.STATUS_APPROVED);
			if (null != instances && !instances.isEmpty()) {
				Iterator<String> it = instances.iterator();
				while (it.hasNext()) {
					stat.setString(index++, it.next());
				}
			}
			res = stat.executeQuery();
			if (res.next()) {
				comments = res.getInt(1);
			}
		} catch (Throwable t) {
			_logger.error("Error loading ActiveComments",  t);
			throw new RuntimeException("Error loading ActiveComments", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return comments;
	}

	protected static final String INSERT_COMMENT = "INSERT INTO jpcollaboration_idea_comments(" +
	"id, ideaid, creationdate, commenttext, status, username) VALUES (?, ?, ?, ?, ?, ?)";

	protected static final String UPDATE_COMMENT = "UPDATE jpcollaboration_idea_comments " +
	"set commenttext = ?, status = ? where id = ?";

	protected static final String LOAD_COMMENT = "SELECT " +
	"id, ideaid, creationdate, commenttext, status, username FROM jpcollaboration_idea_comments WHERE id=?";

	protected static final String DELETE_COMMENT = "DELETE FROM jpcollaboration_idea_comments " +
	"WHERE id=?";

	protected static final String DELETE_COMMENTS = "DELETE FROM jpcollaboration_idea_comments " +
	"WHERE ideaid=?";

	protected static final String BASE_SEARCH_COMMENT = "SELECT id FROM jpcollaboration_idea_comments #FILTERS# ORDER BY #SORTING#";

	private static final String LOAD_ACTIVE_COMMENTS_COUNT = "SELECT count(c.id)  AS comments FROM jpcollaboration_ideainstance i " +
			"INNER JOIN jpcollaboration_idea idea ON i.code = idea.instancecode " +
			"INNER JOIN jpcollaboration_idea_comments c ON idea.id = c.ideaid " +
			"WHERE idea.status=? AND c.status=? #INSTANCES#";

}

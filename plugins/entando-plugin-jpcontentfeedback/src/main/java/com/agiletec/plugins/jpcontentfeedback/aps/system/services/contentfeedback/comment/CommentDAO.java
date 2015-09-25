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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.ICommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingDAO;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.RatingDAO;
/**
 *
 * @author D.Cherchi
 *
 */
public class CommentDAO  extends AbstractDAO implements ICommentDAO {

	private static final Logger _logger = LoggerFactory.getLogger(CommentDAO.class);
	
	@Override
	public void addComment(IComment comment) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_COMMENT);
			stat.setInt(1, comment.getId());
			stat.setString(2, comment.getContentId());
			stat.setTimestamp(3, new Timestamp(new Date().getTime()));
			stat.setString(4, comment.getComment());
			stat.setInt(5, comment.getStatus());
			stat.setString(6, comment.getUsername());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding a comment",  t);
			throw new RuntimeException("Error adding a comment", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}

	@Override
	public void deleteComment(int commentId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			((RatingDAO)this.getRatingDAO()).removeRatingInTransaction(conn, commentId);
			stat = conn.prepareStatement(DELETE_COMMENT);
			stat.setInt(1, commentId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting a comment",  t);
			throw new RuntimeException("Error deleting a comment", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public List<String> searchCommentsId(ICommentSearchBean searchBean) {
		List<String> comments = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = this.createQueryString(searchBean);
			stat = conn.prepareStatement(query);
			this.buildStatement(searchBean, stat);
			res = stat.executeQuery();
			while (res.next()) {
				String commentId = res.getString(1);
				comments.add(commentId);
			}
		} catch (Throwable t) {
			_logger.error("Error loading comments list",  t);
			throw new RuntimeException("Error loading comments list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return comments;
	}




	protected int buildStatement(ICommentSearchBean searchBean, PreparedStatement stat) throws SQLException {
		int pos = 1;
		if (searchBean!=null) {
			String username = searchBean.getUsername();
			if (username!=null && username.length()>0) {
				stat.setString(pos++, username);
			}
			int status = searchBean.getStatus();
			if (status!=0) {
				stat.setInt(pos++, status);
			}
			String comment = searchBean.getComment();
			if (comment!=null && comment.length()>0) {
				stat.setString(pos++, this.searchLikeString(comment));
			}

			Date fromDate = searchBean.getCreationFROMDate();
			if (fromDate!=null) {
				Calendar data = new GregorianCalendar();
				data.setTime(fromDate);
				data.set(GregorianCalendar.MILLISECOND, 0);
				data.set(GregorianCalendar.SECOND, 0);
				data.set(GregorianCalendar.MINUTE, 0);
				data.set(GregorianCalendar.HOUR, 0);
				stat.setTimestamp(pos++, new Timestamp(data.getTimeInMillis()));
			}
			Date toDate = searchBean.getCreationTODate();
			if (toDate!=null) {
				Calendar data = new GregorianCalendar();
				data.setTime(toDate);
				data.set(GregorianCalendar.MILLISECOND, 999);
				data.set(GregorianCalendar.SECOND, 59);
				data.set(GregorianCalendar.MINUTE, 59);
				data.set(GregorianCalendar.HOUR_OF_DAY, 23);
				stat.setTimestamp(pos++, new Timestamp(data.getTimeInMillis()));
			}

			String contentId = searchBean.getContentId();
			if (contentId!=null && contentId.length()>0) {
				stat.setString(pos++, contentId);
			}
			
		}
		return pos;
	}

	@Override
	public void updateStatus(int id, int status) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_STATUS_COMMENT);
			stat.setInt(1, status);
			stat.setInt(2, id);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating comment status to {} for comment {} ", status, id,  t);
			throw new RuntimeException("Error updating comment status", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}

	@Override
	public Comment getComment(int id) {
		Comment comment = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res =null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(LOAD_COMMENT);
			stat.setInt(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				comment=this.popualteComment(res);
			}
		} catch (Throwable t) {
			_logger.error("Error load comment {}", id, t);
			throw new RuntimeException("Error load comment", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return comment;
	}

	private String searchLikeString(String searchValue) {
		String result = "";
		searchValue.trim();
		String[] titleSplit = searchValue.split(" ");
		for ( int i = 0;  i < titleSplit.length; i++) {
			if ( titleSplit[i].length() > 0 ) {
				result += "%" + titleSplit[i];
			}
		}
		return result + "%" ;
	}

	private String createQueryString(ICommentSearchBean searchBean){
		StringBuffer query = new StringBuffer(SELECT_COMMENTS_CODES);
		if (searchBean!=null) {
			boolean appendWhere = true;
			String username = searchBean.getUsername();
			if (username!=null && username.length()>0) {
				query.append(APPEND_WHERE);
				query.append(APPEND_AUTHOR_CLAUSE);
				appendWhere = false;
			}

			int status = searchBean.getStatus();
			if (status!=0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_STATUS_CLAUSE);
				appendWhere = false;
			}

			String comment = searchBean.getComment();
			if (comment!=null && comment.length()>0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_COMMENT_CLAUSE);
				appendWhere = false;
			}

			Date fromDate = searchBean.getCreationFROMDate();
			if (fromDate!=null) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_DATE_FROM_CLAUSE);
				appendWhere = false;
			}

			Date toDate = searchBean.getCreationTODate();
			if (toDate!=null) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_DATE_TO_CLAUSE);
				appendWhere = false;
			}

			String contentID = searchBean.getContentId();
			if (contentID!=null && contentID.length()>0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_CONTENT_ID_CLAUSE);
				appendWhere = false;
			}
			
			query.append(APPEND_ORDERBY_CLAUSE);
			String sort = searchBean.getSort();
			if (StringUtils.isBlank(sort) || !sort.equalsIgnoreCase(ICommentSearchBean.SORT_DESC)) {
				query.append(ICommentSearchBean.SORT_ASC);
			} else {
				query.append(ICommentSearchBean.SORT_DESC);
			}
		}
		return query.toString();
	}

	/**
	 * Dato il resultSet, ne estrae i dati e popola l'oggetto comment
	 * @param res
	 * @return
	 * @throws ApsSystemException
	 */
	private Comment popualteComment(ResultSet res) throws ApsSystemException {
		Comment comment = new Comment();
		try {
			comment.setId(res.getInt("id"));
			comment.setContentId(res.getString("contentid"));
			Timestamp creationDate = res.getTimestamp("creationDate");
			if (null != creationDate) {
				comment.setCreationDate(new Date(creationDate.getTime()));
			}
			comment.setComment(res.getString("usercomment"));
			comment.setStatus(res.getInt("status"));
			comment.setUsername(res.getString("username"));
		} catch (Throwable t) {
			throw new ApsSystemException("Errore loading comment", t);
		}
		return comment;
	}

	public void setRatingDAO(IRatingDAO ratingDAO) {
		this._ratingDAO = ratingDAO;
	}

	public IRatingDAO getRatingDAO() {
		return _ratingDAO;
	}

	private static final String ADD_COMMENT =
			"INSERT INTO jpcontentfeedback_comments  (id, contentid, creationdate,  usercomment, status, username) VALUES (?, ?, ?, ?, ?, ?) ";

	private static final String DELETE_COMMENT=
			"DELETE FROM jpcontentfeedback_comments WHERE id = ?";

	private static final String UPDATE_STATUS_COMMENT=
			"UPDATE jpcontentfeedback_comments SET status=? WHERE id=?";

	private final String SELECT_COMMENTS_CODES =
			"SELECT id FROM jpcontentfeedback_comments ";

	private final String LOAD_COMMENT =
			"SELECT * FROM jpcontentfeedback_comments WHERE id = ?";

	private final String APPEND_WHERE = "WHERE ";
	private final String APPEND_AND = "AND ";
	private final String APPEND_AUTHOR_CLAUSE  = "username = ? ";
	private final String APPEND_STATUS_CLAUSE  = "status = ? ";
	private final String APPEND_CONTENT_ID_CLAUSE  = "contentid = ? ";
	private final String APPEND_DATE_FROM_CLAUSE  = "creationDate >= ? ";
	private final String APPEND_DATE_TO_CLAUSE  = "creationDate <= ? ";
	private final String APPEND_COMMENT_CLAUSE  = "usercomment LIKE ? ";
	private final String APPEND_ORDERBY_CLAUSE = " ORDER BY creationdate ";

	private IRatingDAO _ratingDAO;

}

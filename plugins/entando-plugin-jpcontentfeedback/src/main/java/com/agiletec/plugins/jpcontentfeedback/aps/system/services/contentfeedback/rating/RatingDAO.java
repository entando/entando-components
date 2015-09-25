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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRatingSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.Rating;

public class RatingDAO  extends AbstractDAO implements IRatingDAO{

	private static final Logger _logger = LoggerFactory.getLogger(RatingDAO.class);
	
	@Override
	public synchronized void addRating(IRating rating) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_RATING);
			stat.setInt(1, rating.getId());
			stat.setString(2, rating.getContentId());
			if (rating.getCommentId() == 0){
				stat.setNull(3, Types.INTEGER);
			}else{
				stat.setInt(3, rating.getCommentId());
			}
			stat.setInt(4, rating.getVoters());
			stat.setInt(5, rating.getSumvote());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding a rating",  t);
			throw new RuntimeException("Error adding a rating", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void updateRating(IRating rating) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_RATING);
			stat.setInt(1, rating.getVoters());
			stat.setInt(2, rating.getSumvote());
			stat.setInt(3, rating.getId());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating a rating", t);
			throw new RuntimeException("Error updating a rating", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}


	@Override
	public Rating getRating(IRatingSearchBean searchBean){
		Rating rating = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = this.createQueryString(searchBean);
			stat = conn.prepareStatement(query);
			this.buildStatement(searchBean, stat);
			res = stat.executeQuery();
			if (res.next()) {
				rating = new Rating();
				rating.setId(res.getInt("id"));
				rating.setCommentId(res.getInt("commentid"));
				rating.setContentId(res.getString("contentid"));
				rating.setVote(res.getInt("voters"), res.getInt("sumvote"));
			}
		} catch (Throwable t) {
			_logger.error("Error while search rating",  t);
			throw new RuntimeException("Error while search rating", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return rating;
	}

	@Override
	public void removeRating(int commentId) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			String query = REMOVE_RATING_BY_COMMENT_ID;
			stat = conn.prepareStatement(query);
			stat.setInt(1, commentId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing rating for comment {}", commentId, t);
			throw new RuntimeException("Error while remove rating", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}

	@Override
	public void removeContentRating(String contentId) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			String query = DELETE_RATING_BY_CONTENT_ID;
			stat = conn.prepareStatement(query);
			stat.setString(1, contentId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing rating for comment {}", contentId,  t);
			throw new RuntimeException("Errore while remove rating", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	public void removeRatingInTransaction(Connection conn, int commentId) {
		PreparedStatement stat = null;
		try {
			String query = REMOVE_RATING_BY_COMMENT_ID;
			stat = conn.prepareStatement(query);
			stat.setInt(1, commentId);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error removing rating for comment {}", commentId, t);
			throw new RuntimeException("Error removing rating for comment", t);
		} finally {
			closeDaoResources(null, stat, null);
		}

	}

	protected int buildStatement(IRatingSearchBean searchBean, PreparedStatement stat) throws SQLException {
		int pos = 1;
		if (searchBean!=null) {
			int  id = searchBean.getId();
			int commentId = searchBean.getCommentId();
			String contentId = searchBean.getContentId();

			if (id!=0) {
				stat.setInt(1, id);
			} else if (contentId!=null && contentId.length()>0) {
				stat.setString(1, contentId);
			} else if (commentId!=0){
				stat.setInt(1, commentId);
			}
		}
		return pos;
	}
	private String createQueryString(IRatingSearchBean searchBean){
		StringBuffer query = new StringBuffer(LOAD_RATING);
		if (searchBean!=null) {
			int  id = searchBean.getId();
			int commentId = searchBean.getCommentId();
			String contentId = searchBean.getContentId();
			if (id!=0) {
				query.append(APPEND_WHERE);
				query.append(APPEND_RATING_ID_CLAUSE);
			} else if (contentId!=null && contentId.length()>0) {
				query.append(APPEND_WHERE);
				query.append(APPEND_CONTENT_ID_CLAUSE);
			} else if (commentId!=0){
				query.append(APPEND_WHERE);
				query.append(APPEND_COMMENT_ID_CLAUSE);
			}
		}
		return query.toString();
	}

	private static final String ADD_RATING =
		"INSERT INTO jpcontentfeedback_rating (id, contentid, commentid, voters, sumvote) VALUES (?, ?, ?, ?, ?) ";

	private static final String UPDATE_RATING =
		"UPDATE jpcontentfeedback_rating SET voters=?, sumvote=? WHERE id=?";

	private static final String DELETE_RATING_BY_CONTENT_ID=
		"DELETE FROM jpcontentfeedback_rating WHERE contentid = ?";

	private static final String REMOVE_RATING_BY_COMMENT_ID=
		"DELETE FROM jpcontentfeedback_rating WHERE commentid = ?";

	private final String LOAD_RATING =
		"SELECT * FROM jpcontentfeedback_rating ";

	private final String APPEND_WHERE = "WHERE ";
	private final String APPEND_CONTENT_ID_CLAUSE  = "contentid = ? ";
	private final String APPEND_COMMENT_ID_CLAUSE  = "commentid = ? ";
	private final String APPEND_RATING_ID_CLAUSE  = "id = ? ";



}
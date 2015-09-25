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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.comment.IIdeaCommentDAO;

public class IdeaDAO extends AbstractDAO implements IIdeaDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaDAO.class);

	@Override
	public void insertIdea(IIdea idea) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(INSERT_IDEA);
			int index = 1;
			stat.setString(index++, idea.getId());
			stat.setString(index++, idea.getTitle());
			stat.setString(index++, idea.getDescr());
			stat.setTimestamp(index++, new Timestamp(idea.getPubDate().getTime()));
			stat.setString(index++, idea.getUsername());
			stat.setInt(index++, idea.getStatus());
			stat.setInt(index++, idea.getVotePositive());
			stat.setInt(index++, idea.getVoteNegative());
			stat.setString(index++, idea.getInstanceCode());
			stat.executeUpdate();

			this.updateTags(idea, conn);

			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error adding Idea",  t);
			throw new RuntimeException("Error adding Idea", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	public void removeIdeas(List<String> codes, Connection conn) {
		PreparedStatement stat = null;
		try {
			if (null != codes && !codes.isEmpty()) {
				for (int i = 0; i < codes.size(); i++) {
					this.removeIdeaBlock(codes.get(i), conn);
				}
			}
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing ideas",  t);
			throw new RuntimeException("Error removing ideas", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeIdea(String id) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.getIdeaCommentDAO().removeComments(id, conn);
			this.removeComments(id, conn);
			this.removeTags(id, conn);
			this.removeIdea(id, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing Idea", t);
			throw new RuntimeException("Error removing Idea", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}


	public void removeIdeaBlock(String id, Connection conn) {
		PreparedStatement stat = null;
		try {
			this.getIdeaCommentDAO().removeComments(id, conn);
			this.removeComments(id, conn);
			this.removeTags(id, conn);
			this.removeIdea(id, conn);
		} catch (Throwable t) {
			_logger.error("Error removing Idea",  t);
			throw new RuntimeException("Error removing Idea", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}


	private void removeComments(String id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_IDEA_COMMENTS);
			stat.setString(1, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error removing comments", t);
			throw new RuntimeException("Error removing comments", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	protected void removeTags(String id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_IDEA_TAGS);
			stat.setString(1, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error removing tags for {}", id,  t);
			throw new RuntimeException("Error removing tags", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	private void removeIdea(String id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_IDEA);
			stat.setString(1, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error removing idea with id {}", id,  t);
			throw new RuntimeException("Error removing idea", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	@Override
	public IIdea loadIdea(String id) {
		IIdea idea = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_IDEA);
			stat.setString(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				List<String> tags = this.loadTags(id, conn);
				Map<Integer, List<Integer>> comments =  this.loadComments(id, conn);
				idea = this.buildIdeaFromRes(res);
				((Idea) idea).setTags(tags);
				((Idea) idea).setComments(comments);
			}
		} catch (Throwable t) {
			_logger.error("Error loading idea {}", id,  t);
			throw new RuntimeException("Error loading idea", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return idea;
	}

	protected IIdea buildIdeaFromRes(ResultSet res) throws SQLException {
		Idea idea = null;
		try {
			idea = new Idea();
			idea.setId(res.getString("id"));
			idea.setTitle(res.getString("title"));
			idea.setDescr(res.getString("descr"));
			idea.setPubDate(new Date(res.getTimestamp("pubdate").getTime()));
			idea.setUsername(res.getString("username"));
			idea.setStatus(res.getInt("status"));
			idea.setVoteNegative(res.getInt("votenegative"));
			idea.setVotePositive(res.getInt("votepositive"));
			idea.setInstanceCode(res.getString("instancecode"));
		} catch (SQLException e) {
			_logger.error("error in buildIdeaFromRes", e);
			throw e;
		}
		return idea;
	}

	private List<String> loadTags(String id, Connection conn) {
		List<String> tags = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_IDEA_TAGS);
			stat.setString(1, id);
			res = stat.executeQuery();
			while (res.next()) {
				String tag = res.getString("catcode");
				Category cat = this.getCategoryManager().getCategory(tag);
				if (null != cat) {
					tags.add(tag);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading tags for idea {}", id,  t);
			throw new RuntimeException("Error loading tags for idea", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return tags;
	}

	protected Map<Integer, List<Integer>> loadComments(String id, Connection conn) {
		Map<Integer, List<Integer>> commentsId = new HashMap<Integer, List<Integer>>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_IDEA_COMMENTS);
			stat.setString(1, id);
			res = stat.executeQuery();
			while (res.next()) {
				int commentId = res.getInt("id");
				int status = res.getInt("status");
				if (!commentsId.containsKey(status)) {
					commentsId.put(status, new ArrayList<Integer>());
				}
				commentsId.get(status).add(commentId);
			}
		} catch (Throwable t) {
			_logger.error("Error loading commentsId for idea {}", id, t);
			throw new RuntimeException("Error loading commentsId", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return commentsId;
	}

	@Override
	public void updateIdea(IIdea idea) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_IDEA);
			int index = 1;
			stat.setString(index++, idea.getTitle());
			stat.setString(index++, idea.getDescr());
			stat.setInt(index++, idea.getStatus());
			stat.setInt(index++, idea.getVotePositive());
			stat.setInt(index++, idea.getVoteNegative());
			stat.setString(index++, idea.getInstanceCode());
			stat.setString(index++, idea.getId());
			stat.executeUpdate();

			this.updateTags(idea, conn);

			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating Idea {}", idea.getId(), t);
			throw new RuntimeException("Error updating Idea", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	protected void updateTags(IIdea idea, Connection conn) {
		PreparedStatement stat = null;
		try {
			this.removeTags(idea.getId(), conn);

			stat = conn.prepareStatement(INSERT_IDEA_TAGS);
			this.addTagsRelationsRecord(idea, stat);
			stat.executeBatch();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error updating Idea tags for {}", idea.getId(), t);
			throw new RuntimeException("Error updating Idea tags", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	private void addTagsRelationsRecord(IIdea idea, PreparedStatement stat) throws ApsSystemException {
		if (idea.getTags().size() > 0) {
			try {
				Iterator<String> codeIter = idea.getTags().iterator();

				while (codeIter.hasNext()) {
					String code = codeIter.next();
					int i = 1;
					stat.setString(i++, idea.getId());
					stat.setString(i++, code);
					stat.addBatch();
					stat.clearParameters();
				}
			} catch (SQLException e) {
				_logger.error("Errore in aggiunta record tabella collaboration_idea_tags {}", idea.getId(),  e);
			throw new RuntimeException("Errore in aggiunta record tabella collaboration_idea_tags", e);
			}
		}
	}

	@Override
	public List<String> getCategoryUtilizers(String categoryCode) {
		List<String> list = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_IDEAS_BY_TAG);
			stat.setString(1, categoryCode);
			res = stat.executeQuery();
			while (res.next()) {
				list.add(res.getString("ideaid"));
			}
		} catch (Throwable t) {
			_logger.error("Error loading list idea id for category {}", categoryCode, t);
			throw new RuntimeException("Error loading list idea id for category", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return list;
	}

	@Override
	public List<String> searchIdea(String instancecode, Integer status, String text, String category, Integer order) {
		List<String> list = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = BASE_SEARCH_IDEA;
			StringBuffer whereBuffer = new StringBuffer();
			StringBuffer sortBuffer = new StringBuffer();

			boolean first = true;
			if (StringUtils.isNotBlank(instancecode)) {
				first = this.appendSqlConjunction(whereBuffer, first);
				whereBuffer.append(" i.instancecode = ? ");
			}
			if (null != status) {
				first = this.appendSqlConjunction(whereBuffer, first);
				whereBuffer.append(" i.status = ? ");
			}
			if (null != text && text.trim().length() > 0) {
				first = this.appendSqlConjunction(whereBuffer, first);
				whereBuffer.append(" (i.title LIKE ? OR i.descr LIKE ?) ");
			}
			if (null != category && category.trim().length() > 0) {
				first = this.appendSqlConjunction(whereBuffer, first);
				whereBuffer.append(" t.catcode = ? ");
			}
			//
			if (null == order || order == IIdeaDAO.SORT_LATEST) {
				sortBuffer.append(" i.pubdate DESC ");
			} else if (order == IIdeaDAO.SORT_MOST_RATED) {
				sortBuffer.append(" votes DESC ");
			}
			query = query.replaceAll("#FILTERS#", whereBuffer.toString());
			query = query.replaceAll("#SORTING#", sortBuffer.toString());
			stat = conn.prepareStatement(query);
			int index = 1;
			if (StringUtils.isNotBlank(instancecode)) {
				stat.setString(index++, instancecode);
			}
			if (null != status) {
				stat.setInt(index++, status);
			}
			if (null != text && text.trim().length() > 0) {
				stat.setString(index++, "%" + text + "%");
				stat.setString(index++, "%" + text + "%");
			}
			if (null != category && category.trim().length() > 0) {
				stat.setString(index++, category);
			}

			res = stat.executeQuery();
			while (res.next()) {
				boolean addToList = true;
				if (null != order && order == IIdeaDAO.SORT_MOST_RATED) {
					if (res.getInt("votes") == 0) {
						addToList = false;
					}
				}
				if (addToList) {
					list.add(res.getString(1));
				}
			}
		} catch (Throwable t) {			
			_logger.error("Error searching idea",  t);
			throw new RuntimeException("Error searching idea", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return list;
	}

	@Override
	public Map<String, Integer> loadIdeaTags(Integer status) {
		Map<String, Integer> list = new HashMap<String, Integer>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = BASE_SEARCH_TAGS;
			StringBuffer whereBuffer = new StringBuffer();

			boolean first = true;
			if (null != status) {
				first = this.appendSqlConjunction(whereBuffer, first);
				whereBuffer.append(" i.status = ? ");
			}
			query = query.replaceAll("#FILTERS#", whereBuffer.toString());
			stat = conn.prepareStatement(query);
			int index = 1;
			if (null != status) {
				stat.setInt(index++, status);
			}
			res = stat.executeQuery();
			while (res.next()) {
				String catCode = res.getString(1);
				Integer countIdee = res.getInt(2);
				list.put(catCode, countIdee);
			}
		} catch (Throwable t) {
			_logger.error("Error loadIdeaTags", t);
			throw new RuntimeException("Error loadIdeaTags", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return list;
	}

	@Override
	public StatisticInfoBean loadActiveIdeaStatistics(Collection<String> instances) {
		StatisticInfoBean statisticInfoBean = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			int idee = this.loadCountIdee(instances, conn);
			int votes = this.loadCountVotes(instances, conn);
			int users = this.loadCountUsers(instances, conn);
			int comments = this.getIdeaCommentDAO().getActiveComments(instances, conn);
			statisticInfoBean = new StatisticInfoBean();
			statisticInfoBean.setIdeas(idee);
			statisticInfoBean.setVotes(votes);
			statisticInfoBean.setUsers(users);
			statisticInfoBean.setComments(comments);
		} catch (Throwable t) {
			_logger.error("Error loading ActiveIdeaStatistics", t);
			throw new RuntimeException("Error loading ActiveIdeaStatistics", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return statisticInfoBean;
	}

	private Integer loadCountIdee(Collection<String> instances, Connection conn) {
		int count  = 0;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			String q = LOAD_STATISTICS_IDEE;
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
			if (null != instances && !instances.isEmpty()) {
				Iterator<String> it = instances.iterator();
				while (it.hasNext()) {
					stat.setString(index++, it.next());
				}
			}

			res = stat.executeQuery();
			if (res.next()) {
				count = res.getInt(1);
			}
		} catch (Throwable t) {
			_logger.error("Error loading loadCountIdee", t);
			throw new RuntimeException("Error loading loadCountIdee", t);
			//processDaoException(t, "Error loading loadCountIdee", "loadCountIdee");
		} finally {
			closeDaoResources(res, stat, null);
		}
		return count;
	}

	private Integer loadCountVotes(Collection<String> instances, Connection conn) {
		int count  = 0;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			String q = LOAD_STATISTICS_VOTES;
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
			if (null != instances && !instances.isEmpty()) {
				Iterator<String> it = instances.iterator();
				while (it.hasNext()) {
					stat.setString(index++, it.next());
				}
			}

			res = stat.executeQuery();
			if (res.next()) {
				count = res.getInt(1);
			}
		} catch (Throwable t) {
			_logger.error("Error loading loadCountVotes", t);
			throw new RuntimeException("Error loading loadCountVotes", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return count;
	}

	private Integer loadCountUsers(Collection<String> instances, Connection conn) {
		int count  = 0;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {

			Set<String> usersIdea = this.loadUsersForIdeaInstance(instances, conn);
			Set<String> usersIdeaComments = this.loadUsersForIdeaInstanceComments(instances, conn);

			usersIdea.addAll(usersIdeaComments);

			count = usersIdea.size();

		} catch (Throwable t) {
			_logger.error("Error loading loadCountUsers", t);
			throw new RuntimeException("Error loading loadCountUsers", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return count;
	}

	private Set<String> loadUsersForIdeaInstance(Collection<String> instances, Connection conn) {
		Set<String> users = new HashSet<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			String q = LOAD_USERS_FOR_IDEA_INSTANCE;
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
			if (null != instances && !instances.isEmpty()) {
				Iterator<String> it = instances.iterator();
				while (it.hasNext()) {
					stat.setString(index++, it.next());
				}
			}

			res = stat.executeQuery();
			while (res.next()) {
				users.add(res.getString("username"));
			}
		} catch (Throwable t) {
			_logger.error("Error loading users for instance {}", instances,  t);
			throw new RuntimeException("Error loading users for instance", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return users;
	}

	private Set<String> loadUsersForIdeaInstanceComments(Collection<String> instances, Connection conn) {
		Set<String> users = new HashSet<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			String q = LOAD_USERS_FOR_IDEA_INSTANCE_COMMENTS;
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
			if (null != instances && !instances.isEmpty()) {
				Iterator<String> it = instances.iterator();
				while (it.hasNext()) {
					stat.setString(index++, it.next());
				}
			}

			res = stat.executeQuery();
			while (res.next()) {
				users.add(res.getString("username"));
			}
		} catch (Throwable t) {
			_logger.error("Error loading users in comments for instance",  t);
			throw new RuntimeException("Error loading users in comments for instance", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return users;
	}

	private boolean appendSqlConjunction(StringBuffer filterBlock, Boolean first) {
		if (first == true) {
			filterBlock.append( " WHERE ");
			first = false;
		} else {
			filterBlock.append( " AND ");
		}
		return first;
	}

	protected static final String INSERT_IDEA = "INSERT INTO jpcollaboration_idea" +
			"(id, title, descr, pubdate, username, status, votepositive, votenegative, instancecode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


	private static final String INSERT_IDEA_TAGS = "INSERT INTO jpcollaboration_idea_tags (ideaid,catcode) VALUES (?, ?)";

	private static final String DELETE_IDEA = "DELETE FROM jpcollaboration_idea WHERE id = ?";

	private static final String DELETE_IDEA_COMMENTS = "DELETE FROM jpcollaboration_idea_comments WHERE ideaid = ?";

	private static final String DELETE_IDEA_TAGS = "DELETE FROM jpcollaboration_idea_tags WHERE ideaid=?";

	protected static final String LOAD_IDEA = "SELECT id, title, descr, pubdate, username, status, votepositive, votenegative, instancecode " +
			"FROM jpcollaboration_idea where id=?";

	private static final String LOAD_IDEA_TAGS = "SELECT ideaid, catcode FROM jpcollaboration_idea_tags WHERE ideaid=?";

	private static final String LOAD_IDEAS_BY_TAG = "SELECT distinct(ideaid) FROM jpcollaboration_idea_tags WHERE catcode=?";

	private static final String LOAD_IDEA_COMMENTS = "SELECT id, status FROM jpcollaboration_idea_comments WHERE ideaid=? ORDER BY creationdate DESC";

	private static final String UPDATE_IDEA = "UPDATE jpcollaboration_idea  SET title=?, descr=?,  status=?, votepositive=?, votenegative=?, instancecode=? WHERE id=?";

	private final String BASE_SEARCH_IDEA = "SELECT distinct(i.id), i.pubdate, i.votepositive as votes " +
			"FROM jpcollaboration_idea i " +
			"LEFT JOIN jpcollaboration_idea_tags t ON t.ideaid = i.id " +
			"LEFT JOIN jpcollaboration_idea_comments c ON c.ideaid = i.id " +
			"#FILTERS# " +
			"GROUP BY i.id, i.pubdate, i.votepositive " +
			"ORDER BY #SORTING#";

	private static final String BASE_SEARCH_TAGS = "SELECT DISTINCT(catcode), COUNT(i.id) AS countidea FROM jpcollaboration_idea_tags t " +
			"INNER JOIN jpcollaboration_idea i ON i.id = t.ideaid #FILTERS# GROUP BY catcode";

	private static final String LOAD_STATISTICS_IDEE = "SELECT count(idea.id) FROM jpcollaboration_ideainstance i " +
			"INNER JOIN jpcollaboration_idea idea ON i.code = idea.instancecode " +
			"where idea.status=? #INSTANCES#";

	private static final String LOAD_STATISTICS_VOTES = "SELECT SUM(idea.votepositive + idea.votenegative) FROM jpcollaboration_ideainstance i " +
			"INNER JOIN jpcollaboration_idea idea ON i.code=idea.instancecode " +
			"where idea.status=? #INSTANCES#";

	private static final String LOAD_USERS_FOR_IDEA_INSTANCE = "SELECT distinct(idea.username) AS username FROM jpcollaboration_ideainstance i " +
			"INNER JOIN jpcollaboration_idea idea ON i.code=idea.instancecode " +
			"WHERE idea.status=? #INSTANCES#";

	private static final String LOAD_USERS_FOR_IDEA_INSTANCE_COMMENTS = "SELECT distinct(c.username) AS username FROM jpcollaboration_ideainstance i " +
			"INNER JOIN jpcollaboration_idea idea ON i.code=idea.instancecode " +
			"INNER JOIN jpcollaboration_idea_comments c ON idea.id=c.ideaid " +
			"WHERE idea.status=? #INSTANCES#";

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setIdeaCommentDAO(IIdeaCommentDAO ideaCommentDAO) {
		this._ideaCommentDAO = ideaCommentDAO;
	}
	protected IIdeaCommentDAO getIdeaCommentDAO() {
		return _ideaCommentDAO;
	}

	private ICategoryManager _categoryManager;
	private IIdeaCommentDAO _ideaCommentDAO;
}

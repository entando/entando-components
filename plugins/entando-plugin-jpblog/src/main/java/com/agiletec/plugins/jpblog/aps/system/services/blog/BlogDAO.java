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
package com.agiletec.plugins.jpblog.aps.system.services.blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.PublicContentSearcherDAO;

public class BlogDAO extends PublicContentSearcherDAO implements IBlogDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogDAO.class);
	
	/*
	@Override
	public List<BlogArchiveInfoBean> getOccurrenceByDate(String contentType, List<String> userGroupCodes) {
		List<BlogArchiveInfoBean> list = new ArrayList<BlogArchiveInfoBean>();
		Set<String> groupCodes = new HashSet<String>();
		if (null != userGroupCodes) groupCodes.addAll(userGroupCodes);
		groupCodes.add(Group.FREE_GROUP_NAME);
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatementForDate(contentType, groupCodes, conn);
			result = stat.executeQuery();
			while (result.next()) {
				BlogArchiveInfoBean bean = new BlogArchiveInfoBean();
				bean.setGroupNames(userGroupCodes);
				bean.setMonth(result.getString("mese"));
				bean.setOccurrences(result.getInt("count"));
				bean.setYear(result.getString("anno"));
				list.add(bean);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error in loading occurrences", "getOccurrences");
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return list;
	}
	
	protected PreparedStatement buildStatementForDate(String contentType, Set<String> userGroupCodes,  Connection conn) {
		List<String> contentTypeCodes = new ArrayList<String>();
		contentTypeCodes.add(contentType);
		Collection<String> groupsForSelect = this.getGroupsForSelect(userGroupCodes);
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT COUNT(distinct(contentid)) AS count, anno, mese FROM contentview WHERE contenttype = ? ");
		boolean first = true;

		if (null != groupsForSelect) {
			queryBuffer.append(" AND (");
			for (int i = 0; i < groupsForSelect.size(); i++) {
				if (!first) {
					queryBuffer.append(" OR ");
				}
				queryBuffer.append("refgroup = ?");
				first = false;
			}
			queryBuffer.append(")");
		}
		queryBuffer.append(" GROUP BY anno, mese ORDER BY anno DESC, mese DESC");
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(queryBuffer.toString());
			int index = 0;
			if (null != contentTypeCodes && !contentTypeCodes.isEmpty()) {
				for (int i=0; i<contentTypeCodes.size(); i++) {
					stat.setString(++index, contentTypeCodes.get(i));
				}
			}
			if (groupsForSelect != null) {
				Iterator<String> it = userGroupCodes.iterator();
				while (it.hasNext()) {
					stat.setString(++index, it.next());
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error in creation statement.", "buildStatement");
		}
		return stat;
	}
	//*/
	@Override
	public Map<String, Integer> getOccurrences(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> userGroupCodes) {
		Set<String> groupCodes = new HashSet<String>();
		if (null != userGroupCodes) groupCodes.addAll(userGroupCodes);
		groupCodes.add(Group.FREE_GROUP_NAME);
		Map<String, Integer> occurrences = new HashMap<String, Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatementForCategory(contentTypeCodes, facetNodeCodes, groupCodes, conn);
			result = stat.executeQuery();
			while (result.next()) {
				String facetNode = result.getString(1);
				Integer number = new Integer(result.getInt(2));
				occurrences.put(facetNode, number);
			}
		} catch (Throwable t) {
			_logger.error("Error in loading occurrences",  t);
			throw new RuntimeException("Error in loading occurrences", t);
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return occurrences;
	}
	
	protected PreparedStatement buildStatementForCategory(List<String> contentTypeCodes, List<String> facetNodeCodes, Set<String> userGroupCodes, Connection conn) {
		Collection<String> groupsForSelect = this.getGroupsForSelect(userGroupCodes);
		String query = this.createQueryStringForSelectOccurrences(contentTypeCodes, facetNodeCodes, groupsForSelect);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			if (null != contentTypeCodes && !contentTypeCodes.isEmpty()) {
				for (int i=0; i<contentTypeCodes.size(); i++) {
					stat.setString(++index, contentTypeCodes.get(i));
				}
			}
			if (groupsForSelect != null) {
				index = this.addGroupStatementBlock(groupsForSelect, index, stat);
			}
			if (facetNodeCodes != null && !facetNodeCodes.isEmpty()) {
				for (int i=0; i<facetNodeCodes.size(); i++) {
					stat.setString(++index, facetNodeCodes.get(i));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error in creation statement", t);
			throw new RuntimeException("Error in creation statement", t);
		}
		return stat;
	}
	
	private String createQueryStringForSelectOccurrences(List<String> contentTypeCodes, List<String> facetNodes,
			Collection<String> groupsForSelect) {
		StringBuffer query = new StringBuffer();
		query.append("SELECT contentrelations.refcategory, count(contentrelations.refcategory) FROM contentrelations ")
			.append("LEFT JOIN contents on contentrelations.contentid = contents.contentid ")
			.append("WHERE contentrelations.refcategory IS NOT NULL ");
		this.addCommonBlock(contentTypeCodes, facetNodes, groupsForSelect, query);
		query.append(" GROUP BY contentrelations.").append(FACET_REF_FIELDS_MARKER);
		String queryString = query.toString();
		queryString = queryString.replaceAll(FACET_REF_FIELDS_MARKER, this.getFacetRefFieldName());
		return queryString;
	}
	
	private void addCommonBlock(List<String> contentTypeCodes, List<String> facetNodes, Collection<String> groupsForSelect,	StringBuffer query) {
		boolean hasAppendWhereClause = true;
		if (contentTypeCodes != null && !contentTypeCodes.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			query.append(" ( ");
			for (int i=0; i<contentTypeCodes.size(); i++) {
				if (i>0) query.append(" OR ");
				query.append(" contents.contenttype = ? ");
			}
			query.append(" ) ");
		}
		if (groupsForSelect != null && !groupsForSelect.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			this.addGroupsQueryBlock(query, groupsForSelect);
		}
		if (facetNodes != null && !facetNodes.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			query.append("(");
			for (int i=0; i<facetNodes.size(); i++) {
				if (i>0) query.append(" OR ");
				query.append(" contents.contentid IN (SELECT contentid FROM ")
				.append(this.getContentRelationsTableName())
				.append(" WHERE ").append(this.getContentRelationsTableName()).append(".").append(FACET_REF_FIELDS_MARKER).append(" = ? ) ");
			}
			query.append(")");
		} else {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			query.append("(");
//			for (int i=0; i<facetNodes.size(); i++) {
//				if (i>0) query.append(" OR ");
				query.append(" contents.contentid IN (SELECT contentid FROM ")
				.append(this.getContentRelationsTableName())
				.append(" WHERE ").append(this.getContentRelationsTableName()).append(".").append(FACET_REF_FIELDS_MARKER).append(" IS NOT NULL ) ");
//			}
			query.append(")");

		}
	}
	
	protected String getFacetRefFieldName() {
		return _facetRefFieldName;
	}
	public void setFacetRefFieldName(String facetRefFieldName) {
		this._facetRefFieldName = facetRefFieldName;
	}
	
	private String _facetRefFieldName;
	
	private final String FACET_REF_FIELDS_MARKER = "###FACET_REF_MARKER###";
	
}
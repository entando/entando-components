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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public class ContentSearcherDAO extends com.agiletec.plugins.jacms.aps.system.services.content.WorkContentSearcherDAO 
		implements IContentSearcherDAO {

	private static final Logger _logger = LoggerFactory.getLogger(ContentSearcherDAO.class);
	
	@Override
	public List<String> loadContentsId(List<WorkflowSearchFilter> workflowFilters, String[] categories,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		List<String> contentsId = new ArrayList<String>();
		if (workflowFilters.isEmpty() || userGroupCodes == null || userGroupCodes.size()==0) {
			return contentsId;
		}
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(workflowFilters, filters, categories, userGroupCodes, conn);
			result = stat.executeQuery();
			this.flowResult(contentsId, filters, result);
		} catch (Throwable t) {
			_logger.error("Error loading content list",  t);
			throw new RuntimeException("Error loading content list", t);
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return contentsId;
	}
	
	private PreparedStatement buildStatement(List<WorkflowSearchFilter> workflowFilters, EntitySearchFilter[] filters, 
			String[] categories, Collection<String> userGroupCodes, Connection conn) {
		Collection<String> groupsForSelect = this.getGroupsForSelect(userGroupCodes);
		String query = this.createQueryString(workflowFilters, filters, categories, groupsForSelect);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			index = super.addAttributeFilterStatementBlock(filters, index, stat);
			index = this.addMetadataFieldFilterStatementBlock(filters, index, stat);
			if (groupsForSelect != null) {
				index = this.addGroupStatementBlock(groupsForSelect, index, stat);
			}
			if (categories != null) {
				for (int i=0; i<categories.length; i++) {
					stat.setString(++index, categories[i]);
				}
			}
			if (null != workflowFilters) {
				for (int i = 0; i < workflowFilters.size(); i++) {
					WorkflowSearchFilter filter = workflowFilters.get(i);
					stat.setString(++index, filter.getTypeCode());
					for (int j = 0; j < filter.getAllowedSteps().size(); j++) {
						String step = filter.getAllowedSteps().get(j);
						stat.setString(++index, step);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error creating statement",  t);
			throw new RuntimeException("Error creating statement", t);
		}
		return stat;
	}
	
	private String createQueryString(List<WorkflowSearchFilter> workflowFilters, EntitySearchFilter[] filters, String[] categories, 
			Collection<String> groupsForSelect) {
		StringBuffer query = this.createBaseQueryBlock(filters, false);
		
		boolean hasAppendWhereClause = this.appendFullAttributeFilterQueryBlocks(filters, query, false);
		hasAppendWhereClause = this.appendMetadataFieldFilterQueryBlocks(filters, query, hasAppendWhereClause);
		
		if (groupsForSelect != null && !groupsForSelect.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			this.addGroupsQueryBlock(query, groupsForSelect);
		}
		if (categories != null) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			for (int i=0; i<categories.length; i++) {
				if (i>0) query.append(" AND ");
				query.append(" contents.contentid IN (SELECT contentid FROM ")
					.append(this.getContentRelationsTableName()).append(" WHERE ")
					.append(this.getContentRelationsTableName()).append(".refcategory = ? ) ");
			}
		}
		hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
		query.append(" ( ");
		int i = 0;
		for (WorkflowSearchFilter filter : workflowFilters) {
			if (i++>0) query.append(" OR ");
			query.append("( contents.contenttype = ? AND contents.status IN ( ");
			int steps = filter.getAllowedSteps().size();
			if (steps-- > 0) {
				query.append("? ");
			}
			while (steps-- > 0) {
				query.append(", ?");
			}
			query.append(" )) ");
		}
		query.append(") ");
		
		appendOrderQueryBlocks(filters, query, false);
		return query.toString();
	}
	
}
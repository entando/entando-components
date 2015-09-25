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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jacms.aps.system.services.content.PublicContentSearcherDAO;

/**
 * @author E.Santoboni
 */
public class NewsletterSearcherDAO extends PublicContentSearcherDAO implements INewsletterSearcherDAO {
	
	@Override
	public List<String> loadNewsletterContentsId(String[] contentTypes, 
			String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		List<String> contentsId = new ArrayList<String>();
		if (userGroupCodes == null || userGroupCodes.size()==0 || contentTypes == null || contentTypes.length==0) {
			return contentsId;
		}
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(contentTypes, filters, categories, userGroupCodes, conn);
			result = stat.executeQuery();
			this.flowResult(contentsId, filters, result);
		} catch (Throwable t) {
			processDaoException(t, "Errore in caricamento lista id contenuti", "loadContentsId");
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return contentsId;
	}
	
	private PreparedStatement buildStatement(String[] contentTypes, EntitySearchFilter[] filters, String[] categories, 
			Collection<String> userGroupCodes, Connection conn) {
		Collection<String> groupsForSelect = this.getGroupsForSelect(userGroupCodes);
		// TODO Modificare ricerca per contentTypes sfruttando nuova versione.
		String query = this.createQueryString(contentTypes, filters, categories, groupsForSelect);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			for (int i = 0; i < contentTypes.length; i++) {
				stat.setString(++index, contentTypes[i]);
			}
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
		} catch (Throwable t) {
			processDaoException(t, "Errore in fase di creazione statement", "buildStatement");
		}
		return stat;
	}
	
	protected String createQueryString(String[] contentTypes, EntitySearchFilter[] filters, 
			String[] categories, Collection<String> groupsForSelect) {
		StringBuffer query = this.createBaseQueryBlock(filters, false);
		for (int i = 0; i < contentTypes.length; i++) {
			if (i == 0) {
				query.append(" WHERE (");
			} else query.append(" OR ");
			query.append(" contents.contenttype = ? ");
			if (i==contentTypes.length-1) query.append(" ) ");
		}
		// Potenziale errore in hasAppendWhereClause se content types vuoti (anche se non ha senso)
		boolean hasAppendWhereClause = this.appendFullAttributeFilterQueryBlocks(filters, query, true);
		hasAppendWhereClause = this.appendMetadataFieldFilterQueryBlocks(filters, query, hasAppendWhereClause);
		if (groupsForSelect != null && !groupsForSelect.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			this.addGroupsQueryBlock(query, groupsForSelect);
		}
		if (categories != null && categories.length > 0) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			query.append(" ( ");
			for (int i=0; i<categories.length; i++) {
				if (i>0) query.append(" OR ");
				query.append(" contents.contentid IN (SELECT contentid FROM contentrelations WHERE contentrelations.refcategory = ? ) ");
			}
			query.append(" ) ");
		}
		boolean ordered = appendOrderQueryBlocks(filters, query, false);
		return query.toString();
	}
	
}
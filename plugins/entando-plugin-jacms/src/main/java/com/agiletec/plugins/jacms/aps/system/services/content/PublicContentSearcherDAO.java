/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.group.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class PublicContentSearcherDAO extends AbstractContentSearcherDAO implements IContentSearcherDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(PublicContentSearcherDAO.class);
	
	@Override
    public List<String> loadContentsId(String[] categories, 
			boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
        Set<String> groupCodes = new HashSet<>();
		if (null != userGroupCodes) {
			groupCodes.addAll(userGroupCodes);
		}
		groupCodes.add(Group.FREE_GROUP_NAME);
		EntitySearchFilter onLineFilter = new EntitySearchFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, false);
		filters = this.addFilter(filters, onLineFilter);
        return super.loadContentsId(categories, orClauseCategoryFilter, filters, groupCodes);
    }
	
	@Override
	protected void addGroupsQueryBlock(StringBuffer query, Collection<String> userGroupCodes) {
		query.append(" ( ");
		int size = userGroupCodes.size();
		for (int i=0; i<size; i++) {
			if (i!=0) {
				query.append("OR ");
			}
			query.append("contents.maingroup = ? ");
		}
		query.append(" OR contents.contentid IN ( SELECT contentid FROM ")
				.append(this.getContentRelationsTableName()).append(" WHERE ");
		for (int i=0; i<size; i++) {
			if (i!=0) query.append("OR ");
			query.append(this.getContentRelationsTableName()).append(".refgroup = ? ");
		}
		query.append(") ");
		query.append(") ");
	}
	
	@Override
	protected int addGroupStatementBlock(Collection<String> groupCodes, int index, PreparedStatement stat) throws Throwable {
		List<String> groups = new ArrayList<>(groupCodes);
		for (int i=0; i<groups.size(); i++) {
			String groupName = groups.get(i);
			stat.setString(++index, groupName);
		}
		for (int i=0; i<groups.size(); i++) {
			String groupName = groups.get(i);
			stat.setString(++index, groupName);
		}
		return index;
	}
	
	@Override
	protected String getEntitySearchTableName() {
		return "contentsearch";
	}
	@Override
	protected String getEntitySearchTableIdFieldName() {
		return "contentid";
	}
	@Override
	protected String getContentRelationsTableName() {
		return "contentrelations";
	}
	@Override
	protected String getEntityAttributeRoleTableName() {
		return "contentattributeroles";
	}
	@Override
	protected String getEntityAttributeRoleTableIdFieldName() {
		return "contentid";
	}
	
}
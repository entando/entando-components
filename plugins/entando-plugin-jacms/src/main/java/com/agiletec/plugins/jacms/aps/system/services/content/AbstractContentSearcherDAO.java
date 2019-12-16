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

import com.agiletec.aps.system.SystemConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.AbstractEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract Data access object used to search contents.
 * @author E.Santoboni
 */
public abstract class AbstractContentSearcherDAO extends AbstractEntitySearcherDAO implements IContentSearcherDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractContentSearcherDAO.class);
    
    @Override
    public int countContents(String[] categories, boolean orClauseCategoryFilter, 
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
        Connection conn = null;
        int count = 0;
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            conn = this.getConnection();
            stat = this.buildStatement(filters, categories, 
                    orClauseCategoryFilter, userGroupCodes, true, false, conn);
            result = stat.executeQuery();
            if (result.next()) {
                count = result.getInt(1);
            }
        } catch (Throwable t) {
            _logger.error("Error while loading the count of IDs", t);
            throw new RuntimeException("Error while loading the count of IDs", t);
        } finally {
            closeDaoResources(result, stat, conn);
        }
        return count;
    }
    
    @Override
	public List<String> loadContentsId(String contentType, String[] categories, boolean orClauseCategoryFilter, 
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		if (!StringUtils.isBlank(contentType)) {
			EntitySearchFilter typeFilter = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, contentType, false);
			filters = this.addFilter(filters, typeFilter);
		}
		return this.loadContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
	}
    
    @Override
    public List<String> loadContentsId(String[] categories, 
			boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		List<String> contentsId = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(filters, categories, orClauseCategoryFilter, userGroupCodes, false, false, conn);
			result = stat.executeQuery();
            while (result.next()) {
                String id = result.getString(this.getMasterTableIdFieldName());
                if (!contentsId.contains(id)) {
                    contentsId.add(id);
                }
            }
		} catch (Throwable t) {
			_logger.error("Error loading contents id list",  t);
			throw new RuntimeException("Error loading contents id list", t);
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return contentsId;
	}
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		if (metadataFieldKey.equals(IContentManager.ENTITY_ID_FILTER_KEY)) {
			return this.getEntityMasterTableIdFieldName();
		} else if (metadataFieldKey.equals(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY)) {
			return this.getEntityMasterTableIdTypeFieldName();
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_DESCR_FILTER_KEY)) {
			return "descr";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_STATUS_FILTER_KEY)) {
			return "status";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY)) {
			return "created";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY)) {
			return "lastmodified";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_PUBLISH_DATE_FILTER_KEY)) {
			return "published";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_ONLINE_FILTER_KEY)) {
			return "onlinexml";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY)) {
			return "maingroup";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_CURRENT_VERSION_FILTER_KEY)) {
			return "currentversion";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_FIRST_EDITOR_FILTER_KEY)) {
			return "firsteditor";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_LAST_EDITOR_FILTER_KEY)) {
			return "lasteditor";
		} else if (metadataFieldKey.equals(IContentManager.CONTENT_RESTRICTION_FILTER_KEY)) {
			return "restriction";
		}else if (metadataFieldKey.equals(IContentManager.CONTENT_GROUP_FILTER_KEY)) {
			return "group";
		} else throw new RuntimeException("Chiave di ricerca '" + metadataFieldKey + "' non riconosciuta");
	}
	
	protected PreparedStatement buildStatement(EntitySearchFilter[] filters,
			Collection<String> userGroupCodes, boolean selectAll, Connection conn) {
		return this.buildStatement(filters, null, false, userGroupCodes, false, selectAll, conn);
	}
	
	protected PreparedStatement buildStatement(EntitySearchFilter[] filters,
			String[] categories, Collection<String> userGroupCodes, boolean selectAll, Connection conn) {
		return this.buildStatement(filters, categories, false, userGroupCodes, false, selectAll, conn);
	}
	
	protected PreparedStatement buildStatement(EntitySearchFilter[] filters,
			String[] categories, boolean orClauseCategoryFilter, 
			Collection<String> userGroupCodes, boolean isCount, boolean selectAll, Connection conn) {
		Collection<String> groupsForSelect = this.getGroupsForSelect(userGroupCodes);

		String query = this.createQueryString(filters, null, categories, orClauseCategoryFilter, groupsForSelect, isCount, selectAll);
		//System.out.println("QUERY : " + query);
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
				index = this.addCategoryStatementBlock(categories, index, stat);
			}
		} catch (Throwable t) {
			_logger.error("Errore in fase di creazione statement",  t);
			throw new RuntimeException("Errore in fase di creazione statement", t);
			//processDaoException(t, "Errore in fase di creazione statement", "buildStatement");
		}
		return stat;
	}
	
	protected int addGroupStatementBlock(Collection<String> groupCodes, int index, PreparedStatement stat) throws Throwable {
		Iterator<String> groupIter = groupCodes.iterator();
		while (groupIter.hasNext()) {
			String groupName = groupIter.next();
			stat.setString(++index, groupName);
		}
		return index;
	}
	
	protected int addCategoryStatementBlock(String[] categories, int index, PreparedStatement stat) throws Throwable {
		if (null == categories) return index;
		for (int i = 0; i < categories.length; i++) {
			stat.setString(++index, categories[i]);
		}
		return index;
	}
	
	protected String createQueryString(EntitySearchFilter[] filters, Collection<String> groupsForSelect, boolean selectAll) {
		return this.createQueryString(filters, null, null, false, groupsForSelect, false, selectAll);
	}
	
	protected String createQueryString(EntitySearchFilter[] filters, 
			String[] categories, Collection<String> groupsForSelect, boolean selectAll) {
		return this.createQueryString(filters, null, categories, false, groupsForSelect, false, selectAll);
	}
	
	protected String createQueryString(EntitySearchFilter[] filters, String[] groups,
			String[] categories, boolean orClauseCategoryFilter, Collection<String> groupsForSelect, boolean isCount, boolean selectAll) {
		StringBuffer query = this.createBaseQueryBlock(filters, isCount, selectAll);
		boolean hasAppendWhereClause = this.appendFullAttributeFilterQueryBlocks(filters, query, false);
		hasAppendWhereClause = this.appendMetadataFieldFilterQueryBlocks(filters, query, hasAppendWhereClause);
		if (null != groupsForSelect && !groupsForSelect.isEmpty()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			this.addGroupsQueryBlock(query, groupsForSelect);
		}
		if (null != categories && categories.length > 0) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			this.addCategoriesQueryBlock(query, categories, !orClauseCategoryFilter);
		}
		if (null != groups && groups.length > 0) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			this.addGroupsQueryBlock(query, groups);
		}
        if (!isCount) {
            boolean ordered = this.appendOrderQueryBlocks(filters, query, false);
            this.appendLimitQueryBlock(filters, query);
        }
		//System.out.println("********** " + query.toString());
		return query.toString();
	}
	
	protected void addGroupsQueryBlock(StringBuffer query, Collection<String> userGroupCodes) {
		query.append(" ( ");
		int size = userGroupCodes.size();
		for (int i=0; i<size; i++) {
			if (i!=0) query.append("OR ");
			query.append("contents.maingroup = ? ");
		}
		query.append(") ");
	}
	
	protected void addCategoriesQueryBlock(StringBuffer query, String[] categories, boolean andClause) {
		if (categories != null && categories.length > 0) {
			query.append(" ( ");
			for (int i=0; i<categories.length; i++) {
				if (i>0) {
					if (andClause) {
						query.append(" AND ");
					} else {
						query.append(" OR ");
					}
				}
				query.append(" contents.contentid IN (SELECT contentid FROM ")
					.append(this.getContentRelationsTableName()).append(" WHERE ")
					.append(this.getContentRelationsTableName()).append(".refcategory = ? ) ");
			}
			query.append(" ) ");
		}
	}

    protected void addGroupsQueryBlock(StringBuffer query, String[] groups) {
        if (groups != null && groups.length > 0) {
            query.append(" ( ");
            for (int i = 0; i < groups.length; i++) {
                if (i > 0) {
                    query.append(" AND ");
                }
                query.append(" contents.contentid IN (SELECT contentid FROM ")
                        .append(this.getContentRelationsTableName()).append(" WHERE ")
                        .append(this.getContentRelationsTableName()).append(".refgroup = ? ) ");
            }
            query.append(" ) ");
        }
    }

	protected Collection<String> getGroupsForSelect(Collection<String> userGroupCodes) {
		if (userGroupCodes != null && userGroupCodes.contains(Group.ADMINS_GROUP_NAME)) {
			return null;
		} else {
			Collection<String> groupsForSelect = new HashSet<>();
			if (userGroupCodes != null) groupsForSelect.addAll(userGroupCodes);
			return groupsForSelect;
		}
	}
	
	@Override
	protected ApsEntityRecord createRecord(ResultSet result) throws Throwable {
		ContentRecordVO contentVo = new ContentRecordVO();
		contentVo.setId(result.getString("contentid"));
		contentVo.setTypeCode(result.getString("contenttype"));
		contentVo.setDescription(result.getString("descr"));
		contentVo.setStatus(result.getString("status"));
		String xmlWork = result.getString("workxml");
        contentVo.setCreate(DateConverter.parseDate(result.getString("created"), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT));
		contentVo.setModify(DateConverter.parseDate(result.getString("lastmodified"), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT));
		contentVo.setPublish(DateConverter.parseDate(result.getString("published"), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT));
		String xmlOnLine = result.getString("onlinexml");
        contentVo.setOnLine(!StringUtils.isBlank(xmlOnLine));
		contentVo.setSync(result.getInt("sync") == 1);
		String mainGroupCode = result.getString("maingroup");
		contentVo.setMainGroupCode(mainGroupCode);
		contentVo.setXmlWork(xmlWork);
		contentVo.setXmlOnLine(xmlOnLine);
		contentVo.setVersion(result.getString("currentversion"));
		contentVo.setLastEditor(result.getString("lasteditor"));
		contentVo.setRestriction(result.getString("restriction"));
		return contentVo;
	}
	
	@Override
	protected String getEntityMasterTableName() {
		return "contents";
	}
	@Override
	protected String getEntityMasterTableIdFieldName() {
		return "contentid";
	}
	@Override
	protected String getEntityMasterTableIdTypeFieldName() {
		return "contenttype";
	}
	
	protected abstract String getContentRelationsTableName();
	
}

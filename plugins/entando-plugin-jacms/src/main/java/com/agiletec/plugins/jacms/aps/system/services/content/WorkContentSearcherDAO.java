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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class WorkContentSearcherDAO extends AbstractContentSearcherDAO implements IContentSearcherDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(WorkContentSearcherDAO.class);
	
	@Override
	public List<String> loadContentsId(String[] categories, boolean orClauseCategoryFilter, 
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) {
		List<String> contentsId = new ArrayList<>();
		if (userGroupCodes == null || userGroupCodes.isEmpty()) {
			return contentsId;
		}
		return super.loadContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
	}
	
	@Override
	protected String getEntitySearchTableName() {
		return "workcontentsearch";
	}
	@Override
	protected String getEntitySearchTableIdFieldName() {
		return "contentid";
	}
	@Override
	protected String getContentRelationsTableName() {
		return "workcontentrelations";
	}
	@Override
	protected String getEntityAttributeRoleTableName() {
		return "workcontentattributeroles";
	}
	@Override
	protected String getEntityAttributeRoleTableIdFieldName() {
		return "contentid";
	}
	
}
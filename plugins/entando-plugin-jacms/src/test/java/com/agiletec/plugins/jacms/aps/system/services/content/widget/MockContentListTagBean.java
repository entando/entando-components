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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author E.Santoboni
 */
public class MockContentListTagBean implements IContentListTagBean {
	
	private String listName;
	private String contentType;
	private String[] categories = new String[0];
	private EntitySearchFilter[] filters = new EntitySearchFilter[0];
	private String category;
	private List<UserFilterOptionBean> userFilterOptions = new ArrayList<>();
	
	@Override
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	
	@Override
	public String getContentType() {
		return contentType;
	}
	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Override
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	
	@Override
	public EntitySearchFilter[] getFilters() {
		return filters;
	}
	public void setFilters(EntitySearchFilter[] filters) {
		this.filters = filters;
	}
	
	@Override
	public String getCategory() {
		return category;
	}
	@Override
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public boolean isCacheable() {
		return true;
	}
	
	@Override
	public void addCategory(String category) {
        this.categories = ArrayUtils.add(this.categories, category);
	}
	
	@Override
	public void addFilter(EntitySearchFilter filter) {
        this.filters = ArrayUtils.add(this.filters, filter);
	}
	@Override
	public void addUserFilterOption(UserFilterOptionBean filter) {
		if (null == filter) return;
		if (null == this.getUserFilterOptions()) {
			this.setUserFilterOptions(new ArrayList<>());
		}
		this.getUserFilterOptions().add(filter);
	}
	
	@Override
	public List<UserFilterOptionBean> getUserFilterOptions() {
		return userFilterOptions;
	}
	public void setUserFilterOptions(List<UserFilterOptionBean> userFilterOptions) {
		this.userFilterOptions = userFilterOptions;
	}

    @Override
    public boolean isOrClauseCategoryFilter() {
        return false;
    }
	
}
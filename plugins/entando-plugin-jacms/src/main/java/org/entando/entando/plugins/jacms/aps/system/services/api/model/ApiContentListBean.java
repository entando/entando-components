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
package org.entando.entando.plugins.jacms.aps.system.services.api.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentListBean;
import java.util.stream.Collectors;

/**
 * @author E.Santoboni
 */
public class ApiContentListBean implements IContentListBean {
    
    private String contentType;
    private EntitySearchFilter[] filters;
    private String[] categories;
    private boolean orClauseCategoryFilter; 

    public ApiContentListBean(String contentType, EntitySearchFilter[] filters, String[] categories, boolean orClauseCategoryFilter) {
        this.setContentType(contentType);
        this.setCategories(categories);
        this.setFilters(filters);
        this.setOrClauseCategoryFilter(orClauseCategoryFilter);
    }
    
    @Override
    public String getListName() {
        StringBuilder buffer = new StringBuilder("listName_api");
        buffer.append("-TYPE:").append(this.getContentType());
        buffer.append("_FILTERS:");
        if (null != this.getFilters() && this.getFilters().length > 0) {
            BaseFilterUtils filterUtils = new BaseFilterUtils();
            buffer.append(filterUtils.getFilterParam(this.getFilters()));
        } else {
            buffer.append("NULL");
        }
        buffer.append("_CATEGORIES:");
        if (null != this.getCategories() && this.getCategories().length > 0) {
            List<String> categoryList = Arrays.asList(this.getCategories());
            Collections.sort(categoryList);
            buffer.append(categoryList.stream().collect(Collectors.joining("+")));
            buffer.append("_ORCLAUSE:").append(this.isOrClauseCategoryFilter());
        } else {
            buffer.append("NULL");
        }
        return buffer.toString();
    }
    
    @Override
    public String getContentType() {
        return contentType;
    }
    protected void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    @Override
    public String[] getCategories() {
        return this.categories;
    }
    protected void setCategories(String[] categories) {
        this.categories = categories;
    }
    
    @Override
    public EntitySearchFilter[] getFilters() {
        return this.filters;
    }
    protected void setFilters(EntitySearchFilter[] filters) {
        this.filters = filters;
    }
    
    @Override
    public boolean isCacheable() {
        return true;
    }
    
    @Override
    public boolean isOrClauseCategoryFilter() {
        return orClauseCategoryFilter;
    }
    public void setOrClauseCategoryFilter(boolean orClauseCategoryFilter) {
        this.orClauseCategoryFilter = orClauseCategoryFilter;
    }
    
}
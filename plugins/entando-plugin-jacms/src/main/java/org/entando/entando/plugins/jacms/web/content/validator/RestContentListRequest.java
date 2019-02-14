/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.web.content.validator;

import org.entando.entando.web.common.model.RestEntityListRequest;

public class RestContentListRequest extends RestEntityListRequest {

    private String[] categories;
    private boolean orClauseCategoryFilter;
    private String text;

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public boolean isOrClauseCategoryFilter() {
        return orClauseCategoryFilter;
    }

    public void setOrClauseCategoryFilter(boolean orClauseCategoryFilter) {
        this.orClauseCategoryFilter = orClauseCategoryFilter;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

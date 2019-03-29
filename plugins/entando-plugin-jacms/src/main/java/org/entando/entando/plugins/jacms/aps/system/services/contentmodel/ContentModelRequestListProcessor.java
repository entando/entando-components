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
package org.entando.entando.plugins.jacms.aps.system.services.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.util.FilterUtils;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class ContentModelRequestListProcessor extends RequestListProcessor<ContentModel> {

    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT_TYPE = "contentType";
    private static final String KEY_DESCR = "descr";

    public ContentModelRequestListProcessor(RestListRequest restListRequest, Stream<ContentModel> stream) {
        super(restListRequest, stream);
    }

    public ContentModelRequestListProcessor(RestListRequest restListRequest, List<ContentModel> stream) {
        super(restListRequest, stream);
    }

    @Override
    protected Function<Filter, Predicate<ContentModel>> getPredicates() {
        return (filter) -> {
            switch (filter.getAttribute()) {
                case KEY_ID:
                    return c -> FilterUtils.filterLong(filter, c.getId());
                case KEY_CONTENT_TYPE:
                    return c -> FilterUtils.filterString(filter, c.getContentType());
                case KEY_DESCR:
                    return c -> FilterUtils.filterString(filter, c.getDescription());
                default:
                    return null;
            }
        };
    }

    @Override
    protected Function<String, Comparator<ContentModel>> getComparators() {
        return sort -> {
            switch (sort) {
                case KEY_ID:
                default:
                    return (a, b) -> Long.compare(a.getId(), b.getId());
                case KEY_CONTENT_TYPE:
                    return (a, b) -> StringUtils.compare(a.getContentType(), b.getContentType());
                case KEY_DESCR:
                    return (a, b) -> StringUtils.compare(a.getDescription(), b.getDescription());
            }
        };
    }
}

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

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelReference;
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

public class ContentModelReferencesRequestListProcessor extends RequestListProcessor<ContentModelReference> {

    private static final String KEY_PAGE_CODE = "pageCode";
    private static final String KEY_ONLINE = "online";
    private static final String KEY_WIDGED_POSITION = "widgetPosition";

    public ContentModelReferencesRequestListProcessor(RestListRequest restListRequest, Stream<ContentModelReference> stream) {
        super(restListRequest, stream);
    }

    public ContentModelReferencesRequestListProcessor(RestListRequest restListRequest, List<ContentModelReference> stream) {
        super(restListRequest, stream);
    }

    @Override
    protected Function<Filter, Predicate<ContentModelReference>> getPredicates() {
        return (filter) -> {
            switch (filter.getAttribute()) {
                case KEY_PAGE_CODE:
                    return c -> FilterUtils.filterString(filter, c.getPageCode());
                case KEY_ONLINE:
                    return c -> FilterUtils.filterBoolean(filter, c.isOnline());
                case KEY_WIDGED_POSITION:
                    return c -> FilterUtils.filterInt(filter, c.getWidgetPosition());
                default:
                    return null;
            }
        };
    }

    @Override
    protected Function<String, Comparator<ContentModelReference>> getComparators() {
        return sort -> {
            switch (sort) {
                case KEY_PAGE_CODE:
                default:
                    return (a, b) -> StringUtils.compare(a.getPageCode(), b.getPageCode());
                case KEY_ONLINE:
                    return (a, b) -> Boolean.compare(a.isOnline(), b.isOnline());
                case KEY_WIDGED_POSITION:
                    return Comparator.comparingInt(ContentModelReference::getWidgetPosition);
            }
        };
    }
}

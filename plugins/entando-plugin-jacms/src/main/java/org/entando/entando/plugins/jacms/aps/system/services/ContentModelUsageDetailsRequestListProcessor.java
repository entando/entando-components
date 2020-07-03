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
package org.entando.entando.plugins.jacms.aps.system.services;

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
import org.entando.entando.web.component.ComponentUsageEntity;

public class ContentModelUsageDetailsRequestListProcessor extends RequestListProcessor<ComponentUsageEntity> {

    private static final String KEY_TYPE = "type";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CODE = "code";

    public ContentModelUsageDetailsRequestListProcessor(RestListRequest restListRequest, Stream<ComponentUsageEntity> stream) {
        super(restListRequest, stream);
    }

    public ContentModelUsageDetailsRequestListProcessor(RestListRequest restListRequest, List<ComponentUsageEntity> stream) {
        super(restListRequest, stream);
    }

    @Override
    protected Function<Filter, Predicate<ComponentUsageEntity>> getPredicates() {
        return (filter) -> {
            switch (filter.getAttribute()) {
                case KEY_TYPE:
                    return c -> FilterUtils.filterString(filter, c.getType());
                case KEY_STATUS:
                    return c -> FilterUtils.filterString(filter, c.getStatus());
                case KEY_CODE:
                    return c -> FilterUtils.filterString(filter, c.getCode());
                default:
                    return null;
            }
        };
    }

    @Override
    protected Function<String, Comparator<ComponentUsageEntity>> getComparators() {
        return sort -> {
            switch (sort) {
                case KEY_CODE:
                default:
                    return (a, b) -> StringUtils.compare(a.getCode(), b.getCode());
                case KEY_STATUS:
                    return (a, b) -> StringUtils.compare(a.getStatus(), b.getStatus());
                case KEY_TYPE:
                    return (a, b) -> StringUtils.compare(a.getType(), b.getType());
            }
        };
    }
}

/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.pagemodel;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModelDto;
import org.entando.entando.aps.util.FilterUtils;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class DigitalExchangePageModelListProcessor extends RequestListProcessor<DigitalExchangePageModelDto> {

    private static final String CODE = "code";
    private static final String DESCRIPTION = "descr";
    private static final String MAIN_FRAME = "mainFrame";
    private static final String PLUGIN_CODE = "pluginCode";
    private static final String TEMPLATE = "template";
    private static final String DIGITAL_EXCHANGE = "digitalExchange";

    public DigitalExchangePageModelListProcessor(RestListRequest restListRequest, List<DigitalExchangePageModelDto> items) {
        super(restListRequest, items);
    }

    @Override
    protected Function<Filter, Predicate<DigitalExchangePageModelDto>> getPredicates() {
        return (filter) -> {
            switch (filter.getAttribute()) {
                case CODE:
                    return p -> FilterUtils.filterString(filter, p::getCode);
                case DESCRIPTION:
                    return p -> FilterUtils.filterString(filter, p::getDescr);
                case MAIN_FRAME:
                    return p -> FilterUtils.filterInt(filter, p::getMainFrame);
                case PLUGIN_CODE:
                    return p -> FilterUtils.filterString(filter, p::getPluginCode);
                case TEMPLATE:
                    return p -> FilterUtils.filterString(filter, p::getTemplate);
                case DIGITAL_EXCHANGE:
                    return p -> FilterUtils.filterString(filter, p::getDigitalExchange);
                default:
                    return null;
            }
        };
    }

    @Override
    protected Function<String, Comparator<DigitalExchangePageModelDto>> getComparators() {
        return sort -> {
            switch (sort) {
                case DESCRIPTION:
                    return (a, b) -> a.getPluginCode().compareTo(b.getPluginCode());
                case MAIN_FRAME:
                    return (a, b) -> Integer.compare(a.getMainFrame(), b.getMainFrame());
                case PLUGIN_CODE:
                    return (a, b) -> a.getPluginCode().compareTo(b.getPluginCode());
                case TEMPLATE:
                    return (a, b) -> a.getTemplate().compareTo(b.getTemplate());
                case DIGITAL_EXCHANGE:
                    return (a, b) -> a.getDigitalExchange().compareTo(b.getDigitalExchange());
                case CODE: // code is the default sorting field
                default:
                    return (a, b) -> a.getCode().compareTo(b.getCode());
            }
        };
    }
}

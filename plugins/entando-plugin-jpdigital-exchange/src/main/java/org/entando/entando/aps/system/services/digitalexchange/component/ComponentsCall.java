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
package org.entando.entando.aps.system.services.digitalexchange.component;

import java.util.List;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.client.PagedDigitalExchangeCall;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.springframework.core.ParameterizedTypeReference;

public class ComponentsCall extends PagedDigitalExchangeCall<DigitalExchangeComponent> {

    private final DigitalExchangesService exchangesService;

    public ComponentsCall(DigitalExchangesService exchangesService, RestListRequest requestList) {
        super(requestList, new ParameterizedTypeReference<PagedRestResponse<DigitalExchangeComponent>>() {
        }, "digitalExchange", "components");
        this.exchangesService = exchangesService;
    }

    public ComponentsCall(DigitalExchangesService exchangesService, RestListRequest requestList, String componentType) {
        this(exchangesService, filterByType(requestList, componentType));
    }

    @Override
    protected void preprocessResponse(String exchangeId, PagedRestResponse<DigitalExchangeComponent> response) {
        if (response.getErrors().isEmpty()) {
            String exchangeName = exchangesService.findById(exchangeId).getName();
            response.getPayload().forEach(de -> de.setDigitalExchange(exchangeName));
        }
    }

    @Override
    protected RequestListProcessor<DigitalExchangeComponent> getRequestListProcessor(RestListRequest request, List<DigitalExchangeComponent> joinedList) {
        return new DigitalExchangeComponentListProcessor(request, joinedList);
    }

    private static RestListRequest filterByType(RestListRequest requestList, String componentType) {
        Filter filter = new Filter();
        filter.setAttribute("type");
        filter.setValue(componentType);
        filter.setOperator(FilterOperator.EQUAL.getValue());
        requestList.addFilter(filter);
        return requestList;
    }
}

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
package org.entando.entando.aps.system.services.digitalexchange.component;

import java.util.Arrays;
import java.util.List;
import org.entando.entando.aps.system.init.IInitializerManager;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DigitalExchangeComponentsServiceImpl implements DigitalExchangeComponentsService {

    private static final List<String> LOCAL_FILTERS = Arrays.asList("digitalExchangeName", "digitalExchangeId", "installed");

    private final DigitalExchangesClient client;
    private final DigitalExchangesService exchangesService;
    private final IInitializerManager initializerManager;

    @Autowired
    public DigitalExchangeComponentsServiceImpl(DigitalExchangesClient client,
            DigitalExchangesService exchangesService,
            IInitializerManager initializerManager) {
        this.client = client;
        this.exchangesService = exchangesService;
        this.initializerManager = initializerManager;
    }

    @Override
    public ResilientPagedMetadata<DigitalExchangeComponent> getComponents(RestListRequest requestList) {
        ResilientPagedMetadata<DigitalExchangeComponent> combinedResult = client.getCombinedResult(
                new ComponentsCall(exchangesService, buildForwardedRequest(requestList)));

        // Fill installed fields
        SystemInstallationReport installationReport = initializerManager.getCurrentReport();
        combinedResult.getBody().forEach(component -> {
            component.setInstalled(installationReport.getComponentReport(component.getName(), false) != null);
        });

        List<DigitalExchangeComponent> localFilteredList = new DigitalExchangeComponentListProcessor(
                requestList, combinedResult.getBody()).filterAndSort().toList();

        combinedResult.setTotalItems(localFilteredList.size());
        combinedResult.setBody(requestList.getSublist(localFilteredList));
        combinedResult.setPage(requestList.getPage());
        combinedResult.setPageSize(requestList.getPageSize());

        return combinedResult;
    }

    private RestListRequest buildForwardedRequest(RestListRequest originalRequest) {
        RestListRequest forwaredRequest = new RestListRequest();
        forwaredRequest.setDirection(originalRequest.getDirection());
        forwaredRequest.setSort(originalRequest.getSort());
        forwaredRequest.setPageSize(Integer.MAX_VALUE);
        forwaredRequest.setPage(1);

        if (originalRequest.getFilters() != null) {
            Filter[] forwaredFilters = Arrays.stream(originalRequest.getFilters())
                    .filter(f -> !LOCAL_FILTERS.contains(f.getAttribute()))
                    .toArray(Filter[]::new);

            forwaredRequest.setFilters(forwaredFilters);
        }

        return forwaredRequest;
    }
}

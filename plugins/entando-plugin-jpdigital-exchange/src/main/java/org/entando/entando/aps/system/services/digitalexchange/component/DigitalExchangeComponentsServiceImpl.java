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

import java.util.List;
import org.entando.entando.aps.system.init.IInitializerManager;
import org.entando.entando.aps.system.init.model.ComponentInstallationReport;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.PagedDigitalExchangeCall;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.ParameterizedTypeReference;

@Service
public class DigitalExchangeComponentsServiceImpl implements DigitalExchangeComponentsService {

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
        ResilientPagedMetadata<DigitalExchangeComponent> combinedResult = client.getCombinedResult(new ComponentsCall(exchangesService, requestList));

        // Fill installed fields
        SystemInstallationReport installationReport = initializerManager.getCurrentReport();
        combinedResult.getBody().forEach(component -> {
            component.setInstalled(installationReport.getComponentReport(component.getId(), false) != null);
        });

        return combinedResult;
    }

    private static class ComponentsCall extends PagedDigitalExchangeCall<DigitalExchangeComponent> {

        private final DigitalExchangesService exchangesService;

        public ComponentsCall(DigitalExchangesService exchangesService, RestListRequest requestList) {
            super(requestList, new ParameterizedTypeReference<PagedRestResponse<DigitalExchangeComponent>>() {
            }, "digitalExchange", "components");
            this.exchangesService = exchangesService;
        }

        @Override
        protected void preprocessResponse(String exchangeId, PagedRestResponse<DigitalExchangeComponent> response) {
            if (response.getErrors().isEmpty()) {
                String exchangeName = exchangesService.findById(exchangeId).getName();
                response.getPayload().forEach(de -> {
                    de.setDigitalExchangeName(exchangeName);
                    de.setDigitalExchangeId(exchangeId);
                });
            }
        }

        @Override
        protected RequestListProcessor<DigitalExchangeComponent> getRequestListProcessor(RestListRequest request, List<DigitalExchangeComponent> joinedList) {
            return new DigitalExchangeComponentListProcessor(request, joinedList);
        }
    }
}

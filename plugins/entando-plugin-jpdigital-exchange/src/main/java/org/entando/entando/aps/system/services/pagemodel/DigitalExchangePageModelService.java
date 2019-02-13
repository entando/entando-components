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

import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.component.ComponentsCall;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModelDto;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModelDtoBuilder;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.springframework.beans.factory.annotation.Autowired;

public class DigitalExchangePageModelService extends PageModelService {

    private final DigitalExchangesService exchangesService;
    private final DigitalExchangesClient client;

    @Autowired
    public DigitalExchangePageModelService(IPageModelManager pageModelManager,
            DigitalExchangePageModelDtoBuilder dtoBuilder,
            DigitalExchangesService exchangesService, DigitalExchangesClient client) {
        super(pageModelManager, dtoBuilder);
        this.exchangesService = exchangesService;
        this.client = client;
    }

    @Override
    public PagedMetadata<PageModelDto> getPageModels(RestListRequest restListReq, Map<String, String> requestParams) {

        PagedMetadata<PageModelDto> response = super.getPageModels(restListReq, requestParams);

        if (excludeDe(requestParams)) {
            return response;
        }

        List<DigitalExchangePageModelDto> pageModels = wrapLocalPageModels(response.getBody());

        pageModels = joinPageModels(pageModels, getPageModelsFromExchanges());

        DigitalExchangePageModelListProcessor requestListProcessor = new DigitalExchangePageModelListProcessor(restListReq, pageModels);
        pageModels = requestListProcessor.filterAndSort().toList();

        response.setTotalItems(pageModels.size());
        response.imposeLimits();
        response.setBody(new ArrayList<>(pageModels));

        return response;
    }

    private List<DigitalExchangePageModelDto> wrapLocalPageModels(List<PageModelDto> localPageModels) {
        List<DigitalExchangePageModelDto> pageModels = new ArrayList<>();
        localPageModels.forEach(pageModelDto -> {
            DigitalExchangePageModelDto dePageModel = new DigitalExchangePageModelDto(pageModelDto);
            dePageModel.setInstalled(true);
            pageModels.add(dePageModel);
        });
        return pageModels;
    }

    private boolean excludeDe(Map<String, String> requestParams) {
        if (requestParams.containsKey("excludeDe")) {
            return Boolean.parseBoolean(requestParams.get("excludeDe"));
        }
        return false;
    }

    private List<DigitalExchangePageModelDto> getPageModelsFromExchanges() {

        // In this case we must retrieve ALL the remote pageModels, because we
        // have to compare them with the local ones.
        RestListRequest deRequest = new RestListRequest();
        deRequest.setPage(1);
        deRequest.setPageSize(Integer.MAX_VALUE);

        ComponentsCall call = new ComponentsCall(exchangesService, deRequest, "pageModel");
        ResilientPagedMetadata<DigitalExchangeComponent> pageModels = client.getCombinedResult(call);

        return pageModels.getBody().stream()
                .map(this::toPageModelDto)
                .collect(Collectors.toList());
    }

    private DigitalExchangePageModelDto toPageModelDto(DigitalExchangeComponent component) {
        DigitalExchangePageModelDto pageModelDto = new DigitalExchangePageModelDto();
        pageModelDto.setCode(component.getName());
        pageModelDto.setDescr(component.getDescription());
        pageModelDto.setDigitalExchangeName(component.getDigitalExchangeName());
        return pageModelDto;
    }

    private List<DigitalExchangePageModelDto> joinPageModels(
            List<DigitalExchangePageModelDto> localPageModels,
            List<DigitalExchangePageModelDto> dePageModels) {

        List<DigitalExchangePageModelDto> allPageModels = new ArrayList<>();

        // remote + installed page models
        dePageModels.forEach(dePageModel -> {
            allPageModels.add(getInstalled(localPageModels, dePageModel).orElse(dePageModel));
        });

        // local-only page models
        allPageModels.addAll(localPageModels.stream()
                .filter(pm -> pm.getDigitalExchange() == null)
                .collect(Collectors.toList()));

        return allPageModels;
    }

    private Optional<DigitalExchangePageModelDto> getInstalled(List<DigitalExchangePageModelDto> localPageModels, DigitalExchangePageModelDto dePageModel) {
        return localPageModels.stream()
                .filter(pm -> pm.getCode().equals(dePageModel.getCode()))
                .peek(pm -> pm.setDigitalExchangeName(dePageModel.getDigitalExchange()))
                .findFirst();
    }
}

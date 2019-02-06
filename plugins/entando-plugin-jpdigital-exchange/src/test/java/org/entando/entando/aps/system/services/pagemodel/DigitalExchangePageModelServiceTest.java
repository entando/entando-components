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

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModel;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModelDtoBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.mockito.Spy;
import org.mockito.InjectMocks;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DigitalExchangePageModelServiceTest {

    private static final String PAGE_MODEL_CODE = "1_TEST_PM";
    private static final String DE_PAGE_MODEL_INSTALLED_CODE = "2_TEST_PM_DE_INSTALLED";
    private static final String DE_PAGE_MODEL_NOT_INSTALLED_CODE = "3_TEST_PM_DE_NOT_INSTALLED";
    private static final String EXCHANGE = "Leonardo's Exchange";

    private static final RestListRequest EMPTY_REQUEST = new RestListRequest();

    @Mock
    IPageModelManager pageModelManager;

    @Spy
    private DigitalExchangePageModelDtoBuilder dtoBuilder;

    @Mock
    private DigitalExchangesClient client;

    @InjectMocks
    private DigitalExchangePageModelService dePageModelService;

    @Before
    public void setUp() throws Exception {
        when(pageModelManager.searchPageModels(any())).thenReturn(localPageModels());
    }

    @Test
    public void get_all_page_models_with_de_excluded_returns_local_page_models() throws ApsSystemException {

        PagedMetadata<PageModelDto> result = dePageModelService.getPageModels(EMPTY_REQUEST, params(true));

        PagedMetadata<PageModelDto> expected = localResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void get_all_page_models_with_de_not_excluded_returns_de_and_local_page_models() throws ApsSystemException {

        when(client.getCombinedResult(any())).thenReturn(clientResponse());

        PagedMetadata<PageModelDto> result = dePageModelService.getPageModels(EMPTY_REQUEST, params(false));

        PagedMetadata<PageModelDto> expected = completeResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    private static SearcherDaoPaginatedResult<PageModel> localPageModels() {
        return new SearcherDaoPaginatedResult<>(asList(localPageModel(), localPageModelInstalledFromDE()));
    }

    private PagedMetadata<PageModelDto> localResultPagedMetadata() {
        return new PagedMetadata<>(EMPTY_REQUEST, dtoList(localPageModel(), localPageModelInstalledFromDE()), 2);
    }

    private PagedMetadata<PageModelDto> completeResultPagedMetadata() {
        return new PagedMetadata<>(EMPTY_REQUEST,
                dtoList(localPageModelWithDeInfo(), dePageModelInstalled(), dePageModelNotInstalled()), 3);
    }

    private static ResilientPagedMetadata<DigitalExchangeComponent> clientResponse() {
        ResilientPagedMetadata<DigitalExchangeComponent> response = new ResilientPagedMetadata<>();
        response.setBody(ImmutableList.of(installedComponent(), installableComponent()));
        return response;
    }

    private static PageModel localPageModel() {
        PageModel localPageModel = new PageModel();
        localPageModel.setCode(PAGE_MODEL_CODE);
        return localPageModel;
    }

    private static PageModel localPageModelInstalledFromDE() {
        PageModel pageModel = new PageModel();
        pageModel.setCode(DE_PAGE_MODEL_INSTALLED_CODE);
        return pageModel;
    }

    private DigitalExchangePageModel localPageModelWithDeInfo() {
        DigitalExchangePageModel pageModel = new DigitalExchangePageModel();
        pageModel.setCode(PAGE_MODEL_CODE);
        pageModel.setInstalled(true);
        return pageModel;
    }

    private static PageModel dePageModelInstalled() {
        DigitalExchangePageModel pageModel = new DigitalExchangePageModel();
        pageModel.setCode(DE_PAGE_MODEL_INSTALLED_CODE);
        pageModel.setDigitalExchangeName(EXCHANGE);
        pageModel.setInstalled(true);
        return pageModel;
    }

    private static PageModel dePageModelNotInstalled() {
        DigitalExchangePageModel pageModel = new DigitalExchangePageModel();
        pageModel.setCode(DE_PAGE_MODEL_NOT_INSTALLED_CODE);
        pageModel.setDigitalExchangeName(EXCHANGE);
        pageModel.setInstalled(false);
        return pageModel;
    }

    private static DigitalExchangeComponent installedComponent() {
        DigitalExchangeComponent component = new DigitalExchangeComponent();
        component.setType("pageModel");
        component.setId(DE_PAGE_MODEL_INSTALLED_CODE);
        component.setName(DE_PAGE_MODEL_INSTALLED_CODE);
        component.setDigitalExchangeName(EXCHANGE);
        return component;
    }

    private static DigitalExchangeComponent installableComponent() {
        DigitalExchangeComponent component = new DigitalExchangeComponent();
        component.setType("pageModel");
        component.setId(DE_PAGE_MODEL_NOT_INSTALLED_CODE);
        component.setName(DE_PAGE_MODEL_NOT_INSTALLED_CODE);
        component.setDigitalExchangeName(EXCHANGE);
        return component;
    }

    private static Map<String, String> params(boolean excludeDe) {
        return ImmutableMap.of("excludeDe", String.valueOf(excludeDe));
    }

    private List<PageModelDto> dtoList(PageModel... pageModels) {
        List<PageModelDto> dtoList = new ArrayList<>();
        for (PageModel pageModel : pageModels) {
            dtoList.add(dtoBuilder.convert(pageModel));
        }
        return dtoList;
    }
}

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

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModelDto;
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
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
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
    private static final String EXCHANGE = "Leonardo Exchange";
    private static final String DESCRIPTION_PREFIX = "description_";
    private static final String PLUGIN_CODE_PREFIX = "plugin_code";
    private static final String TEMPLATE = "<h1>Template</h1>";

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
        when(pageModelManager.searchPageModels(any())).thenReturn(FakeData.localPageModels());
        when(client.getCombinedResult(any())).thenReturn(FakeData.clientResponse());
    }

    @Test
    public void excludingDeShouldReturnLocalPageModels() throws ApsSystemException {

        PagedMetadata<PageModelDto> result = dePageModelService.getPageModels(EMPTY_REQUEST, params(true));

        PagedMetadata<PageModelDto> expected = ExpectedResults.localResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldReturnAllPageModels() throws ApsSystemException {

        PagedMetadata<PageModelDto> result = dePageModelService.getPageModels(EMPTY_REQUEST, params(false));

        PagedMetadata<PageModelDto> expected = ExpectedResults.completeResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldFilterByCode() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("code");
        filter.setValue(PAGE_MODEL_CODE);
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        verifyFilter(listRequest, ExpectedResults.localPageModelWithDeInfoDto());
    }

    @Test
    public void shouldFilterByDescription() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("description");
        filter.setValue(DESCRIPTION_PREFIX + DE_PAGE_MODEL_INSTALLED_CODE);
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        verifyFilter(listRequest, ExpectedResults.dePageModelInstalledDto());
    }

    @Test
    public void shouldFilterByMainFrame() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("mainFrame");
        filter.setValue("1");
        listRequest.addFilter(filter);

        verifyFilter(listRequest, ExpectedResults.localPageModelWithDeInfoDto());
    }

    @Test
    public void shouldFilterByPluginCode() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("pluginCode");
        filter.setValue(PLUGIN_CODE_PREFIX + DE_PAGE_MODEL_INSTALLED_CODE);
        listRequest.addFilter(filter);

        verifyFilter(listRequest, ExpectedResults.dePageModelInstalledDto());
    }

    @Test
    public void shouldFilterByTemplate() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("template");
        filter.setValue(TEMPLATE);
        listRequest.addFilter(filter);

        verifyFilter(listRequest, ExpectedResults.localPageModelWithDeInfoDto());
    }

    @Test
    public void shouldFilterByDigitalExchange() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("digitalExchangeName");
        filter.setValue(EXCHANGE);
        listRequest.addFilter(filter);

        verifyFilter(listRequest, ExpectedResults.dePageModelInstalledDto(), ExpectedResults.dePageModelNotInstalledDto());
    }

    @Test
    public void shouldSortByDescription() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("description");
        listRequest.setDirection(FieldSearchFilter.DESC_ORDER);

        verifyFirst(listRequest, ExpectedResults.dePageModelInstalledDto());
    }

    @Test
    public void shouldSortByMainFrame() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("mainFrame");
        listRequest.setDirection(FieldSearchFilter.DESC_ORDER);

        verifyFirst(listRequest, ExpectedResults.dePageModelInstalledDto());
    }

    @Test
    public void shouldSortByPluginCode() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("pluginCode");
        listRequest.setDirection(FieldSearchFilter.DESC_ORDER);

        verifyFirst(listRequest, ExpectedResults.dePageModelInstalledDto());
    }

    @Test
    public void shouldSortByDigitalExchangeName() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("digitalExchangeName");
        listRequest.setDirection(FieldSearchFilter.DESC_ORDER);

        verifyFirst(listRequest, ExpectedResults.dePageModelInstalledDto());
    }

    private void verifyFilter(RestListRequest request, PageModelDto... filteredPageModels) {

        PagedMetadata<PageModelDto> pagedMetadata = dePageModelService.getPageModels(request, params(false));

        assertThat(pagedMetadata.getTotalItems()).isEqualTo(filteredPageModels.length);
        assertThat(pagedMetadata.getBody())
                .isNotNull().hasSize(filteredPageModels.length)
                .containsExactly(filteredPageModels);
    }

    private void verifyFirst(RestListRequest request, PageModelDto pageModel) {

        PagedMetadata<PageModelDto> pagedMetadata = dePageModelService.getPageModels(request, params(false));

        assertThat(pagedMetadata.getBody()).isNotNull().isNotEmpty();
        assertThat(pagedMetadata.getBody().get(0)).isEqualTo(pageModel);
    }

    private static Map<String, String> params(boolean excludeDe) {
        return ImmutableMap.of("excludeDe", String.valueOf(excludeDe));
    }

    private static class FakeData {

        private static SearcherDaoPaginatedResult<PageModel> localPageModels() {
            return new SearcherDaoPaginatedResult<>(asList(localPageModel(), localPageModelInstalledFromDE()));
        }

        private static PageModel localPageModel() {
            PageModel pageModel = new PageModel();
            pageModel.setCode(PAGE_MODEL_CODE);
            pageModel.setDescription(DESCRIPTION_PREFIX + PAGE_MODEL_CODE);
            pageModel.setMainFrame(1);
            pageModel.setTemplate(TEMPLATE);
            pageModel.setPluginCode(PLUGIN_CODE_PREFIX + PAGE_MODEL_CODE);
            return pageModel;
        }

        private static PageModel localPageModelInstalledFromDE() {
            PageModel pageModel = new PageModel();
            pageModel.setCode(DE_PAGE_MODEL_INSTALLED_CODE);
            pageModel.setDescription(DESCRIPTION_PREFIX + DE_PAGE_MODEL_INSTALLED_CODE);
            pageModel.setMainFrame(2);
            pageModel.setPluginCode(PLUGIN_CODE_PREFIX + DE_PAGE_MODEL_INSTALLED_CODE);
            return pageModel;
        }

        private static ResilientPagedMetadata<DigitalExchangeComponent> clientResponse() {
            ResilientPagedMetadata<DigitalExchangeComponent> response = new ResilientPagedMetadata<>();
            response.setBody(ImmutableList.of(installedComponent(), installableComponent()));
            return response;
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
    }

    private static class ExpectedResults {

        private static PageModelDto localPageModelDto() {
            PageModelDto dto = new PageModelDto();
            dto.setCode(PAGE_MODEL_CODE);
            dto.setDescr(DESCRIPTION_PREFIX + PAGE_MODEL_CODE);
            dto.setMainFrame(1);
            dto.setTemplate(TEMPLATE);
            dto.setPluginCode(PLUGIN_CODE_PREFIX + PAGE_MODEL_CODE);
            return dto;
        }

        private static PageModelDto localPageModelInstalledFromDEDto() {
            PageModelDto dto = new PageModelDto();
            dto.setCode(DE_PAGE_MODEL_INSTALLED_CODE);
            dto.setDescr(DESCRIPTION_PREFIX + DE_PAGE_MODEL_INSTALLED_CODE);
            dto.setMainFrame(2);
            dto.setPluginCode(PLUGIN_CODE_PREFIX + DE_PAGE_MODEL_INSTALLED_CODE);
            return dto;
        }

        private static DigitalExchangePageModelDto localPageModelWithDeInfoDto() {
            DigitalExchangePageModelDto dto = new DigitalExchangePageModelDto();
            dto.setCode(PAGE_MODEL_CODE);
            dto.setDescr(DESCRIPTION_PREFIX + PAGE_MODEL_CODE);
            dto.setTemplate(TEMPLATE);
            dto.setMainFrame(1);
            dto.setPluginCode(PLUGIN_CODE_PREFIX + PAGE_MODEL_CODE);
            dto.setInstalled(true);
            return dto;
        }

        private static DigitalExchangePageModelDto dePageModelInstalledDto() {
            DigitalExchangePageModelDto dto = new DigitalExchangePageModelDto();
            dto.setCode(DE_PAGE_MODEL_INSTALLED_CODE);
            dto.setDescr(DESCRIPTION_PREFIX + DE_PAGE_MODEL_INSTALLED_CODE);
            dto.setMainFrame(2);
            dto.setPluginCode(PLUGIN_CODE_PREFIX + DE_PAGE_MODEL_INSTALLED_CODE);
            dto.setDigitalExchangeName(EXCHANGE);
            dto.setInstalled(true);
            return dto;
        }

        private static DigitalExchangePageModelDto dePageModelNotInstalledDto() {
            DigitalExchangePageModelDto dto = new DigitalExchangePageModelDto();
            dto.setCode(DE_PAGE_MODEL_NOT_INSTALLED_CODE);
            dto.setDigitalExchangeName(EXCHANGE);
            return dto;
        }

        private static PagedMetadata<PageModelDto> localResultPagedMetadata() {
            return new PagedMetadata<>(EMPTY_REQUEST, asList(localPageModelDto(), localPageModelInstalledFromDEDto()), 2);
        }

        private static PagedMetadata<PageModelDto> completeResultPagedMetadata() {
            return new PagedMetadata<>(EMPTY_REQUEST,
                    asList(localPageModelWithDeInfoDto(), dePageModelInstalledDto(), dePageModelNotInstalledDto()), 3);
        }
    }
}

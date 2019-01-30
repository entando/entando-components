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
package org.entando.entando.web.digitalexchange;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModel;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDtoBuilder;
import org.entando.entando.aps.system.services.pagemodel.DigitalExchangePageModelService;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModelDtoBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DigitalExchangePageModelServiceTest {

    private static final String PAGE_MODEL_CODE = "TEST_PM_CODE";
    private static final String DE_PAGE_MODEL_CODE = "TEST_DE_PM_CODE";
    private static final String EXCHANGE = "Leonardo's Exchange";

    private static final RestListRequest EMPTY_REQUEST = new RestListRequest();

    @Mock
    IPageModelManager pageModelManager;

    private DigitalExchangePageModelDtoBuilder dtoBuilder;
    private DigitalExchangePageModelService dePageModelService;

    @Before
    public void setUp() throws Exception {
        dtoBuilder = new DigitalExchangePageModelDtoBuilder();
        dePageModelService = new DigitalExchangePageModelService(pageModelManager, dtoBuilder);
    }

    @Test
    public void get_all_page_models_with_de_excluded_returns_local_page_models() throws ApsSystemException {
        when(pageModelManager.searchPageModels(any())).thenReturn(localPageModels());

        PagedMetadata<PageModelDto> result = dePageModelService.getPageModels(EMPTY_REQUEST, getParams(true));

        PagedMetadata<PageModelDto> expected = localResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void get_all_page_models_with_de_not_excluded_returns_de_and_page_models() throws ApsSystemException {

        when(pageModelManager.searchPageModels(any())).thenReturn(localPageModels());

        PagedMetadata<PageModelDto> result = dePageModelService.getPageModels(EMPTY_REQUEST, getParams(false));

        PagedMetadata<PageModelDto> expected = completeResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    private static SearcherDaoPaginatedResult<PageModel> localPageModels() {
        return new SearcherDaoPaginatedResult<>(asList(localPageModel()));
    }

    private PagedMetadata<PageModelDto> localResultPagedMetadata() {
        RestListRequest request = new RestListRequest();

        return new PagedMetadata<>(request, asList(dtoBuilder.convert(localPageModel())), 1);
    }

    private PagedMetadata<PageModelDto> completeResultPagedMetadata() {
        return new PagedMetadata<>(EMPTY_REQUEST,
                asList(dtoBuilder.convert(localPageModel()), dtoBuilder.convert(dePageModel())),
                2);
    }

    private static PageModel localPageModel() {
        DigitalExchangePageModel localPageModel = new DigitalExchangePageModel();
        localPageModel.setCode(PAGE_MODEL_CODE);
        return localPageModel;
    }

    private static PageModel dePageModel() {
        DigitalExchangePageModel pageModel = new DigitalExchangePageModel();
        pageModel.setCode(DE_PAGE_MODEL_CODE);
        pageModel.setDigitalExchange(EXCHANGE);
        return pageModel;
    }

    private Map<String, String> getParams(boolean excludeDe) {
        return ImmutableMap.of("excludeDe", String.valueOf(excludeDe));
    }
}

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
package org.entando.entando.web.pagemodel;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.user.UserDetails;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.pagemodel.DigitalExchangePageModelService;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModel;
import org.entando.entando.aps.system.services.pagemodel.model.DigitalExchangePageModelDtoBuilder;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.pagemodel.validator.PageModelValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DigitalExchangePageModelControllerTest extends AbstractControllerTest {

    private static final String EXCHANGE_NAME = "Leonardo's Exchange";
    private static final String PAGE_MODEL_CODE = "1_TEST_PM";
    private static final String DE_PAGE_MODEL_INSTALLED_CODE = "2_TEST_PM_DE_INSTALLED";
    private static final String DE_PAGE_MODEL_NOT_INSTALLED_CODE = "3_TEST_PM_DE_NOT_INSTALLED";

    private String accessToken;
    private DigitalExchangePageModelDtoBuilder dtoBuilder;

    @Mock
    private DigitalExchangePageModelService pageModelService;

    @Spy
    private PageModelValidator pageModelValidator;

    @InjectMocks
    private PageModelController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        accessToken = mockOAuthInterceptor(user);
        dtoBuilder = new DigitalExchangePageModelDtoBuilder();
    }

    @Test
    public void get_page_models_excluding_de_return_ok() throws Exception {

        when(pageModelService.getPageModels(any(RestListRequest.class), any())).thenReturn(nonDePagedMetadata());

        mockMvc.perform(
                get("/pageModels")
                        .param("excludeDe", "true")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData.totalItems", is(2)))
                .andExpect(jsonPath("$.payload[0].code", is(PAGE_MODEL_CODE)))
                .andExpect(jsonPath("$.payload[0].digitalExchange").doesNotExist())
                .andExpect(jsonPath("$.payload[0].installed").doesNotExist())
                .andExpect(jsonPath("$.payload[1].code", is(DE_PAGE_MODEL_INSTALLED_CODE)))
                .andExpect(jsonPath("$.payload[1].digitalExchange").doesNotExist())
                .andExpect(jsonPath("$.payload[1].installed").doesNotExist());

        RestListRequest restListReq = new RestListRequest();

        verify(pageModelService, times(1)).getPageModels(eq(restListReq), any());
    }

    //@Test
    public void get_all_page_models_return_ok() throws Exception {

        when(pageModelService.getPageModels(any(RestListRequest.class), any())).thenReturn(completePagedMetadata());

        ResultActions result = mockMvc.perform(
                get("/pageModels")
                        .param("excludeDe", "false")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData.totalItems", is(3)))
                .andExpect(jsonPath("$.payload[0].code", is(PAGE_MODEL_CODE)))
                .andExpect(jsonPath("$.payload[0].digitalExchange").doesNotExist())
                .andExpect(jsonPath("$.payload[0].installed", is("true")))
                .andExpect(jsonPath("$.payload[1].code", is(DE_PAGE_MODEL_INSTALLED_CODE)))
                .andExpect(jsonPath("$.payload[1].digitalExchange", is(EXCHANGE_NAME)))
                .andExpect(jsonPath("$.payload[1].installed", is("true")))
                .andExpect(jsonPath("$.payload[2].code", is(DE_PAGE_MODEL_NOT_INSTALLED_CODE)))
                .andExpect(jsonPath("$.payload[2].digitalExchange", is(EXCHANGE_NAME)))
                .andExpect(jsonPath("$.payload[2].installed", is("false")));

        RestListRequest restListReq = new RestListRequest();

        verify(pageModelService, times(1)).getPageModels(eq(restListReq), any());
    }

    private PagedMetadata<PageModelDto> completePagedMetadata() {
        return createPagedMetadata(ImmutableList.of(localPageModelWithDeInfo(), dePageModelInstalled(), dePageModelNotInstalled()));
    }

    private PagedMetadata<PageModelDto> nonDePagedMetadata() {
        return createPagedMetadata(ImmutableList.of(localPageModel(), localPageModelInstalledFromDE()));
    }

    private PagedMetadata<PageModelDto> createPagedMetadata(List<PageModel> pageModels) {
        SearcherDaoPaginatedResult<PageModel> paginatedResult
                = new SearcherDaoPaginatedResult<>(pageModels);

        PagedMetadata<PageModelDto> metadata = new PagedMetadata<>(new RestListRequest(), paginatedResult);

        metadata.setBody(bodyFrom(pageModels));

        return metadata;
    }

    private List<PageModelDto> bodyFrom(List<PageModel> pageModels) {
        List<PageModelDto> pageModelDtos = new ArrayList<>();

        for (PageModel pageModel : pageModels) {
            pageModelDtos.add(dtoBuilder.convert(pageModel));
        }

        return pageModelDtos;
    }

    private PageModel localPageModel() {
        PageModel pageModel = new PageModel();
        pageModel.setCode(PAGE_MODEL_CODE);
        return pageModel;
    }

    private PageModel localPageModelInstalledFromDE() {
        PageModel pageModel = new PageModel();
        pageModel.setCode(DE_PAGE_MODEL_INSTALLED_CODE);
        return pageModel;
    }

    private DigitalExchangePageModel localPageModelWithDeInfo() {
        DigitalExchangePageModel pageModel = new DigitalExchangePageModel();
        pageModel.setCode(PAGE_MODEL_CODE);
        return pageModel;
    }

    private DigitalExchangePageModel dePageModelInstalled() {
        DigitalExchangePageModel pageModel = new DigitalExchangePageModel();
        pageModel.setCode(DE_PAGE_MODEL_INSTALLED_CODE);
        pageModel.setDigitalExchangeName(EXCHANGE_NAME);
        return pageModel;
    }

    private DigitalExchangePageModel dePageModelNotInstalled() {
        DigitalExchangePageModel pageModel = new DigitalExchangePageModel();
        pageModel.setCode(DE_PAGE_MODEL_NOT_INSTALLED_CODE);
        pageModel.setDigitalExchangeName(EXCHANGE_NAME);
        return pageModel;
    }
}

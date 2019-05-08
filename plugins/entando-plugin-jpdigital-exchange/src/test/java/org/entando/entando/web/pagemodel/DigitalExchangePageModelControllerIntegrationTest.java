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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.PageModelManager;
import com.agiletec.aps.system.services.user.User;
import com.google.common.collect.ImmutableList;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesMocker;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;
import org.entando.entando.crypt.DefaultTextEncryptor;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("DEPageModelsTest")
public class DigitalExchangePageModelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String USERNAME = "jack_bauer";
    private static final String PASSWORD = "0x24";

    private static final String PAGE_MODEL_CODE = "1_TEST_PM_CODE";
    private static final String DE_PAGE_MODEL_CODE = "2_TEST_DE_PM_CODE";

    private String accessToken;

    @Autowired
    private PageModelManager pageModelManager;

    @Configuration
    @Profile("DEPageModelsTest")
    public static class TestConfig {

        @Bean
        @Primary
        public DigitalExchangeOAuth2RestTemplateFactory getRestTemplateFactory() {
            return new DigitalExchangesMocker()
                    .addDigitalExchange(DE_1_ID, req -> DEResponse())
                    .addDigitalExchange(DE_2_ID, () -> {
                        throw new ResourceAccessException("Connection Refused");
                    })
                    .initMocks();
        }
        
        @Bean
        @Primary
        public TextEncryptor getDigitalExchangeTextEncryptor() {
            return new DefaultTextEncryptor("changeit");
        }
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        setupAuthenticationDetails();
        deletePageModelsFromPreviousTests();
    }

    private void setupAuthenticationDetails() {
        User user = new OAuth2TestUtils.UserBuilder(USERNAME, PASSWORD)
                .grantedToRoleAdmin()
                .build();

        accessToken = mockOAuthInterceptor(user);
    }

    private void deletePageModelsFromPreviousTests() throws ApsSystemException {
        pageModelManager.deletePageModel(PAGE_MODEL_CODE);
    }

    @Test
    public void get_all_page_models_return_OK() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/pageModels")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void get_DE_page_model_return_OK() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/pageModels")
                        .param("filters[0].attribute", "code")
                        .param("filters[0].value", DE_PAGE_MODEL_CODE)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData.totalItems", is(1)))
                .andExpect(jsonPath("$.payload[0].code", is(DE_PAGE_MODEL_CODE)))
                .andExpect(jsonPath("$.payload[0].digitalExchange", is(DE_1_NAME)));
    }

    @Test
    public void get_DE_page_model_excluding_DE_return_OK_empty() throws Exception {

        ResultActions result = mockMvc.perform(
                get("/pageModels")
                        .param("excludeDe", "true")
                        .param("filters[0].attribute", "code")
                        .param("filters[0].value", DE_PAGE_MODEL_CODE)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData.totalItems", is(0)))
                .andExpect(jsonPath("$.payload", empty()));
    }

    private static PagedRestResponse<DigitalExchangeComponent> DEResponse() {

        PagedMetadata<DigitalExchangeComponent> pagedMetadata = new PagedMetadata<>();

        DigitalExchangeComponent component = new DigitalExchangeComponent();
        component.setType("pageModel");
        component.setId(DE_PAGE_MODEL_CODE);
        component.setName(DE_PAGE_MODEL_CODE);

        pagedMetadata.setBody(ImmutableList.of(component));

        return new PagedRestResponse<>(pagedMetadata);
    }
}

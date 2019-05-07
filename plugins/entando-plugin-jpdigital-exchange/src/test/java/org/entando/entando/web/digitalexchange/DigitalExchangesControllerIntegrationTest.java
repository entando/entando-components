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
package org.entando.entando.web.digitalexchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;

import com.jayway.jsonpath.JsonPath;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactory;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesMocker;
import org.entando.entando.web.common.IgnoreJacksonWriteOnlyAccess;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResourceAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyArray;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("DEconfigTest")
public class DigitalExchangesControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URL = "/digitalExchange/exchanges";

    @Autowired
    private DigitalExchangesService digitalExchangeService;

    @Configuration
    @Profile("DEconfigTest")
    public static class TestConfig {

        @Bean
        @Primary
        public DigitalExchangeOAuth2RestTemplateFactory getRestTemplateFactory() {
            return new DigitalExchangesMocker()
                    .addDigitalExchange(DE_1_ID, new SimpleRestResponse<>(
                            ImmutableMap.of(DE_1_ID, new ArrayList<>())))
                    .addDigitalExchange(DE_2_ID, () -> {
                        throw new ResourceAccessException("Connection Refused");
                    })
                    .initMocks();
        }
    }

    @Test
    public void shouldReturnDigitalExchanges() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL)).execute();

        result.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload", hasSize(2)))
                .andExpect(jsonPath("$.payload[0].id", is(DE_1_ID)))
                .andExpect(jsonPath("$.payload[0].name", is(DE_1_NAME)))
                .andExpect(jsonPath("$.payload[0].key", is("client-key")))
                .andExpect(jsonPath("$.payload[0].secret").doesNotExist());

        // Verify that the secret has been correctly decrypted even if it is not serialized
        assertThat(digitalExchangeService.getDigitalExchanges().get(0).getClientSecret())
                .isEqualTo("client-secret");
    }

    @Test
    public void testCRUDDigitalExchange() throws Exception {

        String generatedId = null;
        String clientSecret = "my-secret";

        ObjectMapper ignoreWriteOnlyMapper = new ObjectMapper();
        ignoreWriteOnlyMapper.setAnnotationIntrospector(new IgnoreJacksonWriteOnlyAccess());

        try {
            // Create
            DigitalExchange digitalExchange = getNewDE();
            digitalExchange.setClientSecret(clientSecret);

            ResultActions result = createAuthRequest(post(BASE_URL)
                    .content(ignoreWriteOnlyMapper.writeValueAsString(digitalExchange)))
                    .execute();

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.metaData").isEmpty())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.payload.name", is(digitalExchange.getName())))
                    .andExpect(jsonPath("$.payload.secret").doesNotExist());

            String jsonResponse = result.andReturn().getResponse().getContentAsString();

            SimpleRestResponse<DigitalExchange> response = new ObjectMapper()
                    .readValue(jsonResponse, new TypeReference<SimpleRestResponse<DigitalExchange>>() {
                    });

            generatedId = response.getPayload().getId();
            assertThat(generatedId).isNotNull().hasSize(20);
            digitalExchange.setId(generatedId);

            // Verify that the secret has been correctly decrypted even if it is not serialized
            assertThat(digitalExchangeService.findById(generatedId).getClientSecret())
                    .isEqualTo(clientSecret);

            // Read
            result = createAuthRequest(get(BASE_URL + "/{id}", generatedId)).execute();

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.metaData").isEmpty())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.payload.id", is(generatedId)));

            // Update
            String url = "http://www.entando.com/";
            digitalExchange.setUrl(url);
            result = createAuthRequest(put(BASE_URL + "/{id}", generatedId)
                    .content(ignoreWriteOnlyMapper.writeValueAsString(digitalExchange)))
                    .execute();

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.metaData").isEmpty())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.payload.id", is(generatedId)))
                    .andExpect(jsonPath("$.payload.url", is(url)))
                    .andExpect(jsonPath("$.payload.secret").doesNotExist());

            // Delete
            result = createAuthRequest(delete(BASE_URL + "/{id}", generatedId)).execute();

            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.metaData").isEmpty())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.payload", is(generatedId)));

        } catch (Exception ex) {
            if (generatedId != null) {
                digitalExchangeService.delete(generatedId);
            }
            throw ex;
        }
    }

    @Test
    public void shouldFailCreatingDigitalExchangeWithGivenId() throws Exception {

        DigitalExchange digitalExchange = getDigitalExchange("chosen_id", NEW_DE_NAME);

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(digitalExchange).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_ID_AUTOGENERATED)));
    }

    @Test
    public void shouldFailCreatingDigitalExchangeBecauseNameAlreadyTaken() throws Exception {
        DigitalExchange digitalExchange = getDigitalExchange(null, DE_1_NAME);

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(digitalExchange).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_NAME_TAKEN)));
    }

    @Test
    public void shouldSaveInactiveDigitalExchangeBecauseNoPublicKeyAvailable() throws Exception {

        String generatedId = null;
        try {
            DigitalExchange digitalExchange = getNewDE();
            digitalExchange.setPublicKey(null);

            ResultActions result = createAuthRequest(post(BASE_URL))
                    .setContent(digitalExchange).execute();

            MvcResult mvcResult = result.andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.errors").isNotEmpty())
                    .andReturn();

            generatedId = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.payload.id");
        } catch (Throwable ex) {
            throw ex;
        } finally {
            if (generatedId != null) {
                digitalExchangeService.delete(generatedId);
            }
        }
    }

    @Test
    public void shouldFailRenamingIfNameAlreadyTaken() throws Exception {

        DigitalExchange digitalExchange = getDigitalExchange(DE_1_ID, DE_2_NAME);

        ResultActions result = createAuthRequest(put(BASE_URL + "/{id}", DE_1_ID))
                .setContent(digitalExchange).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_NAME_TAKEN)));
    }

    @Test
    public void shouldFailFindingDigitalExchange() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/{id}", INEXISTENT_DE_ID)).execute();

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND)))
                .andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldFailUpdatingDigitalExchangeBecauseNotFound() throws Exception {

        DigitalExchange digitalExchange = getInexistentDE();

        ResultActions result = createAuthRequest(put(BASE_URL + "/{id}", digitalExchange.getId()))
                .setContent(digitalExchange).execute();

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND)))
                .andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldFailDeletingDigitalExchangeBecauseNotFound() throws Exception {

        ResultActions result = createAuthRequest(delete(BASE_URL + "/{id}", INEXISTENT_DE_ID)).execute();

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_NOT_FOUND)))
                .andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldTestInstance() throws Exception {

        // working instance
        ResultActions result = createAuthRequest(get(BASE_URL + "/test/{id}", DE_1_ID)).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload", is("OK")));

        // unreachable instance
        result = createAuthRequest(get(BASE_URL + "/test/{id}", DE_2_ID)).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.payload").isEmpty());
    }

    public void shouldTestAllInstances() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/test")).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload", hasEntry(is(DE_1_ID), emptyArray())))
                .andExpect(jsonPath("$.payload", hasEntry(is(DE_2_ID), hasSize(1))));
    }
}

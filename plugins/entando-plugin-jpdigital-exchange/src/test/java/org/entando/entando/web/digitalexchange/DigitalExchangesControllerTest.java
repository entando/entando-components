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

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Spy;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.empty;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;

public class DigitalExchangesControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/digitalExchange/exchanges";

    @Mock
    private DigitalExchangesService service;

    @Spy
    private DigitalExchangeValidator digitalExchangeValidator;

    @InjectMocks
    private DigitalExchangesResourceController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();

        when(service.getDigitalExchanges()).thenReturn(getFakeDigitalExchanges());
        when(service.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(service.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(service.findById(any())).thenReturn(getFakeDigitalExchanges().get(0));
        when(service.test(any())).thenReturn(new ArrayList<>());
        when(service.testAll()).thenReturn(
                ImmutableMap.of(DE_1_ID, new ArrayList<>(), DE_2_ID, new ArrayList<>()));
    }

    @Test
    public void shouldCreateDigitalExchangeWithGivenId() throws Exception {

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getNewDE()).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload.id", is(NEW_DE_ID)))
                .andExpect(jsonPath("$.payload.name", is(NEW_DE_NAME)));
    }

    @Test
    public void shouldFailCreatingDigitalExchangeBecauseNameIsEmpty() throws Exception {

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getDigitalExchange(null, "")).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldFailCreatingDigitalExchangeBecauseIdIsNotAlphanumeric() throws Exception {

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(getDigitalExchange("Spaces not allowed", NEW_DE_NAME)).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldFailCreatingDigitalExchangeBecauseURLIsNotSet() throws Exception {
        DigitalExchange digitalExchange = getDigitalExchange(null, NEW_DE_NAME);
        digitalExchange.setUrl(null);

        ResultActions result = createAuthRequest(post(BASE_URL))
                .setContent(digitalExchange).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldReturnDigitalExchanges() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL)).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload", hasSize(2)))
                .andExpect(jsonPath("$.payload[0].id", is(DE_1_ID)))
                .andExpect(jsonPath("$.payload[0].name", is(DE_1_NAME)))
                .andExpect(jsonPath("$.payload[1].id", is(DE_2_ID)))
                .andExpect(jsonPath("$.payload[1].name", is(DE_2_NAME)));
    }

    @Test
    public void shouldFindDigitalExchange() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/{id}", DE_1_ID)).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload.id", is(DE_1_ID)))
                .andExpect(jsonPath("$.payload.name", is(DE_1_NAME)));
    }

    @Test
    public void shouldUpdateDigitalExchangeName() throws Exception {

        String newName = "DE Renamed";
        DigitalExchange digitalExchange = getDigitalExchange(DE_1_ID, newName);

        ResultActions result = createAuthRequest(put(BASE_URL + "/{id}", DE_1_ID))
                .setContent(digitalExchange).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload.id", is(DE_1_ID)))
                .andExpect(jsonPath("$.payload.name", is(newName)));
    }

    @Test
    public void shouldFailUpdatingDigitalExchangeBecauseIdMismatch() throws Exception {

        DigitalExchange digitalExchange = getDigitalExchange(DE_1_ID, DE_1_NAME);

        ResultActions result = createAuthRequest(put(BASE_URL + "/{id}", "Different_ID"))
                .setContent(digitalExchange).execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(DigitalExchangeValidator.ERRCODE_URI_ID_MISMATCH)))
                .andExpect(jsonPath("$.payload").isEmpty());
    }

    @Test
    public void shouldDeleteDigitalExchange() throws Exception {

        String id = DE_1_ID;
        ResultActions result = createAuthRequest(delete(BASE_URL + "/{id}", id)).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload", is(id)));
    }

    @Test
    public void shouldTestInstance() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/test/{id}", DE_1_ID)).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload", is("OK")));
    }

    @Test
    public void shouldTestAllInstances() throws Exception {

        ResultActions result = createAuthRequest(get(BASE_URL + "/test")).execute();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.metaData").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.payload", hasEntry(is(DE_1_ID), empty())))
                .andExpect(jsonPath("$.payload", hasEntry(is(DE_2_ID), empty())));
    }

    private List<DigitalExchange> getFakeDigitalExchanges() {
        List<DigitalExchange> digitalExchanges = new ArrayList<>();
        digitalExchanges.add(getDE1());
        digitalExchanges.add(getDE2());
        return digitalExchanges;
    }
}

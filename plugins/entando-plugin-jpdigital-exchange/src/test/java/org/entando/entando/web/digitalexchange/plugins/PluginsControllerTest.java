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
package org.entando.entando.web.digitalexchange.plugins;

import java.util.Optional;
import org.entando.entando.aps.system.services.digitalexchange.plugins.PluginClient;
import org.entando.entando.web.AbstractControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PluginsControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/plugins";
    private static final String PLUGIN_ID = "test_plugin";

    @Mock
    private PluginClient pluginClient;

    @InjectMocks
    private PluginsResourceController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();

        ResponseEntity<?> mockedResponse = ResponseEntity.ok("");
        doReturn(mockedResponse).when(pluginClient).call(any(), any(), any(), any(), any());
    }

    @Test
    public void testForwardGetRequest() throws Exception {

        ResultActions result = createAuthRequest(
                get(BASE_URL + "/{pluginId}/other/endpoint", PLUGIN_ID))
                .execute();

        result.andDo(print()).andExpect(status().isOk());

        verify(pluginClient, times(1)).call(eq(PLUGIN_ID), eq("/other/endpoint"),
                eq(Optional.empty()), eq(HttpMethod.GET), any());
    }

    @Test
    public void testForwardPostRequest() throws Exception {

        ResultActions result = createAuthRequest(
                post(BASE_URL + "/{pluginId}/other/endpoint", PLUGIN_ID)
                        .content("body"))
                .execute();

        result.andDo(print()).andExpect(status().isOk());

        ArgumentCaptor<Optional<String>> optionalCaptor = ArgumentCaptor.forClass(Optional.class);
        verify(pluginClient, times(1)).call(eq(PLUGIN_ID), eq("/other/endpoint"),
                optionalCaptor.capture(), eq(HttpMethod.POST), any());
        assertThat(optionalCaptor.getValue().get()).isEqualTo("body");
    }
}

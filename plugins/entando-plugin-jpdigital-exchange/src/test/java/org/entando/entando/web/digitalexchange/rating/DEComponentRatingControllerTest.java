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
package org.entando.entando.web.digitalexchange.rating;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.entando.entando.aps.system.services.digitalexchange.rating.DEComponentRatingResult;
import org.entando.entando.aps.system.services.digitalexchange.rating.DERatingService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.RestError;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;
import static org.mockito.ArgumentMatchers.any;

public class DEComponentRatingControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/digitalExchange/rate";

    private static final String COMPONENT_ID = "component_id";

    @Mock
    private DERatingService ratingService;

    @InjectMocks
    private DEComponentRatingResourceController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void shouldFailIfRatingIsLowerThan1() throws Exception {
        DERatingValue ratingValue = new DERatingValue(0);

        ResultActions result = createAuthRequest(
                post(BASE_URL + "/{exchangeId}/{componentId}", DE_1_ID, COMPONENT_ID))
                .setContent(ratingValue)
                .execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldFailIfRatingIsGreaterThan5() throws Exception {
        DERatingValue ratingValue = new DERatingValue(6);

        ResultActions result = createAuthRequest(
                post(BASE_URL + "/{exchangeId}/{componentId}", DE_1_ID, COMPONENT_ID))
                .setContent(ratingValue)
                .execute();

        result.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void shouldFailRatingWhenMissingUserInformation() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        ResponseEntity<?> response = controller.rateComponent("exchange", "component", new DERatingValue(4), request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testRatingNotSupported() throws Exception {

        DEComponentRatingResult componentRatingResult = new DEComponentRatingResult();
        componentRatingResult.setRatingUnsupported();

        when(ratingService.rateComponent(any(), any())).thenReturn(componentRatingResult);

        DERatingValue ratingValue = new DERatingValue(3);

        ResultActions result = createAuthRequest(
                post(BASE_URL + "/{exchangeId}/{componentId}", DE_1_ID, COMPONENT_ID))
                .setContent(ratingValue)
                .execute();

        result.andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void testForwardError() throws Exception {

        List<RestError> errors = Collections.singletonList(new RestError());

        DEComponentRatingResult componentRatingResult = new DEComponentRatingResult();
        componentRatingResult.setErrors(errors);

        when(ratingService.rateComponent(any(), any())).thenReturn(componentRatingResult);

        DERatingValue ratingValue = new DERatingValue(3);

        ResultActions result = createAuthRequest(
                post(BASE_URL + "/{exchangeId}/{componentId}", DE_1_ID, COMPONENT_ID))
                .setContent(ratingValue)
                .execute();

        result.andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errors", hasSize(errors.size())));
    }
}

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
package org.entando.entando.aps.system.services.digitalexchange;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.digitalexchange.DigitalExchangeValidator;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.common.model.RestError;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;

public class DigitalExchangesServiceTest {

    @Mock
    private DigitalExchangesManager manager;

    @Mock
    private DigitalExchangesClient client;

    @InjectMocks
    private DigitalExchangesServiceImpl service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockManager();
        mockClient();
    }

    @Test
    public void shouldReturnDigitalExchanges() {
        assertThat(service.getDigitalExchanges())
                .isNotNull().hasSize(1);
    }

    @Test
    public void shouldFindDigitalExchange() {
        assertNotNull(service.findById(DE_1_ID));
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFindNothing() {
        service.findById(INEXISTENT_DE_ID);
    }

    @Test
    public void shouldAddDigitalExchange() {
        service.create(getDE2());
    }

    @Test(expected = ValidationConflictException.class)
    public void shouldFailAddingDigitalExchange() {
        try {
            service.create(getDE1());
        } catch (ValidationConflictException ex) {
            assertEquals(1, ex.getBindingResult().getErrorCount());
            assertEquals(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_ALREADY_EXISTS,
                    ex.getBindingResult().getAllErrors().get(0).getCode());
            throw ex;
        }
    }

    @Test
    public void shouldUpdateDigitalExchange() {
        service.update(getDE1());
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFailUpdatingDigitalExchange() {
        service.update(getInexistentDE());
    }

    @Test
    public void shouldDeleteDigitalExchange() {
        service.delete(DE_1_ID);
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void shouldFailDeletingDigitalExchange() {
        service.delete(INEXISTENT_DE_ID);
    }

    @Test(expected = ValidationConflictException.class)
    public void shouldFailCreatingDigitalExchangeWithInvalidURL() {
        try {
            DigitalExchange digitalExchange = getNewDE();
            digitalExchange.setUrl("invalid-url");
            service.create(digitalExchange);
        } catch (ValidationConflictException ex) {
            assertEquals(1, ex.getBindingResult().getErrorCount());
            assertEquals(DigitalExchangeValidator.ERRCODE_DIGITAL_EXCHANGE_INVALID_URL,
                    ex.getBindingResult().getAllErrors().get(0).getCode());
            throw ex;
        }
    }

    @Test
    public void shouldTestInstance() {
        assertThat(service.test(DE_1_ID)).isEmpty();
    }

    @Test
    public void shouldTestAllInstances() {
        assertThat(service.testAll()).hasSize(1);
    }

    private void mockManager() {

        List<DigitalExchange> exchanges = Arrays.asList(getDE1());

        when(manager.getDigitalExchanges()).thenReturn(exchanges);

        when(manager.findById(any(String.class)))
                .thenAnswer(invocation -> {
                    String id = invocation.getArgument(0);
                    return exchanges.stream().filter(de -> de.getId().equals(id)).findFirst();
                });
    }

    private void mockClient() {
        when(client.getSingleResponse(any(DigitalExchange.class), any()))
                .thenReturn(new SimpleRestResponse<>(ImmutableMap.of("OK", new ArrayList<>())));

        Map<String, List<RestError>> testAllResult = new HashMap<>();
        testAllResult.put(DE_1_ID, new ArrayList<>());
        when(client.getCombinedResult(any())).thenReturn(testAllResult);
    }
}

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
package org.entando.entando.aps.system.services.digitalexchange.component;

import java.util.Arrays;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClientMocker;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.FilterOperator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DigitalExchangeComponentsServiceTest {

    private static final String[] COMPONENTS_1 = new String[]{"A", "B", "C", "F", "I", "M", "N", "P"};
    private static final String[] COMPONENTS_2 = new String[]{"D", "E", "G", "H", "L", "O"};

    @Mock
    private DigitalExchangesService exchangesService;

    private DigitalExchangeComponentsServiceImpl service;

    @Before
    public void setUp() {
        DigitalExchangesClientMocker clientMocker = new DigitalExchangesClientMocker();
        clientMocker.getDigitalExchangesMocker()
                .addDigitalExchange(DE_1_ID, DigitalExchangeComponentsMocker.mock(COMPONENTS_1))
                .addDigitalExchange(DE_2_ID, DigitalExchangeComponentsMocker.mock(COMPONENTS_2));

        DigitalExchangesClient mockedClient = clientMocker.build();

        when(exchangesService.findById(DE_1_ID)).thenReturn(getDE1());
        when(exchangesService.findById(DE_2_ID)).thenReturn(getDE2());

        service = new DigitalExchangeComponentsServiceImpl(mockedClient, exchangesService);
    }

    @Test
    public void shouldPaginate() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setPageSize(3);
        listRequest.setSort("name");

        listRequest.setPage(1);
        new PageVerifier(service.getComponents(listRequest))
                .contains(DE_1_NAME, "A")
                .contains(DE_1_NAME, "B")
                .contains(DE_1_NAME, "C");

        listRequest.setPage(2);
        new PageVerifier(service.getComponents(listRequest))
                .contains(DE_2_NAME, "D")
                .contains(DE_2_NAME, "E")
                .contains(DE_1_NAME, "F");

        listRequest.setPage(3);
        new PageVerifier(service.getComponents(listRequest))
                .contains(DE_2_NAME, "G")
                .contains(DE_2_NAME, "H")
                .contains(DE_1_NAME, "I");

        listRequest.setPage(4);
        new PageVerifier(service.getComponents(listRequest))
                .contains(DE_2_NAME, "L")
                .contains(DE_1_NAME, "M")
                .contains(DE_1_NAME, "N");

        listRequest.setPage(5);
        new PageVerifier(service.getComponents(listRequest))
                .contains(DE_2_NAME, "O")
                .contains(DE_1_NAME, "P");
    }

    @Test
    public void shouldFilter() {

        RestListRequest listRequest = new RestListRequest();

        Filter filter = new Filter();
        filter.setAttribute("name");
        filter.setValue("M");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata = service.getComponents(listRequest);

        assertNotNull(pagedMetadata.getBody());
        assertEquals(1, pagedMetadata.getBody().size());
        assertEquals("M", pagedMetadata.getBody().get(0).getName());
        assertEquals(1, pagedMetadata.getTotalItems());
    }

    private static class PageVerifier {

        private final ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata;
        private int index = 0;

        public PageVerifier(ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata) {
            this.pagedMetadata = pagedMetadata;
            assertThat(pagedMetadata.getTotalItems()).isEqualTo(COMPONENTS_1.length + COMPONENTS_2.length);
        }

        public PageVerifier contains(Object... values) {
            assertThat(pagedMetadata.getBody().get(index++))
                    .extracting(DigitalExchangeComponent::getDigitalExchange, DigitalExchangeComponent::getName)
                    .contains(values);
            return this;
        }
    }
}

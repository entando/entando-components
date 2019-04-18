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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.entando.entando.aps.system.init.model.ComponentInstallationReport;
import org.entando.entando.aps.system.init.IInitializerManager;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
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
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DigitalExchangeComponentsServiceTest {

    private static final DigitalExchangeComponent[] COMPONENTS_1 = getMockedComponents1();
    private static final String[] COMPONENTS_2 = new String[]{"D", "E", "G", "H", "L", "O"};
    private static final String INSTALLED_COMPONENT = "A";
    private static final String LAST_UPDATE = "2018-01-01 00:00:00";
    private static final String VERSION = "V1";
    private static final String WIDGET_TYPE = "widget";
    private static final String CONTENT_MODEL_TYPE = "contentModel";
    private static final int RATING = 3;

    @Mock
    private DigitalExchangesService exchangesService;

    @Mock
    private IInitializerManager initializerManager;

    @Mock
    private SystemInstallationReport installationReport;

    @Mock
    private ComponentInstallationReport componentInstallationReport;

    private DigitalExchangeComponentsServiceImpl service;

    @Before
    public void setUp() {
        DigitalExchangesClientMocker clientMocker = new DigitalExchangesClientMocker();
        clientMocker.getDigitalExchangesMocker()
                .addDigitalExchange(DE_1_ID, DigitalExchangeComponentsMocker.mock(COMPONENTS_1))
                .addDigitalExchange(DE_2_ID, DigitalExchangeComponentsMocker.mock(COMPONENTS_2));

        DigitalExchangesClient mockedClient = clientMocker.build();

        when(componentInstallationReport.getStatus()).thenReturn(SystemInstallationReport.Status.OK);
        
        when(initializerManager.getCurrentReport()).thenReturn(installationReport);
        when(installationReport.getComponentReport(INSTALLED_COMPONENT, false))
                .thenReturn(componentInstallationReport);

        when(exchangesService.findById(DE_1_ID)).thenReturn(getDE1());
        when(exchangesService.findById(DE_2_ID)).thenReturn(getDE2());

        service = new DigitalExchangeComponentsServiceImpl(mockedClient, exchangesService, initializerManager);
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
    public void shouldFilterById() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setValue("B");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        verifyFilter(listRequest, COMPONENTS_1[1]);
    }

    @Test
    public void shouldFilterByName() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("name");
        filter.setValue("M");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        verifyFilter(listRequest, COMPONENTS_1[5]);
    }

    @Test
    public void shouldFilterByLastUpdate() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("lastUpdate");
        filter.setValue("2019-01-01 00:00:00");
        filter.setOperator(FilterOperator.LOWER.getValue());
        listRequest.addFilter(filter);

        verifyFilter(listRequest, COMPONENTS_1[1]);
    }

    @Test
    public void shouldFilterByVersion() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("version");
        filter.setValue(VERSION);
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        verifyFilter(listRequest, COMPONENTS_1[2]);
    }

    @Test
    public void shouldFilterByType() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("type");
        filter.setAllowedValues(new String[]{WIDGET_TYPE, CONTENT_MODEL_TYPE});
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        verifyFilter(listRequest, COMPONENTS_1[3], COMPONENTS_1[4]);
    }

    @Test
    public void shouldFilterByRating() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("rating");
        filter.setValue("4");
        filter.setOperator(FilterOperator.LOWER.getValue());
        listRequest.addFilter(filter);

        verifyFilter(listRequest, COMPONENTS_1[5]);
    }

    @Test
    public void shouldFilterByInstalled() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("installed");
        filter.setValue("true");
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata = service.getComponents(listRequest);

        assertThat(pagedMetadata.getTotalItems()).isEqualTo(1);
        assertThat(pagedMetadata.getBody().get(0).getName()).isEqualTo(INSTALLED_COMPONENT);
    }

    @Test
    public void shouldFilterByDigitalExchangeName() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("digitalExchangeName");
        filter.setValue(DE_1_NAME);
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata = service.getComponents(listRequest);

        assertThat(pagedMetadata.getTotalItems()).isEqualTo(COMPONENTS_1.length);
    }

    @Test
    public void shouldFilterByDigitalExchangeId() {

        RestListRequest listRequest = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("digitalExchangeId");
        filter.setValue(DE_2_ID);
        filter.setOperator(FilterOperator.EQUAL.getValue());
        listRequest.addFilter(filter);

        ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata = service.getComponents(listRequest);

        assertThat(pagedMetadata.getTotalItems()).isEqualTo(COMPONENTS_2.length);
    }

    @Test
    public void shouldSortByLastUpdate() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("lastUpdate");

        verifyFirst(listRequest, COMPONENTS_1[1]);
    }

    @Test
    public void shouldSortByVersion() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("version");
        listRequest.setDirection(FieldSearchFilter.DESC_ORDER);

        verifyFirst(listRequest, COMPONENTS_1[2]);
    }

    @Test
    public void shouldSortByType() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("type");

        verifyFirst(listRequest, COMPONENTS_1[4]);
    }

    @Test
    public void shouldSortByRating() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("rating");

        verifyFirst(listRequest, COMPONENTS_1[5]);
    }

    @Test
    public void shouldSortByInstalled() {

        RestListRequest listRequest = new RestListRequest();
        listRequest.setSort("installed");
        listRequest.setDirection(FieldSearchFilter.DESC_ORDER);

        verifyFirst(listRequest, COMPONENTS_1[0]);
    }

    private static class PageVerifier {

        private final ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata;
        private int index = 0;

        public PageVerifier(ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata) {
            this.pagedMetadata = pagedMetadata;
            assertThat(pagedMetadata.getTotalItems()).isEqualTo(COMPONENTS_1.length + COMPONENTS_2.length);
            pagedMetadata.getBody().forEach(component -> {
                if (INSTALLED_COMPONENT.equals(component.getId())) {
                    assertThat(component.isInstalled()).isTrue();
                } else {
                    assertThat(component.isInstalled()).isFalse();
                }
            });
        }

        public PageVerifier contains(Object... values) {
            assertThat(pagedMetadata.getBody().get(index++))
                    .extracting(DigitalExchangeComponent::getDigitalExchangeName, DigitalExchangeComponent::getName)
                    .contains(values);
            return this;
        }
    }

    private void verifyFilter(RestListRequest request, DigitalExchangeComponent... filteredComponents) {

        ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata = service.getComponents(request);

        assertThat(pagedMetadata.getTotalItems()).isEqualTo(filteredComponents.length);
        assertThat(pagedMetadata.getBody())
                .isNotNull().hasSize(filteredComponents.length)
                .containsExactly(filteredComponents);
    }

    private void verifyFirst(RestListRequest request, DigitalExchangeComponent component) {
        ResilientPagedMetadata<DigitalExchangeComponent> pagedMetadata = service.getComponents(request);
        assertThat(pagedMetadata.getBody()).isNotNull().isNotEmpty();
        assertThat(pagedMetadata.getBody().get(0)).isEqualTo(component);
    }

    private static DigitalExchangeComponent[] getMockedComponents1() {
        List<DigitalExchangeComponent> components
                = DigitalExchangeComponentsMocker.getMockedComponents("A", "B", "C", "F", "I", "M", "N", "P");

        // Some values for testing filtering and sorting
        setLastUpdate(components.get(1));
        components.get(2).setVersion(VERSION);
        components.get(3).setType(WIDGET_TYPE);
        components.get(4).setType(CONTENT_MODEL_TYPE);
        components.get(5).setRating(RATING);

        return components.toArray(new DigitalExchangeComponent[components.size()]);
    }

    private static void setLastUpdate(DigitalExchangeComponent component) {
        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
        try {
            component.setLastUpdate(sdf.parse(LAST_UPDATE));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}

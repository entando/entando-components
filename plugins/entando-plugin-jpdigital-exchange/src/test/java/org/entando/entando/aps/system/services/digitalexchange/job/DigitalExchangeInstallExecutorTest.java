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
package org.entando.entando.aps.system.services.digitalexchange.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import org.entando.entando.aps.system.init.DatabaseManager;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClientMocker;
import org.junit.AfterClass;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.DE_1_ID;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DigitalExchangeInstallExecutorTest {

    private static File packageFile;

    private DigitalExchangesClient client;

    @Mock
    private ComponentStorageManager storageManager;

    @Mock
    private DatabaseManager databaseManager;

    @Mock
    private InitializerManager initializerManager;

    @Mock
    private CommandExecutor commandExecutor;

    private DigitalExchangeInstallExecutor installExecutor;

    private int calls;

    @BeforeClass
    public static void setUpClass() {
        packageFile = ComponentZipUtil.getTestWidgetZip();
    }

    @AfterClass
    public static void tearDownClass() {
        if (packageFile != null) {
            packageFile.delete();
        }
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        DigitalExchangesClientMocker clientMocker = new DigitalExchangesClientMocker();
        clientMocker.getDigitalExchangesMocker()
                .addDigitalExchange(DE_1_ID, request -> {

                    calls++;

                    switch (calls) {
                        case 1:
                            return getComponentInfoResponse();
                        case 2:
                            return getNotFoundException();
                        case 3:
                            return getInProgressJobResponse();
                        case 4:
                            return getCompletedJobResponse();
                        case 5:
                            return getPackageStreamResponse();
                        default:
                            return null;
                    }
                });

        client = clientMocker.build();

        mockStorageManager();

        installExecutor = spy(new DigitalExchangeInstallExecutor(
                client, storageManager, databaseManager, initializerManager, commandExecutor));

        doNothing().when(installExecutor).reloadSystem();
    }

    @Test
    public void testPackageRetrievalFromGit() throws Exception {

        DigitalExchangeJob job = new DigitalExchangeJob();
        job.setId("jobId");
        job.setComponentId("componentId");
        job.setDigitalExchangeId(DE_1_ID);

        installExecutor.execute(job, j -> {
        });

        verify(installExecutor, times(1)).reloadSystem();

        assertThat(job.getStatus()).isEqualTo(JobStatus.COMPLETED);
        assertThat(job.getProgress()).isEqualTo(1);
    }

    private PagedRestResponse<DigitalExchangeComponent> getComponentInfoResponse() {
        List<DigitalExchangeComponent> component = Collections.singletonList(new DigitalExchangeComponent());
        PagedMetadata<DigitalExchangeComponent> componentInfo = new PagedMetadata<>(new RestListRequest(), component, 1);
        return new PagedRestResponse<>(componentInfo);
    }

    private RestClientResponseException getNotFoundException() {
        RestClientResponseException notFoundException = mock(RestClientResponseException.class);
        doReturn(HttpStatus.NOT_FOUND.value()).when(notFoundException).getRawStatusCode();
        throw notFoundException;
    }

    private SimpleRestResponse<DigitalExchangeJob> getInProgressJobResponse() {
        DigitalExchangeJob inProgressJob = new DigitalExchangeJob();
        return new SimpleRestResponse<>(inProgressJob);
    }

    private SimpleRestResponse<DigitalExchangeJob> getCompletedJobResponse() {
        DigitalExchangeJob completedJob = new DigitalExchangeJob();
        completedJob.setStatus(JobStatus.COMPLETED);
        return new SimpleRestResponse<>(completedJob);
    }

    private Resource getPackageStreamResponse() {
        try {
            Resource resource = mock(Resource.class);
            when(resource.getInputStream()).thenReturn(new FileInputStream(packageFile));
            return resource;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private void mockStorageManager() throws Exception {
        when(storageManager.existsProtected(endsWith("component.xml"))).thenReturn(true);

        when(storageManager.getProtectedStream(endsWith("component.xml")))
                .thenReturn(getClass().getClassLoader()
                        .getResourceAsStream("components/de_test_widget/component.xml"));
    }
}

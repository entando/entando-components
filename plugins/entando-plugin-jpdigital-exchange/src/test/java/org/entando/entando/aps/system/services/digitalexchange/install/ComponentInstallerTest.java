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
package org.entando.entando.aps.system.services.digitalexchange.install;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import org.entando.entando.aps.system.init.DatabaseManager;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ComponentInstallerTest {

    private static File tempZipFile;

    @Mock
    private DigitalExchangesClient client;

    @Mock
    private IStorageManager storageManager;

    @Mock
    private DatabaseManager databaseManager;

    @Mock
    private InitializerManager initializerManager;

    @Mock
    private Consumer<ComponentInstallationJob> jobConsumer;

    @InjectMocks
    @Spy
    private ComponentInstaller installer;

    private ComponentInstallationJob job;

    @BeforeClass
    public static void setupZip() {
        tempZipFile = ComponentZipUtil.getTestWidgetZip();
    }

    @AfterClass
    public static void deleteZip() {
        if (tempZipFile != null) {
            tempZipFile.delete();
        }
    }

    @Before
    public void setUp() throws Exception {

        job = new ComponentInstallationJob();
        job.setComponent("test_widget");

        when(client.getStreamResponse(any(), any()))
                .thenReturn(new FileInputStream(tempZipFile));

        when(storageManager.getStream(endsWith("component.xml"), anyBoolean()))
                .thenReturn(getClass().getClassLoader().getResourceAsStream("components/test_widget/component.xml"));
        when(storageManager.getStream(endsWith("port_data_test.sql"), anyBoolean()))
                .thenReturn(getClass().getClassLoader().getResourceAsStream("components/test_widget/port_data_test.sql"));

        when(storageManager.exists(any(), anyBoolean())).thenReturn(true);

        doNothing().when(installer).reloadSystem();
    }

    @Test
    public void shouldInstallComponent() throws ApsSystemException {

        installer.install(job, jobConsumer);

        ArgumentCaptor<ComponentInstallationJob> captor = ArgumentCaptor.forClass(ComponentInstallationJob.class);
        verify(jobConsumer, times(3)).accept(captor.capture());
        assertThat(captor.getValue().getProgress()).isEqualTo(1);
        assertThat(captor.getValue().getStatus()).isEqualTo(InstallationStatus.COMPLETED);

        verify(databaseManager, times(1)).initComponentDatabases(any(), any(), anyBoolean());
        verify(databaseManager, times(1)).initComponentDefaultResources(any(), any(), anyBoolean());
        verify(initializerManager, times(1)).saveReport(any());
    }

    @Test(expected = InstallationException.class)
    public void shouldFailOnDownload() {

        when(client.getStreamResponse(any(), any())).thenThrow(UncheckedIOException.class);

        installer.install(job, jobConsumer);
    }

    @Test(expected = InstallationException.class)
    public void shouldFailOnUnzip() throws IOException, ApsSystemException {

        doThrow(ApsSystemException.class).when(storageManager)
                .saveFile(any(), anyBoolean(), any());

        installer.install(job, jobConsumer);
    }

    @Test(expected = InstallationException.class)
    public void shouldFailOnMissingComponentDefinition() throws ApsSystemException {

        when(storageManager.exists(endsWith("component.xml"), anyBoolean())).thenReturn(false);

        installer.install(job, jobConsumer);
    }

    @Test(expected = InstallationException.class)
    public void shouldFailOnParsingXML() throws ApsSystemException {

        when(storageManager.getStream(endsWith("component.xml"), anyBoolean())).thenReturn(null);

        installer.install(job, jobConsumer);
    }

    @Test(expected = InstallationException.class)
    public void shouldFailOnInstallation() throws ApsSystemException {

        doThrow(ApsSystemException.class)
                .when(databaseManager).initComponentDatabases(any(), any(), anyBoolean());

        installer.install(job, jobConsumer);
    }
}

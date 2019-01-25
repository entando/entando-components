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

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.web.common.exceptions.ValidationConflictException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;

@RunWith(MockitoJUnitRunner.class)
public class DigitalExchangeComponentInstallationServiceTest {

    @Mock
    private DigitalExchangesService exchangesService;

    @Mock
    private DigitalExchangeComponentInstallationDAO dao;

    @Mock
    private ComponentInstaller componentInstaller;

    @InjectMocks
    private DigitalExchangeComponentInstallationServiceImpl service;

    @Test
    public void testJobFailure() {

        when(exchangesService.findById(DE_1_ID)).thenReturn(getDE1());

        String errorMsg = "error_msg";
        doThrow(new InstallationException(errorMsg)).when(componentInstaller).install(any(), any());

        ComponentInstallationJob job = service.install(DE_1_ID, "test", "admin");
        assertThat(job).isNotNull();
        assertThat(job.getId()).hasSize(20);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        assertThat(job.getStatus()).isEqualTo(InstallationStatus.ERROR);
        assertThat(job.getErrorMessage()).isEqualTo(errorMsg);
    }

    @Test(expected = ValidationConflictException.class)
    public void testAlreadyRunning() {

        ComponentInstallationJob job = new ComponentInstallationJob();
        job.setStatus(InstallationStatus.IN_PROGRESS);
        when(dao.findLast("test")).thenReturn(Optional.of(job));

        service.install(DE_1_ID, "test", "admin");
    }
}

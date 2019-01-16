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

import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class DigitalExchangeComponentInstallationServiceTest {

    @Mock
    private DigitalExchangeComponentInstallationDAO dao;

    @Mock
    private ComponentInstaller componentInstaller;

    @InjectMocks
    private DigitalExchangeComponentInstallationServiceImpl service;

    @Test
    public void testJobFailure() {

        String errorMsg = "error_msg";
        doThrow(new InstallationException(errorMsg)).when(componentInstaller).install(any(), any());

        ComponentInstallationJob job = service.install(new DigitalExchange(), "test");
        assertThat(job).isNotNull();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }

        assertThat(job.getStatus()).isEqualTo(InstallationStatus.ERROR);
        assertThat(job.getErrorMessage()).isEqualTo(errorMsg);
    }
}

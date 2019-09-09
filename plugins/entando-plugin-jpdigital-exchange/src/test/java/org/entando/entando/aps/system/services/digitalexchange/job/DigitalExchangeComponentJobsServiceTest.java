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

import java.util.Optional;

import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.web.common.exceptions.ValidationConflictException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.entando.entando.aps.system.services.digitalexchange.DigitalExchangeTestUtils.*;

@RunWith(MockitoJUnitRunner.class)
public class DigitalExchangeComponentJobsServiceTest {

    @Mock
    private DigitalExchangesService exchangesService;

    @Mock
    private DigitalExchangeJobService jobService;

    @Mock
    private DigitalExchangeInstallExecutor digitalExchangeInstallExecutor;

    @InjectMocks
    private DigitalExchangeComponentInstallationServiceImpl service;

    @Test
    public void testJobFailure() {

        when(exchangesService.findById(DE_1_ID)).thenReturn(getDE1());
        when(jobService.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String errorMsg = "error_msg";
        doThrow(new JobExecutionException(errorMsg)).when(digitalExchangeInstallExecutor).execute(any(), any(), any());

        DigitalExchangeJob job = service.install(DE_1_ID, "test", "admin");
        assertThat(job).isNotNull();
        verify(jobService, atLeast(1)).save(job);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }

        assertThat(job.getStatus()).isEqualTo(JobStatus.ERROR);
        assertThat(job.getErrorMessage()).isEqualTo(errorMsg.getBytes());
    }

    @Test(expected = ValidationConflictException.class)
    public void testAlreadyRunning() {

        DigitalExchangeJob job = new DigitalExchangeJob();
        job.setStatus(JobStatus.IN_PROGRESS);
        when(jobService.findLast("test", JobType.INSTALL)).thenReturn(Optional.of(job));

        service.install(DE_1_ID, "test", "admin");
    }
}

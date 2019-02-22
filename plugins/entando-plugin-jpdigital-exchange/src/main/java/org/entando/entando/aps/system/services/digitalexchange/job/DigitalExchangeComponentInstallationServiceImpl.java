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

import java.util.Date;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

@Service
public class DigitalExchangeComponentInstallationServiceImpl extends JobRunner implements DigitalExchangeComponentInstallationService {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeComponentInstallationServiceImpl.class);

    private static final String ERRCODE_COMPONENT_INSTALLATION_RUNNING = "1";

    private final DigitalExchangesService exchangesService;
    private final DigitalExchangeJobService jobService;
    private final DigitalExchangeAbstractJobExecutor installExecutor;
    private final DigitalExchangeAbstractJobExecutor uninstallExecutor;

    @Autowired
    public DigitalExchangeComponentInstallationServiceImpl(
            DigitalExchangesService exchangesService,
            DigitalExchangeJobService jobService,
            DigitalExchangeInstallExecutor installExecutor,
            DigitalExchangeUninstallExecutor uninstallExecutor) {

        super(jobService);
        this.exchangesService = exchangesService;
        this.jobService = jobService;
        this.installExecutor = installExecutor;
        this.uninstallExecutor = uninstallExecutor;
    }

    @Override
    public DigitalExchangeJob install(String exchangeId, String componentId, String username) {

        checkIfAlreadyRunning(componentId, JobType.INSTALL);

        DigitalExchange digitalExchange = exchangesService.findById(exchangeId);

        DigitalExchangeJob job = createNewJob(digitalExchange, componentId, JobType.INSTALL);
        job.setUser(username);
        job = this.jobService.save(job);

        this.executeJob(job, this.installExecutor);

        return job;
    }

    @Override
    public DigitalExchangeJob uninstall(String componentId, String username) {

        checkIfAlreadyRunning(componentId, JobType.UNINSTALL);

        DigitalExchangeJob job = createNewJob(componentId, JobType.UNINSTALL);
        job.setUser(username);
        job = this.jobService.save(job);

        this.executeJob(job, this.uninstallExecutor);

        return job;
    }

    private synchronized void checkIfAlreadyRunning(String componentId, JobType jobType) {

        jobService.findLast(componentId, jobType).ifPresent(job -> {
            if (job.getStatus() != JobStatus.COMPLETED
                    && job.getStatus() != JobStatus.ERROR) {
                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(componentId, "component");
                bindingResult.reject(ERRCODE_COMPONENT_INSTALLATION_RUNNING, new String[]{}, "digitalExchange.installation.alreadyRunning");
                throw new ValidationConflictException(bindingResult);
            }
        });
    }

    private DigitalExchangeJob createNewJob(DigitalExchange digitalExchange, String componentId, JobType type) {

        DigitalExchangeJob job = new DigitalExchangeJob();
        job.setComponentId(componentId);
        job.setStarted(new Date());

        if (digitalExchange != null) {
            job.setDigitalExchangeId(digitalExchange.getId());
            job.setDigitalExchangeUrl(digitalExchange.getUrl());
        }

        job.setJobType(type);

        return job;
    }

    private DigitalExchangeJob createNewJob(String componentId, JobType type) {

        DigitalExchangeJob job = new DigitalExchangeJob();
        job.setComponentId(componentId);
        job.setStarted(new Date());
        job.setJobType(type);

        return job;
    }

    @Override
    public DigitalExchangeJob checkJobStatus(String componentId, JobType jobType) {
        return jobService.findLast(componentId, jobType)
                .orElseThrow(() -> new ResourceNotFoundException("component", componentId));
    }

}

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

import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang.RandomStringUtils;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DigitalExchangeComponentInstallationServiceImpl implements DigitalExchangeComponentInstallationService {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeComponentInstallationServiceImpl.class);

    private final DigitalExchangeComponentInstallationDAO dao;
    private final ComponentInstaller componentInstaller;

    @Autowired
    public DigitalExchangeComponentInstallationServiceImpl(
            DigitalExchangeComponentInstallationDAO dao,
            ComponentInstaller componentInstaller) {

        this.dao = dao;
        this.componentInstaller = componentInstaller;
    }

    @Override
    public ComponentInstallationJob install(DigitalExchange digitalExchange, String componentName) {

        ComponentInstallationJob job = createNewJob(digitalExchange, componentName);
        dao.createComponentInstallationJob(job);

        CompletableFuture.runAsync(() -> {
            try {
                componentInstaller.install(job, dao::updateComponentInstallationJob);
            } catch (InstallationException ex) {
                logger.error("Error while installing " + componentName, ex);
                job.setStatus(InstallationStatus.ERROR);
                job.setErrorMessage(ex.getMessage());
                dao.updateComponentInstallationJob(job);
            }
        });

        return job;
    }

    private ComponentInstallationJob createNewJob(DigitalExchange digitalExchange, String componentName) {

        ComponentInstallationJob job = new ComponentInstallationJob();
        job.setId(RandomStringUtils.randomAlphanumeric(20));
        job.setComponent(componentName);
        job.setDigitalExchange(digitalExchange.getId());
        job.setStatus(InstallationStatus.CREATED);

        return job;
    }

    @Override
    public ComponentInstallationJob checkInstallationStatus(String jobId) {
        return dao.fingById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("There is no job having id " + jobId));
    }
}

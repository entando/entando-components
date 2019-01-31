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

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.lang.RandomStringUtils;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

@Service
public class DigitalExchangeComponentInstallationServiceImpl implements DigitalExchangeComponentInstallationService {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeComponentInstallationServiceImpl.class);

    private static final String ERRCODE_COMPONENT_INSTALLATION_RUNNING = "1";

    private final DigitalExchangesService exchangesService;
    private final DigitalExchangeComponentInstallationDAO dao;
    private final ComponentInstaller componentInstaller;

    @Autowired
    public DigitalExchangeComponentInstallationServiceImpl(
            DigitalExchangesService exchangesService,
            DigitalExchangeComponentInstallationDAO dao,
            ComponentInstaller componentInstaller) {

        this.exchangesService = exchangesService;
        this.dao = dao;
        this.componentInstaller = componentInstaller;
    }

    @Override
    public ComponentInstallationJob install(String exchangeId, String componentId, String username) {

        checkIfAlreadyRunning(componentId);

        DigitalExchange digitalExchange = exchangesService.findById(exchangeId);

        ComponentInstallationJob job = createNewJob(digitalExchange, componentId);
        job.setUser(username);
        dao.createComponentInstallationJob(job);

        CompletableFuture.runAsync(() -> {
            try {
                componentInstaller.install(job, dao::updateComponentInstallationJob);
            } catch (Throwable ex) {
                logger.error("Error while installing " + componentId, ex);
                job.setStatus(InstallationStatus.ERROR);
                job.setErrorMessage(ex.getMessage());
                job.setEnded(new Date());
                dao.updateComponentInstallationJob(job);
            }
        });

        return job;
    }

    private synchronized void checkIfAlreadyRunning(String componentId) {

        dao.findLast(componentId).ifPresent(job -> {
            if (job.getStatus() != InstallationStatus.COMPLETED
                    && job.getStatus() != InstallationStatus.ERROR) {
                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(componentId, "component");
                bindingResult.reject(ERRCODE_COMPONENT_INSTALLATION_RUNNING, new String[]{}, "digitalExchange.installation.alreadyRunning");
                throw new ValidationConflictException(bindingResult);
            }
        });
    }

    private ComponentInstallationJob createNewJob(DigitalExchange digitalExchange, String componentId) {

        ComponentInstallationJob job = new ComponentInstallationJob();
        job.setId(RandomStringUtils.randomAlphanumeric(20));
        job.setComponentId(componentId);
        job.setStarted(new Date());
        job.setDigitalExchangeId(digitalExchange.getId());
        job.setDigitalExchangeUrl(digitalExchange.getUrl());
        job.setStatus(InstallationStatus.CREATED);

        return job;
    }

    @Override
    public ComponentInstallationJob checkInstallationStatus(String componentId) {
        return dao.findLast(componentId)
                .orElseThrow(() -> new IllegalArgumentException("No job found for component " + componentId));
    }
}

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
import java.util.concurrent.CompletableFuture;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JobRunner {

    private static final Logger logger = LoggerFactory.getLogger(JobRunner.class);

    private final DigitalExchangeJobService jobService;

    public JobRunner(DigitalExchangeJobService jobService) {
        this.jobService = jobService;
    }

    protected void executeJob(DigitalExchangeJob job, DigitalExchangeJobExecutor<?> jobExecutor) {
        executeJob(job, jobExecutor, null);
    }

    protected <T> void executeJob(DigitalExchangeJob job, DigitalExchangeJobExecutor<T> jobExecutor, T additionalParam) {
        CompletableFuture.runAsync(() -> {
            try {
                jobExecutor.execute(job, jobService::save, additionalParam);
            } catch (Throwable ex) {
                logger.error("Error while executing job " + job.getId(), ex);
                job.setStatus(JobStatus.ERROR);
                job.setErrorMessage(ex.getMessage());
                job.setEnded(new Date());
                jobService.save(job);
            }
        });
    }
}

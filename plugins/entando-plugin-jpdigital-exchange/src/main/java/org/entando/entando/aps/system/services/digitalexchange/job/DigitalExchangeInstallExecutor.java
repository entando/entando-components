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

import com.agiletec.aps.system.exception.ApsSystemException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.entando.entando.aps.system.init.DatabaseManager;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeBaseCall;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.PagedDigitalExchangeCall;
import org.entando.entando.aps.system.services.digitalexchange.client.SimpleDigitalExchangeCall;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentListProcessor;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

@org.springframework.stereotype.Component
public class DigitalExchangeInstallExecutor extends DigitalExchangeAbstractJobExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeInstallExecutor.class);

    @Autowired
    public DigitalExchangeInstallExecutor(DigitalExchangesClient client, ComponentStorageManager storageManager,
            DatabaseManager databaseManager, InitializerManager initializerManager,
            CommandExecutor commandExecutor) {
        super(client, storageManager, databaseManager, initializerManager, commandExecutor);
    }

    @Override
    public void execute(DigitalExchangeJob job, Consumer<DigitalExchangeJob> updater) throws JobExecutionException {

        //TODO defines steps and percentages as Enums
        double steps = 6;
        int currentStep = 0;

        fillComponentInfo(job);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        downloadComponent(job);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        ComponentDescriptor component = parseComponentDescriptor(job.getComponentId());
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        initializeComponent(component);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        component.getInstallationCommands().forEach(commandExecutor::execute);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        completeJob(job, updater);
    }

    private void fillComponentInfo(DigitalExchangeJob job) {

        RestListRequest requestList = new RestListRequest();
        requestList.setPage(1);
        requestList.setPageSize(1);
        Filter componentIdFilter = new Filter("id", job.getComponentId());
        requestList.setFilters(new Filter[]{componentIdFilter});

        PagedDigitalExchangeCall<DigitalExchangeComponent> call = new PagedDigitalExchangeCall<DigitalExchangeComponent>(
                requestList, new ParameterizedTypeReference<PagedRestResponse<DigitalExchangeComponent>>() {
        }, "digitalExchange", "components") {

            @Override
            protected RequestListProcessor<DigitalExchangeComponent> getRequestListProcessor(
                    RestListRequest request, List<DigitalExchangeComponent> joinedList) {
                return new DigitalExchangeComponentListProcessor(request, joinedList);
            }
        };

        PagedRestResponse<DigitalExchangeComponent> response
                = client.getSingleResponse(job.getDigitalExchangeId(), call);

        List<DigitalExchangeComponent> components = response.getPayload();

        if (components.size() != 1) {
            throw new JobExecutionException("Found " + components.size() + " components for " + job.getComponentId());
        }

        DigitalExchangeComponent component = components.get(0);
        job.setComponentName(component.getName());
        job.setComponentVersion(component.getVersion());
    }

    private void downloadComponent(DigitalExchangeJob job) {

        Path tempZipPath = null;

        try {

            tempZipPath = Files.createTempFile(job.getComponentId(), "");

            DigitalExchangeBaseCall<InputStream> call = new DigitalExchangeBaseCall<>(
                    HttpMethod.GET, "digitalExchange", "components", job.getComponentId(), "package");

            call.setErrorResponseHandler(getPackageNotFoundExceptionHandler(job, call));

            try (InputStream in = client.getStreamResponse(job.getDigitalExchangeId(), call)) {
                Files.copy(in, tempZipPath, StandardCopyOption.REPLACE_EXISTING);
            }

            extractZip(tempZipPath, job.getComponentId());

        } catch (IOException | UncheckedIOException ex) {
            logger.error("Error while downloading component", ex);
            throw new JobExecutionException("Unable to save component", ex);
        } finally {
            if (tempZipPath != null) {
                if (!tempZipPath.toFile().delete()) {
                    logger.warn("Unable to delete temporary zip file {}",
                            tempZipPath.toFile().getAbsolutePath());
                }
            }
        }
    }

    private Function<RestClientResponseException, Optional<InputStream>> getPackageNotFoundExceptionHandler(
            DigitalExchangeJob job, DigitalExchangeBaseCall<InputStream> call) {

        return ex -> {
            if (ex.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {

                waitForPackageAvailability(job);

                call.setErrorResponseHandler(null);
                return Optional.of(client.getStreamResponse(job.getDigitalExchangeId(), call));
            }

            return Optional.empty();
        };
    }

    private void waitForPackageAvailability(DigitalExchangeJob job) {

        DigitalExchangeJob packageRetrievalJob = startPackageCreation(job);

        if (packageRetrievalJob.getStatus() == JobStatus.COMPLETED) {
            // package already available
            return;
        }

        boolean packageAvailable = false;

        int attempt = 0;
        while (!packageAvailable && attempt < 30) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }

            // Check job status
            packageAvailable = checkPackageAvailable(job.getDigitalExchangeId(), packageRetrievalJob.getId());
            attempt++;
        }

        if (!packageAvailable) {
            throw new JobExecutionException("Timeout error: the DE spent too much time preparing the package");
        }
    }

    private DigitalExchangeJob startPackageCreation(DigitalExchangeJob job) {

        SimpleDigitalExchangeCall<DigitalExchangeJob> packageCreationCall = new SimpleDigitalExchangeCall<>(
                HttpMethod.POST, new ParameterizedTypeReference<SimpleRestResponse<DigitalExchangeJob>>() {
        }, "digitalExchange", "components", job.getComponentId(), "package");

        DigitalExchangeJob packageRetrievalJob = client.getSingleResponse(job.getDigitalExchangeId(), packageCreationCall).getPayload();

        if (packageRetrievalJob.getStatus() == JobStatus.ERROR) {
            throw new JobExecutionException("An error happened when the DE was preparing the package");
        }

        return packageRetrievalJob;
    }

    private boolean checkPackageAvailable(String exchangeId, String jobId) {
        SimpleDigitalExchangeCall<DigitalExchangeJob> jobStatusCall = new SimpleDigitalExchangeCall<>(
                HttpMethod.GET, new ParameterizedTypeReference<SimpleRestResponse<DigitalExchangeJob>>() {
        }, "digitalExchange", "jobs", jobId);

        DigitalExchangeJob packageRetrievalJob = client.getSingleResponse(exchangeId, jobStatusCall).getPayload();

        if (packageRetrievalJob.getStatus() == JobStatus.ERROR) {
            throw new JobExecutionException("An error happened when the DE was preparing the package");
        }

        return packageRetrievalJob.getStatus() == JobStatus.COMPLETED;
    }

    private void extractZip(Path tempZipPath, String componentId) {

        try (ZipFile zip = new ZipFile(tempZipPath.toFile())) {

            for (ZipEntry entry : Collections.list(zip.entries())) {

                String path = entry.getName();

                if (path.startsWith(RESOURCES_DIR)) {
                    String resourcePath = componentId + path.substring(RESOURCES_DIR.length());
                    if (entry.isDirectory()) {
                        storageManager.createResourceDirectory(resourcePath);
                    } else {
                        storageManager.saveResourceFile(resourcePath, zip.getInputStream(entry));
                    }
                } else {
                    String protectedPath = componentId + File.separator + path;
                    if (entry.isDirectory()) {
                        storageManager.createProtectedDirectory(protectedPath);
                    } else {
                        storageManager.saveProtectedFile(protectedPath, zip.getInputStream(entry));
                    }
                }
            }
        } catch (ApsSystemException | IOException ex) {
            logger.error("Error while extracting zip file", ex);
            throw new JobExecutionException("Unable to extract zip file", ex);
        }
    }

    private void initializeComponent(ComponentDescriptor component) {

        try {

            SystemInstallationReport systemInstallationReport = initializerManager.getCurrentReport();

            databaseManager.initComponentDatabases(component, systemInstallationReport, true);
            databaseManager.initComponentDefaultResources(component, systemInstallationReport, true);

            initializerManager.saveReport(systemInstallationReport);

            initializerManager.executeComponentPostInitProcesses(component, systemInstallationReport);

        } catch (ApsSystemException ex) {
            logger.error("Error during component installation", ex);
            throw new JobExecutionException("Unable to install component", ex);
        }
    }
}

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
import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.servlet.ServletContext;
import org.entando.entando.aps.system.init.DatabaseManager;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeBaseCall;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.ServletContextAware;

@org.springframework.stereotype.Component
public class ComponentInstaller implements ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ComponentInstaller.class);

    protected static final String COMPONENTS_DIR = "de_components";
    protected static final boolean PROTECTED_RESOURCE = true;

    private final DigitalExchangesClient client;
    private final IStorageManager storageManager;
    private final DatabaseManager databaseManager;
    private final InitializerManager initializerManager;

    private ServletContext servletContext;

    @Autowired
    public ComponentInstaller(DigitalExchangesClient client, IStorageManager storageManager,
            DatabaseManager databaseManager, InitializerManager initializerManager) {
        this.client = client;
        this.storageManager = storageManager;
        this.databaseManager = databaseManager;
        this.initializerManager = initializerManager;
    }

    public void install(ComponentInstallationJob job, Consumer<ComponentInstallationJob> updater) {

        double steps = 3;
        int currentStep = 0;

        downloadComponent(job);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        installComponent(job);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        reloadSystem();
        job.setProgress(1);
        job.setStatus(InstallationStatus.COMPLETED);
        updater.accept(job);
    }

    private void downloadComponent(ComponentInstallationJob job) {

        Path tempZipPath = null;

        try {

            tempZipPath = Files.createTempFile(job.getComponent(), "");

            DigitalExchangeBaseCall call = new DigitalExchangeBaseCall(
                    HttpMethod.GET, "digitalExchange", "components", job.getComponent(), "packages");

            try (InputStream in = client.getStreamResponse(job.getDigitalExchange(), call)) {
                Files.copy(in, tempZipPath, StandardCopyOption.REPLACE_EXISTING);
            }

            createComponentsDir();
            extractZip(tempZipPath);

        } catch (ApsSystemException | IOException | UncheckedIOException ex) {
            logger.error("Error while downloading component", ex);
            throw new InstallationException("Unable to save component", ex);
        } finally {
            if (tempZipPath != null) {
                if (!tempZipPath.toFile().delete()) {
                    logger.warn("Unable to delete temporary zip file {}",
                            tempZipPath.toFile().getAbsolutePath());
                }
            }
        }
    }

    private void createComponentsDir() throws ApsSystemException {
        if (!storageManager.exists(COMPONENTS_DIR, PROTECTED_RESOURCE)) {
            storageManager.createDirectory(COMPONENTS_DIR, PROTECTED_RESOURCE);
        }
    }

    private void extractZip(Path tempZipPath) throws IOException {

        try (ZipFile zip = new ZipFile(tempZipPath.toFile())) {

            for (ZipEntry entry : Collections.list(zip.entries())) {

                String path = COMPONENTS_DIR + File.separator + entry.getName();

                if (!entry.isDirectory()) {
                    storageManager.saveFile(path, PROTECTED_RESOURCE, zip.getInputStream(entry));
                } else if (!storageManager.exists(path, PROTECTED_RESOURCE)) {
                    storageManager.createDirectory(path, PROTECTED_RESOURCE);
                }
            }
        } catch (ApsSystemException | IOException ex) {
            logger.error("Error while extracting zip file", ex);
            throw new InstallationException("Unable to extract zip file", ex);
        }
    }

    private void installComponent(ComponentInstallationJob job) {

        try {

            Component component = getComponentDefinition(job.getComponent());

            SystemInstallationReport systemInstallationReport = initializerManager.getCurrentReport();

            databaseManager.initComponentDatabases(component, systemInstallationReport, true);
            databaseManager.initComponentDefaultResources(component, systemInstallationReport, true);

            initializerManager.saveReport(systemInstallationReport);

            initializerManager.executeComponentPostInitProcesses(component, systemInstallationReport);

        } catch (ApsSystemException ex) {
            logger.error("Error during component installation", ex);
            throw new InstallationException("Unable to install component", ex);
        }
    }

    private Component getComponentDefinition(String componentCode) throws ApsSystemException {

        String componentDefinitionPath = COMPONENTS_DIR + File.separator
                + componentCode + File.separator + "component.xml";

        if (!storageManager.exists(componentDefinitionPath, PROTECTED_RESOURCE)) {
            throw new InstallationException("component.xml not found for " + componentCode);
        }

        try (InputStream in = storageManager.getStream(componentDefinitionPath, PROTECTED_RESOURCE)) {
            Document document = new SAXBuilder().build(in);
            Component component = new Component(document.getRootElement(), new HashMap<>());
            component.getEnvironments().replaceAll((key, value)
                    -> new DigitalExchangeComponentEnvironment(storageManager, value));
            return component;
        } catch (Throwable t) {
            logger.error("Error during component defintion parsing", t);
            throw new InstallationException("Unable to parse component.xml for " + componentCode);
        }
    }

    protected void reloadSystem() {
        try {
            ApsWebApplicationUtils.executeSystemRefresh(servletContext);
        } catch (Throwable t) {
            logger.error("Error during system reloading", t);
            throw new InstallationException("Unable to reload the system after the installation");
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}

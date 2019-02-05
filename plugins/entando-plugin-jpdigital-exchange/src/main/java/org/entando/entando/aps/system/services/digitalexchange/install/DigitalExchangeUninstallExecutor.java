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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletContext;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.aps.system.init.DatabaseManager;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.services.RequestListProcessor;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeBaseCall;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.PagedDigitalExchangeCall;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentListProcessor;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@org.springframework.stereotype.Component
public class DigitalExchangeUninstallExecutor extends DigitalExchangeAbstractJobExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeUninstallExecutor.class);


    @Autowired
    public DigitalExchangeUninstallExecutor(DigitalExchangesClient client, ComponentStorageManager storageManager,
                                            DatabaseManager databaseManager, InitializerManager initializerManager,
                                            CommandExecutor commandExecutor) {
        super(client, storageManager, databaseManager, initializerManager, commandExecutor);
    }


    public void execute(DigitalExchangeJob job, Consumer<DigitalExchangeJob> updater) throws JobExecutionException {

        double steps = 5;
        int currentStep = 0;

        ComponentDescriptor component = parseComponentDescriptor(job.getComponentId());
        job.setProgress(++currentStep / steps);
        updater.accept(job);
        component.getUninstallationCommands().forEach(commandExecutor::execute);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        cleanComponentDatabase(component);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        cleanComponentResources(component);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        removeComponentDownloadedContent(component);
        job.setProgress(++currentStep / steps);
        updater.accept(job);

        completeJob(job, updater);

    }

    private void cleanComponentDatabase(ComponentDescriptor component) {

        try {

            SystemInstallationReport systemInstallationReport = initializerManager.getCurrentReport();

            databaseManager.uninstallComponentResources(component, systemInstallationReport);

            initializerManager.saveReport(systemInstallationReport);

//            initializerManager.executeComponentPostInitProcesses(component, systemInstallationReport);

        } catch (ApsSystemException ex) {
            logger.error("Error during component installation", ex);
            throw new JobExecutionException("Unable to install component", ex);
        }
    }

    private void cleanComponentResources(ComponentDescriptor component) {

        //TODO check and implement using the storage manager

//        ComponentUninstallerInfo uninstallInfo = component.getUninstallerInfo();
//
//        try {
//            for (String resourcePath: uninstallInfo.getResourcesPaths()) {
//
//                Resource componentResource = new ClassPathResource(resourcePath);
//                if (componentResource.exists()) {
//                    File resourceFile = componentResource.getFile();
//                    if (resourceFile.isDirectory()) {
//                        Files.walk(Paths.get(resourceFile.getAbsolutePath()))
//                                .map(Path::toFile)
//                                .sorted((o1, o2) -> -o1.compareTo(o2))
//                                .forEach(File::delete);
//                    } else {
//                        resourceFile.delete();
//                    }
//
//                }
//            }
//
//        } catch(IOException ex) {
//
//            logger.error("Error during removal of component resources", ex);
//            throw new JobExecutionException("Unable to remove component resoruces", ex);
//        }
//
//
    }

    private void removeComponentDownloadedContent(ComponentDescriptor component) {

        String componentId = component.getCode();

        try {
            if (storageManager.existsProtected(componentId) ) {
                storageManager.deleteProtectedDirectory(componentId);
            }

            if (storageManager.existsResource(componentId)) {
                storageManager.deleteResourceDirectory(componentId);
            }

        } catch (ApsSystemException ex) {
            logger.error("An error occurred while removing downloaded content for the component", ex);
            throw new JobExecutionException("Unable to remove component's downloaed content", ex);
        }

    }

}

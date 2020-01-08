/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceDOM;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.*;

import java.util.*;

/**
 * Base abstract class for the implementation of multi instance resource objects.
 */
public abstract class AbstractMultiInstanceResource extends AbstractResource {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMultiInstanceResource.class);

    private Map<String, ResourceInstance> instances;

    /**
     * Initializes the map of instances of the resource
     */
    public AbstractMultiInstanceResource() {
        super();
        instances = new HashMap<>();
    }

    @Override
    public void deleteResourceInstances() throws ApsSystemException {
        try {
            Collection<ResourceInstance> resources = this.getInstances().values();
            for (ResourceInstance currentInstance : resources) {
                String fileName = currentInstance.getFileName();
                String subPath = this.getDiskSubFolder() + fileName;
                this.getStorageManager().deleteFile(subPath, this.isProtectedResource());
            }
        } catch (Throwable t) {
            logger.error("Error on deleting resource instances", t);
            throw new ApsSystemException("Error on deleting resource instances", t);
        }
    }

    /**
     * Implementazione del metodo isMultiInstance() di AbstractResource.
     * Restituisce sempre true in quanto questa classe astratta è alla base di
     * tutte le risorse MultiInstance.
     *
     * @return true in quanto la risorsa è MultiInstance.
     */
    @Override
    public boolean isMultiInstance() {
        return true;
    }

    /**
     * Restituisce l'xml completo della risorsa.
     *
     * @return L'xml completo della risorsa.
     */
    @Override
    public String getXML() {
        ResourceDOM resourceDom = getResourceDOM();
        List<ResourceInstance> resources = new ArrayList<>(this.getInstances().values());
        for (ResourceInstance currentInstance : resources) {
            resourceDom.addInstance(currentInstance.getJDOMElement());
        }
        Map<String, String> metadata = getMetadata();
        if (null != metadata) {
            resourceDom.addMetadata(metadata);
        }
        return resourceDom.getXMLDocument();
    }

    /**
     * Restituisce il nome corretto del file con cui un'istanza di una risorsa
     * viene salvata nel fileSystem.
     *
     * @param masterFileName Il nome del file principale da cui ricavare
     * l'istanza.
     * @param size Il size dell'istanza della risorsa multiInstanza.
     * @param langCode Il codice lingua dell'istanza della risorsa
     * multiInstanza.
     * @return Il nome corretto del file.
     * @deprecated Use getNewInstanceFileName
     */
    @Deprecated
    public abstract String getInstanceFileName(String masterFileName, int size, String langCode);

    /**
     * Returns the correct name of a resource to be saved in the system.
     * @param masterFileName Original file name
     * @param size Order number of the file being saved
     * @param langCode Language code of file being saved
     * @return Correct name to save file
     */
    String getNewInstanceFileName(String masterFileName, int size, String langCode) {
        String baseName = FilenameUtils.getBaseName(masterFileName);
        String extension = FilenameUtils.getExtension(masterFileName);
        String suffix = "";
        if (size >= 0) {
            suffix += "_d" + size;
        }
        if (langCode != null) {
            suffix += "_" + langCode;
        }
        return this.createFileName(
                super.getMultiFileUniqueBaseName(baseName, suffix, extension),
                extension
        );
    }

    /**
     * Restituisce un'istanza della risorsa.
     *
     * @param size Il size dell'istanza della risorsa multiInstanza.
     * @param langCode Il codice lingua dell'istanza della risorsa
     * multiInstanza.
     * @return L'istanza cercata.
     */
    public abstract ResourceInstance getInstance(int size, String langCode);

    /**
     * Restituisce la mappa delle istanze della risorsa, indicizzate in base al
     * size o alla lingua dell'istanza.
     *
     * @return La mappa delle istanze della risorsa.
     */
    public Map<String, ResourceInstance> getInstances() {
        return instances;
    }

}

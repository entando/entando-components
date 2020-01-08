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
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceDOM;
import org.apache.commons.io.FilenameUtils;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.slf4j.*;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;

public abstract class AbstractResource implements ResourceInterface, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractResource.class);

    private String id;
    private String typeCode;
    private String description;
    private String mainGroup;
    private String masterFileName;
    private List<Category> categories;
    private String folder;
    private String protectedBaseURL;
    private String owner;

    private String allowedExtensions;

    private Date creationDate;
    private Date lastModified;

    private IStorageManager storageManager;
    private Map<String, String> metadata;

    private String metadataIgnoreKeys;
    
    /**
     * Inizializza gli elementi base costituenti la Risorsa.
     */
    public AbstractResource() {
        this.setId("");
        this.setType("");
        this.setDescription("");
        this.setMainGroup("");
        this.setMasterFileName("");
        this.categories = new ArrayList<>();
        this.setFolder("");
        this.setCreationDate(null);
        this.setMetadata(new HashMap<>());
        this.setLastModified(null);
        this.setOwner(null);
    }

    /**
     * Restituisce l'identificativo della risorsa.
     *
     * @return L'identificativo della risorsa.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Setta l'identificativo della risorsa.
     *
     * @param id L'identificativo della risorsa.
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Restituisce il codice del tipo di risorsa.
     *
     * @return Il codice del tipo di risorsa.
     */
    @Override
    public String getType() {
        return typeCode;
    }

    /**
     * Setta il codice del tipo di risorsa.
     *
     * @param typeCode Il codice del tipo di risorsa.
     */
    @Override
    public void setType(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * @deprecated use getDescription method
     */
    @Override
    @Deprecated
    public String getDescr() {
        return this.getDescription();
    }

    /**
     * @deprecated use setDescription method
     */
    @Override
    @Deprecated
    public void setDescr(String descr) {
        this.setDescription(descr);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Restituisce la stringa identificante il gruppo principale di cui la
     * risorsa è membro.
     *
     * @return Il gruppo principale di cui la risorsa è membro.
     */
    @Override
    public String getMainGroup() {
        return mainGroup;
    }

    /**
     * Setta la stringa identificante il gruppo principale di cui il contenuto è
     * membro.
     *
     * @param mainGroup Il gruppo principale di cui il contenuto è membro.
     */
    @Override
    public void setMainGroup(String mainGroup) {
        this.mainGroup = mainGroup;
    }

    @Override
    public String getMasterFileName() {
        return masterFileName;
    }

    @Override
    public void setMasterFileName(String masterFileName) {
        this.masterFileName = masterFileName;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Aggiunge una categoria alla lista delle categorie della risorsa.
     *
     * @param category La categoria da aggiungere.
     */
    @Override
    public void addCategory(Category category) {
        this.categories.add(category);
    }

    /**
     * Restituisce la lista di categorie associate alla risorsa.
     *
     * @return La lista di categorie associate alla risorsa.
     */
    @Override
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Setta la lista di categorie associate alla risorsa.
     *
     * @param categories La lista di categorie associate alla risorsa.
     */
    @Override
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Rimuove una categoria alla lista delle categorie della risorsa.
     *
     * @param category La categoria da rimuovere.
     */
    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    /**
     * Restituisce il nome della cartella contenitore delle risorse.
     *
     * @return Il nome della cartella contenitore delle risorse.
     */
    @Override
    public String getFolder() {
        return folder;
    }

    /**
     * Setta il nome della cartella contenitore delle risorse.
     *
     * @param folder Il nome della cartella contenitore delle risorse.
     */
    @Override
    public void setFolder(String folder) {
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        this.folder = folder;
    }

    /**
     * Restituisce l'url base della cartella delle risorse pretette.
     *
     * @return L'url base della cartella delle risorse protette.
     */
    protected String getProtectedBaseURL() {
        return protectedBaseURL;
    }

    public void setProtectedBaseURL(String protBaseURL) {
        this.protectedBaseURL = protBaseURL;
    }

    @Override
    public String[] getAllowedFileTypes() {
        return this.getAllowedExtensions().split(",");
    }

    /**
     * Setta la stringa rappresentante l'insieme delle estensioni consentite
     * separate da virgola.
     *
     * @return L'insieme delle estensioni consentite.
     */
    protected String getAllowedExtensions() {
        return allowedExtensions;
    }

    /**
     * Setta la stringa rappresentante l'insieme delle estensioni consentite
     * separate da virgola.
     *
     * @param allowedExtensions L'insieme delle estensioni consentite.
     */
    public void setAllowedExtensions(String allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public ResourceInterface getResourcePrototype() {
        AbstractResource prototype;
        try {
            Class resourceClass = Class.forName(this.getClass().getName());
            prototype = (AbstractResource) resourceClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException t) {
            throw new RuntimeException("Error in creation of prototype of resource type"
                    + " '" + typeCode + "'", t);
        }
        prototype.setId("");
        prototype.setType(typeCode);
        prototype.setDescription("");
        prototype.setMainGroup("");
        prototype.setMasterFileName("");
        prototype.setCategories(new ArrayList<>());
        prototype.setFolder(folder);
        prototype.setProtectedBaseURL(protectedBaseURL);
        prototype.setAllowedExtensions(allowedExtensions);
        prototype.setStorageManager(storageManager);
        prototype.setCreationDate(null);
        prototype.setLastModified(null);
        prototype.setMetadata(new HashMap<>());
        prototype.setMetadataIgnoreKeys(metadataIgnoreKeys);
        return prototype;
    }

    /**
     * Restituisce la classe dom (necessaria per la generazione dell'xml della
     * risorsa) preparata con gli attributi base della risorsa.
     *
     * @return La classe dom preparata con gli attributi base della risorsa.
     */
    protected ResourceDOM getResourceDOM() {
        ResourceDOM resourceDom = this.getNewResourceDOM();
        resourceDom.setTypeCode(typeCode);
        resourceDom.setId(id);
        resourceDom.setDescription(description);
        resourceDom.setMainGroup(mainGroup);
        resourceDom.setMasterFileName(masterFileName);
        if (categories != null) {
            for (Category cat : categories) {
                resourceDom.addCategory(cat.getCode());
            }
        }
        return resourceDom;
    }

    protected ResourceDOM getNewResourceDOM() {
        return new ResourceDOM();
    }

    protected String getDiskSubFolder() {
        StringBuilder diskFolder = new StringBuilder(folder);
        if (this.isProtectedResource()) {
            diskFolder.append(mainGroup).append("/");
        }
        return diskFolder.toString();
    }

    @Override
    public InputStream getResourceStream(ResourceInstance instance) {
        return this.getResourceStream(instance.getSize(), instance.getLangCode());
    }

    @Override
    public String getDefaultUrlPath() {
        ResourceInstance defaultInstance = this.getDefaultInstance();
        return this.getUrlPath(defaultInstance);
    }

    /**
     * Repurn the url path of the given istance.
     *
     * @param instance the resource instance
     * @return Il path del file relativo all'istanza.
     */
    protected String getUrlPath(ResourceInstance instance) {
        if (instance == null) {
            return null;
        }
        StringBuilder urlPath = new StringBuilder();
        if (this.isProtectedResource()) {
            //PATH di richiamo della servlet di autorizzazione
            //Sintassi /<RES_ID>/<SIZE>/<LANG_CODE>/
            final String def = "def";
            urlPath.append(this.getProtectedBaseURL());
            if (!urlPath.toString().endsWith("/")) {
                urlPath.append("/");
            }
            urlPath.append(this.getId()).append("/");
            if (instance.getSize() < 0) {
                urlPath.append(def);
            } else {
                urlPath.append(instance.getSize());
            }
            urlPath.append("/");
            if (instance.getLangCode() == null) {
                urlPath.append(def);
            } else {
                urlPath.append(instance.getLangCode());
            }
            urlPath.append("/");
        } else {
            StringBuilder subFolder = new StringBuilder(this.getFolder());
            if (!subFolder.toString().endsWith("/")) {
                subFolder.append("/");
            }
            subFolder.append(instance.getFileName());
            String path = this.getStorageManager().getResourceUrl(subFolder.toString(), false);
            urlPath.append(path);
        }
        return urlPath.toString();
    }

    protected boolean isProtectedResource() {
        return (!Group.FREE_GROUP_NAME.equals(this.getMainGroup()));
    }

    protected File saveTempFile(String filename, InputStream is) throws ApsSystemException, IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + filename;
        FileOutputStream outStream = null;
        try {
            byte[] buffer = new byte[1024];
            int length;
            outStream = new FileOutputStream(filePath);
            while ((length = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (Throwable t) {
            logger.error("Error on saving temporary file '{}'", filename, t);
            throw new ApsSystemException("Error on saving temporary file", t);
        } finally {
            if (null != outStream) {
                outStream.close();
            }
            if (null != is) {
                is.close();
            }
        }
        return new File(filePath);
    }
    
    protected String getUniqueBaseName(String originalFileName) {
        Assert.hasLength(originalFileName, "File name must not be null or empty");
        String baseName = FilenameUtils.getBaseName(originalFileName);
        String extension = FilenameUtils.getExtension(originalFileName);
        baseName = this.purgeBaseName(baseName);
        String suggestedName = baseName;
        int fileOrder = 1;
        while(this.exists(this.createFileName(suggestedName, extension))) {
            suggestedName = baseName + '_' + fileOrder;
            fileOrder ++;
        }
        return suggestedName;
    }

    protected String getMultiFileUniqueBaseName(String baseName, String suffix, String extension) {
        Assert.hasLength(baseName, "base name of file can't be null or empty");
        Assert.notNull(suffix, "file suffix can't be null");
        baseName = this.purgeBaseName(baseName);
        String suggestedName = baseName + suffix;
        int fileOrder = 1;
        while(this.exists(this.createFileName(suggestedName, extension))) {
            suggestedName = baseName + '_' + fileOrder + suffix;
            fileOrder ++;
        }
        return suggestedName;
    }
    
    private String purgeBaseName(String baseName) {
        String purgedName = baseName.replaceAll("[^ _.a-zA-Z0-9]", "");
        return purgedName.trim().replace(' ', '_');
    }

    protected String createFileName(String baseName, String extension) {
        return extension == null ? baseName : baseName + '.' + extension;
    }

    protected boolean exists(String instanceFileName) {
        try {
            String subPath = this.getDiskSubFolder() + instanceFileName;
            return this.getStorageManager().exists(subPath, this.isProtectedResource());
        } catch (Throwable t) {
            logger.error("Error testing existing file '{}'", instanceFileName, t);
            throw new RuntimeException("Error testing existing file", t);
        }
    }

    protected IStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public String getMetadataIgnoreKeys() {
        return metadataIgnoreKeys;
    }

    @Override
    public void setMetadataIgnoreKeys(String metadataIgnoreKeys) {
        this.metadataIgnoreKeys = metadataIgnoreKeys;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
}

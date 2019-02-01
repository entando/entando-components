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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Wrapper of the StorageManager handling storage of DE component installation
 * files.
 */
@Service
public class ComponentStorageManager {

    private static final String COMPONENTS_DIR = "components";

    private final IStorageManager storageManager;

    @Autowired
    public ComponentStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public InputStream getProtectedStream(String subPath) throws ApsSystemException {
        return storageManager.getStream(getPath(subPath), true);
    }

    public boolean existsProtected(String subPath) throws ApsSystemException {
        return storageManager.exists(getPath(subPath), true);
    }

    public boolean existsResource(String subPath) throws ApsSystemException {
        return storageManager.exists(getPath(subPath), false);
    }

    public void createProtectedDirectory(String subPath) throws ApsSystemException {
        storageManager.createDirectory(getPath(subPath), true);
    }

    public void createResourceDirectory(String subPath) throws ApsSystemException {
        storageManager.createDirectory(getPath(subPath), false);
    }

    public void saveProtectedFile(String subPath, InputStream in) throws ApsSystemException, IOException {
        storageManager.saveFile(getPath(subPath), true, in);
    }

    public void saveResourceFile(String subPath, InputStream in) throws ApsSystemException, IOException {
        storageManager.saveFile(getPath(subPath), false, in);
    }

    public void deleteProtectedDirectory(String subPath) throws ApsSystemException {
        storageManager.deleteDirectory(getPath(subPath), true);
    }

    public void deleteResourceDirectory(String subPath) throws ApsSystemException {
        storageManager.deleteDirectory(getPath(subPath), false);
    }

    private String getPath(String subPath) {
        String path = COMPONENTS_DIR;
        if (!subPath.startsWith(File.separator)) {
            path += File.separator;
        }
        path += subPath;
        return path;
    }

}

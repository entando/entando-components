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
import org.springframework.core.io.AbstractResource;

/**
 * Resource implementation handling resources internally managed by the
 * StorageManager. It is used in the installation process as an alternative to
 * loading the resources from the classpath (as done by the default
 * ComponentModule implementation).
 */
public class ComponentResource extends AbstractResource {

    private final IStorageManager storageManager;
    private final String path;
    private final String description;

    public ComponentResource(IStorageManager storageManager, String subpath, String description) {
        this.storageManager = storageManager;
        this.path = ComponentInstaller.COMPONENTS_DIR + File.separator + subpath;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return storageManager.getStream(path, ComponentInstaller.PROTECTED_RESOURCE);
        } catch (ApsSystemException ex) {
            throw new IOException(ex);
        }
    }
}

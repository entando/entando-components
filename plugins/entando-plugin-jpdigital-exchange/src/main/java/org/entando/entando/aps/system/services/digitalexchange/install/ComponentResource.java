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

public class ComponentResource extends AbstractResource {

    private final IStorageManager storageManager;
    private final String path;

    public ComponentResource(IStorageManager storageManager, String subpath) {
        this.storageManager = storageManager;
        this.path = ComponentInstaller.COMPONENTS_DIR + File.separator + subpath;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return storageManager.getStream(path, true);
        } catch (ApsSystemException ex) {
            throw new IOException(ex);
        }
    }
}

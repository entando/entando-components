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

import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.init.model.ComponentEnvironment;
import org.entando.entando.aps.system.init.model.IPostProcess;
import org.springframework.core.io.Resource;

/**
 * ComponentEnvironment able to retrieve the SQL resources from the
 * ComponentStorageManager instead of the classpath (as done by the
 * implementation in core).
 */
public class DigitalExchangeComponentEnvironment implements ComponentEnvironment {

    private final ComponentStorageManager storageManager;
    private final ComponentEnvironment componentEnvironment;

    public DigitalExchangeComponentEnvironment(ComponentStorageManager storageManager, ComponentEnvironment componentEnvironment) {
        this.storageManager = storageManager;
        this.componentEnvironment = componentEnvironment;
    }

    @Override
    public Resource getSqlResources(String datasourceName) {

        String path = componentEnvironment.getSqlResourcesPaths().get(datasourceName);
        if (null == path) {
            return null;
        }
        return new ComponentResource(storageManager, path,
                String.format("sqlResource for %s (%s)",
                        componentEnvironment.getCode(), datasourceName));
    }

    @Override
    public String getCode() {
        return componentEnvironment.getCode();
    }

    @Override
    public List<IPostProcess> getPostProcesses() {
        return componentEnvironment.getPostProcesses();
    }

    @Override
    public Map<String, String> getSqlResourcesPaths() {
        return componentEnvironment.getSqlResourcesPaths();
    }
}

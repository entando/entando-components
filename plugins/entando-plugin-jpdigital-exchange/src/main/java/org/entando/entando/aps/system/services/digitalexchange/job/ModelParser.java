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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import javax.xml.bind.JAXB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelParser {

    private ComponentStorageManager componentStorageManager;

    @Autowired
    public void ModelParser(ComponentStorageManager componentStorageManager) {
        this.componentStorageManager = componentStorageManager;
    }

    public Object parse(String file, Class<?> type) throws ApsSystemException, IOException {
        try (InputStream in = componentStorageManager.getProtectedStream(file)) {
            if (file.endsWith(".xml")) {
                return JAXB.unmarshal(in, type);
            } else {
                return fromJson(in, type);
            }
        }
    }

    public static Object fromJson(InputStream in, Class<?> type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, type);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}

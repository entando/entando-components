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
package org.entando.entando.aps.system.services.digitalexchange;

import javax.servlet.ServletContext;
import org.entando.entando.crypt.DefaultTextEncryptor;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

public class DigitalExchangeTextEncryptor implements TextEncryptor, ServletContextAware {

    private ServletContext servletContext;
    private String keyFile;

    private TextEncryptor textEncryptor;

    private TextEncryptor getTextEncryptor() {
        if (textEncryptor == null) {
            Resource resource = new ServletContextResource(servletContext, keyFile);
            this.textEncryptor = new DefaultTextEncryptor(resource);
        }
        return textEncryptor;
    }

    @Override
    public String encrypt(String value) {
        return getTextEncryptor().encrypt(value);
    }

    @Override
    public String decrypt(String value) {
        return getTextEncryptor().decrypt(value);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }
}

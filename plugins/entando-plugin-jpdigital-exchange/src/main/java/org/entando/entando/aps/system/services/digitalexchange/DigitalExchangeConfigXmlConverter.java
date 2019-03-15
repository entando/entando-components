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

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXB;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchangesConfig;
import org.entando.entando.aps.util.crypto.DefaultTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class DigitalExchangeConfigXmlConverter {

    private final TextEncryptor encryptor;

    @Autowired
    public DigitalExchangeConfigXmlConverter(DefaultTextEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public String marshal(DigitalExchangesConfig config) {
        DigitalExchangesConfig copiedConfig = new DigitalExchangesConfig(config);
        copiedConfig.getDigitalExchanges().forEach(exchange -> {
            if (exchange.getClientSecret() != null) {
                exchange.setClientSecret(encryptor.encrypt(exchange.getClientSecret()));
            }
        });
        StringWriter sw = new StringWriter();
        JAXB.marshal(copiedConfig, sw);
        return sw.toString();
    }

    public DigitalExchangesConfig unmarshal(String xml) {
        StringReader reader = new StringReader(xml);
        DigitalExchangesConfig config = JAXB.unmarshal(reader, DigitalExchangesConfig.class);
        config.getDigitalExchanges().forEach(exchange -> {
            if (exchange.getClientSecret() != null) {
                exchange.setClientSecret(encryptor.decrypt(exchange.getClientSecret()));
            }
        });
        return config;
    }
}

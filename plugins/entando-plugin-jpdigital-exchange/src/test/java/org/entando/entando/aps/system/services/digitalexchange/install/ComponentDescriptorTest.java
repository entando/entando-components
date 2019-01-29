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

import java.io.InputStream;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ComponentDescriptorTest {

    @Mock
    private ComponentStorageManager storageManager;

    @Test
    public void shouldParseComponentXml() throws Throwable {

        ComponentDescriptor component;

        try (InputStream in = ComponentDescriptorTest.class.getClassLoader().getResourceAsStream("components/de_test_page_model/component.xml")) {
            Document document = new SAXBuilder().build(in);
            component = new ComponentDescriptor(document.getRootElement(), storageManager);
        }

        assertThat(component.getInstallationCommands()).isNotEmpty();
        assertThat(component.getEnvironments()).isNotEmpty();
        assertThat(component.getEnvironments().get("test"))
                .isInstanceOf(DigitalExchangeComponentEnvironment.class);
    }
}

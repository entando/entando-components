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
package org.entando.entando.aps.system.services.digitalexchange.plugins;

import io.fabric8.kubernetes.client.server.mock.KubernetesServer;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ServicePluginInstallerTest {

    @Rule
    public KubernetesServer server = new KubernetesServer(true, true);

    @Before
    public void setUp() throws Exception {
        try (InputStream in = ServicePluginInstallerTest.class.getClassLoader().getResourceAsStream("crd.yaml")) {
            server.getClient().customResourceDefinitions().load(in).createNew();
        }
    }

    @Test
    public void testPlugin() throws Exception {

        String pluginId = "example-entando-plugin";

        ServicePluginInstaller servicePluginInstaller = new ServicePluginInstaller(server.getClient(), null);

        try (InputStream in = ServicePluginInstallerTest.class.getClassLoader().getResourceAsStream("test-k8-resource.yaml")) {
            servicePluginInstaller.createPluginResource(pluginId, in);
        }

        servicePluginInstaller.deletePluginResource(pluginId);
    }
}

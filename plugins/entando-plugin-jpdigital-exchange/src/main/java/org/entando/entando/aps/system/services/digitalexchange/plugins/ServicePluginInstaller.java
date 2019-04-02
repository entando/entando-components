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

import org.entando.entando.kubernetes.controller.EntandoPlugin;
import org.entando.entando.kubernetes.controller.EntandoPluginList;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinitionBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.internal.KubernetesDeserializer;
import java.io.InputStream;
import org.entando.entando.aps.system.services.digitalexchange.job.ComponentStorageManager;
import org.entando.entando.kubernetes.controller.DoneableEntandoPlugin;
import org.entando.entando.kubernetes.controller.EntandoPluginSpec;
import org.entando.entando.kubernetes.controller.EntandoPluginStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicePluginInstaller {

    private final KubernetesClient client;
    private final ComponentStorageManager componentStorageManager;

    @Autowired
    public ServicePluginInstaller(KubernetesClient client, ComponentStorageManager componentStorageManager) {
        this.client = client;
        this.componentStorageManager = componentStorageManager;
        KubernetesDeserializer.registerCustomKind("entando.org/v1alpha1#EntandoPlugin", EntandoPlugin.class);
        KubernetesDeserializer.registerCustomKind("entando.org/v1alpha1#EntandoPluginSpec", EntandoPluginSpec.class);
        KubernetesDeserializer.registerCustomKind("entando.org/v1alpha1#EntandoPluginStatus", EntandoPluginStatus.class);
    }

    public void createPluginResource(String pluginId, String customResourceFile) throws Exception {

        try (InputStream in = componentStorageManager.getProtectedStream(customResourceFile)) {
            createPluginResource(pluginId, in);
        }
    }
    
    protected void createPluginResource(String pluginId, InputStream customResourceFile) {

        getClientOperation()
                .load(customResourceFile)
                .createOrReplace();

        checkResourceStatus(pluginId);
    }

    public void deletePluginResource(String pluginId) {

        EntandoPlugin plugin = getClientOperation()
                .withName(pluginId).get();

        getClientOperation()
                .delete(plugin);
    }

    private void checkResourceStatus(String pluginId) {
        
        EntandoPlugin plugin = getClientOperation()
                .withName(pluginId).get();
        
        // TODO
    }
    
    private NonNamespaceOperation<EntandoPlugin, EntandoPluginList, DoneableEntandoPlugin, Resource<EntandoPlugin, DoneableEntandoPlugin>>
            getClientOperation() {

        CustomResourceDefinition crd = getCustomResourceDefinition();

        return client.customResources(crd, EntandoPlugin.class, EntandoPluginList.class, DoneableEntandoPlugin.class)
                .inNamespace("default");
    }
    
    private CustomResourceDefinition getCustomResourceDefinition() {
        return new CustomResourceDefinitionBuilder()
            .withApiVersion("apiextensions.k8s.io/v1beta1")
            .withNewMetadata()
                .withName("entandoplugins.entando.org")
                .endMetadata()
            .withNewSpec()
                .withGroup("entando.org")
                .withVersion("v1alpha1")
                .withScope("Namespaced")
                .withNewNames()
                    .withKind("EntandoPlugin")
                    .withPlural("entandoplugins")
                    .endNames()
                .endSpec()
            .build();
    }
}

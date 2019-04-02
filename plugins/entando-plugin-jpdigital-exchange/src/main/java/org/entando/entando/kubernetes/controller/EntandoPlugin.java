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
package org.entando.entando.kubernetes.controller;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.client.CustomResource;

@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class EntandoPlugin extends CustomResource {

    private EntandoPluginSpec spec;
    private EntandoPluginStatus status;

    public EntandoPluginSpec getSpec() {
        return spec;
    }

    public void setSpec(EntandoPluginSpec spec) {
        this.spec = spec;
    }

    public EntandoPluginStatus getStatus() {
        return status;
    }

    public void setStatus(EntandoPluginStatus status) {
        this.status = status;
    }
}

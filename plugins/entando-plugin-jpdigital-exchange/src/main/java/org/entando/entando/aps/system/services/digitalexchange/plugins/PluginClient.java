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

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.entando.entando.aps.system.jpa.servdb.PluginMetadata;
import org.entando.entando.aps.system.jpa.servdb.repo.PluginMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PluginClient {

    @Autowired
    private PluginMetadataRepository repository;

    @Autowired
    private RestTemplate pluginRestTemplate;

    public ResponseEntity<?> call(String pluginId, String endpoint, Optional<String> optionalBody, HttpMethod method, HttpServletRequest request) {

        String uri = getURL(pluginId, endpoint);

        HttpEntity<String> entity = null;
        if (optionalBody.isPresent()) {
            entity = new HttpEntity<>(optionalBody.get());
        }

        ResponseEntity<String> responseEntity
                = pluginRestTemplate.exchange(uri, method, entity, String.class);

        return responseEntity;
    }

    protected String getURL(String pluginId, String endpoint) {

        PluginMetadata plugin = repository.findById(pluginId).get();

        return UriComponentsBuilder
                .fromHttpUrl(plugin.getUrl())
                .pathSegment(endpoint)
                .build(false) // disable encoding: it will be done by RestTemplate
                .toUriString();
    }
}

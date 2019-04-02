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
package org.entando.entando.web.digitalexchange.plugins;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.entando.entando.aps.system.services.digitalexchange.plugins.PluginClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PluginsResourceController implements PluginsResource {

    @Autowired
    private PluginClient pluginClient;

    @Override
    public ResponseEntity<?> callPlugin(@PathVariable("pluginId") String pluginId,
            @RequestBody Optional<String> body, HttpMethod method, HttpServletRequest request) {

        int endpointStartIndex = ("/plugins/" + pluginId).length();
        String endpoint = request.getPathInfo().substring(endpointStartIndex);

        return pluginClient.call(pluginId, endpoint, body, method, request);
    }
}

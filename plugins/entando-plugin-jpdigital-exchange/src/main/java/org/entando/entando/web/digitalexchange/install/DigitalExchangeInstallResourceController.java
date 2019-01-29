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
package org.entando.entando.web.digitalexchange.install;

import com.agiletec.aps.system.services.user.UserDetails;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import org.entando.entando.aps.system.services.digitalexchange.install.ComponentInstallationJob;
import org.entando.entando.aps.system.services.digitalexchange.install.DigitalExchangeComponentInstallationService;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DigitalExchangeInstallResourceController implements DigitalExchangeInstallResource {

    private final DigitalExchangeComponentInstallationService installationService;

    @Autowired
    public DigitalExchangeInstallResourceController(DigitalExchangeComponentInstallationService service) {
        this.installationService = service;
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ComponentInstallationJob>> install(@PathVariable("exchange") String exchangeId,
            @PathVariable("component") String componentId, HttpServletRequest request) throws URISyntaxException {

        UserDetails currentUser = (UserDetails) request.getSession().getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ComponentInstallationJob job = installationService.install(exchangeId, componentId, currentUser.getUsername());

        return ResponseEntity.created(
                new URI("/plugins/digitalExchange/install/" + componentId))
                .body(new SimpleRestResponse<>(job));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ComponentInstallationJob>> uninstall(String exchangeId, String componentId, HttpServletRequest request) throws URISyntaxException {

        UserDetails currentUser = (UserDetails) request.getSession().getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ComponentInstallationJob job = installationService.uninstall(exchangeId, componentId, currentUser.getUsername());

        return ResponseEntity.created(
                new URI("/plugins/digitalExchange/uninstall/" + componentId))
                .body(new SimpleRestResponse<>(job));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ComponentInstallationJob>> getLastJob(@PathVariable("component") String componentId) {
        return ResponseEntity.ok(new SimpleRestResponse<>(installationService.checkInstallationStatus(componentId)));
    }

}

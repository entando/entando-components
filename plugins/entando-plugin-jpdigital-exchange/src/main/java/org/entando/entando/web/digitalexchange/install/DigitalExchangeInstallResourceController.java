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

import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.entando.entando.aps.system.services.digitalexchange.install.ComponentInstallationJob;
import org.entando.entando.aps.system.services.digitalexchange.install.DigitalExchangeComponentInstallationService;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DigitalExchangeInstallResourceController implements DigitalExchangeInstallResource {

    private final DigitalExchangesService exchangesService;
    private final DigitalExchangeComponentInstallationService installationService;

    @Autowired
    public DigitalExchangeInstallResourceController(DigitalExchangesService exchangesService,
            DigitalExchangeComponentInstallationService service) {
        this.exchangesService = exchangesService;
        this.installationService = service;
    }

    @Override
    public SimpleRestResponse<String> install(@PathVariable("exchange") String digitalExchangeId, @PathVariable("component") String componentName) {
        DigitalExchange digitalExchange = exchangesService.findById(digitalExchangeId);
        ComponentInstallationJob job = installationService.install(digitalExchange, componentName);
        return new SimpleRestResponse<>(job.getId());
    }
}

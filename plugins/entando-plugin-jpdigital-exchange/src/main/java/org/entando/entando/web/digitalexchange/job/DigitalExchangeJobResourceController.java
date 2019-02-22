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
package org.entando.entando.web.digitalexchange.job;

import java.util.Optional;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;

import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.services.digitalexchange.job.DigitalExchangeJobService;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/digitalExchange/jobs")
public class DigitalExchangeJobResourceController implements DigitalExchangeJobResource {

    private final DigitalExchangeJobService jobService;

    @Autowired
    public DigitalExchangeJobResourceController(DigitalExchangeJobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public ResponseEntity<PagedRestResponse<DigitalExchangeJob>> list(RestListRequest restListRequest) {
        return ResponseEntity.ok(jobService.findAll(restListRequest));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<DigitalExchangeJob>> getById(@PathVariable("id") String id) {
        Optional<DigitalExchangeJob> optionalJob = jobService.findById(id);
        return optionalJob
                .map(job -> ResponseEntity.ok(new SimpleRestResponse<>(job)))
                .orElseThrow(() -> new ResourceNotFoundException("job", id));
    }

}

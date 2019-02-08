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

import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJobRepository;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/digitalExchange/job")
public class JobResourceController {

    @Autowired
    private DigitalExchangeJobRepository jobRepository;

    @GetMapping
    public PagedRestResponse<DigitalExchangeJob> list(RestListRequest restListRequest) {
        return jobRepository.findAll(restListRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DigitalExchangeJob> getById(@PathVariable("id") String id) {
        Optional<DigitalExchangeJob> optionalJob = jobRepository.findById(id);
        return optionalJob
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

}
